package com.project.matam.tetris.gamecore;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.matam.tetris.R;
import com.project.matam.tetris.bricks.Brick;
import com.project.matam.tetris.bricks.Brick_I;
import com.project.matam.tetris.bricks.Brick_J;
import com.project.matam.tetris.bricks.Brick_L;
import com.project.matam.tetris.bricks.Brick_O;
import com.project.matam.tetris.bricks.Brick_S;
import com.project.matam.tetris.bricks.Brick_T;
import com.project.matam.tetris.bricks.Brick_Z;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //********************
    //Gridview of the game : Grille où vont etre affichées les cases et les pieces
    private GridView gameGrid;

    //***********************
    //Bitmap of the griedview : Liste des Bitmaps qui vont changer de couleur au cours de la partie
    //pour symboliser les pieces à l'écran
    private ArrayList<Bitmap> items = new ArrayList<>();

    //*************************
    //Nombre lignes et colonnes
    private int mRow = 20;
    private int mCol = 10;

    //*************************************
    //Matrix and matrix manager of the game : Matrice de int symbolique de l'emplacement des pieces
    //et manager des differentes fonctions d'ajout des pieces
    private int[][] gameMatrix = new int[mRow][mCol];
    private BrickManager brickManager = new BrickManager(gameMatrix);

    //***************************************************************
    //Variable utile pour une futur vue adaptable à tout type d'écran
    private int screenWidth;
    private int screenHeight;

    //***************************************
    //Adapter between the grid and the bitmap : Adapteur qui va permetre le lien entre les bitmap
    //et la gridview
    private GameGridAdapter gridAdapter;

    //**************************
    //Game thread and brick list : Thread de jeu et la liste des briques qui seront generées
    // de maniere aleatoire, la liste me permet de faire des preview de 2 briques en plus
    // de la brique en cours de mouvement.
    private Thread thread = null;
    private List<Brick> bricks = new ArrayList<Brick>();

    //********************
    // Objects on the view : Les differents objets qui seront sur l'écran
    private Button bLeft;
    private Button bRight;
    private Button bDown;
    private Button bRotate;
    private TextView tvScore;
    private List<ImageView> previews = new ArrayList<>();
    private ImageView preview;
    private ImageView preview2;

    //******************************************************
    //Permet de mieux gerer le gameOver et la reprise de jeu
    boolean firstGame = true;
    boolean running = true;
    boolean gameContinue = true;

    //****************************
    //Variables utiles pour le jeu
    int score = 0;
    int time = 600;
    int maxTime = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //**************************************
        //Initialise la vue et la matrice de jeu
        initScreenApp();
        initViews();
        initGridItems(this.mRow,this.mCol);

        //********************************************************
        //Initialise la grille de jeu et les Bitmap via l'adapteur
        this.gridAdapter = new GameGridAdapter(this, this.items);
        this.gameGrid.setNumColumns(this.mCol);
        this.gameGrid.setAdapter(this.gridAdapter);

        //*****************************
        //Initialise la première partie
        initGame();

        //**************************
        //Lancement du thread de jeu
        final Handler gameHandler = new Handler();

        final Runnable gameThread = new Runnable() {
            @Override
            public void run() {
                if(running)
                {
                    gameHandler.postDelayed(this,time);
                    if(gameContinue)
                    {
                        if(bricks.get(0).validDown(brickManager.getGameMatrix()))
                        {
                            brickManager.removeBrick(bricks.get(0));
                            bricks.get(0).down();
                            brickManager.addBrick(bricks.get(0));
                            reloadGameGrid(mRow,mCol,bricks.get(0).getClr());
                        }
                        else
                        {
                            //**********************************************************
                            //La piece en cours de jeu ne peut plus faire de déplacement :
                            //on vérifie si une ligne est complete et on met à jour le score
                            playerScore(mRow,mCol);
                            //on augmente la vitesse du jeu
                            time = time - (score/120);
                            if(time<maxTime)
                            {
                                time = maxTime;
                            }
                            //On enleve la piece en cours qui va etre remplacée par la prochaine
                            //dans la liste et on ajoute une nouvelle piece random dans la liste
                            bricks.remove(0);
                            bricks.add(randomBrick());
                            // On actualise la vue des pieces à venir
                            changePreview(0);
                            changePreview(1);
                            gameContinue = brickManager.addBrick(bricks.get(0));
                            reloadGameGrid(mRow,mCol,bricks.get(0).getClr());
                        }
                    }else{
                        //*********************************
                        //GAME OVER réinitialisation du jeu
                        initGridItems(mRow,mCol);
                        initGame();
                    }
                }
            }
        };
        gameHandler.postDelayed(gameThread,time);
    }

    /**
     * Fonction qui permet de gerer les events des boutons de l'écran
     * A chaque clique on verifie si l'action est possible
     * @param v : vue de l'objet sur lequel on clique
     *
     */
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.LeftB:
                if(bricks.get(0).validLeft(brickManager.getGameMatrix()))
                {
                    brickManager.removeBrick(bricks.get(0));
                    bricks.get(0).left();
                    brickManager.addBrick(bricks.get(0));
                    reloadGameGrid(mRow,mCol,bricks.get(0).getClr());
                }
                break;
            case R.id.RightB:
                if(bricks.get(0).validRight(brickManager.getGameMatrix())){
                    brickManager.removeBrick(bricks.get(0));
                    bricks.get(0).right();
                    brickManager.addBrick(bricks.get(0));
                    reloadGameGrid(mRow,mCol,bricks.get(0).getClr());
                }
                break;
            case R.id.DownB:
                while(bricks.get(0).validDown(brickManager.getGameMatrix())){
                        brickManager.removeBrick(bricks.get(0));
                        bricks.get(0).down();
                        brickManager.addBrick(bricks.get(0));

                }
                reloadGameGrid(mRow,mCol,bricks.get(0).getClr());
                break;
            case R.id.RotateB:
                if(bricks.get(0).validRotate(brickManager.getGameMatrix())){
                    brickManager.removeBrick(bricks.get(0));
                    bricks.get(0).rotate();
                    brickManager.addBrick(bricks.get(0));
                    reloadGameGrid(mRow,mCol,bricks.get(0).getClr());
                }
                break;
        }
    }

    /**
     * Initialise toutes les vues et objets de l'ecran
     */
    private void initViews() {
        this.gameGrid = (GridView) findViewById(R.id.gameGrid);
        this.tvScore = (TextView)findViewById(R.id.Score);
        this.preview = (ImageView)findViewById(R.id.Preview);
        this.preview2 = (ImageView)findViewById(R.id.Preview2);
        this.previews.add(this.preview);
        this.previews.add(this.preview2);
        this.bLeft = (Button)findViewById(R.id.LeftB);
        this.bRight = (Button)findViewById(R.id.RightB);
        this.bDown = (Button)findViewById(R.id.DownB);
        this.bRotate = (Button)findViewById(R.id.RotateB);
        this.bLeft.setOnClickListener(this);
        this.bRight.setOnClickListener(this);
        this.bDown.setOnClickListener(this);
        this.bRotate.setOnClickListener(this);
    }

    /**
     * @param row : number of lines on the grid
     * @param col : number of columns on the grid
     * Initialize the box with bitmap for the gameGrid and initialize the matrix
     */
    private void initGridItems(int row, int col)
    {
        for (int y=0;y<row;y++) {
            for(int x = 0; x < col; x++){
                if(this.firstGame)
                {
                    Bitmap item = Bitmap.createBitmap(25,25, Bitmap.Config.RGB_565);
                    item.eraseColor(Color.RED);
                    this.items.add(item);
                }
                this.gameMatrix[y][x] = 0;
            }
        }
        if(this.firstGame)
        {
            this.firstGame = false;
        }
        brickManager.setGameMatrix(this.gameMatrix);
    }

    /**
     * Recupere la taille de l'écran (pour une futur évolution pour gerer tous types d'écrans
     */
    private void initScreenApp()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenHeight = displayMetrics.heightPixels;
        this.screenWidth = displayMetrics.widthPixels;
    }

    /**
     * Initialise tous les parametres utiles pour le jeu et refresh la preview des pieces à venir
     */
    private void initGame()
    {
        this.score = 0;
        this.time = 600;
        this.tvScore.setText(String.valueOf(score));
        Brick brickRand01 = randomBrick();
        Brick brickRand02 = randomBrick();
        Brick brickRand03 = randomBrick();
        this.bricks.clear();
        this.bricks.add(brickRand01);
        this.bricks.add(brickRand02);
        this.bricks.add(brickRand03);
        changePreview(0);
        changePreview(1);
        this.gameContinue = brickManager.addBrick(this.bricks.get(0));
        if(this.gameContinue){
            reloadGameGrid(mRow,mCol,this.bricks.get(0).getClr());
        }
    }

    /**
     * Fonction qui affiche les images  des pieces à venir
     * @param pos : position de la piece à afficher
     */
    private void changePreview(int pos)
    {
        int clr = this.bricks.get(pos+1).getClr();
        switch (clr)
        {
            case Color.GREEN:
                previews.get(pos).setImageResource(R.drawable.i);
                break;
            case Color.BLUE:
                previews.get(pos).setImageResource(R.drawable.j);
                break;
            case Color.BLACK:
                previews.get(pos).setImageResource(R.drawable.l);
                break;
            case Color.MAGENTA:
                previews.get(pos).setImageResource(R.drawable.o);
                break;
            case Color.CYAN:
                previews.get(pos).setImageResource(R.drawable.s);
                break;
            case Color.YELLOW:
                previews.get(pos).setImageResource(R.drawable.z);
                break;
            case Color.GRAY:
                previews.get(pos).setImageResource(R.drawable.t);
                break;
        }
    }

    /**
     * Fonction qui reload les bitmap en fonction de la matrice de 1 et 0
     * @param row : Lignes de la matrice
     * @param col : Colonnes de la matrice
     * @param color : Couleur de la piece en cours
     */
    private void reloadGameGrid(int row, int col, int color)
    {
        int brickX = -1;
        int brickY = -1;
        Brick brick = this.bricks.get(0);
        int maxY = brick.getY()+ brick.getHeight();
        int maxX = brick.getX()+ brick.getWitdh();
        for(int y = brick.getY(); y < maxY; y++ )
        {
            brickY++;
            for(int x = brick.getX(); x < maxX; x++)
            {
                brickX++;
                if(brick.getMatrix()[brickY][brickX] == 1 && brickManager.getGameMatrix()[y][x] == 1)
                {
                    int coord_matrix_to_list = y*col+x;
                    this.items.get(coord_matrix_to_list).eraseColor(color);
                    gridAdapter.notifyDataSetChanged();
                }
            }//end for
            brickX = -1;
        }//end for

        for (int y=0;y<row;y++) {
            for(int x = 0; x < col; x++){
                if(brickManager.getGameMatrix()[y][x] == 0)
                {
                    int coord_matrix_to_list = y*col+x;
                    this.items.get(coord_matrix_to_list).eraseColor(Color.RED);
                    gridAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * Genere des pieces random
     * @return une piece random
     */
    private Brick randomBrick()
    {
        Brick randBrick = null;
        Random rand = new Random();
        int randInt = rand.nextInt(8-1)+1;
        switch (randInt)
        {
            case 1:
                randBrick = new Brick_I(4,0);
                break;
            case 2:
                randBrick = new Brick_J(4,0);
                break;
            case 3:
                randBrick = new Brick_L(4,0);
                break;
            case 4:
                randBrick = new Brick_O(4,0);
                break;
            case 5:
                randBrick = new Brick_S(4,0);
                break;
            case 6:
                randBrick = new Brick_T(4,0);
                break;
            case 7:
                randBrick = new Brick_Z(4,0);
                break;
        }

        return randBrick;
    }

    /**
     * Gere l'effacement des lignes et la gestion du score joueur
     * avec un ratio de combo choisi un peu au hasard.....
     * @param row
     * @param col
     */
    private void playerScore(int row, int col)
    {
        boolean combo = true;
        int comboCount = 0;
        int cpt = 0;
        while(combo)
        {
            combo = brickManager.checkLine(row,col);
            if(combo)
            {
                brickManager.downAll(col);
                cpt+=10;
                if(cpt > 10)
                {
                    comboCount+=10;
                }
            }
        }
        this.score = score + (10*(comboCount+cpt));
        this.tvScore.setText(String.valueOf(score));
    }



}
