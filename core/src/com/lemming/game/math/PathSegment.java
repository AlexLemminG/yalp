package com.lemming.game.math;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Alexander on 20.07.2015.
 */
public class PathSegment {
    private Vector2 start = new Vector2();
    private Vector2 end = new Vector2();
    private Vector2 endMinusStart = new Vector2();
    private Vector2 endMinusStartNormalized = new Vector2();
    float totalLength;

    public PathSegment(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;
        update();
    }

    public void update(){
        endMinusStart.set(end).sub(start);
        endMinusStartNormalized.set(endMinusStart).nor();
        totalLength = endMinusStart.len();
    }

    private Vector2 temp = new Vector2();

    public Vector2 getValueAt(float t){
        return temp.set(start.x + endMinusStart.x * t, start.y + endMinusStart.y * t);
    }

    public Vector2 getDerivativeAt(float t){
        return temp.set(endMinusStartNormalized);
    }

    public float projectXT(float x){
        if(endMinusStart.x == 0){
            return 0;
        }else{
            return (x-start.x)/endMinusStart.x;
        }
    }

    public void setStart(Vector2 start) {
        this.start = start;
        update();
    }

    public void setEnd(Vector2 end) {
        this.end = end;
        update();
    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getEnd() {
        return end;
    }
}
