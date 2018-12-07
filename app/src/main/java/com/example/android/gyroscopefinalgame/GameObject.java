/*
Author: Will Yaj

Contract for Game Objects (Player, obstacle, etc)
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;

public interface GameObject {
    public void draw(Canvas canvas);
    public void update();
}

