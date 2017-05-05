/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Siva
 */
public class GameP extends JPanel implements Runnable, KeyListener {
    
    public static final int WIDTH = 400, HEIGHT = 400;
    
    private Thread thread;
    
    private boolean running;
    
    private BufferedImage image;
    
    private Graphics2D g;
    
    private int FPS = 30, targetTime = 1000 / FPS;
    
    private TileMap tileMap;
    
    private Player player;
    
    public GameP() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
    }
    
    public void addNotify() {
        super.addNotify();
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }
    
    private void init() {
        
        running = true;
        
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        
        tileMap = new TileMap("testmap2.txt", 32); 
        tileMap.loadTiles("Graphics/tileset.gif");
        player = new Player(tileMap);
        player.setX(50);
        player.setY(50);
    }
    
    public void run() {
        
        init();
        
        long startTime;
        long URDTime;
        long waitTime;
        
        while(running) {
            startTime = System.nanoTime();
            
            update();
            render();
            draw();
            
            URDTime = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - URDTime;
            
            if(waitTime<0)
                waitTime = 5;
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameP.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    private void update() {
        tileMap.update();
        player.update();
    }
    
    private void render() {
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        tileMap.draw(g);
        player.draw(g);
    }
    
    private void draw() {
        Graphics g2 = this.getGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
    }

    
    public void keyTyped(KeyEvent e) {

    }


    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if(keyCode ==  KeyEvent.VK_LEFT){
            player.setLeft(true);
        }
        if(keyCode ==  KeyEvent.VK_RIGHT){
            player.setRight(true);
        }
        if(keyCode ==  KeyEvent.VK_SPACE){
            player.setJumping();
        }
    }

    
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if(keyCode ==  KeyEvent.VK_LEFT){
            player.setLeft(false);
        }
        if(keyCode ==  KeyEvent.VK_RIGHT){
            player.setRight(false);
        }
    }
    
}
