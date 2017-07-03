package com.project.matam.tetris.bricks;

import com.project.matam.tetris.gamecore.BrickManager;

import java.util.Arrays;

/**
 * Created by matam on 20/05/2017.
 */

public abstract class Brick implements Movement {

    public int getHeight() {
        return mHeight;
    }

    public int getWitdh() {
        return mWitdh;
    }

    public int[][] getMatrix() {
        return mMatrix;
    }

    public void setMatrix(int[][] mMatrix) {
        this.mMatrix = mMatrix;
    }


    protected int mHeight;
    protected int mWitdh;
    protected int[][] mMatrix;

    public int getX() {
        return mPos_x;
    }

    public int getY() {
        return mPos_y;
    }

    public int getClr() {
        return mColor;
    }

    protected int mPos_x;
    protected int mPos_y;
    protected int mColor;
    protected BrickManager brickManager;

    public Brick(int x, int y)
    {
        this.mPos_x = x;
        this.mPos_y = y;
        this.brickManager = new BrickManager(null);
    }

    /**
     * Differentes fonctions pour les mouvements
     */
    public void down(){
        this.mPos_y = this.mPos_y+1;
    }

    public void left() {
        this.mPos_x = this.mPos_x-1;
    }

    public void right() {
        this.mPos_x = this.mPos_x+1;
    }

    public Brick nextRotationCheck(){
        return null;
    }

    /**
     * Verifie si le mouvement est possible
     * @param gameMatrix
     * @return
     */
    public boolean validRotate(int[][] gameMatrix){
        boolean success = false;
        if(mPos_x+mHeight-1 <= 9 && mPos_y+mWitdh-1 <= 19)
        {
            int [][] testGameMatrix = copyMatrix(gameMatrix);
            brickManager.setGameMatrix(testGameMatrix);
            brickManager.removeBrick(this);
            success = brickManager.addBrick(nextRotationCheck());
            brickManager.setGameMatrix(null);
        }
        return success;
    }

    public boolean validLeft(int[][] gameMatrix){
        boolean success = false;
        if(this.mPos_x > 0)
        {
            int maxY = mPos_y + mHeight;
            for(int x = 0; x < mWitdh; x++)
            {
                int yBrick = 0;
                for (int y = mPos_y; y < maxY; y++)
                {
                    if(x == 0)
                    {
                        if(gameMatrix[y][(mPos_x-1)+x] == 1 && mMatrix[yBrick][x] == 1)
                        {
                            success = false;
                        }
                        else{
                            success = true;
                        }
                    }
                    else{
                        if(gameMatrix[y][(mPos_x-1)+x] == 1 && mMatrix[yBrick][x] == 1 && mMatrix[yBrick][x-1] == 0)
                        {
                            success = false;
                        }
                        else{
                            success = true;
                        }
                    }

                    if(!success){break;}
                    yBrick++;
                }
                if(!success){break;}
            }
        }

        return success;
    }

    public boolean validRight(int[][] gameMatrix){
        boolean success = false;
        if(this.mPos_x+(this.mWitdh-1) < 9)
        {
            int maxY = mPos_y + mHeight;
            int maxX = mPos_x + mWitdh;
            for(int x = 0; x < mWitdh; x++)
            {
                int yBrick = 0;
                for (int y = mPos_y; y < maxY; y++) {
                    if(x == 0)
                    {
                        if (gameMatrix[y][maxX-x] == 1 && mMatrix[yBrick][mWitdh - (1+x)] == 1)
                        {
                            success = false;
                        }
                        else{
                            success = true;
                        }
                    }
                    else{
                        if (gameMatrix[y][maxX-x] == 1 && mMatrix[yBrick][mWitdh - (1+x)] == 1 && mMatrix[yBrick][mWitdh - x] == 0)
                        {
                            success = false;
                        }
                        else{
                            success = true;
                        }
                    }


                    if (!success){break;}
                    yBrick++;
                }
                if (!success){break;}
            }
        }

        return success;
    }

    public boolean validDown(int[][] gameMatrix){
        boolean success = false;
        if(this.mPos_y+(this.mHeight-1) < 19)
        {
            int maxY = mPos_y + mHeight;
            int maxX = mPos_x + mWitdh;
            for(int y = mHeight-1; y >= 0; y--)
            {
                int xBrick = 0;
                for (int x = mPos_x; x < maxX; x++)
                {
                    if(y == mHeight-1)
                    {
                        if(gameMatrix[maxY][x] == 1 && mMatrix[y][xBrick] == 1)
                        {
                            success = false;
                        }
                        else{
                            success = true;
                        }
                    }
                    else{
                        if(gameMatrix[mPos_y+y+1][x] == 1 && mMatrix[y][xBrick] == 1 && mMatrix[y+1][xBrick] == 0)
                        {
                            success = false;
                        }
                        else{
                            success = true;
                        }
                    }


                    if(!success){break;}
                    xBrick++;
                }
                if(!success){break;}
            }

        }
        return success;
    }

    public int[][] copyMatrix(int[][] gameMatrix)
    {
        int [][] test_gameMatrix = new int[gameMatrix.length][];
        for(int i = 0; i < gameMatrix.length; i++)
        {
            int[] aMatrix = gameMatrix[i];
            int   aLength = aMatrix.length;
            test_gameMatrix[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, test_gameMatrix[i], 0, aLength);
        }

        return test_gameMatrix;
    }
}
