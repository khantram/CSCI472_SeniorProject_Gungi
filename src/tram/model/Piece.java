package tram.model;

import java.util.HashMap;
import java.util.Map;

class Movement {
   int offset;
   boolean slide;

   Movement(int offset) {
      this.offset = offset;
      slide = false;
   }

   Movement(int offset, boolean slide) {
      this.offset = offset;
      this.slide = slide;
   }

   /* JUST FOR TESTING
   @Override
   public String toString() {
      String builder = "(" + coordX + ", " + coordY + ")";
      if(infinite) 
         builder += " INF.";
      return builder;
   }
   //*/
}

public class Piece {
   public static final int MARSHAL = 0, GENERAL   = 1,  LT_GEN      = 2, 
                           MAJ_GEN = 3, COUNSEL   = 4,  SAMURAI     = 5,
                           KNIGHT  = 6, CANNON    = 7,  MUSKETEER   = 8,
                           ARCHER  = 9, FORTRESS  = 10, SPY         = 11,
                           PAWN    = 12;
   
   public static final int BLACK = 0, WHITE = 1;

   public enum Tier {
      MARSHAL_T1,                                   //MARSHAL has same moveDictionary on all tiers
      GENERAL_T1,                                   //GENERAL_T2 and GENERAL_T3 moveDictionary == MARSHAL_T1
      GENERAL_T1_INV,
      LT_GEN_T1,                                    //LT_GEN_T2 moveDictionary == GENERAL_T1; LT_GEN_T3 moveDictionary == MARSHAL_T1
      LT_GEN_T1_INV,
      MAJ_GEN_T1,                                   //MAJ_GEN_T2 moveDictionary == LT_GEN_T1; MAJ_GEN_T3 moveDictionary == GENERAL_T1
      MAJ_GEN_T1_INV,
      COUNSEL_T1,                                   //COUNSEL has the same moveDictionary on all tiers
      SAMURAI_T1, SAMURAI_T2, SAMURAI_T3,
      KNIGHT_T1, KNIGHT_T2, KNIGHT_T3,
      KNIGHT_T1_INV, KNIGHT_T2_INV,
      CANNON_T1,                                    //CANNON has the same moveDictionary on all tiers    
      MUSKETEER_T1,                                 //MUSKETEER has the same moveDictionary on all tiers
      MUSKETEER_T1_INV,
      ARCHER_T2, ARCHER_T3,                         //ARCHER_T1 moveDictionary == MARSHAL_T1
      FORTRESS_T1, FORTRESS_T2, FORTRESS_T3,
      SPY_T2, SPY_T3,                               //SPY_T1 moveDictionary == SAMURAI_T1
      PAWN_T1, PAWN_T2,                             //PAWN_T3 moveDictionary == GENERAL_T1
      PAWN_T1_INV, PAWN_T2_INV
   }
   
   private static final Map<Integer, Tier[]> tierDictionary = new HashMap<Integer, Tier[]>();
   private static final Map<Tier, Movement[]> moveDictionary = new HashMap<Tier, Movement[]>();
   private static final Map<Tier, Tier> invertDictionary = new HashMap<Tier, Tier>();
   //*** IMPORTANT: Saving inverts may be pointless if you can just multiply movement offsets by -1? ***
   private static final Map<Integer, String> printDictionary = new HashMap<Integer, String>();
   
