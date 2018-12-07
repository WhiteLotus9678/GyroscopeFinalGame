/*
Author: Will Yaj
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    // Framerate Per Second. Most phones will always be able to run 30 FPS or above
    public static final int MAX_FPS = 60;

    // The average FPS
    private double averageFPS;

    // SurfaceHolder allows control of the surface size and format (editing pixels and monitoring)
    private SurfaceHolder surfaceHolder;

    // Reference to the game panel
    private GamePanel gamePanel;

    // Checks if the thread is running
    private boolean running;

    // Canvas to draw on
    private static Canvas canvas;

    // Setter for running
    public void setRunning(boolean running) {
        this.running = running;
    }

    // Constructor
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    // Handles the thread when it starts running
    @Override
    public void run() {
        // Thread start time
        long startTime;

        // Capture the current time in milliseconds
        long timeMillis;

        // Thread wait time
        long waitTime;

        // Frame count
        int frameCount = 0;

        // Total time thread was running for
        long totalTime = 0;

        // Target time needed to reach the max FPS (60)
        long targetTime = 1000/MAX_FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try {
                if(waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            // FOR DEBUGGING: Converting from ns to ms and then convert to find the average FPS
            if(frameCount == MAX_FPS) {
                averageFPS = 1000/(totalTime/frameCount/1000000);
                frameCount = 0;
                totalTime = 0;

                System.out.println(averageFPS);
            }
        }
    }
}