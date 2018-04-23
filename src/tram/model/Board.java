package tram.model;

import java.util.ArrayList;
import java.util.Iterator;
 
public class Board {
   private Tower[] board;
   
   private ArrayList<Piece> black;  //Way of accessing black pieces in play
   private ArrayList<Piece> white;  //Way of accessing white pieces in play
   
   public Board() {
      board = new Tower[143];
      black = new ArrayList<Piece>();
      white = new ArrayList<Piece>();
      
      for(int i = 0; i < board.length; i++) {
         if((i > 1 && i < 11) || (i > 131 && i < 141) ||
            (i%13 == 0) || (i%13 == 1) || (i%13 == 11) || (i%13 == 12)) {
            board[i] = new Tower(true);
         }
         else {
            board[i] = new Tower();
         }
      }
   }
   
   public void capturePiece(int oldIndex, int newIndex) {
      Piece p = board[oldIndex].pop();
      Piece c = board[newIndex].pop();
      
      p.setIndex(newIndex);
      
      if(c.getColor() == Piece.BLACK) {
         black.remove(c);
      }
      else {
         white.remove(c);
      }
      
      board[newIndex].push(p);
   }
   
   public void dropPiece(int color, int name, int index) {
      if(board[index].isStackable()) {
         Piece p = new Piece(color, name, index);
         
         if(color == Piece.BLACK) {
            black.add(p);
         }
         else {
            white.add(p);
         }
         
         board[index].push(p);
      }
      else {
         System.err.println("ILLEGAL MOVE: Illegal movement; destination is not stackable.");
      }
   }
   
   public void movePiece(int oldIndex, int newIndex) {
      if(board[newIndex].isStackable()) {
         Piece p = board[oldIndex].pop();
         p.setIndex(newIndex);
         board[newIndex].push(p);
      }
      else {
         System.err.println("ILLEGAL MOVE: Illegal movement; destination is not stackable.");
      }
   }
   
   public Piece getPiece(int index) {
      return board[index].peek();
   }
   
   public Piece getPiece(int index, int tier) {
      return board[index].getPiece(tier);
   }
   
   public boolean isCheck(int color) {
      /* TO BE IMPLEMENTED
      Piece marshal;
      ArrayList<Integer> threats;
      
      if(color == Piece.BLACK) {
         marshal = black.get(0);
         for(Piece p : white) {
            threats = generatePseudoLegalMoves(p, true);
            
            for(Integer i : threats) {
               if(i == marshal.getIndex()) {
                  return true;
               }
            }
         }
      }
      else {
         marshal = white.get(0);
         for(Piece p : black) {
            threats = generatePseudoLegalMoves(p, true);
            
            for(Integer i : threats) {
               if(i == marshal.getIndex()) {
                  return true;
               }
            }
         }
      }
      //*/
      return false;
   }
   
   public boolean isCheckmate(int color) {
      
      
      
      return false;
   }
   
   public boolean isStackable(int index) {
      return board[index].isStackable();
   }
   
   public boolean isEmpty(int index) {
      return board[index].isEmpty();
   }
   
   public ArrayList<Integer> generatePossibleDrops(Piece piece) {
      ArrayList<Integer> possibleDrops = new ArrayList<Integer>();
      
      for(int i = 15; i < 127; i++) {
         if(piece.getName() == Piece.PAWN) {
            // SHOULD UPDATE TO CHECK FOR ILLEGAL PAWN DROPS
            //continue;
         }
         
         if(board[i].isStackable()) {
            if(hasPossibleMoves(piece, i)) {
               possibleDrops.add(i);
            }
         }
      }
      
      return possibleDrops;
   }
   
   public ArrayList<Integer> generatePossibleMoves(Piece piece, boolean capture) {
      ArrayList<Integer> moves = generatePseudoLegalMoves(piece, capture);
      
      Iterator<Integer> it = moves.iterator();
      while(it.hasNext()) {
         Integer destination = it.next();
         
         if(isPinned(piece, destination, capture)) {
            it.remove();
         }
      }
      
      return moves;
   }
   
