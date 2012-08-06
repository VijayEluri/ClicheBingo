package com.rogueitem.clichebingo.app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ClicheBingoApp {

  final static String FILE_NAME = "dictionary.txt";
  final static boolean DEBUG = false;
  private BingoBoard board = null;
  
  List<String> dictionary;
  
  /**
   * 
   */
  ClicheBingoApp() {
    init();
    play();
  }
  
  /**
   * Loads a dictionary file into memory
   */
  private void loadFile() throws IOException {
    FileSystem fs = FileSystems.getDefault();
    Path file = fs.getPath(FILE_NAME);
    dictionary = Files.readAllLines(file, StandardCharsets.US_ASCII);
  }
  
  /**
   * Initializes a new board instance
   */
  private void init() {
    try {
      System.err.print("Reading dictionary...");
      loadFile();
      System.err.println("loaded");
      if (DEBUG) {
        Iterator<String> itr = dictionary.iterator();
        while (itr.hasNext()) {
          System.out.println(itr.next());
          itr.next();
        }
      }
    } catch (IOException ex) {
      System.err.println("Error reading dictionary file: " + ex.getMessage());
    }
   
    // initialize and print the BingoBoard
    setBoard(new BingoBoard(dictionary));
    getBoard().print();
  }
  
  /**
   * 
   * @param board
   */
  private void setBoard(BingoBoard board) {
    this.board = board;
  }
  
  /**
   * 
   * @return
   */
  private BingoBoard getBoard() {
    return this.board;
  }
  
  /**
   * receives input and plays the game
   */
  private void play() {
    Scanner input = new Scanner(System.in);
    String[] coords = null;
    while (true) {
      System.out.println("What's the next square (x,y)? Type 'exit' to quit");
      String value = input.next();
      if (value.equals("exit")) return;
  
      coords = value.split(",");
      if (coords.length != 2) {
        System.out.println("expecting coordinates in the pattern x,y");
      }
      try {
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        getBoard().setSquare(x,y);
        getBoard().print();
      } catch (NumberFormatException ex) {
        System.out.println("Only numbers are permitted for x and y");
      } catch (IllegalArgumentException ex) {
        System.out.println("Board is of size " + getBoard().getBoardSize());
      }
    }
  }
  
  
  /**
   * Stores the Bingo Board state
   * @author tremotigue
   *
   */
  private class BingoBoard {
    final static int BOARD_SIZE = 5;
    String[][] grid = new String[BOARD_SIZE][BOARD_SIZE];
    int longestWord = 0;
    
    /**
     * Initialize a new BingoBoard randomly
     * @param dict
     */
    BingoBoard(List<String> dict) {
      Random rnd = new java.util.Random(); 
      for (int i = 0; i <= 4; i++) {
        for (int j = 0; j <= 4; j++) {
          String word = "";
          if (i == 2 && j == 2)
            word = "FREE SQUARE";
          else {
            int key = rnd.nextInt(dict.size() - 1);
            word = dict.get(key);
            dict.remove(key); // only use each word once
          }
          grid[i][j] = word;
          longestWord = (word.length() > longestWord) ? word.length() : longestWord;
        }
      }
    }
    
    /**
     * 
     * @return
     */
    public int getBoardSize() {
      return BOARD_SIZE;
    }
    
    /**
     * Sets the square value
     */
    public void setSquare (int x, int y) {
      if (x >= BOARD_SIZE || y >= BOARD_SIZE)
        throw new IllegalArgumentException("Invalid coordinates entered");
      this.grid[x][y] = "X";
    }

    
    /**
     * Displays the board to stdout
     */
    void print() {
      for (int i = 0; i <= 4; i++) {
        for (int j = 0; j <= 4; j++) {
          String word = grid[i][j];
          int pad = longestWord - word.length(); 
          StringBuilder padding = new StringBuilder();
          for (int k = 0; k < pad; k++) {
            padding.append(" ");
          }
          System.out.print(word + padding + "| ");
        }
        System.out.println();
      }
    }
  }

  
  /**
   * @param args
   */
  public static void main(String[] args) {

    ClicheBingoApp app = new ClicheBingoApp();
    
    System.exit(0);
  }

}
