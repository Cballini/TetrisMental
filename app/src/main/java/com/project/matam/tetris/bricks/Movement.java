package com.project.matam.tetris.bricks;

/**
 * Created by matam on 20/05/2017.
 */

public interface Movement {

    void rotate();
    void left();
    void right();
    void down();

    boolean validRotate(int[][] gameMatrix);
    boolean validLeft(int[][] gameMatrix);
    boolean validRight(int[][] gameMatrix);
    boolean validDown(int[][] gameMatrix);
}
