package tram.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

public class PieceSelectMenuPanel extends JPanel {
   private int color;
   private PieceSelectButton[] pieceSelectButtons;
   
   public PieceSelectMenuPanel(int color) {
      this.color = color;
      
      init();
   }
   
   private final void init() {
      setOpaque(true);
      setLayout(new GridBagLayout());
      setBackground(new Color(223, 194, 127));
            
      initButtons();
   }
   
   private void initButtons() {
      pieceSelectButtons = new PieceSelectButton[13];
      
      for(int i = 0; i < pieceSelectButtons.length; i++) {
         pieceSelectButtons[i] = new PieceSelectButton(color, i);
      }
      
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5, 5, 5, 5);
      
      for(int i = 1; i < 7; i++) {
         gbc.gridy = i;
         
         gbc.gridx = 0;
         add(pieceSelectButtons[i], gbc);
         
         gbc.gridx = 1;
         add(pieceSelectButtons[i+6], gbc);
      }
   }
   
   public PieceSelectButton[] getPieceSelectButtons() {
      return pieceSelectButtons;
   }
}