package TrashGTAGame;

import java.awt.*;

public abstract class GameObject {

    protected int x, y;
    protected float velX = 0, velY = 0;
    protected ID id;

    public GameObject(int x, int y, ID id) {                                           //for positions
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();                                                //updating the objects

    public abstract void render(Graphics g);                                    //object drawing

    public abstract Rectangle getBounds();                                      //for hit box or collision

    public int getX() {                                                         //setters and getters for easy position info
        return x;
    }

    public int getY() {

        return y;
    }

    public float getVelX() {

        return velX;
    }

    public float getVelY() {

        return velY;
    }

    public void setX(int x) {

        this.x = x;
    }

    public void setY(int y) {

        this.y = y;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {

        this.velY = velY;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
