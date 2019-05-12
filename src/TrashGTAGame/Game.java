package TrashGTAGame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;

    private BufferedImage level = null;


    public Game() {                                                             //calling the Window constructor
        new Window(1360, 768, "Trash GTA", this);
        start();                                                                //calling the start method

        handler = new Handler();
        camera = new Camera(0,0);
        this.addKeyListener(new KeyInput(handler));                             //adding keyListener
        this.addMouseListener(new MouseInput(handler, camera));

        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/Trash GTA map.png");

        loadLevel(level);
    }

    private void start() {                                                      //starting the thread
        isRunning = true;
        thread = new Thread(this);                                       //calling this class's run() method
        thread.start();
    }

    private void stop() {                                                       //stopping the thread
        isRunning = false;
        try {                                                                   //trying to detect errors
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {                                                         //game loop *yay for Notch*
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }

    public void tick(){                                                         //updates everything in the game
        for (int i = 0; i < handler.object.size(); i++){                        //camera finding the player's position
            if(handler.object.get(i).getId() == ID.Player){
                camera.tick(handler.object.get(i));
            }
        }
        handler.tick();
    }

    public void render(){                                                       //rendering Graphics class
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);                            //preloading frames in the actual window. While 1 frame shows, the next 2 are already loaded
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;                                        //casting
        //////////////////////////////////                                      //drawing place begins

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,1360,768);

        g2d.translate(-camera.getX(), -camera.getY());                          //getting translated starts here

        handler.render(g);                                                      //needs to be under the background, so it will draw in front of it

        g2d.translate(camera.getX(), camera.getY());                            //getting translated ends here

        //////////////////////////////////
        g.dispose();
        bs.show();
    }

    private void loadLevel(BufferedImage image) {                                //loading the level, drawing the blocks
        int w = image.getWidth();
        int h = image.getHeight();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255)
                    handler.addObject(new Block(xx * 32, yy * 32, ID.Block));

                if (blue == 255)
                    handler.addObject(new PlayerChar(xx * 32, yy * 32, ID.Player, handler));

                if (green == 254 && blue == 254) {                                                   //Cyan color
                    handler.addObject(new Civilian(xx * 32, yy * 32, ID.Civilian, handler));
                }
            }
        }
    }

    public static void main(String args[]) {
        new Game();
    }
}