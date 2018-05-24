package tram.model;

public class Player {
   /*
   public static final int MARSHAL = 0, GENERAL   = 1,  LT_GEN      = 2, 
                            MAJ_GEN = 3, COUNSEL   = 4,  SAMURAI     = 5,
                            KNIGHT  = 6, CANNON    = 7,  MUSKETEER   = 8,
                            ARCHER  = 9, FORTRESS  = 10, SPY         = 11,
                            PAWN    = 12;
   */
   
   private int piecesInHand[];
   private int piecesPlaceableRemaining;
   
   public Player(int piecesPlaceableRemaining) {
      this.piecesPlaceableRemaining = piecesPlaceableRemaining;
      piecesInHand = new int[13];
      
      piecesInHand[Piece.MARSHAL] = 1;
      
      for(int i = 1; i < 12; i++) {
         piecesInHand[i] = 2;
      }
      
      piecesInHand[Piece.PAWN] = 9;
   }
   
   public void droppedPiece(int name) {
      if(piecesInHand[name] < 1) {
         System.err.println("ILLEGAL MOVE: Illegal drop; no pieces remaining.");
      }
      else {
         piecesInHand[name] -= 1;
      }
   }
   
   public void placedPiece(int name) {
      droppedPiece(name);
      piecesPlaceableRemaining--;
   }
   
   public int getPiecesInHand(int name) {
      return piecesInHand[name];
   }
   
   public boolean hasPiece(int name) {
      return (piecesInHand[name] > 0);
   }
   
   public boolean maxPiecesPlaced() {
      return (piecesPlaceableRemaining <= 0);
   }
}