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
            material += board.getPieceLocation(p).size();
         } else {
            material -= board.getPieceLocation(p).size();
         }
      }
      return material;
   }
}
