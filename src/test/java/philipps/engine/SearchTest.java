package philipps.engine;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;

import org.junit.Test;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class SearchTest {
   @Test
   public void mateInOne() {
      String[] cases = { "3k4/8/3K4/8/5R2/8/8/8 w - - 0 1",
            "8/8/8/5r2/8/3k4/8/3K4 b - - 0 1",
            "1k1r2R1/8/1K6/8/8/8/8/8 w - - 0 1",
            "8/8/8/8/8/1k6/8/1K1R2r1 b - - 0 1" };
      String[] moves = { "f4f8", "f5f1", "g8d8", "g1d1" };
      for (int i = 0; i < cases.length; i++) {
         Board b = new Board();
         b.loadFromFen(cases[i]);
         int[] score = {0};
         Move move = Search.TLnegamax(b, 3, 1, score);
         String movestr = move.toString();
         assertTrue(cases[i] + " failed with move " + movestr, moves[i].equals(movestr));
      }
   }
}
