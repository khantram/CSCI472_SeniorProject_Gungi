package tram.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;
import java.io.File;

public class PieceDropDialog extends JDialog{
   private JButton[] dropPieceButtons = new JButton[13];
   private Image[] dropPieceImages = new Image[13];
   private JLabel[] dropPieceCounters = new JLabel[13];
   private int[] piecesRemaining = new int[13];
   private int color;
   
   public PieceDropDialog(int color) {
      this.color = color;
      
      piecesRemaining[0] = 1;
      for(int i = 1; i < 12; i++) {
         piecesRemaining[i] = 2;
      }
      piecesRemaining[12] = 9;
      
      loadImages();
      init();
   }
   
   private final void init() {
      this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
      
      this.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      
      this.setTitle("Select a piece to drop");
      this.setModal(true);
      
      Insets buttonMargin = new Insets(0, 0, 0, 0);
      for(int i = 0; i < dropPieceButtons.length; i++) {
         JButton b = new JButton();
         b.setMargin(buttonMargin);
         b.setBackground(Color.WHITE);
         
         b.setIcon(new ImageIcon(dropPieceImages[i]));
         b.putClientProperty("name", i);
         dropPieceButtons[i] = b;
         
         JLabel l = new JLabel();
         l.setText("" + piecesRemaining[i]);
         dropPieceCounters[i] = l;
         
         gbc.gridx = i;
         
         gbc.gridy = 0;
         this.add(b, gbc);
         
         gbc.gridy = 1;
         this.add(l, gbc);
      }
   }
   
   public JButton[] getDropPieceButtons() {
      return dropPieceButtons;
   }
   
   public void decrementPieceRemaining(int name) {
      piecesRemaining[name] -= 1;
      dropPieceCounters[name].setText("" + piecesRemaining[name]);
   }
   
   private final void loadImages() {
      try {
         BufferedImage img = ImageIO.read(new File("res/pieces_simple.png"));
         for(int i = 0; i < dropPieceImages.length; i++) {
            dropPieceImages[i] = img.getSubimage(i*50, color*50, 50, 50);
         }
      } catch(Exception e) {
         e.printStackTrace();
         System.exit(1);
      }
   }
}