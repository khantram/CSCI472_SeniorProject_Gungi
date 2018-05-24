
package tram.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class BoardGlassPane extends JComponent {
   public BoardGlassPane() {
      init();
   }
   
   private final void init() {
      setOpaque(false);
      setLayout(new GridLayout(0, 1));
      setFocusCycleRoot(true);
      
      JLabel padding = new JLabel();
      add(padding);
      
      addMouseListener(new MouseAdapter() {});
      addMouseMotionListener(new MouseMotionAdapter() {});
      addKeyListener(new KeyAdapter() {});
   }
}