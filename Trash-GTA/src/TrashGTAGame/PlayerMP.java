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
        super.tick();                                                                //might not need
/*        x += velX;
        y += velY;

        collision();

        if (handler.isUp()) velY = -5;                                               //movement
        else if (!handler.isDown()) velY = 0;

        if (handler.isDown()) velY = 5;
        else if (!handler.isUp()) velY = 0;

        if (handler.isRight()) velX = 5;
        else if (!handler.isLeft()) velX = 0;

        if (handler.isLeft()) velX = -5;
        else if (!handler.isRight()) velX = 0;*/

    }

/*    private void collision() {
        for (int i = 0; i < handler.object.size(); i++) {

            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Block) {                                     //collision with a block

                if (getBounds().intersects(tempObject.getBounds())) {
                    x += velX * (-1);
                    y += velY * (-1);
                }

            }
            if (tempObject.getId() == ID.Civilian) {                                  //collision with a civilian (might be easier with a || logic)

                if (getBounds().intersects(tempObject.getBounds())) {
                    x += velX * (-1);
                    y += velY * (-1);
                }
            }

            if (tempObject.getId() == ID.EnemyBullet) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    handler.removeObject(tempObject);
                    game.hpPlayer -= 5;
                }
            }
        }
    }*/

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
