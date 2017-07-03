package com.project.matam.tetris.bricks;

import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by matam on 20/05/2017.
 */

public class Brick_Z extends Brick {

    private int[][] rotA = new int[][]{{1,1,0},{0,1,1}};
    private int[][] rotB = new int[][]{{0,1},{1,1},{1,0}};

    public Brick_Z(int x, int y){
        super(x,y);
        this.mHeight = 2;
        this.mWitdh = 3;
        this.mMatrix = new int[][]{
                {1,1,0},
                {0,1,1}
        };
        this.mColor = Color.YELLOW;

        this.mPos_x = x;
        this.mPos_y = y;
    }

    @Override
    public Brick nextRotationCheck()
    {
        Brick brick_rotate = new Brick_Z(this.mPos_x,this.mPos_y);
        if(Arrays.deepEquals(this.mMatrix,rotA))
        {
            brick_rotate.setMatrix(rotA);
            brick_rotate.rotate();
        }
        else if(Arrays.deepEquals(this.mMatrix,rotB))
        {
            brick_rotate.setMatrix(rotB);
            brick_rotate.rotate();
        }
        return brick_rotate;
    }

    @Override
    public void rotate() {

        if (Arrays.deepEquals(this.mMatrix,rotA)){
            this.mMatrix = rotB;
            this.mWitdh = 2;
            this.mHeight = 3;
        }
        else if(Arrays.deepEquals(this.mMatrix,rotB)){
            this.mMatrix = rotA;
            this.mWitdh = 3;
            this.mHeight = 2;
        }
    }
}
