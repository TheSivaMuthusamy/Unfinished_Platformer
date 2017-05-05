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
public class Tile {
    
    private BufferedImage image;
    private boolean blocked;
    
    public Tile(BufferedImage image, boolean blocked) {
        this.image = image;
        this.blocked = blocked;
    }
    
    public BufferedImage getImage() { return image; }
    public boolean isBlocked() { return blocked; }
    
}