   public ArrayList<Integer> generatePseudoLegalMoves(Piece piece, boolean capture) {
      ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
      Movement[] movements = piece.getMoveset();
      int index = piece.getIndex();
      
      for(Movement movement : movements) {
         int destination = index + movement.offset;
         
         if(isInBounds(destination) && isObstructedMove(piece, movement.offset, destination, capture)) { 
            possibleMoves.add(destination);
         }
         
         if(piece.getName() == Piece.CANNON && capture) { //!!! Probably not optimal...
            destination += movement.offset;
            
            while(isInBounds(destination)) {
               if(isObstructedMove(piece, movement.offset, destination, capture)) {
                  possibleMoves.add(destination);
                  break;
               }
               destination += movement.offset;
            }
         }
         else if(movement.slide) {
            destination += movement.offset;
            
            if(capture) {
               while(isInBounds(destination)) {
                  if(isObstructedMove(piece, movement.offset, destination, capture)) {
                     possibleMoves.add(destination);
                  }
                  destination += movement.offset;
               }
            }
            else {
               while(isInBounds(destination) && isObstructedMove(piece, movement.offset, destination, capture)) {
                  possibleMoves.add(destination);
                  destination += movement.offset;
               }
            }
         }
      }
      
      return possibleMoves;
   }
   
   public boolean hasPossibleMoves(Piece piece, int index) {
      piece.setIndex(index);
      ArrayList<Integer> possibleMoves = generatePossibleMoves(piece, false);
      ArrayList<Integer> possibleCaptures = generatePossibleMoves(piece, true);
      
      return (!possibleMoves.isEmpty()) || (!possibleCaptures.isEmpty());
   }
   
   /**
    * This method should check for obstructing pieces (?)
    * -> Check for "pseudo-legal" moves first, then for pinned pieces (?)
         -> Or, maybe vice-versa
    */
   public boolean isObstructedMove(Piece piece, int offset, int destination, boolean capture) {      
      if(piece.getName() == Piece.CANNON && capture) { //!!! Probably not optimal...
         boolean screen = false;
         for(int i = piece.getIndex()+offset; i != destination; i+=offset) {
            if(!board[i].isEmpty()) {
               if(screen) {
                  return false;
               }
               screen = true;
            }
         }
         if(screen && !board[destination].isEmpty()) {
            return isCapturable(piece, destination);
         }
         else {
            return false;
         }
      }
      else if(piece.getName() != Piece.KNIGHT && piece.getName() != Piece.ARCHER) {
         for(int i = piece.getIndex()+offset; i != destination; i+=offset) {
            if(!board[i].isEmpty()) {
               return false;
            }
         }
      }
      
      if(!board[destination].isEmpty()) {
         if(capture) {
            return isCapturable(piece, destination);
         }
         else {
            return board[destination].isStackable();
         }
      }
      
      return !capture;
   }
   
   public boolean isCapturable(Piece piece, int index) {      
      return (piece.getColor() != board[index].peek().getColor());
   }
   
   public boolean isInBounds(int destination) {
      return !((destination < 0) || (destination > board.length) || (board[destination].isBorder()));
   }
   
   public boolean isPinned(Piece piece, int destination, boolean capture) {
      /* TO BE IMPLEMENTED
      boolean pinned;
      
      Tower[] origBoard = new Tower[board.length];
      ArrayList<Piece> origBlack = new ArrayList<Piece>();
      ArrayList<Piece> origWhite = new ArrayList<Piece>();
      int origIndex = piece.getIndex();
      
      for(int i = 0; i < board.length; i++) {
         origBoard[i] = new Tower(board[i]);
      }
      for(Piece p : black) {
         origBlack.add(p.copy());
      }
      for(Piece p: white) {
         origWhite.add(p.copy());
      }
      
      if(capture) {
         capturePiece(piece.getIndex(), destination);
         pinned = isCheck(piece.getColor());
      }
      else {
         movePiece(piece.getIndex(), destination);
         pinned = isCheck(piece.getColor());
      }
      
      board = origBoard;
      black = origBlack;
      white = origWhite;
      
      piece.setIndex(origIndex);
      
      return pinned;
      //*/
      
      return false;
   }
   
   public void printPieces() {
      System.out.println("\n BLACK");
      for(Piece p : black) {
         System.out.println(p + " " + p.getIndex());
      }
      
      System.out.println("\n WHITE");
      for(Piece p : white) {
         System.out.println(p + " " + p.getIndex());
      }
   }
   
   public void printBoard() {
      String output = "  -------------------------------------\n";
      for(int i = 15; i < 128; i++) {
         if(i%13 == 2)
            output += 10-(i/13) + " |";
         
         if(board[i].isBorder())
            continue;
         else if(board[i].isEmpty())
            output += "   ";
         else
            output += board[i].peek();

         output += "|";
         
         if(i%13 == 10)
            output += "\n  -------------------------------------\n";
      }
      output += "    1   2   3   4   5   6   7   8   9";

      System.out.println(output);
   }
}
