package tram;

import tram.model.GameModel;
import tram.view.GameView;
import tram.controller.GameController;

/**
 * COMPILE:    javac -d bin src/tram/model/*.java src/tram/controller/*.java src/tram/view/*.java src/tram/*.java
 */

 /**
 * >> HIGH-PRIORITY TO-DO LIST <<
 * - Checks for check and checkmate
 * - Check for pinned/absolutely pinned pieces/moves
 * - Prevent pieces with no possible moves from being selected
 * - Check to make sure players don't end placement phase in check
 * - When checking for possible drops, check for illegal pawn drops
 *
 * >> CLEAN-UP LIST <<
 * - Turn repeated lines of code in BoardPanel class into methods
 * - Capitalize Action Listener classes (?)
 * - Set certain methods final (?)
 * - Clean up excess imports
 *
 * >> TO-DO LIST/FUTURE WORK <<
 * - MouseListener stuff for pieces/board squares
 *    - Mouse wheel to highlight other pieces in tower
 * - Resizable window (while maining aspect ratio)
 * - Hold TAB to see captured menu
 * - Main menu
 * - Player decision to end placement phase early
 */
 
public class Main {
   public static void main(String [] args) {
      GameModel model = new GameModel();
      GameController controller = new GameController(model);
      GameView view = new GameView(controller);
      view.createAndShowGUI();
   }
}