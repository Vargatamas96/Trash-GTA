package TrashGTAGame;

import java.awt.*;

public class Ballas extends GameObject {

    private Handler handler;
    private Game game;
    private Spawn spawn;

    int hp = 100;

    public Ballas(int x, int y, ID id, Handler handler,Game game, Spawn spawn) {
        super(x, y, id);
        this.handler = handler;
        this.game = game;
        this.spawn = spawn;
    }

    @Override
    public void tick() {
        if(hp <= 0){
            handler.removeObject(this);
            game.ballasTrigger = game.ballasTrigger + 1;
            game.score = game.score + 100;
            spawn.ballasCounter -= 1;
        }

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

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
        g.setColor(new Color(0xD70096));
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
