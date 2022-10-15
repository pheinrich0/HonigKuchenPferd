package philipps.engine;

import com.github.bhlangonijr.chesslib.*;;

public class Evaluation {
   static int mateScore = 10000;

   public static int evaluate(Board board, int depth) {
      Side us = board.getSideToMove();
      if (board.legalMoves().isEmpty()) {
         return board.isKingAttacked() ? -mateScore * (depth + 1) : 0;
      } else if (board.isDraw()) {
         return 0;
      }
      int material = 0;
      for (Piece p : Piece.allPieces) {
         if (p == Piece.NONE) {
            continue;
         }
         Side pSide = p.getPieceSide();
         if (pSide == us) {
            material += getPieceValue(p);
         } else {
            material -= getPieceValue(p);
         }
      }
      return material;
   }
   public static int getPieceValue (Piece p) {
      switch (p.getPieceType()){
         case PAWN:
         return 100;
         case KNIGHT:
         return 300;
         case BISHOP:
         return 300;
         case ROOK:
         return 500;
         case QUEEN:
         return 900;
         case KING:
         return 0;
         default:
         break;
         
      
      }

   }
}
