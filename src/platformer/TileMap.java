/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;
import java.io.*;
import java.awt.*;
import java.awt.image.*; 
import javax.imageio.ImageIO;
/**
 *
 * @author Siva
 */
public class TileMap {
   
    private int x, y, minx, miny, maxx = 0, maxy = 0;
    
    private int tileSize;
    private int[][] map;
    private int mapWidth, mapHeight;
    
    private BufferedImage tileset;
    private Tile[][] tiles;
    
    public TileMap(String s, int tileSize) {
        this.tileSize = tileSize;
        
        try {
            
            BufferedReader br = new BufferedReader(new FileReader(s));
            
            mapWidth = Integer.parseInt(br.readLine());
            mapHeight = Integer.parseInt(br.readLine());
            map = new int[mapHeight][mapWidth];
            
            minx = GameP.WIDTH - mapWidth * tileSize;
            miny = GameP.HEIGHT - mapHeight * tileSize;
            
            String delimeters = "\\s+";
            for(int row = 0; row < mapHeight; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delimeters);
                for(int col = 0; col < mapWidth; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public void loadTiles(String s) {
        
        try {
           tileset = ImageIO.read(new File(s));
           int numTilesAcross = (tileset.getWidth() + 1) / (tileSize + 1);
           tiles = new Tile[2][numTilesAcross];
           
           BufferedImage subimage;
           for(int col = 0; col < numTilesAcross; col++) {
               subimage = tileset.getSubimage(col * tileSize + col,
                       0, tileSize, tileSize);
               tiles[0][col] = new Tile(subimage, false);
               subimage = tileset.getSubimage(col * tileSize + col,
                       tileSize + 1, tileSize, tileSize);
               tiles[1][col] = new Tile(subimage, true); 
           }
           
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public int getColTile(int x) {
        return x / tileSize;
    }
    
    public int getRowTile(int y) {
        return y / tileSize;
    }
    
    public int getTile(int row, int col) {
        return map[row][col];
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public boolean isBlocked(int row, int col) {
        int rc = map[row][col];
        int r = rc / tiles[0].length;
        int c = rc % tiles[0].length;
        return tiles[r][c].isBlocked();
    }
    
    public void setX(int i) { 
        x = i;
        if(x < minx) {
            x = minx;
        }
        if(x > maxx) {
            x = maxx;
        }
    }
    public void setY(int i) { 
        y = i;
        if(y < miny) {
            y = miny;
        }
        if(y > maxy) {
            y = maxy;
        }
    }
    
    public void update() {
        
    }
    
    public void draw(Graphics2D g) {
        
        for(int row = 0; row < mapHeight; row++) {
            for(int col = 0 ; col < mapWidth; col++ ) {
                
                int rc = map[row][col];
                
                int r = rc / tiles[0].length;
                int c = rc % tiles[0].length;
                
                g.drawImage(tiles[r][c].getImage(), 
                        x + col * tileSize, 
                        y + row * tileSize, 
                        null);
            }
        }
    }
}
