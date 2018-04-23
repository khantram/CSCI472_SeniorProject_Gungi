package tram.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import tram.controller.BoardViewController;

public class GameView extends JPanel implements BoardView{
   private final boolean AI = false;
   private BoardViewController controller;
   private JFrame frame;
   private JLayeredPane layeredPane;
   
   private BoardGlassPane glassPane1;
   private BoardGlassPane glassPane2;
   
   private JPanel tabletop;
   private JPanel board;
   private TowerButton[][] towers = new TowerButton[9][9];
   
   private JPanel cardNotificationPanel;
   
   // MENUS
   private JPanel cardSidePanel;
   
   private JPanel blackCardPiecePanel;
   private PieceSelectButton[] blackPieceSelectButtons;
   
   private JPanel whiteCardPiecePanel;
   private PieceSelectButton[] whitePieceSelectButtons;
   
   private ActionMenuPanel attackMenuPanel;
   private MenuButton attackMenuCaptureButton;
   private MenuButton attackMenuStackButton;
   private MenuButton attackMenuCancelButton;
   
   private ActionMenuPanel dropMenuPanel;
   private MenuButton dropMenuDropButton;
   private MenuButton dropMenuCancelButton;
   
   private ActionMenuPanel moveMenuPanel;
   private MenuButton moveMenuMoveButton;
   private MenuButton moveMenuCancelButton;
   
   private ActionMenuPanel stackMenuPanel;
   private MenuButton stackMenuStackButton;
   private MenuButton stackMenuCancelButton;
   
   // STATES
   private int currentPlayerState;
   private boolean blackPlacementPhaseState;
   private boolean whitePlacementPhaseState;
   private boolean firstPlacementState;
   
   private Point movingPieceSelectedState;
   private Point movingPieceDestinationState;
   
   private int droppingPieceSelectedState;
   private Point droppingPiecePositionState;
   
   public GameView(BoardViewController controller) {
      setBoardViewController(controller);
      init();
   }
   
   private final void init() {
      Color color_d2ab50ff = new Color(210, 171, 80);
      Color color_5d3c1fff = new Color(93, 60, 31);
      
      layeredPane = new JLayeredPane();
      layeredPane.setPreferredSize(new Dimension(1024, 675));
      
      tabletop = new JPanel(new GridBagLayout());
      tabletop.setBounds(0, 0, 652, 675);
      tabletop.setBackground(color_d2ab50ff);
      
      initGlassPanes();
      initCardNotificationPanel();
      initCardPiecePanels();
      initSidePanels();
      initAttackMenuPanel();
      initDropMenuPanel();
      initMoveMenuPanel();
      initStackMenuPanel();
      
      tabletop.setBorder(new EmptyBorder(5, 5, 5, 5));
      GridBagConstraints gbc = new GridBagConstraints();
      
      Dimension v = new Dimension(20, 64);
      Dimension h = new Dimension(64, 20);
      
      // SETTING LABELED BORDERS AROUND GAMEBOARD
      for(int i = 1; i < 10; i++) {
         JLabel rowLabels = new JLabel("" + (10-i), SwingConstants.CENTER);
         rowLabels.setPreferredSize(v);
         
         JLabel colLabels = new JLabel("" + i, SwingConstants.CENTER);
         colLabels.setPreferredSize(h);
         
         gbc.weightx = 0.09;
         gbc.weighty = 0.09;
         
         // - VERTICAL BORDERS/EDGES
         // --- LEFT EDGE
         gbc.gridx = 0;
         gbc.gridy = i;
         tabletop.add(rowLabels, gbc);
         
         /* --- RIGHT EDGE
         gbc.gridx = 10;
         gbc.gridy = i;
         tabletop.add(rowLabels, gbc);
         //*/
         
         // - HORIZONTAL BORDERS/EDGES
         /* --- TOP EDGE
         gbc.gridx = i;
         gbc.gridy = 0;
         tabletop.add(colLabels, gbc);
         //*/
         
         // --- BOTTOM EDGE
         gbc.gridx = i;
         gbc.gridy = 10;
         tabletop.add(colLabels, gbc);
      }
      
      JComponent vRigidArea = new Box.Filler(v, v, v);
      JComponent hRigidArea = new Box.Filler(h, h, h);
      
      gbc.gridx = 10;
      gbc.gridy = 5;
      tabletop.add(vRigidArea, gbc);
      
      gbc.gridx = 5;
      gbc.gridy = 0;
      tabletop.add(hRigidArea, gbc);
      
      board = new JPanel(new GridLayout(0, 9)) {
         @Override
         public final Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            Dimension prefSize = null;
            Component c = getParent();
            
            if(c == null) {
               prefSize = new Dimension((int)d.getWidth(), (int)d.getHeight());
            }
            else if(c != null && 
                    c.getWidth() > d.getWidth() && 
                    c.getHeight() > d.getHeight()) {
               prefSize = c.getSize();
            }
            else {
               prefSize = d;
            }
            
            int w = (int) prefSize.getWidth();
            int h = (int) prefSize.getHeight();
            
            int s = (w > h ? h : w);
            return new Dimension(s, s);
         }
      };
      
