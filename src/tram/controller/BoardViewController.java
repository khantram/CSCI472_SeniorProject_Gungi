package tram.controller;

import tram.view.BoardView;

public interface BoardViewController {
   public void highlightedPiece(BoardView view, int row, int col);
   
   public void openedAttackMenu(BoardView view, int row, int col);
   public void openedDropMenu(BoardView view, int row, int col);
   public void openedMoveMenu(BoardView view, int row, int col);
   public void openedPieceSelectMenu(BoardView view, int color);
   
   public void pieceCaptured(BoardView view, int oldRow, int oldCol, int newRow, int newCol);
   public void pieceDropped(BoardView view, int name, int row, int col);
   public void pieceMoved(BoardView view, int oldRow, int oldCol, int newRow, int newCol);
   public void piecePlaced(BoardView view, int color, int name, int row, int col);
   public void pieceSelected(BoardView view, int row, int col);
}