/*
Author: Will Yaj

Contract for Game Objects (Player, obstacle, etc)
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;

public interface GameObject {
    void draw(Canvas canvas);
    void update();
}

