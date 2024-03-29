package philipps.engine;

// adapted from https://github.com/stressGC/Java-Chess-Engine/blob/master/src/georges/cosson/UCI.java

import java.util.*;
import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;

/*
 * class responsible for the communication through UCI protocol
 */
public class UCI {

   // UCI listener
   public static void uciCommunication() {

      @SuppressWarnings("resource")
      Scanner input = new Scanner(System.in);
      Board board = new Board();
      // infinite loop to listen to incoming commands
      while (true) {
         String inputString = input.nextLine();

         /*
          * parsing the input based on the different existing commands
          * http://wbec-ridderkerk.nl/html/UCIProtocol.html
          */
         if ("uci".equals(inputString)) {
            inputUCI();
         } else if (inputString.startsWith("setoption")) {
            inputSetOption(inputString);
         } else if ("isready".equals(inputString)) {
            inputIsReady();
         } else if ("ucinewgame".equals(inputString)) {
            inputUCINewGame(board);
         } else if (inputString.startsWith("position")) {
            inputPosition(board, inputString);
         } else if (inputString.startsWith("go")) {
            inputGo(board, inputString);
         } else if (inputString.equals("quit")) {
            inputQuit();
         }
      }
   }

   // function called the make sure the UCI protocol is supproted
   public static void inputUCI() {
      System.out.println("id name TollerTestBot");
      System.out.println("id author philippH");
      System.out.println("uciok");
   }

   // function called to set options, not supported ATM
   public static void inputSetOption(String inputString) {
      // set options ?
   }

   // function called to make sure the engine is ready
   public static void inputIsReady() {
      System.out.println("readyok");
   }

   // function called when a new game is about to start, not supported ATM
   public static void inputUCINewGame(Board board) {
      // clear cache and other stuffs ?
      board.clear();
   }

   // function called when the platform updates the positions of the pieces on the
   // board
   public static void inputPosition(Board board, String input) {

      input = input.substring(9).concat(" ");
      boolean fenEnd = false;
      if (input.contains("startpos ")) {
         fenEnd = true;
         int pos = input.indexOf("startpos");
         input = input.substring(pos + 9);
         board.loadFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
      }
      StringTokenizer tokens = new StringTokenizer(input, " ");

      String fen = "";
      while (tokens.hasMoreTokens()) {
         String str = tokens.nextToken();
         if ("fen".equals(str)) {
            continue;
         } else if ("moves".equals(str)) {
            if (!(fenEnd || fen.isBlank())) {
               board.loadFromFen(fen.trim());
               fenEnd = true;
               continue;
            } else {
               continue;
            }
         }
         if (fenEnd) {
            // fen is over, now parsing moves
            board.doMove(str);
         } else {
            fen += " " + str;
         }
      }
      if (!fenEnd) {
         board.loadFromFen(fen.trim());
      }
   }

   // function called when its our turn to compute !
   public static void inputGo(Board board, String input) {
      String color = board.getSideToMove().equals(Side.WHITE) ? "w" : "b";
      long start = System.currentTimeMillis();
      int fixedTime = 0;
      int myTime = 100;
      int myIncrement = 0;
      StringTokenizer tokens = new StringTokenizer(input, " ");
      while (tokens.hasMoreTokens()) {
         String str = tokens.nextToken();
         if ((color + "time").equals(str)) {
            str = tokens.nextToken();
            myTime = Integer.parseInt(str);
         } else if ((color + "inc").equals(str)) {
            str = tokens.nextToken();
            myIncrement = Integer.parseInt(str);
         } else if ("movetime".equals(str)) {
            str = tokens.nextToken();
            fixedTime = Integer.parseInt(str);
         } else if ("depth".equals(str)) {
            str = tokens.nextToken();
            int goDepth = Integer.parseInt(str);
            int[] score = { 0 };
            long evalStart = System.nanoTime();
            Move bestMove = Search.TLnegamax(board, goDepth, 0, score);
            long elapsed = (System.nanoTime() - evalStart) / 1000000;
            System.out.printf("info depth %d score cp %d time %d\r\n", goDepth, score[0], elapsed);
            System.out.println("bestmove " + bestMove);
            return;
         }
      }
      long timeToUse = myTime / 50;
      if (myTime * 3 > myIncrement) {
         timeToUse += myIncrement * 10 / 9;
      }
      final long finalTime = fixedTime > 0 ? fixedTime : timeToUse;
      Move bestMove = Search.iterativeDeepening(board, finalTime);
      long end = System.currentTimeMillis();

      // time taken for computation, debug purpose
      System.out.println("found in " + (end - start) + " ms");
   }

   // function called when "quit" is received through UCI, quits the program, debug
   // purpose
   public static void inputQuit() {
      System.exit(0);
   }
}