   static {
      //-~-~-~-~-~-~-~-~-~-~- TIER DICTIONARY -~-~-~-~-~-~-~-~-~-~-
      tierDictionary.put(MARSHAL, new Tier[] {
         Tier.MARSHAL_T1,
         Tier.MARSHAL_T1,
         Tier.MARSHAL_T1
      });

      tierDictionary.put(GENERAL, new Tier[] {
         Tier.GENERAL_T1,
         Tier.MARSHAL_T1,
         Tier.MARSHAL_T1
      });

      tierDictionary.put(LT_GEN, new Tier[] {
         Tier.LT_GEN_T1,
         Tier.GENERAL_T1,
         Tier.MARSHAL_T1
      });

      tierDictionary.put(MAJ_GEN, new Tier[] {
         Tier.MAJ_GEN_T1,
         Tier.LT_GEN_T1,
         Tier.GENERAL_T1
      });
      tierDictionary.put(COUNSEL, new Tier[] {
         Tier.COUNSEL_T1,
         Tier.COUNSEL_T1,
         Tier.COUNSEL_T1
      });

      tierDictionary.put(SAMURAI, new Tier[] {
         Tier.SAMURAI_T1,
         Tier.SAMURAI_T2,
         Tier.SAMURAI_T3
      });

      tierDictionary.put(KNIGHT, new Tier[] {
         Tier.KNIGHT_T1,
         Tier.KNIGHT_T2,
         Tier.KNIGHT_T3
      });

      tierDictionary.put(CANNON, new Tier[] {
         Tier.CANNON_T1,
         Tier.CANNON_T1,
         Tier.CANNON_T1
      });

      tierDictionary.put(MUSKETEER, new Tier[] {
         Tier.MUSKETEER_T1,
         Tier.MUSKETEER_T1,
         Tier.MUSKETEER_T1
      });

      tierDictionary.put(ARCHER, new Tier[] {
         Tier.MARSHAL_T1,
         Tier.ARCHER_T2,
         Tier.ARCHER_T3
      });

      tierDictionary.put(FORTRESS, new Tier[] {
         Tier.FORTRESS_T1,
         Tier.FORTRESS_T2,
         Tier.FORTRESS_T3
      });

      tierDictionary.put(SPY, new Tier[] {
         Tier.SAMURAI_T1,
         Tier.SPY_T2,
         Tier.SPY_T3
      });

      tierDictionary.put(PAWN, new Tier[] {
         Tier.PAWN_T1,
         Tier.PAWN_T2,
         Tier.GENERAL_T1
      });
      
      //-~-~-~-~-~-~-~-~-~-~- MOVESET DICTIONARY -~-~-~-~-~-~-~-~-~-~-
      moveDictionary.put(Tier.MARSHAL_T1, new Movement[] {
         new Movement(-13),   // N
         new Movement(-12),   // NE
         new Movement(1),     // E
         new Movement(14),    // SE
         new Movement(13),    // S
         new Movement(12),    // SW
         new Movement(-1),    // W
         new Movement(-14)    // NW
      });

      moveDictionary.put(Tier.GENERAL_T1, new Movement[] {
         new Movement(-13),
         new Movement(-12),
         new Movement(1),
         new Movement(13),
         new Movement(-1),
         new Movement(-14)
      });
      
      moveDictionary.put(Tier.GENERAL_T1_INV, new Movement[] {
         new Movement(13),
         new Movement(12),
         new Movement(-1),
         new Movement(-13),
         new Movement(1),
         new Movement(14)
      });

      moveDictionary.put(Tier.LT_GEN_T1, new Movement[] {
         new Movement(-13),
         new Movement(-12),
         new Movement(14),
         new Movement(12),
         new Movement(-14)
      });
      
      moveDictionary.put(Tier.LT_GEN_T1_INV, new Movement[] {
         new Movement(13),
         new Movement(12),
         new Movement(-14),
         new Movement(-12),
         new Movement(14)
      });

      moveDictionary.put(Tier.MAJ_GEN_T1, new Movement[] {
         new Movement(-14),
         new Movement(-12)
      });
      
      moveDictionary.put(Tier.MAJ_GEN_T1_INV, new Movement[] {
         new Movement(14),
         new Movement(12)
      });
      
      moveDictionary.put(Tier.COUNSEL_T1, new Movement[] {
         new Movement(-24),
         new Movement(24),
         new Movement(28),
         new Movement(-28)
      });
      
      moveDictionary.put(Tier.SAMURAI_T1, new Movement[] {
         new Movement(-12),
         new Movement(14),
         new Movement(12),
         new Movement(-14)
      });
      
      moveDictionary.put(Tier.SAMURAI_T2, new Movement[] {
         new Movement(-12, true),
         new Movement(14, true),
         new Movement(12, true),
         new Movement(-14, true)
      });
      
      moveDictionary.put(Tier.SAMURAI_T3, new Movement[] {
         new Movement(-13),
         new Movement(-12, true),
         new Movement(1),
         new Movement(14, true),
         new Movement(13),
         new Movement(12, true),
         new Movement(-1),
         new Movement(-14, true)
      });
      
      moveDictionary.put(Tier.KNIGHT_T1, new Movement[] {
         new Movement(-25),
         new Movement(1),
         new Movement(-1),
         new Movement(-27)
      });
      
      moveDictionary.put(Tier.KNIGHT_T1_INV, new Movement[] {
         new Movement(25),
         new Movement(-1),
         new Movement(1),
         new Movement(27)
      });
      
      moveDictionary.put(Tier.KNIGHT_T2, new Movement[] {
         new Movement(-25),
         new Movement(-11),
         new Movement(-15),
         new Movement(-27)
      });
      
      moveDictionary.put(Tier.KNIGHT_T2_INV, new Movement[] {
         new Movement(25),
         new Movement(11),
         new Movement(15),
         new Movement(27)
      });
      
      moveDictionary.put(Tier.KNIGHT_T3, new Movement[] {
         new Movement(-25),
         new Movement(-11),
         new Movement(-15),
         new Movement(-27),
         new Movement(25),
         new Movement(11),
         new Movement(15),
         new Movement(27)
      });
      
      moveDictionary.put(Tier.CANNON_T1, new Movement[] {
         new Movement(-13, true),
         new Movement(1, true),
         new Movement(13, true),
         new Movement(-1, true)
      });
      
      moveDictionary.put(Tier.MUSKETEER_T1, new Movement[] {
         new Movement(-13, true)
      });
      
      moveDictionary.put(Tier.MUSKETEER_T1_INV, new Movement[] {
         new Movement(13, true)
      });
      
      moveDictionary.put(Tier.ARCHER_T2, new Movement[] {
         new Movement(-26),
         new Movement(-25),
         new Movement(-24),
         new Movement(-11),
         new Movement(2),
         new Movement(15),
         new Movement(28),
         new Movement(27),
         new Movement(26),
         new Movement(25),
         new Movement(24),
         new Movement(11),
         new Movement(-2),
         new Movement(-15),
         new Movement(-28),
         new Movement(-27)
      });

      moveDictionary.put(Tier.ARCHER_T3, new Movement[] {
         new Movement(-39),
         new Movement(-38),
         new Movement(-37),
         new Movement(-36),
         new Movement(-23),
         new Movement(-10),
         new Movement(3),
         new Movement(16),
         new Movement(29),
         new Movement(42),
         new Movement(41),
         new Movement(40),
         new Movement(39),
         new Movement(38),
         new Movement(37),
         new Movement(36),
         new Movement(23),
         new Movement(10),
         new Movement(-3),
         new Movement(-16),
         new Movement(-29),
         new Movement(-42),
         new Movement(-41),
         new Movement(-40)
      });
      
      moveDictionary.put(Tier.FORTRESS_T1, new Movement[] {
         new Movement(-13),
         new Movement(1),
         new Movement(13),
         new Movement(-1)
      });
      
      moveDictionary.put(Tier.FORTRESS_T2, new Movement[] {
         new Movement(-13, true),
         new Movement(1, true),
         new Movement(13, true),
         new Movement(-1, true)
      });
      
      moveDictionary.put(Tier.FORTRESS_T3, new Movement[] {
         new Movement(-13, true),
         new Movement(-12),
         new Movement(1, true),
         new Movement(14),
         new Movement(13, true),
         new Movement(12),
         new Movement(-1, true),
         new Movement(-14)
      });
      
      moveDictionary.put(Tier.SPY_T2, new Movement[] {
         new Movement(-12),
         new Movement(-11),
         new Movement(14),
         new Movement(15),
         new Movement(12),
         new Movement(11),
         new Movement(-14),
         new Movement(-15)
      });
      
      //TENTATIVE MOVESET
      moveDictionary.put(Tier.SPY_T3, new Movement[] {
         new Movement(-12),
         new Movement(-11),
         new Movement(-10),
         new Movement(14),
         new Movement(15),
         new Movement(16),
         new Movement(12),
         new Movement(11),
         new Movement(10),
         new Movement(-14),
         new Movement(-15),
         new Movement(-16)
      });
      
      moveDictionary.put(Tier.PAWN_T1, new Movement[] {
         new Movement(-13)
      });
      
      moveDictionary.put(Tier.PAWN_T1_INV, new Movement[] {
         new Movement(13)
      });
      
      moveDictionary.put(Tier.PAWN_T2, new Movement[] {
         new Movement(-13),
         new Movement(1),
         new Movement(-1)
      });
      
      moveDictionary.put(Tier.PAWN_T2_INV, new Movement[] {
         new Movement(13),
         new Movement(-1),
         new Movement(1)
      });
      
      //-~-~-~-~-~-~-~-~-~-~- INVERTATION DICTIONARY -~-~-~-~-~-~-~-~-~-~-
      invertDictionary.put(Tier.MARSHAL_T1,     Tier.MARSHAL_T1);
      invertDictionary.put(Tier.GENERAL_T1,     Tier.GENERAL_T1_INV);
      invertDictionary.put(Tier.LT_GEN_T1,      Tier.LT_GEN_T1_INV);
      invertDictionary.put(Tier.MAJ_GEN_T1,     Tier.MAJ_GEN_T1_INV);
      invertDictionary.put(Tier.COUNSEL_T1,     Tier.COUNSEL_T1);
      invertDictionary.put(Tier.SAMURAI_T1,     Tier.SAMURAI_T1);
      invertDictionary.put(Tier.SAMURAI_T2,     Tier.SAMURAI_T2);
      invertDictionary.put(Tier.SAMURAI_T3,     Tier.SAMURAI_T3);
      invertDictionary.put(Tier.KNIGHT_T1,      Tier.KNIGHT_T1_INV);
      invertDictionary.put(Tier.KNIGHT_T2,      Tier.KNIGHT_T2_INV);
      invertDictionary.put(Tier.KNIGHT_T3,      Tier.KNIGHT_T3);
      invertDictionary.put(Tier.CANNON_T1,      Tier.CANNON_T1);
      invertDictionary.put(Tier.MUSKETEER_T1,   Tier.MUSKETEER_T1_INV);
      invertDictionary.put(Tier.ARCHER_T2,      Tier.ARCHER_T2);
      invertDictionary.put(Tier.ARCHER_T3,      Tier.ARCHER_T3);
      invertDictionary.put(Tier.FORTRESS_T1,    Tier.FORTRESS_T1);
      invertDictionary.put(Tier.FORTRESS_T2,    Tier.FORTRESS_T2);
      invertDictionary.put(Tier.FORTRESS_T3,    Tier.FORTRESS_T3);
      invertDictionary.put(Tier.SPY_T2,         Tier.SPY_T2);
      invertDictionary.put(Tier.SPY_T3,         Tier.SPY_T3);
      invertDictionary.put(Tier.PAWN_T1,        Tier.PAWN_T1_INV);
      invertDictionary.put(Tier.PAWN_T2,        Tier.PAWN_T2_INV);
      
      //-~-~-~-~-~-~-~-~-~-~- PRINT DICTIONARY -~-~-~-~-~-~-~-~-~-~-
      printDictionary.put(MARSHAL, "M");
      printDictionary.put(GENERAL, "G");
      printDictionary.put(LT_GEN, "L");
      printDictionary.put(MAJ_GEN, "J");
      printDictionary.put(COUNSEL, "E");
      printDictionary.put(SAMURAI, "S");
      printDictionary.put(KNIGHT, "K");
      printDictionary.put(CANNON, "N");
      printDictionary.put(MUSKETEER, "U");
      printDictionary.put(ARCHER, "A");
      printDictionary.put(FORTRESS, "F");
      printDictionary.put(SPY, "Y");
      printDictionary.put(PAWN, "P");
   }
   
