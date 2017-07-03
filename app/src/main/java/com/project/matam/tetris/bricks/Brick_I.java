package com.project.matam.tetris.bricks;

import android.graphics.Color;

import java.util.Arrays;

/**
 * Created by matam on 20/05/2017.
 */

public class Brick_I extends Brick {


    private int[][] rotA = new int[4][1];
    private int[][] rotB = new int[1][4];

    public Brick_I(int x, int y){
        super(x,y);
        this.mHeight = 4;
        this.mWitdh = 1;
        this.mMatrix = new int[][]{{1},{1},{1},{1}};

        /*for(int cy = 0; cy < mHeight; cy++){
            for(int cx = 0; cx < mWitdh; cx++)
            {
                this.mMatrix[cy][cx] = 1;
            }
        }*/

        this.mColor = Color.GREEN;

        for(int cy = 0; cy < 4; cy++){
            for(int cx = 0; cx < 1; cx++)
            {
                rotA[cy][cx] = 1;
                rotB[cx][cy] = 1;
            }
        }
    }

    @Override
    public Brick nextRotationCheck()
    {
        Brick brick_rotate = new Brick_I(this.mPos_x,this.mPos_y);
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

        if(Arrays.deepEquals(this.mMatrix,rotA)){
            this.mMatrix = rotB;
            this.mWitdh = 4;
            this.mHeight = 1;
        }
        else if(Arrays.deepEquals(this.mMatrix,rotB)){
            this.mMatrix = rotA;
            this.mWitdh = 1;
            this.mHeight = 4;
        }
    }
}
