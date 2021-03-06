package TrashGTAGame;

import Multiplayer.GameClient;
import Multiplayer.GameServer;
import Multiplayer.Packet00Login;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private Camera camera;
    private Spawn spawn;
    private GameClient socketClient;
    private GameServer socketServer;
    private JFrame frame;
    private PlayerChar playerChar;
    private PlayerMP playerMP;

    private BufferedImage level = null;

    public static final int WIDTH = 1360;
    public static final int HEIGHT = 768;

    public int ammo = 12;
    public int hpPlayer = 100;

    public int score = 0;
    public String highScore = "";
    public int ballasTrigger = 0;
    public int wanted = 0;


    public Game() {                                                             //calling the Window constructor
        new Window(WIDTH, HEIGHT, "Trash GTA", this);
        start();                                                                //calling the start method

        handler = new Handler();
        camera = new Camera(0, 0);
        spawn = new Spawn(handler, this);

        if (JOptionPane.showConfirmDialog(this, "Do you want to run the server?") == 0) {
            socketServer = new GameServer(this, handler);
            socketServer.start();
        }

        if (socketServer != null) {
            socketClient = new GameClient(this, "localhost", handler);
            socketClient.start();
            playerMP = new PlayerMP(300, 300, JOptionPane.showInputDialog(this, "Please enter username: "), ID.PlayerMP, handler, this, null, -1);
            handler.addObject(playerMP);
            Packet00Login loginPacket = new Packet00Login(playerMP.getUsername());
            socketServer.addConnection(playerMP, loginPacket);
            loginPacket.writeData(socketClient);
        }

        this.addKeyListener(new KeyInput(handler, this));                             //adding keyListener
        this.addMouseListener(new MouseInput(handler, camera, this));

        BufferedImageLoader loader = new BufferedImageLoader();
        level = loader.loadImage("/Trash GTA map.png");

        loadLevel(level);


        if (highScore.equals("")) {
            highScore = this.GetHighScoreValue();
        }
    }

    private void start() {                                                      //starting the thread
        isRunning = true;
        thread = new Thread(this);                                       //calling this class's run() method
        thread.start();
    }

    private void stop() {                                                       //stopping the thread
        isRunning = false;
        try {                                                                   //trying to detect errors
            CheckScore();
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
            if (hpPlayer <= 0) {
                isRunning = false;
            }
        }
        stop();
    }

    public void tick() {                                                         //updates everything in the game
        for (int i = 0; i < handler.object.size(); i++) {                        //camera finding the player's position
            if (handler.object.get(i).getId() == ID.Player) {
                camera.tick(handler.object.get(i));
            }
        }
        handler.tick();
        spawn.tick();
    }

    public void render() {                                                       //rendering Graphics class
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);                            //preloading frames in the actual window. While 1 frame shows, the next 2 are already loaded
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;                                        //casting
        //////////////////////////////////                                      //drawing place begins

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 1360, 768);

        g2d.translate(-camera.getX(), -camera.getY());                          //getting translated starts here

        handler.render(g);                                                      //needs to be under the background, so it will draw in front of it

        g2d.translate(camera.getX(), camera.getY());                            //getting translated ends here

        g.setColor(Color.RED);                                                 //health bar
        g.fillRect(5, 5, 200, 32);
        g.setColor(Color.GREEN);
        g.fillRect(5, 5, hpPlayer * 2, 32);
        g.setColor(Color.BLACK);
        g.drawRect(5, 5, 200, 32);
        g.setColor(Color.WHITE);
        g.drawString("Ammo: " + ammo, 5, 50);
        g.setColor(Color.WHITE);
        g.drawString("Wanted: " + wanted, 5, 75);
        g.setColor(Color.WHITE);
        g.drawString("Ballas Trigger: " + ballasTrigger, 5, 100);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 5, 125);
        DrawScore(g);

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
                int red = (pixel >> 16) & 0xff;                                 //masking
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;

                if (red == 255 && green == 0 && blue == 0)
                    handler.addObject(new Block(xx * 32, yy * 32, ID.Block));

                if (blue == 255 && green == 0 && red == 0)
                    handler.addObject(new PlayerChar(xx * 32, yy * 32, JOptionPane.showInputDialog(this, "Please enter username: "), ID.Player, handler, this));

                if (green == 254 && blue == 254)                                                                  //Cyan color
                    handler.addObject(new Civilian(xx * 32, yy * 32, ID.Civilian, handler, this));
                if (red == 215 && blue == 150)
                    handler.addObject(new Ballas(xx * 32, yy * 32, ID.Ballas, handler, this, spawn));
            }
        }
    }

    public String GetHighScoreValue() {

        FileReader readFile = null;
        BufferedReader reader = null;

        try {
            readFile = new FileReader("highscore.dat");                              //users can't edit it :))
            reader = new BufferedReader(readFile);
            return reader.readLine();
        } catch (Exception e) {
            return "Nobody:0";
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void DrawScore(Graphics g) {
        g.drawString("Score: " + score, 0, HEIGHT * WIDTH);
        g.drawString("Highscore: " + highScore, 5, 150);
    }

    public void CheckScore() {                                                                                                  //writes the highscore to the file
        if (score > Integer.parseInt(highScore.split(":")[1]))                                                            //setting a new record, splitting the string into integer
        {
            frame = (JFrame) new Frame();
            JOptionPane.showMessageDialog(frame, "");
            String name = this.getName();
            highScore = name + ":" + score;

            File scoreFile = new File("highscore.dat");
            if (!scoreFile.exists()) {
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writeFile = null;
            BufferedWriter writer = null;
            try {
                writeFile = new FileWriter(scoreFile);
                writer = new BufferedWriter(writeFile);
                writer.write(this.highScore);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String args[]) {
        new Game();
    }

}
