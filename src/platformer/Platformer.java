/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platformer;

import javax.swing.JFrame;
/**
 *
 * @author Siva
 */
public class Platformer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("Platformer");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.setContentPane(new GameP());
      
        window.pack();
        window.setVisible(true);
    }
    
}