   private int color;
   private int name;
   private int index;
   private int tier;

   public Piece(int color, int name) {
      this.color = color;
      this.name = name;
      index = -1;
      tier = -1;
   }
   
   public Piece(int color, int name, int index) {
      this.color = color;
      this.name = name;
      this.index = index;
      tier = 0;
   }
   
   public Piece(int color, int name, int index, int tier) {
      this.color = color;
      this.name = name;
      this.index = index;
      this.tier = tier;
   }
   
   public Piece copy() {
      return new Piece(color, name, index, tier);
   }
   
   public int getName() {
      return name;
   }

   public int getColor() {
      return color;
   }
   
   public void setIndex(int index) {
      this.index = index;
   }
   
   public int getIndex() {
      return index;
   }
   
   public void setTier(int tier) {
      this.tier = tier;
   }

   public int getTier() {
      return tier;
   }
   
   public Movement[] getMoveset() {
      if(color == BLACK) {
         return moveDictionary.get(invertDictionary.get(tierDictionary.get(name)[tier-1]));
      }
      else {
         return moveDictionary.get(tierDictionary.get(name)[tier-1]);
      }
   }
   
   @Override
   public String toString() {
      String builder = "";
      
      if(color == BLACK)
         builder += "B";
      else
         builder += "W";
      
      builder += printDictionary.get(name);
      
      builder += tier;
      
      return builder;
   }
}
