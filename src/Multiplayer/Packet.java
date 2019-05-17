package Multiplayer;

public abstract class Packet {

    public static enum PacketTypes {
        Invalid(-1),
        Login(00),
        Disconnect(01);

        private int packetId;

        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();                                               //byte array that we send back and forth

    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PacketTypes.Invalid;
        }
    }

    public static PacketTypes lookupPacket(int id) {                                //loops through enum
        for (PacketTypes p : PacketTypes.values()) {
            if (p.getId() == id) {
                return p;
            }
        }

        return  PacketTypes.Invalid;
    }

}
