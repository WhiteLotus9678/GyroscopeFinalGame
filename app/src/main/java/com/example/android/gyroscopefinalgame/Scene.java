/*
Author: Will Yaj

Contact for Scenes
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    void update();
    void draw(Canvas canvas);
    void terminate();
    void receiveTouch(MotionEvent event);
}
