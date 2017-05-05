/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;
import java.awt.image.*;
/**
 *
 * @author Siva
 */
public class Animation {
    
    private BufferedImage[] frames;
    
    private int currentFrame;
    
    private long startTime, delay;
    
    public Animation() {}
    
    public void setFrames(BufferedImage[] images) {
        frames = images;
        if(currentFrame >= frames.length)
            currentFrame = 0;
    }
    
    public void setDelay(long d) {
        delay = d;
    }
    
    public void update() {
        
        if(delay == -1)
            return;
        
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame >= frames.length)
            currentFrame = 0;
    }
    
    public BufferedImage getImage() {
        return frames[currentFrame];
    }
}
