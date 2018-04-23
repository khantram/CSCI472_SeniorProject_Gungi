package tram.view;

import java.awt.Point;
import tram.controller.BoardViewController;

public interface BoardView {   
   public void decrementRemainingPieces(int type);
   
   public void disableCaptureButton();
   public void disableDropButton();
   public void disableMoveButton();
   public void disablePieceSelectButton(int type);
   
   public void gameOver();
   public void highlightPossibleMoves(Point[] captures, Point[] moves);
   public void isolatePossibleMoves(Point[] captures, Point[] moves);
   
   public void setBoardViewController(BoardViewController controller);
   public void endPlacementPhase();
   
   public void switchPhase();
   public void switchPlayers();
}

