package tram.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

public class ActionMenuPanel extends JPanel {
   private int totalButtons;
   private GridBagConstraints gbc;
   
   public ActionMenuPanel() {
      setOpaque(false);
      totalButtons = 0;
      init();
   }
   
   private final void init() {
      setLayout(new GridBagLayout());
      gbc = new GridBagConstraints();
      
      gbc.weightx = 0.33;
      gbc.weighty = 0.33;
      
      Dimension d = new Dimension(50, 50);
      JComponent rigidArea = new Box.Filler(d, d, d);
      for(int i = 0; i < 3; i++) {
         for(int j = 0; j < 3; j++) {
            gbc.gridx = j;
            gbc.gridy = i;
            add(rigidArea, gbc);
         }
      }
   }
   
   public void addButton(JButton b) {
      if(totalButtons < 4) {
         switch(totalButtons) {
            case 0:
               gbc.gridx = 0;
               gbc.gridy = 1;
               add(b, gbc);
               break;
            case 1:
               gbc.gridx = 2;
               gbc.gridy = 1;
               add(b, gbc);
               break;
            case 2:
               gbc.gridx = 1;
               gbc.gridy = 0;
               add(b, gbc);
               break;
            case 3:
               gbc.gridx = 1;
               gbc.gridy = 2;
               add(b, gbc);
         }
      }
      
      totalButtons++;
   }
   
}