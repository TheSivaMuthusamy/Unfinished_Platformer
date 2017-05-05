/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
/**
 *
 * @author Siva
 */
public class Player {
    
    private double x, y, dx, dy, moveSpeed, maxSpeed, maxFallingSpeed, 
            stopSpeed, jumpStart, gravity;
    
    private int width, height;
    
    private boolean left, right, jumping, falling, topLeft, topRight, bottomLeft,
            bottomRight, facingLeft;
    
    private Animation animation;
    private BufferedImage[] idleSprites, walkingSprites, jumpingSprites, fallingSprites;
    
    private TileMap tileMap;
    
    public Player(TileMap tm) {
        
        tileMap = tm;
        
        width = 22;
        height = 22;
        
        moveSpeed = 0.6;
        maxSpeed = 4.2;
        maxFallingSpeed = 12;
        stopSpeed = 0.30;
        jumpStart = -11.0;
        gravity = 0.64;
        
        try {
            
            idleSprites = new BufferedImage[1];
            jumpingSprites = new BufferedImage[1];
            fallingSprites = new BufferedImage[1];
            walkingSprites = new BufferedImage[6];
            
            idleSprites[0] = ImageIO.read(new File("Graphics/player/kirbyidle.gif"));
            jumpingSprites[0] = ImageIO.read(new File("Graphics/player/kirbyjump.gif"));
            fallingSprites[0] = ImageIO.read(new File("Graphics/player/kirbyfall.gif"));
            
            BufferedImage image = ImageIO.read(new File("Graphics/player/kirbywalk.gif"));
            for(int i = 0; i < walkingSprites.length; i++) {
                walkingSprites[i] = image.getSubimage(
                        i * width + i,
                        0,
                        width,
                        height
                );
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        facingLeft = false;
    }
    
    public void setX(int i) { x = i; }
    public void setY(int i) { y = i; }
    
    public void setLeft(boolean b) { left = b; };
    public void setRight(boolean b) { right = b; };
    public void setJumping() {
        if(!falling) {
            jumping = true;
        }    
    }
    
    private void calculateCorners(double x, double y) {
        int leftTile = tileMap.getColTile((int) (x - width /2));
        int rightTile = tileMap.getColTile((int) (x + width /2) - 1);
        int topTile = tileMap.getRowTile((int) (y - height /2));
        int bottomTile = tileMap.getRowTile((int) (y + height /2) - 1);
        topLeft = tileMap.isBlocked(topTile, leftTile);
        topRight = tileMap.isBlocked(topTile, rightTile);
        bottomLeft = tileMap.isBlocked(bottomTile, leftTile);
        bottomRight = tileMap.isBlocked(bottomTile, rightTile);    
    }
    
    public void update() {
        
        // determine next postion
        if(left) {
            dx -= moveSpeed;
            if(dx < -maxSpeed) {
                dx = -maxSpeed;
            }
        }
        else if(right) {
            dx += moveSpeed;
            if(dx > maxSpeed) {
                dx = maxSpeed;
            }
        }
        else {
            if( dx > 0) {
                dx -= stopSpeed;
                if(dx < 0) {
                    dx = 0;
                }
            }
            else if(dx < 0) {
                dx += stopSpeed;
                if(dx > 0) {
                    dx = 0;
                }
            }
        }
       
        if(jumping) {
            dy = jumpStart;
            falling = true;
            jumping = false;
        }
        
        if(falling) {
            dy += gravity;
            if(dy > maxFallingSpeed) {
                dy = maxFallingSpeed;
            }
        }
        else {
            dy = 0;
        }
        
        //collision detection
        
        int currentCol = tileMap.getColTile( (int) x);
        int currentRow = tileMap.getRowTile( (int) y);
        
        double tox = x + dx;
        double toy = y + dy;
        
        double tempx = x;
        double tempy = y;
        
        calculateCorners(x, toy);
        if(dy < 0) {
            if(topLeft || topRight) {
                dy = 0;
                tempy = currentRow * tileMap.getTileSize() + height / 2;
            }
            else {
                tempy += dy;
            }
        }
        if(dy > 0) {
            if(bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                tempy = (currentRow + 1) *  tileMap.getTileSize() - height / 2;
            }
            else {
                tempy += dy;
            }
        }
        
        calculateCorners(tox, y);
        if(dx < 0) {
            if(topLeft || bottomLeft) {
                dx = 0;
                tempx = currentCol *  tileMap.getTileSize() + width / 2;
            }
            else {
                tempx += dx;
            }
        }
        if(dx > 0) {
            if(topRight || bottomRight) {
                dx = 0;
                tempx = (currentCol + 1)  * tileMap.getTileSize() - width / 2;
            }
            else {
                tempx += dx;
            }
        }
        
        if(!falling) {
            calculateCorners(x, y + 1);
            if(!bottomLeft && !bottomRight) {
                falling = true;
            }
        }
        
        x = tempx;
        y = tempy;
        
        //scroll
        
        tileMap.setX((int) (GameP.WIDTH / 2 - x));
        tileMap.setY((int) (GameP.HEIGHT / 2 - y));
        
        //animation
        
        if(left || right) {
            animation.setFrames(walkingSprites);
            animation.setDelay(100);
        }
        else {
            animation.setFrames(idleSprites);
            animation.setDelay(-1);
        }
        if(dy < 0) {
            animation.setFrames(jumpingSprites);
            animation.setDelay(-1);
        }
        if(dy > 0) {
            animation.setFrames(fallingSprites);
            animation.setDelay(-1);
        }
        
        animation.update();
        
        if(dx < 0) {
            facingLeft = true;
        }
        if(dx > 0) {
            facingLeft = false;
        }
    }
    
    public void draw(Graphics2D g) {
        
        int tx = tileMap.getX();
        int ty = tileMap.getY();
        
        g.setColor(Color.red);
        if(facingLeft) {
            g.drawImage(animation.getImage(), 
                    (int) (tx + x - width / 2), 
                    (int) (ty + y - height / 2), 
                    null);
        }
        else {
            g.drawImage(animation.getImage(), 
                    (int) (tx + x - width / 2 + width), 
                    (int) (ty + y - height / 2), 
                    -width,
                    height,
                    null);
        }
    }
}
