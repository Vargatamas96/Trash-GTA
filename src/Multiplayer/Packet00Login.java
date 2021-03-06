package Multiplayer;

public class Packet00Login extends Packet {

    private String username;

    public Packet00Login(byte[] data) {                             //for retrieving the data
        super(00);
        this.username = readData(data);
    }

    public Packet00Login(String username) {                         //for sending from the original instance
        super(00);
        this.username = username;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }
}
