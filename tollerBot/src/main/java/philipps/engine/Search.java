package philipps.engine;

import java.util.List;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

public class Search {
   public static int negamax(Board board, int depth, int ply) {
      List<Move> moves = board.legalMoves();
      if (depth <= 0 || moves.isEmpty()) {
         return Evaluation.evaluate(board, depth);
      } else if (board.isDraw()) {
         return 0;
      }
      int bestEval = Integer.MIN_VALUE;
      for (Move m : moves) {
         board.doMove(m);
         int eval = -negamax(board, depth - 1, ply + 1);
         board.undoMove();
         if (eval > bestEval) {
            bestEval = eval;
         }
      }
      return bestEval;
   }

   public static Move TLnegamax(Board board, int depth, int ply, int returnScore[]) {
      List<Move> moves = board.legalMoves();
      if (depth <= 0 || moves.isEmpty()) {
         return new Move("a2a4", Side.WHITE);
      }
      int bestEval = Integer.MIN_VALUE;
      Move bestMove = moves.get(0);
      for (Move m : moves) {
         board.doMove(m);
         int eval = -negamax(board, depth - 1, ply + 1);
         board.undoMove();
         if (eval > bestEval) {
            bestEval = eval;
            bestMove = m;
         }
      }
      returnScore[0] = bestEval;
      return bestMove;
   }
}
