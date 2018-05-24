package tram.view;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

import javax.imageio.ImageIO;
import java.io.File;

public class PieceSelectButton extends JButton {
   public static final int MARSHAL = 0, GENERAL   = 1,  LT_GEN      = 2, 
                           MAJ_GEN = 3, COUNSEL   = 4,  SAMURAI     = 5,
                           KNIGHT  = 6, CANNON    = 7,  MUSKETEER   = 8,
                           ARCHER  = 9, FORTRESS  = 10, SPY         = 11,
                           PAWN    = 12;
                           
   public static final int BLACK = 0, WHITE = 1;
   
   private Shape shape;
   private BufferedImage defaultImage;
   private BufferedImage rolloverImage;
   private BufferedImage disabledImage;
   private BufferedImage[] counterImage;
   
   private int type;
   private int color;
   private int remaining;
   
   public PieceSelectButton(int color, int type) {
      setPreferredSize(new Dimension(50, 50));
      setMargin(new Insets(5, 5, 5, 5));
      
      setFocusPainted(false);
      setBorderPainted(false);
      setContentAreaFilled(false);
      
      counterImage = new BufferedImage[10];
      this.color = color;
      this.type = type;
      
      if(type == MARSHAL)
         remaining = 1;
      else if(type == PAWN)
         remaining = 9;
      else
         remaining = 2;
      
      loadImages();
   }
   
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      BufferedImage img;
      
      if(getModel().isRollover()) {
         img = rolloverImage;
      }
      else if(isEnabled()) {
         img = defaultImage;
      }
      else {
         img = disabledImage;
      }
      
      g.drawImage(img, 0, 0, null);
      g.drawImage(counterImage[remaining], 25, 25, null);
   }
   
   @Override
   public boolean contains(int x, int y) {
      if(shape == null || !shape.getBounds().equals(getBounds())) {
         shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
      }
      return shape.contains(x, y);
   }
   
   public void loadImages() {
      try {
         BufferedImage img = ImageIO.read(new File("res/pieces_simple.png"));
         defaultImage = img.getSubimage(type*50, color*150, 50, 50);
         rolloverImage = img.getSubimage(type*50, (color*150)+50, 50, 50);
         disabledImage = img.getSubimage(type*50, (color*150)+100, 50, 50);
         
         img = ImageIO.read(new File("res/counters.png"));
         for(int i = 0; i < 10; i++) {
            counterImage[i] = img.getSubimage(i*25, 0, 25, 25);
         }
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   }
   
   public void decrement() {
      --remaining;
      repaint();
   }
   
   public int getType() {
      return type;
   }
}