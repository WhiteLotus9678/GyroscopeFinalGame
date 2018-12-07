/*
Author: Will Yaj
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

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

    // Constructor
    public GameplayScene() {
        // create player object here (separate player class)
        // setup player position here
        // make sure to update player
        player = new Player(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        // create obstacles here (separate class to handle the random generations)

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

        // draw obstacles

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

            if(playerPoint.x < 0)
                playerPoint.x = 0;
            else if(playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;
            if(playerPoint.y < 0)
                playerPoint.y = 0;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);
            // Check whether the player has collided with an obstacle
            /*
            if() {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
            */
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