      // SETS BORDER AROUND GAME BOARD
      board.setBorder(new LineBorder(color_5d3c1fff));
      
      JPanel boardConstraint = new JPanel(new GridBagLayout());
      boardConstraint.add(board);
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.gridwidth = 9;
      gbc.gridheight = 9;
      tabletop.add(boardConstraint, gbc);
      
      Insets buttonMargin = new Insets(0, 0, 0, 0);
      for(int i = 0; i < towers.length; i++) { 
         for(int j = 0; j < towers[i].length; j++) {
            TowerButton b = new TowerButton(i, j);
            b.setMargin(buttonMargin);
            
            ImageIcon empty = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
            b.setIcon(empty);
            
            b.setBackground(color_d2ab50ff);
            
            Border border = BorderFactory.createLineBorder(color_5d3c1fff);
            b.setBorder(border);
            
            b.putClientProperty("position", new Point(i, j));
            //b.addMouseListener(new pieceML());
            b.addActionListener(new TowerButtonAL());
            
            towers[i][j] = b;
            board.add(towers[i][j]);
         }
      }
      
      layeredPane.add(tabletop, new Integer(1));
      add(layeredPane);
      
      newGame();
   }
   
   /* /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ Initialization Methods /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ */
   private void initGlassPanes() {
      glassPane1 = new BoardGlassPane();
      glassPane1.setBounds(0, 0, 652, 675);
      glassPane1.setVisible(false);
      layeredPane.add(glassPane1, new Integer(10));
      
      glassPane2 = new BoardGlassPane();
      glassPane2.setBounds(0, 0, 652, 675);
      glassPane2.setVisible(false);
      layeredPane.add(glassPane2, new Integer(10));
   }
   
   private void initCardNotificationPanel() {
      try {
         BufferedImage img = ImageIO.read(new File("res/notifications.png"));
         BufferedImage temp;
         
         cardNotificationPanel = new JPanel(new CardLayout());
         cardNotificationPanel.setOpaque(false);
         
         temp = img.getSubimage(0, 0, 652, 150);
         JLabel blackPlaceMarshalLabel = new JLabel(new ImageIcon(temp));
         cardNotificationPanel.add(blackPlaceMarshalLabel, "BLACK_MARSHAL");
         
         temp = img.getSubimage(0, 150, 652, 150);
         JLabel whitePlaceMarshalLabel = new JLabel(new ImageIcon(temp));
         cardNotificationPanel.add(whitePlaceMarshalLabel, "WHITE_MARSHAL");
         
         temp = img.getSubimage(0, 300, 652, 150);
         JLabel blackPlacementPhaseLabel = new JLabel(new ImageIcon(temp));
         cardNotificationPanel.add(blackPlacementPhaseLabel, "BLACK_PLACEMENT");
         
         temp = img.getSubimage(0, 450, 652, 150);
         JLabel whitePlacementPhaseLabel = new JLabel(new ImageIcon(temp));
         cardNotificationPanel.add(whitePlacementPhaseLabel, "WHITE_PLACEMENT");
         
         temp = img.getSubimage(0, 600, 652, 150);
         JLabel blackWinLabel = new JLabel(new ImageIcon(temp));
         cardNotificationPanel.add(blackWinLabel, "BLACK_WIN");
         
         temp = img.getSubimage(0, 750, 652, 150);
         JLabel whiteWinLabel = new JLabel(new ImageIcon(temp));
         cardNotificationPanel.add(whiteWinLabel, "WHITE_WIN");
         
         temp = img.getSubimage(0, 900, 652, 150);
         JLabel placementPhaseCompletedLabel = new JLabel(new ImageIcon(temp));
         cardNotificationPanel.add(placementPhaseCompletedLabel, "PLACEMENT_COMPLETED");
         
         cardNotificationPanel.setBounds(0, 263, 652, 150);
         cardNotificationPanel.setVisible(false);
         layeredPane.add(cardNotificationPanel, new Integer(20));
      }
      catch (IOException e){
         System.err.println("ERROR: \"res/notifications.png\" not found... Exiting program.");
         System.exit(1);
      }
   }
   
   private void initCardPiecePanels() { 
      try {
         BufferedImage img = ImageIO.read(new File("res/side_piece_label.png"));
      
         blackCardPiecePanel = new JPanel(new CardLayout());
         blackCardPiecePanel.setOpaque(false);
         
         whiteCardPiecePanel = new JPanel(new CardLayout());
         whiteCardPiecePanel.setOpaque(false);
         
         for(int i = 0; i < 13; i++) {
            BufferedImage bImg = img.getSubimage(0, i*240,372, 240);
            JLabel bImgLabel = new JLabel(new ImageIcon(bImg));
            blackCardPiecePanel.add(bImgLabel, Integer.toString(i));
            
            BufferedImage wImg = img.getSubimage(372, i*240, 372, 240);
            JLabel wImgLabel = new JLabel(new ImageIcon(wImg));
            whiteCardPiecePanel.add(wImgLabel, Integer.toString(i));
         }
         
         CardLayout cl = (CardLayout)(blackCardPiecePanel.getLayout());
         cl.show(blackCardPiecePanel, "0");
         
         cl = (CardLayout)(whiteCardPiecePanel.getLayout());
         cl.show(whiteCardPiecePanel, "0");
      }
      catch(IOException e) {
         System.err.println("ERROR: \"res/side_piece_label.png\" not found... Exiting Program.");
         System.exit(1);
      }
      
   }
   
   private void initSidePanels() {
      try {
         cardSidePanel = new JPanel(new CardLayout());
         
         BufferedImage img = ImageIO.read(new File("res/side_label.png"));
         BufferedImage selectImg = img.getSubimage(0, 360, 372, 120);
         BufferedImage selectedImg = img.getSubimage(0, 240, 372, 120);
         
         // BLACK SIDE PANELS
         BufferedImage bImg = img.getSubimage(0, 0, 372, 120);
         JLabel blackPlayerTurnLabel1 = new JLabel(new ImageIcon(bImg));
         JLabel blackPlayerTurnLabel2 = new JLabel(new ImageIcon(bImg));
         
         JLabel blackSelectPieceLabel = new JLabel(new ImageIcon(selectImg));
         JLabel blackSelectedPieceLabel = new JLabel(new ImageIcon(selectedImg));
         
         // -> Player Turn Side Panel
         SidePanel blackTurnSidePanel = new SidePanel();
         blackTurnSidePanel.addComponent(blackPlayerTurnLabel1);
         
         PieceSelectMenuPanel blackPieceSelectMenuPanel = new PieceSelectMenuPanel(0);
         blackPieceSelectButtons = blackPieceSelectMenuPanel.getPieceSelectButtons();
         for(int i = 0; i < blackPieceSelectButtons.length; i++) {
            blackPieceSelectButtons[i].addActionListener(new PieceSelectButtonAL());
         }
         blackTurnSidePanel.addComponent(blackPieceSelectMenuPanel);
         
         blackTurnSidePanel.addComponent(blackSelectPieceLabel);
         
         // -> Piece Selected Side Panel
         SidePanel blackPieceSelectedSidePanel = new SidePanel();
         blackPieceSelectedSidePanel.addComponent(blackPlayerTurnLabel2);
         blackPieceSelectedSidePanel.addComponent(blackCardPiecePanel);
         blackPieceSelectedSidePanel.addComponent(blackSelectedPieceLabel);
         
         // -> Add to cardSidePanel
         cardSidePanel.add(blackTurnSidePanel, "BLACK_TURN");
         cardSidePanel.add(blackPieceSelectedSidePanel, "BLACK_SELECTED");
         
         // WHITE SIDE PANELS
         BufferedImage wImg = img.getSubimage(0, 120, 372, 120);
         JLabel whitePlayerTurnLabel1 = new JLabel(new ImageIcon(wImg));
         JLabel whitePlayerTurnLabel2 = new JLabel(new ImageIcon(wImg));
         
         JLabel whiteSelectPieceLabel = new JLabel(new ImageIcon(selectImg));
         JLabel whiteSelectedPieceLabel = new JLabel(new ImageIcon(selectedImg));
         
         // -> Player Turn Side Panel
         SidePanel whiteTurnSidePanel = new SidePanel();
         whiteTurnSidePanel.addComponent(whitePlayerTurnLabel1);
         
         PieceSelectMenuPanel whitePieceSelectMenuPanel = new PieceSelectMenuPanel(1);
         whitePieceSelectButtons = whitePieceSelectMenuPanel.getPieceSelectButtons();
         for(int i = 0; i < whitePieceSelectButtons.length; i++) {
            whitePieceSelectButtons[i].addActionListener(new PieceSelectButtonAL());
         }
         whiteTurnSidePanel.addComponent(whitePieceSelectMenuPanel);
         
         whiteTurnSidePanel.addComponent(whiteSelectPieceLabel);
         
         // -> Piece Selected Side Panel
         SidePanel whitePieceSelectedSidePanel = new SidePanel();
         whitePieceSelectedSidePanel.addComponent(whitePlayerTurnLabel2);
         whitePieceSelectedSidePanel.addComponent(whiteCardPiecePanel);
         whitePieceSelectedSidePanel.addComponent(whiteSelectedPieceLabel);
         
         // -> Add to cardSidePanel
         cardSidePanel.add(whiteTurnSidePanel, "WHITE_TURN");
         cardSidePanel.add(whitePieceSelectedSidePanel, "WHITE_SELECTED");
         
         // ADD cardSidePanel TO layeredPane
         cardSidePanel.setBounds(652, 0, 372, 675);
         cardSidePanel.setVisible(true);
         layeredPane.add(cardSidePanel, new Integer(1));
      } catch(IOException e) {
         System.err.println("ERROR: \"res/side_label.png\" not found... Exiting Program.");
         System.exit(1);
      }
   }
   
   private void initAttackMenuPanel() {
      attackMenuPanel = new ActionMenuPanel();
      
      attackMenuCaptureButton = new MenuButton(MenuButton.CAPTURE);
      attackMenuStackButton = new MenuButton(MenuButton.STACK);
      attackMenuCancelButton = new MenuButton(MenuButton.CANCEL);
      
      attackMenuCaptureButton.addActionListener(new CaptureButtonAL());
      attackMenuStackButton.addActionListener(new MoveButtonAL());
      attackMenuCancelButton.addActionListener(new CancelButtonAL());
      
      attackMenuPanel.addButton(attackMenuCaptureButton);
      attackMenuPanel.addButton(attackMenuCancelButton);
      attackMenuPanel.addButton(attackMenuStackButton);
      
      attackMenuPanel.setBounds(0, 0, 150, 150);
      attackMenuPanel.setVisible(false);
      layeredPane.add(attackMenuPanel, new Integer(11));
   }
   
   private void initDropMenuPanel() {
      dropMenuPanel = new ActionMenuPanel();
      
      dropMenuDropButton = new MenuButton(MenuButton.DROP);
      dropMenuCancelButton = new MenuButton(MenuButton.CANCEL);
      
      dropMenuDropButton.addActionListener(new DropButtonAL());
      dropMenuCancelButton.addActionListener(new CancelButtonAL());
      
      dropMenuPanel.addButton(dropMenuDropButton);
      dropMenuPanel.addButton(dropMenuCancelButton);
      
      dropMenuPanel.setBounds(0, 0, 150, 150);
      dropMenuPanel.setVisible(false);
      layeredPane.add(dropMenuPanel, new Integer(11));
   }
   
   private void initMoveMenuPanel() {
      moveMenuPanel = new ActionMenuPanel();
      
      moveMenuMoveButton = new MenuButton(MenuButton.MOVE);
      moveMenuCancelButton = new MenuButton(MenuButton.CANCEL);
      
      moveMenuMoveButton.addActionListener(new MoveButtonAL());
      moveMenuCancelButton.addActionListener(new CancelButtonAL());
      
      moveMenuPanel.addButton(moveMenuMoveButton);
      moveMenuPanel.addButton(moveMenuCancelButton);
      
      moveMenuPanel.setBounds(0, 0, 150, 150);
      moveMenuPanel.setVisible(false);
      layeredPane.add(moveMenuPanel, new Integer(11));
   }
   
   private void initStackMenuPanel() {
      stackMenuPanel = new ActionMenuPanel();
      
      stackMenuStackButton = new MenuButton(MenuButton.STACK);
      stackMenuCancelButton = new MenuButton(MenuButton.CANCEL);
      
      stackMenuStackButton.addActionListener(new MoveButtonAL());
      stackMenuCancelButton.addActionListener(new CancelButtonAL());
      
      stackMenuPanel.addButton(stackMenuStackButton);
      stackMenuPanel.addButton(stackMenuCancelButton);
      
      stackMenuPanel.setBounds(0, 0, 150, 150);
      stackMenuPanel.setVisible(false);
      layeredPane.add(stackMenuPanel, new Integer(11));
   }
   
   /* /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ Other Methods /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ */
   
   private void newGame() {
      // INITIALIZE STATES
      blackPlacementPhaseState = true;
      whitePlacementPhaseState = true;
      firstPlacementState = true;
      movingPieceDestinationState = null;
      droppingPiecePositionState = null;
      droppingPieceSelectedState = -1;
      
      currentPlayerState = TowerButton.BLACK;
      disableTowerButtonRows(4, 9);
      
      cardNotificationPanel.setVisible(true);
      
      CardLayout cn = (CardLayout)(cardNotificationPanel.getLayout());
      cn.show(cardNotificationPanel, "BLACK_MARSHAL");
      
      CardLayout cs = (CardLayout)(cardSidePanel.getLayout());
      cs.show(cardSidePanel, "BLACK_SELECTED");
      
      //cl.show(cardSidePanel, "BLACK_TURN");
      //cl.show(cardSidePanel, "BLACK_SELECTED");
      //cl.show(cardSidePanel, "WHITE_TURN");
      //cl.show(cardSidePanel, "WHITE_SELECTED");
      
   }
   
   private void cancelSelection() {
      movingPieceSelectedState = null;
      movingPieceDestinationState = null;
      droppingPieceSelectedState = -1;
      droppingPiecePositionState = null;
      
      unhighlightAllMoves();
      enableAllTowerButtons();
      disableNonPlayerTowerButtons();
   }
   
   private void enableAllMenuButtons() {
      attackMenuCaptureButton.setEnabled(true);
      attackMenuStackButton.setEnabled(true);
      dropMenuDropButton.setEnabled(true);
      moveMenuMoveButton.setEnabled(true);
      stackMenuStackButton.setEnabled(true);
   }
   
   private void enableAllTowerButtons() {
      for(int i = 0; i < towers.length; i++) {
         for(int j = 0; j < towers[i].length; j++) {
            towers[i][j].setEnabled(true);
         }
      }
   }
   
   private void disableAllTowerButtons() {
      for(int i = 0; i < towers.length; i++) {
         for(int j = 0; j < towers[i].length; j++) {
            towers[i][j].setEnabled(false);
         }
      }
   }
   
   private void disableNonPlayerTowerButtons() {
      for(int i = 0; i < towers.length; i++) {
         for(int j = 0; j < towers[i].length; j++) {
            if(towers[i][j].isEmpty() || towers[i][j].getPlayer() != currentPlayerState) {
               towers[i][j].setEnabled(false);
            }
         }
      }
   }
   
   private void disableTowerButtonRows(int start, int end) {
      for(int i = start-1; i < end; i++) {
         for(int j = 0; j < towers[i].length; j++) {
            towers[i][j].setEnabled(false);
         }
      }
   }
   
   private void showAttackMenuPanel(int x, int y) {
      attackMenuPanel.setLocation(x-15, y-2);
      attackMenuPanel.setVisible(true);
      glassPane1.setVisible(true);
   }
   
   private void showDropMenuPanel(int x, int y) {
      dropMenuPanel.setLocation(x-15, y+35);
      dropMenuPanel.setVisible(true);
      glassPane1.setVisible(true);
   }
   
   private void showMoveMenuPanel(int x, int y) {
      moveMenuPanel.setLocation(x-15, y-2);
      moveMenuPanel.setVisible(true);
      glassPane1.setVisible(true);
   }
   
   private void forceDropPieceSelection() {
      glassPane2.setVisible(true);
   }
   
   private void showStackMenuPanel(int x, int y) {
      stackMenuPanel.setLocation(x-15, y-2);
      stackMenuPanel.setVisible(true);
      glassPane1.setVisible(true);
   }
   
   public void unhighlightAllMoves() {
      for(int i = 0; i < towers.length; i ++) {
         for(int j = 0; j < towers[i].length; j++) {
            towers[i][j].setPossibleCapture(false);
            towers[i][j].setPossibleMove(false);
         }
      }
   }
   
   /* /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ BoardView Interface Methods /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ */
   @Override
   public void decrementRemainingPieces(int type) {
      if(currentPlayerState == TowerButton.BLACK) {
         blackPieceSelectButtons[type].decrement();
      }
      else {
         whitePieceSelectButtons[type].decrement();
      }
   }
   
   @Override
   public void disableCaptureButton() {
      attackMenuCaptureButton.setEnabled(false);
      
   }
   
   @Override
   public void disableDropButton() {
      dropMenuDropButton.setEnabled(false);
   }
   
   @Override
   public void disableMoveButton() {
      moveMenuMoveButton.setEnabled(false);
      attackMenuStackButton.setEnabled(false);
      stackMenuStackButton.setEnabled(false);
   }
   
   @Override
   public void disablePieceSelectButton(int type) {
      if(currentPlayerState == TowerButton.BLACK) {
         blackPieceSelectButtons[type].setEnabled(false);
      }
      else {
         whitePieceSelectButtons[type].setEnabled(false);
      }
   }
   
   @Override
   public void endPlacementPhase() {
      if(currentPlayerState == TowerButton.BLACK) {
         blackPlacementPhaseState = false;
      }
      else {
         whitePlacementPhaseState = false;
      }
   }
   
   @Override
   public void gameOver() {
      
   }
   
   @Override
   public void highlightPossibleMoves(Point[] captures, Point[] moves) {
      for(int i = 0; i < captures.length; i++) {
         Point p = captures[i];
         towers[p.x][p.y].setPossibleCapture(true);
      }
      
      for(int i = 0; i < moves.length; i++) {
         Point p = moves[i];
         towers[p.x][p.y].setPossibleMove(true);
      }
   }
   
   @Override
   public void isolatePossibleMoves(Point[] captures, Point[] moves) {
      disableAllTowerButtons();
      
      for(int i = 0; i < captures.length; i++) {
         Point p = captures[i];
         towers[p.x][p.y].setEnabled(true);
      }
      
      for(int i = 0; i < moves.length; i++) {
         Point p = moves[i];
         towers[p.x][p.y].setEnabled(true);
      }
      
      towers[movingPieceSelectedState.x][movingPieceSelectedState.y].setEnabled(true);
   }
   
   @Override
   public void setBoardViewController(BoardViewController controller) {
      this.controller = controller;
   }
   
   @Override
   public void switchPlayers() {
      unhighlightAllMoves();
      enableAllTowerButtons();
      
      CardLayout cl = (CardLayout)(cardSidePanel.getLayout());
      
      if(currentPlayerState == TowerButton.BLACK)
      {
         currentPlayerState = TowerButton.WHITE;
         controller.openedPieceSelectMenu(GameView.this, currentPlayerState);
         cl.show(cardSidePanel, "WHITE_TURN");
         disableNonPlayerTowerButtons();
      }
      else
      {
         if(AI) {
            
         }
         currentPlayerState = TowerButton.BLACK;
         controller.openedPieceSelectMenu(GameView.this, currentPlayerState);
         cl.show(cardSidePanel, "BLACK_TURN");
         disableNonPlayerTowerButtons();
      }
   }
   
   @Override
   public void switchPhase() {
      CardLayout cn = (CardLayout)(cardNotificationPanel.getLayout());
      CardLayout cs = (CardLayout)(cardSidePanel.getLayout());
      
      if(!(blackPlacementPhaseState || whitePlacementPhaseState)) {
         currentPlayerState = TowerButton.WHITE;
         enableAllTowerButtons();
         disableNonPlayerTowerButtons();
         cn.show(cardNotificationPanel, "PLACEMENT_COMPLETED");
         controller.openedPieceSelectMenu(GameView.this, currentPlayerState);
         cs.show(cardSidePanel, "WHITE_TURN");
      }
      else {
         if(currentPlayerState == TowerButton.BLACK) {
            if(whitePlacementPhaseState) {
               enableAllTowerButtons();
               disableTowerButtonRows(1, 6);
               currentPlayerState = TowerButton.WHITE;
               
               if(firstPlacementState) {
                  cn.show(cardNotificationPanel, "WHITE_MARSHAL");
                  cs.show(cardSidePanel, "WHITE_SELECTED");
               }
               else {
                  controller.openedPieceSelectMenu(GameView.this, currentPlayerState);
                  forceDropPieceSelection();
                  cn.show(cardNotificationPanel, "WHITE_PLACEMENT");
                  controller.openedPieceSelectMenu(GameView.this, currentPlayerState);
                  cs.show(cardSidePanel, "WHITE_TURN");
               }
            }
         }
         else {
            if(blackPlacementPhaseState) {
               if(firstPlacementState) {
                  firstPlacementState = false;
               }
               
               enableAllTowerButtons();
               disableTowerButtonRows(4, 9);
               currentPlayerState = TowerButton.BLACK;
               controller.openedPieceSelectMenu(GameView.this, currentPlayerState);
               forceDropPieceSelection();
               cn.show(cardNotificationPanel, "BLACK_PLACEMENT");
               controller.openedPieceSelectMenu(GameView.this, currentPlayerState);
               cs.show(cardSidePanel, "BLACK_TURN");
            }
         }
      }
   }
   
   /* /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ Action Listener Classes /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ */
   private class CancelButtonAL implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if(firstPlacementState) {
            dropMenuPanel.setVisible(false);
            glassPane1.setVisible(false);
         }
         else {
            if(blackPlacementPhaseState || whitePlacementPhaseState) {
               droppingPieceSelectedState = -1;
               forceDropPieceSelection();
               dropMenuPanel.setVisible(false);
               glassPane1.setVisible(false);
            }
            else {
               movingPieceSelectedState = null;
               movingPieceDestinationState = null;
               droppingPieceSelectedState = -1;
               droppingPiecePositionState = null;
               
               unhighlightAllMoves();
               enableAllTowerButtons();
               disableNonPlayerTowerButtons();
               
               attackMenuPanel.setVisible(false);
               dropMenuPanel.setVisible(false);
               moveMenuPanel.setVisible(false);
               stackMenuPanel.setVisible(false);
               glassPane1.setVisible(false);
            }
            
            if(currentPlayerState == TowerButton.BLACK) {
               CardLayout cl = (CardLayout)(cardSidePanel.getLayout());
               cl.show(cardSidePanel, "BLACK_TURN");
            }
            else {
               CardLayout cl = (CardLayout)(cardSidePanel.getLayout());
               cl.show(cardSidePanel, "WHITE_TURN");
            }
         }
      }
   }
   
   private class CaptureButtonAL implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         Point p1 = movingPieceSelectedState;
         Point p2 = movingPieceDestinationState;
         
         int name = towers[p1.x][p1.y].popPiece();
         towers[p2.x][p2.y].popPiece();
         towers[p2.x][p2.y].pushPiece(currentPlayerState, name);
         
         controller.pieceCaptured(GameView.this, p1.x, p1.y, p2.x, p2.y);
         
         movingPieceSelectedState = null;
         movingPieceDestinationState = null;
         
         attackMenuPanel.setVisible(false);
         glassPane1.setVisible(false);
      }
   }
   
   private class DropButtonAL implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         Point p = droppingPiecePositionState;
         
         towers[p.x][p.y].pushPiece(currentPlayerState, droppingPieceSelectedState);
         
         if(blackPlacementPhaseState || whitePlacementPhaseState) {
            controller.piecePlaced(GameView.this, currentPlayerState, droppingPieceSelectedState, p.x, p.y);
         }
         else {
            controller.pieceDropped(GameView.this, droppingPieceSelectedState, p.x, p.y);
         }
         
         droppingPiecePositionState = null;
         droppingPieceSelectedState = -1;
         
         dropMenuPanel.setVisible(false);
         glassPane1.setVisible(false);
      }
   }
   
   private class MoveButtonAL implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         Point p1 = movingPieceSelectedState;
         Point p2 = movingPieceDestinationState;
         
         int name = towers[p1.x][p1.y].popPiece();
         towers[p2.x][p2.y].pushPiece(currentPlayerState, name);
         
         controller.pieceMoved(GameView.this, p1.x, p1.y, p2.x, p2.y);
         
         movingPieceSelectedState = null;
         movingPieceDestinationState = null;
         
         attackMenuPanel.setVisible(false);
         moveMenuPanel.setVisible(false);
         stackMenuPanel.setVisible(false);
         glassPane1.setVisible(false);
      }
   }
   
   private class PieceSelectButtonAL implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if(!(blackPlacementPhaseState || whitePlacementPhaseState)) {
            cardNotificationPanel.setVisible(false);
            enableAllTowerButtons();
         }
         
         PieceSelectButton b = (PieceSelectButton) e.getSource();
         droppingPieceSelectedState = b.getType();
         
         if(currentPlayerState == TowerButton.BLACK) {
            CardLayout cl = (CardLayout)(blackCardPiecePanel.getLayout());
            cl.show(blackCardPiecePanel, Integer.toString(droppingPieceSelectedState));
            
            cl = (CardLayout)(cardSidePanel.getLayout());
            cl.show(cardSidePanel, "BLACK_SELECTED");
         }
         else {
            CardLayout cl = (CardLayout)(whiteCardPiecePanel.getLayout());
            cl.show(whiteCardPiecePanel, Integer.toString(droppingPieceSelectedState));
            
            cl = (CardLayout)(cardSidePanel.getLayout());
            cl.show(cardSidePanel, "WHITE_SELECTED");
         }
         
         glassPane2.setVisible(false);
      }
   }
   
   private class TowerButtonAL implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if(!(blackPlacementPhaseState || whitePlacementPhaseState)) {
            cardNotificationPanel.setVisible(false);
         }
         
         enableAllMenuButtons();
         
         TowerButton b = (TowerButton) e.getSource();
         Point origin = b.getLocation();
         Point position = (Point) b.getClientProperty("position");
         
         if(b.isEmpty()) {
            if(firstPlacementState) {
               droppingPieceSelectedState = TowerButton.MARSHAL;
               droppingPiecePositionState = position;
               showDropMenuPanel(origin.x, origin.y);
            }
            else if(blackPlacementPhaseState || whitePlacementPhaseState) {
               droppingPiecePositionState = position;
               controller.openedDropMenu(GameView.this, position.x, position.y);
               showDropMenuPanel(origin.x, origin.y);
            }
            else if(droppingPieceSelectedState != -1) {
               droppingPiecePositionState = position;
               controller.openedDropMenu(GameView.this, position.x, position.y);
               showDropMenuPanel(origin.x, origin.y);
            }
            else if(movingPieceSelectedState == null) {
               // SHOULDN'T NEED TO CHECK THIS STATE (?)
               System.err.println(" ! ! ! ! ! ! ! ! ! ! ! \n REACHED AN UNPREDICTED STATE NUMERO UNO \n ! ! ! ! ! ! ! ! ! ! ! ");
            }
            else {
               movingPieceDestinationState = position;
               controller.openedMoveMenu(GameView.this, position.x, position.y);
               showMoveMenuPanel(origin.x, origin.y);
            }
         }
         else if(b.getPlayer() == currentPlayerState) {
            if(blackPlacementPhaseState || whitePlacementPhaseState) {
               droppingPiecePositionState = position;
               controller.openedDropMenu(GameView.this, position.x, position.y);
               showDropMenuPanel(origin.x, origin.y);
            }
            else if(droppingPieceSelectedState != -1) {                    // IDENTICAL TO ABOVE... COMBINE THEM
               droppingPiecePositionState = position;
               controller.openedDropMenu(GameView.this, position.x, position.y);
               showDropMenuPanel(origin.x, origin.y);
            }
            else if(movingPieceSelectedState == null) {
               movingPieceSelectedState = position;
               controller.pieceSelected(GameView.this, position.x, position.y);
            }
            else if(movingPieceSelectedState == position) {
               cancelSelection();
            }
            else {
               movingPieceDestinationState = position;
               controller.openedMoveMenu(GameView.this, position.x, position.y);
               showStackMenuPanel(origin.x, origin.y);
            }
         }
         else {
            if(droppingPieceSelectedState != -1) {
               droppingPiecePositionState = position;
               controller.openedDropMenu(GameView.this, position.x, position.y);
               showDropMenuPanel(origin.x, origin.y);
            }
            else if(movingPieceSelectedState != null) {
               movingPieceDestinationState = position;               
               controller.openedAttackMenu(GameView.this, position.x, position.y);
               if(!towers[position.x][position.y].isPossibleMove()) {
                  disableMoveButton();
               }
               if(!towers[position.x][position.y].isPossibleCapture()) {
                  disableCaptureButton();
               }
               showAttackMenuPanel(origin.x, origin.y);
            }
            else {
               // SHOULDN'T NEED TO CHECK THIS STATE (?)
               System.err.println(" ! ! ! ! ! ! ! ! ! ! ! \n REACHED AN UNPREDICTED STATE NUMERO DOS \n ! ! ! ! ! ! ! ! ! ! ! ");
            }
         }
      }
   }
   
   /* /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ Mouse Listener Classes /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ */
   /* TO BE IMPLEMENTED
   private class RightClickCancel extends MouseAdapter {
      @Override
      public void mouseClicked(MouseEvent e) {
         if(SwingUtilities.isRightMouseButton(e)) {
            moveMenuCancelButton.doClick();
         }
      }
   }
   */
   /* /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\ Displaying GUI /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\  */
   public void createAndShowGUI() {
      frame = new JFrame("GUNGI");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      layeredPane.setOpaque(true);
      frame.setContentPane(layeredPane);
      
      frame.setResizable(false);
      frame.pack();
      
      //frame.setLocationByPlatform(true);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }
}