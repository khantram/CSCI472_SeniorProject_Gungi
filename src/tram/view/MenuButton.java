package tram.view;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

import javax.imageio.ImageIO;
import java.io.File;

public class MenuButton extends JButton {
   public static final int MOVE = 0, STACK = 1, CAPTURE = 2, DROP = 3, CANCEL = 4;
   
   private Shape shape;
   private BufferedImage defaultImage;
   private BufferedImage rolloverImage;
   private BufferedImage disabledImage;
   
   private int type;
   
   public MenuButton(int type) {
      setPreferredSize(new Dimension(50, 50));
      
      setFocusPainted(false);
      setBorderPainted(false);
      setContentAreaFilled(false);
      
      this.type = type;
      
      loadImages();
      
      setIcon(new ImageIcon(defaultImage));
      setRolloverIcon(new ImageIcon(rolloverImage));
      setDisabledIcon(new ImageIcon(disabledImage));
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
         BufferedImage img = ImageIO.read(new File("res/buttons.png"));
         defaultImage = img.getSubimage(type*50, 0, 50, 50);
         rolloverImage = img.getSubimage(type*50, 50, 50, 50);
         disabledImage = img.getSubimage(type*50, 100, 50, 50);
      } catch (Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   }
}