package TrashGTAGame;

import java.awt.*;
import java.net.InetAddress;

public class PlayerMP extends PlayerChar {

    public InetAddress ipAddress;
    public int port;
    private Handler handler;
    private Game game;

    public PlayerMP(int x, int y, String username, ID id, Handler handler, Game game, InetAddress ipAddress, int port) {
        super(x, y, username, id, handler, game);
        this.handler = handler;
        this.game = game;
        this.ipAddress = ipAddress;
        this.port = port;
        this.username = username;
    }

    public PlayerMP(int x, int y, String name, ID id, Game game, InetAddress ipAddress, int port) {                          //for local connection
        super(x, y, name, id, null, game);
        this.game = game;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect((int) x, (int) y, 32, 32);

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 32, 32);
    }


    public String getUsername(){
        return this.username;
    }
}
