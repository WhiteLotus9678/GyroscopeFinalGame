/*
Author: Will Yaj
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    // ArrayList of available scenes
    private ArrayList<Scene> scenes = new ArrayList<>();

    // The active scene
    public static int ACTIVE_SCENE;

    // Constructor
    public SceneManager(int width, int height) {
        ACTIVE_SCENE = 0;
        scenes.add(new GameplayScene(width, height));
    }

    // Handle player controls based on the active scene
    public void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    // Update the scene based on the active scene
    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    // Draw on the canvas based on the active scene
    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }
}