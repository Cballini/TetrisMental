package com.project.matam.tetris.bricks;

import android.graphics.Color;

/**
 * Created by matam on 20/05/2017.
 */

public class Brick_O extends Brick {

    public Brick_O(int x, int y){
        super(x,y);
        this.mHeight = 2;
        this.mWitdh = 2;
        this.mMatrix = new int[][]{
                {1,1},
                {1,1}
        };
        this.mColor = Color.MAGENTA;

        this.mPos_x = x;
        this.mPos_y = y;
    }

    @Override
    public boolean validRotate(int[][] gameMatrix){
        return true;
    }

    @Override
    public void rotate() {
    }
}
