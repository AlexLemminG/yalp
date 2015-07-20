package com.lemming.game.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Alexander on 20.07.2015.
 */
public class Path {
    //TODO check everything
    Array<PathSegment> segments = new Array<PathSegment>();
    Array<Float> lengthSum = new Array<Float>(); //первый элемент - длинна 0го
    float totalLength;

    public Path(Array<Vector2> points){
        for(int i = 0; i < points.size-1; i++){
            PathSegment segment = new PathSegment(points.get(i), points.get(i + 1));
            float l = segment.totalLength;
            segments.add(segment);
            lengthSum.add(totalLength);
            totalLength += l;
        }

    }

    public Vector2 valueAt(float t){
        int s = getSegmentNumAt(t);
        t = t * totalLength;
        t = t - lengthSum.get(s);
        t /= segments.get(s).totalLength;
        return segments.get(s).getValueAt(t);
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

    public float[] toVertices(){
        float[] vertices = new float[(segments.size + 1)*2];
        for(int i = 0; i < segments.size; i++){
            PathSegment s = segments.get(i);
            Vector2 p = s.start;
            vertices[i * 2] = p.x;
            vertices[i * 2 + 1] = p.y;
        }
        vertices[segments.size * 2] = segments.get(segments.size-1).getValueAt(1).x;
        vertices[segments.size * 2+1] = segments.get(segments.size-1).getValueAt(1).y;
        return vertices;
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
}
