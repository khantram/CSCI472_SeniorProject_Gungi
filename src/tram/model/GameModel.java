package tram.model;

import java.util.ArrayList;

import tram.model.Board;
import tram.model.Piece;
import tram.model.Tower;

public class GameModel {
   private final int MAX_PIECES_PLACEABLE = 2;
   private Board board;
   private Player black;
   private Player white;
   private int currentPlayer;
   
   public GameModel() {
      newGame();
   }
   
   public void newGame() {
      board = new Board();
      
      black = new Player(MAX_PIECES_PLACEABLE);
      white = new Player(MAX_PIECES_PLACEABLE);
      
      //currentPlayer = Piece.BLACK; //Black places first
      currentPlayer = Piece.WHITE; // TEMPORARY FIX SINCE WHITE MOVES FIRST
   }
   
   public boolean isCheck() {
      return board.isCheck(currentPlayer);
   }
   
   public boolean isCheckmate() {
      
      return false;
   }
   
   public void switchPlayers() {
      if(currentPlayer == Piece.BLACK) {
         currentPlayer = Piece.WHITE;
      }
      else {
         currentPlayer = Piece.BLACK;
      }
   }
   
   public Board getBoard() {
      return board;
   }
   
   public boolean isPlayerPiece(int index) {
      return (!board.isEmpty(index)) && (board.getPiece(index).getColor() == currentPlayer);
   }
   
   public ArrayList<Integer> getPossibleCaptures(int index) {
      Piece p = board.getPiece(index);
      return board.generatePossibleMoves(p, true);
   }
   
   public ArrayList<Integer> getPossibleMoves(int index) {
      Piece p = board.getPiece(index);
      return board.generatePossibleMoves(p, false);
   }
   
   public boolean maxPlaced(int color) {
      if(color == Piece.BLACK) {
         return black.maxPiecesPlaced();
      }
      else {
         return white.maxPiecesPlaced();
      } 
   }
   
   public void capturePiece(int oldIndex, int newIndex) {      
      board.capturePiece(oldIndex, newIndex);
      
      switchPlayers();
   }
   
   public void dropPiece(int name, int index) {
      if(currentPlayer == Piece.BLACK) {
         black.droppedPiece(name);
      }
      else {
         white.droppedPiece(name);
      }
      
      board.dropPiece(currentPlayer, name, index);
      
      switchPlayers();
   }
   
   public void movePiece(int oldIndex, int newIndex) {
      board.movePiece(oldIndex, newIndex);
      
      switchPlayers();
   }
   
   public void placePiece(int color, int name, int index) {
      if(color == Piece.BLACK) {
         black.placedPiece(name);
      }
      else {
         white.placedPiece(name);
      }
      
      board.dropPiece(color, name, index);
      
   }
   
   public boolean hasPiece(int color, int name) {
      if(color == Piece.BLACK) {
         return black.hasPiece(name);
      }
      else {
         return white.hasPiece(name);
      }
   }
}