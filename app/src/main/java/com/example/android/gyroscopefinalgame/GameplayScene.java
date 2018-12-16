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

public class GameplayScene implements Scene {

    // Shape to bind text within
    private Rect r;

    // Player object
    private Player player;

    // Player position
    private Point playerPoint;

    // Checks if the player is moving
    private boolean movingPlayer = false;

    // Checks if the game is over
    private boolean gameOver = false;

    // Handles the length of the game over loop
    private long gameOverTime;

    // Time between frames
    private long frameTime;

    private Gyroscope gyroscope;

    // ---------------- Obstacles related variables ------------

    private Obstacle mainObstacle;
    private Obstacle mainObstacle1; // does not exist atm
    private Obstacle mainObstacle2; // does not exist atm
    private int score = 0;
    private int width;
    private int height;
    private int posX;
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
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        // ------------------------------------------------- OBSTACLES RELATED ON CREATE-----------------------------------------------------------------
        Obstacle shortObs = new Obstacle(new Rect(100, 100, 200, 200), Color.rgb(0, 200, 0));
        Obstacle medObs = new Obstacle(new Rect(100, 100, 200, 200), Color.rgb(0, 0, 100));
        Obstacle longObs = new Obstacle(new Rect(100, 100, 200, 200), Color.rgb(200, 200, 200));
        // pushing all types of obstacle into an array
        obstacles.add(shortObs);
        obstacles.add(medObs);
        obstacles.add(longObs);

        // generating a random number between 0 and 2 to get a random obstacle in the array list
        int index = genBlock.nextInt(obstacles.size()); // need fix here, can only get
        mainObstacle = obstacles.get(index); // array 0, 1, 2

        // **** Generating the 2nd block here ****
        //index = genBlock.nextInt(obstacles.size());
        //mainObstacle1 = obstacles.get(index);

        Log.d("THE SCREEN WIDTH", Integer.toString(width)); // screen width is 800

        posX = genBlockPosX.nextInt(width); // Randoming between the screen width
        mainObstacle.getRect().offset(posX, -500);
// --------------------------------------------------- END OF OBSTACLES ON CREATE-----------------------------------------------

        gyroscope = new Gyroscope();
        gyroscope.register();

        frameTime = System.currentTimeMillis();

    }

    // Resets the game
    public void reset() {
        // reset player position

        // reset player object
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        // reset obstacles

        movingPlayer = false;
    }

    // Terminate the scene
    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    // TODO: Would out in documentation but waiting for gyroscope sensor data
    @Override
    public void receiveTouch(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Pass in the point tapped and if it lies in the player's rectangle
                // That way, player can't teleport
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY())) { // TODO: check if player finger is in player object area
                    movingPlayer = true;
                }

                // If the game is over and 2s have elapsed then reset
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    reset();
                    gameOver = false;
                    // reset gyroscope data?
                    gyroscope.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer) {
                    // move player to new position
                    playerPoint.set((int) event.getX(), (int) event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
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
        //------ end of drawing the obstacles

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

            if (gyroscope.getOrientation() != null && gyroscope.getStartOrientation() != null)
            {
                float pitch = gyroscope.getOrientation()[1] - gyroscope.getStartOrientation()[1];
                float roll = gyroscope.getOrientation()[2] - gyroscope.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT / 1000f;

                playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                //playerPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime : 0;
            }

            if(playerPoint.x < 0)
                playerPoint.x = 0;
            else if(playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;
            if(playerPoint.y < 0)
                playerPoint.y = 0;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);

            // ------------------------------  OBSTACLES IMPLEMENTATIONS ---------------------------------------------
            // Check if the block has reached the bottom of the screen
            if(mainObstacle.position.y >= 1400){
                int index = genBlock.nextInt(obstacles.size()); // getting random number between obstacles' size
                mainObstacle = obstacles.get(index); // getting the selected block from index
                posX = genBlockPosX.nextInt(width); // Randoming between the screen width which is 800
                //mainObstacle.getRect().offset(posX, -500);
                mainObstacle.resetBlockPosition(posX); // resetting the block position when it hits the bottom screen
                level++;
                // Update the score down here.
            }

            // updating the obstacles position
            mainObstacle.update(mainObstacle.position);

            // Check whether the player has collided with an obstacle
            if(player.getRectangle().intersect(mainObstacle.getRect()) || player.getRectangle().intersect(mainObstacle.getRect())
                    || player.getRectangle().intersect(mainObstacle.getRect())) {

                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }

            if(level == 1){
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

