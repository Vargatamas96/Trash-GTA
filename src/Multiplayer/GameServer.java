package Multiplayer;

import TrashGTAGame.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Thread {

    private DatagramSocket socket;
    private Game game;
    private Handler handler;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();                        //list of players currently connected to the server

    public GameServer(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
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
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
            default:
            case Invalid:
                break;
            case Login:
                packet = new Packet00Login(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " +((Packet00Login)packet).getUsername() + " has connected.");
                PlayerMP player = new PlayerMP(300, 300, ((Packet00Login) packet).getUsername(),ID.PlayerMP, game, address, port);
                this.addConnection(player, (Packet00Login)packet);
                break;
            case Disconnect:
                break;
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers){
            if(player.getUsername().equalsIgnoreCase(p.getUsername())){
                if(p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }

                if(p.port == -1){
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                sendData(packet.getData(), p.ipAddress, p.port);
            }
        }
        if(alreadyConnected) {
            this.connectedPlayers.add(player);
            packet.writeData(this);
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {                                                 //sends data to all the clients
        for (PlayerMP p : connectedPlayers) {
            sendData(data, p.ipAddress, p.port);
        }
    }
}
