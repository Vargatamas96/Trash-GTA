package Multiplayer;

import TrashGTAGame.Game;
import TrashGTAGame.Handler;
import TrashGTAGame.ID;
import TrashGTAGame.PlayerMP;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private Game game;
    private Handler handler;

    public GameClient(Game game, String ipAddress, Handler handler) {
        this.game = game;
        this.handler = handler;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);                        //putting data into the packets
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket((message.substring(0, 2)));
        Packet packet = null;
        switch (type) {
            default:
            case Invalid:
                break;
            case Login:
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "]" +((Packet00Login)packet).getUsername() + " has joined the game.");
                PlayerMP player = new PlayerMP(300, 300, ((Packet00Login) packet).getUsername(), ID.PlayerMP, game, address, port);
                handler.addObject(player);
                break;
            case Disconnect:
                break;
        }
    }

    public void sendData(byte[] data){
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
