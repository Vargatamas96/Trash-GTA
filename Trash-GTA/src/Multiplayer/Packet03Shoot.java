package Multiplayer;

public class Packet03Shoot extends Packet {

    private int x,y;
    private int mx,my;

    public Packet03Shoot(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.x = Integer.parseInt(dataArray[0]);
        this.y = Integer.parseInt(dataArray[1]);
        this.mx = Integer.parseInt(dataArray[2]);
        this.my = Integer.parseInt(dataArray[3]);
    }

    public Packet03Shoot(int x, int y, int mx, int my) {
        super(02);
        this.x = x;
        this.y = y;
        this.mx = mx;
        this.my = my;
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
        return ("03" + this.x + "," + this.y + "," + this.mx + "," + this.my).getBytes();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getMx() {
        return this.mx;
    }

    public int getMy() {
        return this.my;
    }
}
