package com.lemming.game.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.ui.EditableValue;

/**
 * Created by Alexander on 20.07.2015.
 */
public class Path {
    //TODO check everything
    public Array<PathSegment> segments = new Array<PathSegment>();
    Array<Float> lengthSum = new Array<Float>(); //первый элемент - длинна 0го
    float totalLength;
    public EditableValue.Vector2Value pos = new EditableValue.Vector2Value(new Vector2()){
        @Override
        public void set(Vector2 value) {
            dirtyVertices |= get().epsilonEquals(value, 1e-10f);
            super.set(value);
        }
    };
    public EditableValue.FloatValue scale = new EditableValue.FloatValue(1f){
        @Override
        public void set(Float value) {
            super.set(value);
            dirtyVertices |= get() != value.floatValue();
        }
    };
    public EditableValue.FloatValue angleDeg = new EditableValue.FloatValue(0f){
        @Override
        public void set(Float value) {
            super.set(value);
            dirtyVertices |= get() != value.floatValue();
        }
    };
    {
        pos.name = "pos";
        scale.name = "scale";
        angleDeg.name = "angle";
    }



    public float getScale() {
        return scale.get();
    }

    public void setScale(float scale) {
        this.scale.set(scale);
    }

    public float getAngleDeg() {
        return angleDeg.get();
    }

    public void setAngleDeg(float angleDeg) {
        this.angleDeg.set(angleDeg);
    }

    public Vector2 getPos() {
        return pos.get();
    }

    public void setPos(Vector2 pos) {
        this.pos.set(pos);
    }

    boolean dirtyVertices = true;

    public Path(){}

    public Path(Array<Vector2> points){
        for(int i = 0; i < points.size; i++) {
            addPoint(points.get(i));
        }
    }
    private boolean onePoint = false;
    public void addPoint(Vector2... point){
        for(Vector2 p : point) {
            PathSegment segment;
            if(segments.size == 0){
                segment = new PathSegment(p, p);
                onePoint = true;
            }else if(onePoint){
                segment = new PathSegment(segments.get(segments.size - 1).getStart(), p);
                segments.clear();
                lengthSum.clear();
                onePoint = false;
            }else
                segment = new PathSegment(segments.get(segments.size - 1).getEnd(), p);
            float l = segment.totalLength;
            segments.add(segment);
            dirtyVertices = true;
        }
        recountLength();
    }

    public void recountLength(){
        lengthSum.clear();
        totalLength = 0;
        for(PathSegment s : segments){
            lengthSum.add(totalLength);
            totalLength += s.totalLength;
        }
    }

    public Vector2 valueAt(float t){
        if(segments.size == 0) return null;
        int s = getSegmentNumAt(t);
        t = t * totalLength;
        t = t - lengthSum.get(s);
        t /= segments.get(s).totalLength;
        Vector2 valueAt = segments.get(s).getValueAt(t);
        valueAt.scl(scale.get()).rotateRad(angleDeg.get());
        valueAt.add(pos.get());
        return valueAt;
    }

    private int getSegmentNumAt(float t){
        t = t * totalLength;
        int i;
        for(i = 0; i < lengthSum.size; i++){
            if(lengthSum.get(i) > t) break;
        }
        i = i == 0 ? 0 : i-1;
        return i;
    }

    public Vector2 getDerivativeAt(float t){
        return segments.get(getSegmentNumAt(t)).getValueAt(t);
    }
    float[] vertices = new float[0];
    public float[] getVertices(){
        if(dirtyVertices)
            updateVertices();
        return vertices;
    }

    public void updateVertices(){
        if(segments.size != 0) {
            int size = segments.size * 2 + 2;
            if (vertices.length != size)
                vertices = new float[size];
            Vector2 temp = new Vector2();
            for (int i = 0; i < segments.size; i++) {
                PathSegment s = segments.get(i);
                temp.set(s.getValueAt(0));
                temp.scl(getScale()).rotate(getAngleDeg()).add(getPos());
                vertices[i * 2] = temp.x;
                vertices[i * 2 + 1] = temp.y;
            }
            temp.set(segments.get(segments.size - 1).getValueAt(1));
            temp.scl(getScale()).rotate(getAngleDeg()).add(getPos());
            vertices[vertices.length - 2] = temp.x;
            vertices[vertices.length - 1] = temp.y;
        }
        dirtyVertices = false;
    }

    public float projectXT(float x){
        boolean bigger = false;
        for(int i = 0; i < segments.size; i++) {
            PathSegment s = segments.get(i);
            float t = s.projectXT(x);
            if(0 <= t && t <= 1){
                return (t*s.totalLength  + lengthSum.get(i)) / totalLength;
            }else{
                if(t > 1)
                    bigger = true;
            }
        }
        return bigger ? 1 : 0;
    }

    public void setDirtyVertices(boolean dirtyVertices) {
        this.dirtyVertices = dirtyVertices;
    }

    public void setPoint(int pointNum, Vector2 point){
        getPoint(pointNum).set(point);
        if(pointNum != 0)
            segments.get((pointNum-1)%segments.size).update();
        if(pointNum != getPointsNum())
            segments.get((pointNum  )%segments.size).update();
        recountLength();
    }

    public Vector2 getPoint(int i) {
        Vector2 result;

        if (i == segments.size) {
            result = segments.get(i-1).getEnd();
        }else
            result = segments.get(i).getStart();
        return result;
    }

    public int getPointsNum() {
        return (onePoint?1 : (segments.size != 0?segments.size+1:0));
    }


    public Array<EditableValue> getValues(){
        Array<EditableValue> result = new Array<EditableValue>();
        EditableValue.IntValue value = new EditableValue.IntValue() {
            @Override
            public Integer get() {
                return getPointsNum();
            }

            @Override
            public void set(Integer value) {
                int n = value;
                while (getPointsNum() < n)
                    addPoint(new Vector2());
            }
        };
        value.name = "numberOfPoints";
        value.updateEditor = true;
        result.add(value);
        result.add(pos);
        result.add(angleDeg);
        result.add(scale);
        if(segments.size != 0)
            for(int i = 0; i < getPointsNum(); i++){
                final int finalI = i;
                EditableValue.Vector2Value pointI = new EditableValue.Vector2Value(getPoint(finalI)){
                    @Override
                    public void set(Vector2 value) {
                        super.set(value);
                        setPoint(finalI, value);
                    }
                };
                pointI.name = "point" + i;
                result.add(pointI);
            }
        return result;
    }
}
