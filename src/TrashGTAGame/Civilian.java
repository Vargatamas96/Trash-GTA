package TrashGTAGame;

import java.awt.*;
import java.util.Random;

public class Civilian extends GameObject {

    private Handler handler;
    private Game game;
    Random r = new Random();
    int choose = 0;
    int hp = 100;

    public Civilian(int x, int y, ID id, Handler handler,Game game) {
        super(x, y, id);
        this.handler = handler;
        this.game = game;

    }

    @Override
    public void tick() {
        x += velX;
        y += velY;

        choose = r.nextInt(10);

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Block) {
                if (getBoundsBig().intersects(tempObject.getBounds())) {
                    x += (velX*2) * (-1);                                                  //just a random idea
                    y += (velY*2) * (-1);                                                  //for wallstuck debugging
                    velX *= (-1);
                    velY *= (-1);
                } else if (choose == 0) {
                    velX = (r.nextInt(4 - -4) + -4);
                    velY = (r.nextInt(4 - -4) + -4);
                }
            }

            if(tempObject.getId() == ID.Bullet){
                if(getBounds().intersects(tempObject.getBounds())){
                    hp -= 50;
                    handler.removeObject(tempObject);
                    game.score = game.score/2;
                }
            }
        }

        if(hp <= 0) handler.removeObject(this);

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, 32, 32);

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }

    public Rectangle getBoundsBig() {
        return new Rectangle(x - 16, y - 16, 64, 64);
    }
}
