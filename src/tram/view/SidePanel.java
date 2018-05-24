package tram.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

public class SidePanel extends JPanel {
   private int color;
   private int totalComponents;
   private GridBagConstraints gbc;
   private PieceSelectMenuPanel pieceSelectMenuPanel;
   
   public SidePanel() {
      totalComponents = 0;
      init();
   }
   
   private final void init() {
      setOpaque(true);
      setLayout(new BorderLayout());
      setBackground(new Color(223, 194, 127));
   }
   
   public void addComponent(JComponent component) {
      switch(totalComponents) {
         case 0:
            add(component, BorderLayout.PAGE_START);
            totalComponents++;
            break;
         case 1:
            add(component, BorderLayout.CENTER);
            totalComponents++;
            break;
         case 2:
            add(component, BorderLayout.PAGE_END);
            totalComponents++;
            break;
         default:
            System.err.println("ERROR: Maximum amount of panels reached.");
      }
   }
}