/*
Author: Will Yaj
 */
package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Player implements GameObject {
    // Player shape style
    private Rect rectangle;

    // Player color
    private int color;

    // Getter for shape
    public Rect getRectangle() {
        return rectangle;
    }

    // Constructor
    public Player(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
    }

    // Draws the player on the canvas
    @Override
    public void draw(Canvas canvas) {
        // Paint allows a custom style for drawing something
        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawRect(rectangle, paint);
    }

    // Required, but unused
    @Override
    public void update() {
        // Do nothing
    }

    // Moves the rectangle to a different point
    public void update(Point point) {
        // Set as: left side, top side, right side, bottom side
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);
    }
    public int GetWidth()
    {
        return rectangle.width();
    }
}

