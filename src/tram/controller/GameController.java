package tram.controller;

import java.util.ArrayList;

import tram.model.GameModel;
import tram.model.Board;
import tram.model.Tower;
import tram.view.GameView;
import tram.view.TowerButton;
import tram.view.BoardView;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.awt.Point;
import java.awt.event.*;

public class GameController implements BoardViewController {
   private GameModel model;
   private JPopupMenu attackMenu;
   private JPopupMenu dropMenu;
   
   public GameController(GameModel model) {
      this.model = model;
   }
   
   private Point modelToViewIndex(int index) {
      int row = (index/13) - 1;
      int col = (index%13) - 2;
      return new Point(row, col);
   }
   
   private int viewToModelIndex(int row, int col) {
      return (13*(row+1) + (col+2));
   }
   
   @Override
   public void highlightedPiece(BoardView view, int row, int col) {
      int index = viewToModelIndex(row, col);
      
      ArrayList<Integer> possibleMoves = model.getPossibleMoves(index);
      ArrayList<Integer> possibleCaptures = model.getPossibleCaptures(index);
      
      Point[] captures = new Point[possibleCaptures.size()];
      Point[] moves = new Point[possibleMoves.size()];
      
      for(int i = 0; i < captures.length; i++) {
         Point p = modelToViewIndex(possibleCaptures.get(i));
         captures[i] = p;
      }
      
      for(int i = 0; i < moves.length; i++) {
         Point p = modelToViewIndex(possibleMoves.get(i));
         moves[i] = p;
      }
      
      view.highlightPossibleMoves(captures, moves);
   }
   
   @Override
   public void openedAttackMenu(BoardView view, int row, int col) {
      int index = viewToModelIndex(row, col);
      
      if(!model.getBoard().isStackable(index)) {
         view.disableMoveButton();
      }
   }
   
   @Override
   public void openedDropMenu(BoardView view, int row, int col) {
      int index = viewToModelIndex(row, col);
      
      if(!model.getBoard().isStackable(index)) {
         view.disableDropButton();
      }
   }
   
   @Override
   public void openedMoveMenu(BoardView view, int row, int col) {
      int index = viewToModelIndex(row, col);
      
      //TO BE IMPLEMENTED
   }
   
   @Override
   public void openedPieceSelectMenu(BoardView view, int color) {
      for(int i = 1; i < 13; i++) {
         if(!model.hasPiece(color, i)) {
            view.disablePieceSelectButton(i);
         }
      }
   }
   
   @Override
   public void pieceCaptured(BoardView view, int oldRow, int oldCol, int newRow, int newCol) {
      int oldIndex = viewToModelIndex(oldRow, oldCol);
      int newIndex = viewToModelIndex(newRow, newCol);
      
      model.capturePiece(oldIndex, newIndex);
      
      if(model.isCheckmate()) {
         // TO BE IMPLEMENTED
      }
      else if(model.isCheck()) {
         // TO BE IMPLEMENTED
         view.switchPlayers();
      }
      else {
         view.switchPlayers();
      }
      
      model.getBoard().printBoard();
   }
   
   @Override
   public void pieceDropped(BoardView view, int name, int row, int col) {
      int index = viewToModelIndex(row, col);
      
      model.dropPiece(name, index);
      view.decrementRemainingPieces(name);
      
      if(model.isCheckmate()) {
         // TO BE IMPLEMENTED
      }
      else if(model.isCheck()) {
         // TO BE IMPLEMENTED
         view.switchPlayers();
      }
      else {
         view.switchPlayers();
      }
      
      model.getBoard().printBoard();
   }
   
   @Override
   public void pieceMoved(BoardView view, int oldRow, int oldCol, int newRow, int newCol) {
      int oldIndex = viewToModelIndex(oldRow, oldCol);
      int newIndex = viewToModelIndex(newRow, newCol);
      
      model.movePiece(oldIndex, newIndex);
      
      if(model.isCheckmate()) {
         // TO BE IMPLEMENTED
      }
      else if(model.isCheck()) {
         // TO BE IMPLEMENTED
         view.switchPlayers();
      }
      else {
         view.switchPlayers();
      }
      
      model.getBoard().printBoard();
   }
   
   @Override
   public void piecePlaced(BoardView view, int color, int name, int row, int col) {
      int index = viewToModelIndex(row, col);
      
      model.placePiece(color, name, index);
      view.decrementRemainingPieces(name);
      
      if(model.maxPlaced(color)) {
         view.endPlacementPhase();
      }
      
      view.switchPhase();
      
      model.getBoard().printBoard();
   }
   
   @Override
   public void pieceSelected(BoardView view, int row, int col) {
      int index = viewToModelIndex(row, col);
      
      ArrayList<Integer> possibleMoves = model.getPossibleMoves(index);
      ArrayList<Integer> possibleCaptures = model.getPossibleCaptures(index);
      
      Point[] captures = new Point[possibleCaptures.size()];
      Point[] moves = new Point[possibleMoves.size()];
      
      for(int i = 0; i < captures.length; i++) {
         Point p = modelToViewIndex(possibleCaptures.get(i));
         captures[i] = p;
      }
      
      for(int i = 0; i < moves.length; i++) {
         Point p = modelToViewIndex(possibleMoves.get(i));
         moves[i] = p;
      }
      
      view.highlightPossibleMoves(captures, moves);
      view.isolatePossibleMoves(captures, moves);
      
   }
}