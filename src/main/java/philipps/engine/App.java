
package philipps.engine;

import com.github.bhlangonijr.chesslib.Board;

/**
 * Hello world!
 *
 */
public class App {

    static void makeMove(Board board, String move) {
        board.doMove(move);
    }

    public static void main(String[] args) {
        Board b = new Board();
        System.out.println(b.toString());
        makeMove(b, "a2a4");
        b.loadFromFen("r1bqkbnr/1ppp1ppp/p1n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 0 1 moves a2a4");
        System.out.println(b.getFen());
        System.out.println(b.toString());
        System.out.println("Hello World!");
        UCI.uciCommunication();
    }
}
