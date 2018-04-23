package tram.model;

import java.util.Stack;

public class Tower {
   Stack<Piece> tower;
   int size;
   boolean border;
   
   public Tower() {
      tower = new Stack<>();
      size = 0;
      border = false;
   }
   
   public Tower(boolean border) {
      tower = new Stack<>();
      size = -1;
      this.border = border;
   }
   
   public Tower(Stack<Piece> tower, int size, boolean border) {
      this.tower = tower;
      this.size = size;
      this.border = border;
   }
   
   public Tower(Tower t) {
      tower = new Stack<>();
      size = t.size();
      
      for(int i = 0; i < size; i++) {
         tower.push((t.getPiece(i+1).copy()));
      }
      
      border = t.isBorder();
   }
   
   public void push(Piece piece) {
      if(size > 2) { //Should throw an ERROR instead
         System.err.println("ERROR: Maximum tower size reached");
         //return -1;
      }
      else {
         piece.setTier(++size);
         tower.push(piece);
      }
   }
   
   public Piece peek() {
      return tower.peek();
   }
   
   public Piece pop() {
      size--;
      return tower.pop();
   }
   
   public Piece getPiece(int tier) {
      return tower.get(--tier);
   }
   
   public boolean isEmpty() {
      return tower.empty();
   }
   
   public boolean isStackable() {
      if(border || (size != 0 && tower.peek().getName() == Piece.MARSHAL)) {
         return false;
      }
      
      return (size < 3);
   }
   
   public boolean isBorder() {
      return border;
   }
   
   public int size() {
      return size;
   }
}