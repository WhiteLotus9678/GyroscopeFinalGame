/*
Author: Will Yaj

Panel that handles running the program and drawing objects to the Main Activity
 */
package com.example.android.gyroscopefinalgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    // Program execution
    private MainThread thread;

    // Scene manager
    private SceneManager manager;

    // Set the thread and set the gameplay scene
    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        Constants.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);
    }

    // Required, but unused
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Does nothing
    }

    // Start running the thread and capture the initialized time
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        Constants.INIT_TIME = System.currentTimeMillis();

        // Makes game loop start running
        thread.setRunning(true);
        thread.start();
    }

    // Stop the thread when the app is closed out of (prevents memory leaks)
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while(retry) {
            try {
                thread.setRunning(false);
            } catch(Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    // Handles touch inputs using the Scene Manager
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        manager.receiveTouch(event);

        return true;
    }

    // Updates the game using the Scene Manager
    public void update() {
        manager.update();
    }

    // Draws objects to the screen using the Scene Manager
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        manager.draw(canvas);
    }
}
