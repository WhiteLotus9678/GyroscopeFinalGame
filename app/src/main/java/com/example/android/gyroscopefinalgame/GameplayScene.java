/*
Author: Will Yaj
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class GameplayScene implements Scene {

    // Shape to bind text within
    private Rect r = new Rect();

    // Player object
    private Player player;

    // Player position
    private Point playerPoint;

    // Checks if the player is moving
    //private boolean movingPlayer = false;

    // Checks if the game is over
    private boolean gameOver = false;

    // Handles the length of the game over loop
    private long gameOverTime;

    // Time between frames
    private long frameTime;

    private Accelerometer accelerometer;

    // ---------------- Obstacles related variables ------------

    private Obstacle mainObstacle;
    private Obstacle mainObstacle1; // does not exist atm
    private Obstacle mainObstacle2; // does not exist atm
    private int score = 0;
    private int width;
    private int height;
    private int posX;
    private int moveSpeed = 3; // multiplier of base accelerometer speed
    private int max = 750; // randoming the x position of the block
    private int min = 50; //randoming the x position of the block
    private int level = 0;
    Random genBlockPosX = new Random(); // randoming the blocks falling position
    Random genBlock = new Random(); // randoming the blocks
    // create obstacles here (separate class to handle the random generations)
    ArrayList<Obstacle> obstacles = new ArrayList<>();

    //---------------- end of obstacles related variables -----------

    // Constructor
    public GameplayScene(int width, int height) {
        this.width = width;
        this.height = height;
        // create player object here (separate player class)
        // setup player position here
        // make sure to update player
        player = new Player(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 5*Constants.SCREEN_HEIGHT/6);
        player.update(playerPoint);

        // ------------------------------------------------- OBSTACLES RELATED ON CREATE-----------------------------------------------------------------
        Obstacle shortObs = new Obstacle(new Rect(100, 100, 200, 200), Color.rgb(0, 0, 0), 100, 1); // black 1 point
        Obstacle medObs = new Obstacle(new Rect(100, 100, 200, 200), Color.rgb(102, 0, 153), 175,3); // purple 3 point
        Obstacle longObs = new Obstacle(new Rect(100, 100, 200, 200), Color.rgb(255, 204, 51),250,5); // gold 5 point
        // pushing all types of obstacle into an array
        obstacles.add(shortObs);
        obstacles.add(medObs);
        obstacles.add(longObs);

        // generating a random number between 0 and 2 to get a random obstacle in the array list
        int index = genBlock.nextInt(obstacles.size());
        mainObstacle = obstacles.get(index); // array 0, 1, 2

        //Log.d("THE SCREEN WIDTH", Integer.toString(width)); // screen width is 800

        posX = genBlockPosX.nextInt(max-min) + min; // Randoming between the screen width
        mainObstacle.resetBlockPosition(posX);
        mainObstacle.getRect().offset(posX, -500);

        // --------------------------------------------------- END OF OBSTACLES ON CREATE-----------------------------------------------

        // instantiate accelerometer and register it to active sensors
        accelerometer = new Accelerometer();
        accelerometer.register();

        frameTime = System.currentTimeMillis();

    }

    // Resets the game
    public void reset() {
        // reset player position
        player = new Player(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));

        // reset player object
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 5*Constants.SCREEN_HEIGHT/6);
        player.update(playerPoint);

        // reset obstacle speed
        level = 0;
        posX = genBlockPosX.nextInt(max-min) + min;
        mainObstacle.resetBlockPosition(width);

        // reset player score
        score = 0;

        //movingPlayer = false;
    }

    // Terminate the scene
    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // If the game is over and 2 seconds have elapsed, then reset
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    reset();
                    gameOver = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    // Draws all objects onto the scene
    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        //draw the player
        player.draw(canvas);

        //------ Drawing the obstacles -----
        mainObstacle.draw(canvas);
        //mainObstacle1.draw(canvas);
        //------ end of drawing the obstacles

        Paint paintScore = new Paint();
        paintScore.setTextSize(100);
        paintScore.setColor(Color.MAGENTA);

        canvas.drawText("" + score, 150, 150 + paintScore.descent() - paintScore.ascent(), paintScore);

        if(gameOver) {
            Paint paint = new Paint();
            paint.setTextSize(150);
            paint.setColor(Color.MAGENTA);

            drawCenterText(canvas, paint, "GAME OVER");
        }
    }

    // Updates every object in the scene
    @Override
    public void update() {
        if(!gameOver) {
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();

            // Update player position based on accelerometer
            float roll = accelerometer.GetCurrentX();
            playerPoint.x -= roll * moveSpeed;

            // Make sure player stays within the game screen
            if(playerPoint.x < player.GetWidth() * 0.5)
                playerPoint.x = (int)(player.GetWidth() * 0.5);
            else if(playerPoint.x > Constants.SCREEN_WIDTH - player.GetWidth() * 0.5)
                playerPoint.x = Constants.SCREEN_WIDTH - (int)(player.GetWidth() * 0.5);
            if(playerPoint.y < 0)
                playerPoint.y = 0;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);

            // ------------------------------  OBSTACLES IMPLEMENTATIONS ---------------------------------------------
            // Check if the block has reached the bottom of the player's y position
            if(mainObstacle.position.y >= playerPoint.y){

                // Increment the score
                score += mainObstacle.getScore();
                int index = genBlock.nextInt(obstacles.size()); // getting random number between obstacles' size
                mainObstacle = obstacles.get(index); // getting the selected block from index
                /*
                posX = genBlockPosX.nextInt(max-min) + min; // Randomizing between the screen width which is 800
                */
                //mainObstacle.getRect().offset(posX, -500);
                mainObstacle.resetBlockPosition(800); // resetting the block position when it hits the bottom screen

                level++;
            }

            // updating the obstacles position
            mainObstacle.update(mainObstacle.position);

            // Check whether the player has collided with an obstacle
            if(player.getRectangle().intersect(mainObstacle.getRect())) {

                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }

            if(level <= 1){
                mainObstacle.update();
            }else if(level <= 2){
                mainObstacle.update();
                mainObstacle.update();
            }else if(level <= 3){
                for(int i = 0; i <= 3; i++){
                    mainObstacle.update();
                }
            }else if(level <= 4){
                for(int i = 0; i <= 4; i++){
                    mainObstacle.update();
                }
            }else if(level <= 5){
                for(int i = 0; i <= 5; i++){
                    mainObstacle.update();
                }
            }else if(level <= 6){
                for(int i = 0; i <= 6; i++){
                    mainObstacle.update();
                }
            }else if(level <= 7){
                for(int i = 0; i <= 7; i++){
                    mainObstacle.update();
                }
            } else if(level > 7){
                for(int i = 0; i <= 8; i++){
                    mainObstacle.update();
                }
            }

            mainObstacle.update(); // dropping the block

// ------------------------------------------------ End of obstacles implementations ---------------------------------------
        }
    }

    // Draws text on the center of the screen
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}

