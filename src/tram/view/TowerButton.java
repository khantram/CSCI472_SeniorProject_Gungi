package tram.view;

import java.util.ArrayList;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;
import java.io.File;

public class TowerButton extends JButton {
   private final Color captureColor = new Color(255, 85, 85, 100);
   private final Color moveColor = new Color(255, 255, 255, 100);
   private final Color rolloverColor = new Color(0, 102, 255, 100);
   
   private ArrayList<Piece> tower = new ArrayList<Piece>();
   private int row;
   private int col;
   private int currentTier;
   private boolean possibleCapture;
   private boolean possibleMove;
   
   public static final int MARSHAL = 0, GENERAL   = 1,  LT_GEN      = 2, 
                           MAJ_GEN = 3, COUNSEL   = 4,  SAMURAI     = 5,
                           KNIGHT  = 6, CANNON    = 7,  MUSKETEER   = 8,
                           ARCHER  = 9, FORTRESS  = 10, SPY         = 11,
                           PAWN    = 12;
   
   public static final int BLACK = 0, WHITE = 1;
   
   public TowerButton(int row, int col) {
      super.setContentAreaFilled(false);
      
      this.row = row;
      this.col = col;
      currentTier = 0;
      possibleCapture = false;
      possibleMove = false;
   }
   
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      g.setColor(getBackground());
      g.fillRect(0, 0, getWidth(), getHeight());
      
      if(possibleCapture) {
         g.setColor(captureColor);
         g.fillRect(0, 0, getWidth(), getHeight());
      } else if(possibleMove) {
         g.setColor(moveColor);
         g.fillRect(0, 0, getWidth(), getHeight());
      }
      
      if(!isEmpty()) {
         if(getModel().isRollover() && isEnabled()) {
            tower.get(currentTier-1).setRollovered(true);
         }
         else {
            tower.get(currentTier-1).setRollovered(false);
         }
      }
      else {
         if(getModel().isRollover() && isEnabled()) {
            g.setColor(rolloverColor);
            g.fillRect(0, 0, getWidth(), getHeight());
         }
      }
      
      for(TowerButton.Piece p : tower) {
         BufferedImage img;
         
         if(p.isRollovered()) {
            img = p.getRolloverImage();
         }
         else {
            img = p.getDefaultImage();
         }
         int y = 4 + (8*(3 - p.getTier()));
         g.drawImage(img, 2, y, null);
      }
   }
   
   public void pushPiece(int color, int piece) {
      tower.add(new Piece(color, piece, ++currentTier));
      repaint();
   }
   
   public int popPiece() {
      int name = tower.get(currentTier-1).getName();
      tower.remove(--currentTier);
      repaint();
      return name;
   }
   
   public void selectPieceRollover(int tier, boolean rollover) {
      tower.get(tier).setRollovered(rollover);
      repaint();
   }
   
   public boolean isCannon() {
      return (tower.get(currentTier-1).getName() == CANNON);
   }
   
   public boolean isPossibleCapture() {
      return possibleCapture;
   }
   
   public boolean isPossibleMove() {
      return possibleMove;
   }
   
   public boolean isEmpty() {
      return (currentTier == 0);
   }
   
   public int getPlayer() {
      return tower.get(currentTier-1).getColor();
   }
   
   public int getRow() {
      return row;
   }
   
   public int getCol() {
      return col;
   }
   
   public int getCurrentTier() {
      return currentTier;
   }
   
   public void setPossibleCapture(boolean possibleCapture) {
      this.possibleCapture = possibleCapture;
      repaint();
   }
   
   public void setPossibleMove(boolean possibleMove) {
      this.possibleMove = possibleMove;
      repaint();
   }
   
   class Piece {
      private int color;
      private int name;
      private int tier;
      private boolean rollovered;
      private BufferedImage defaultImage;
      private BufferedImage rolloverImage;
      
      public Piece(int color, int name, int tier) {
         this.color = color;
         this.name = name;
         this.tier = tier;
         rollovered = false;
         
         loadImages();
      }
      
      public void loadImages() {
         try {
            BufferedImage img = ImageIO.read(new File("res/pieces.png"));
            defaultImage = img.getSubimage(name*60, color*80, 60, 40);
            rolloverImage = img.getSubimage(name*60, (color*80)+40, 60, 40);
         } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
         }
      }
      
      public int getColor() {
         return color;
      }
      
      public int getName() {
         return name;
      }
      
      public int getTier() {
         return tier;
      }
      
      public void setRollovered(boolean rollovered) {
         this.rollovered = rollovered;
      }
      
      public BufferedImage getDefaultImage() {
         return defaultImage;
      }
      
      public BufferedImage getRolloverImage() {
         return rolloverImage;
      }
      
      public boolean isRollovered() {
         return rollovered;
      }
   }
}

