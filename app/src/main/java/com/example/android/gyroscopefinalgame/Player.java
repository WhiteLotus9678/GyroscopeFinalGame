package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Player implements GameObject {
    private Rect rectangle;
    private int color;

    public Rect getRectangle() {
        return rectangle;
    }

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

    @Override
    public void update() {

    }

    // Moves the rectangle to a different point
    public void update(Point point) {
        // left, top, right, bottom
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);
    }
}

