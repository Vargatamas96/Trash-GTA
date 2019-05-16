package TrashGTAGame;

import java.awt.*;

public class Police extends GameObject {

    private Handler handler;
    private Game game;
    private Spawn spawn;
    private GameObject player;

    private int timer = 100;


    int hp = 100;

    public Police(int x, int y, ID id, Handler handler, Game game, Spawn spawn) {
        super(x, y, id);
        this.handler = handler;
        this.game = game;
        this.spawn = spawn;

        for (int i = 0; i < handler.object.size(); i++) {
            if (handler.object.get(i).getId() == ID.Player)
                player = handler.object.get(i);                     //checking the player
        }
    }

    @Override
    public void tick() {

        if (game.wanted >= 1) {
            timer--;
            x += velX;
            y += velY;

            float diffX = player.getX() - x - 30;
            float diffY = player.getY() - y - 30;
            float distance = (float) Math.sqrt((x - player.getX()) * (x - player.getX()) + (y - player.getY()) * (y - player.getY()));

            velX = ((1 / distance) * diffX);
            velY = ((1 / distance) * diffY);


            if (timer == 0) {
                handler.addObject(new EnemyBullet((int) x + 16, (int) y + 24, ID.EnemyBullet, handler, (int) player.getX(), (int) player.getY()));
                timer += 100;
            }
        }


        if (hp <= 0) {
            handler.removeObject(this);
            if (game.wanted <= 5) game.wanted = game.wanted + 1;
            game.score = game.score + 20;
            spawn.policeCounter -= 1;
        }

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getId() == ID.Block) {
                if (getBoundsBig().intersects(tempObject.getBounds())) {
                    x += (velX * 10) * (-1) + (player.getX() - x)/(-70);                                                  //just a random idea
                    y += (velY * 10) * (-1) + (player.getY() - y)/(-70);                                                 //for wallstuck debugging
                    velX *= (-1);
                    velY *= (-1);
                }
            }
            if (tempObject.getId() == ID.Bullet) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    hp -= 50;
                    handler.removeObject(tempObject);
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0x1E1DD7));
        g.fillRect((int) x, (int) y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    public Rectangle getBoundsBig() {
        return new Rectangle((int) x - 16, (int) y - 16, 64, 64);
    }
}

