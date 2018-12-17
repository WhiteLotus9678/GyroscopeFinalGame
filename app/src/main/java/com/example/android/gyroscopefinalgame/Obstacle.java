package com.example.android.gyroscopefinalgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;

import java.util.Random;

public class Obstacle implements GameObject {
    private Rect block;
    private int color;
    public Point position;
    public int width;
    public int height;
    Random genBlockPosX = new Random();

    public Rect getRect(){ return block;}

    public Obstacle(Rect block, int color, int size){
        this.block = block;
        this.color = color;
        this.width = size;
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

    public void resetBlockPosition(int maxScreenWidth){
        //int index = genBlock.nextInt(obstacles.size()); // getting random number between obstacles' size
        //mainObstacle = obstacles.get(index); // getting the selected block from index
        int min = this.width / 2;
        int randX = genBlockPosX.nextInt(maxScreenWidth-this.width) + min; // Randomizing between the screen width which is 800
        //mainObstacle.getRect().offset(posX, -500);
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
        block.set(point.x - this.width/2, point.y - this.height/2, point.x + this.width/2, point.y + this.height/2);
    }

}
