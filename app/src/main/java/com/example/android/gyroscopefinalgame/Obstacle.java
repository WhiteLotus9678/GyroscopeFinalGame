package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;

public class Obstacle implements GameObject {
    private Rect block;
    private int color;
    public Point position;
    public int width;
    public int height;

    public Rect getRect(){ return block;}

    public Obstacle(Rect block, int color){
        this.block = block;
        this.color = color;
        this.width = 100;
        this.height = 100;
        this.position = new Point(0,0);
    }

    public void dropRec(){
        //block.top += 4;
        //block.left += 5;
        //block.right += 5;
        //block.bottom += 4;
        this.position.y += 2;
        this.update(this.position);
    }

    public void resetBlockPosition(int randX){
        this.position.y = 0;
        this.position.x = randX;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(block, paint);
    }

    @Override
    public void update() {
        dropRec();
    }

    // Moves the rectangle to a different point
    public void update(Point point) {
        // left, top, right, bottom
        block.set(point.x - block.width()/2, point.y - block.height()/2, point.x + block.width()/2, point.y + block.height()/2);
    }

}
