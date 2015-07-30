package com.lemming.game.comps.grid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lemming.game.basic.GObject;
import com.lemming.game.basic.View;
import com.lemming.game.comps.Comp;
import com.lemming.game.ui.EditableValue;
import com.lemming.game.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 29.07.2015.
 */
public class GridComp extends Comp {
    EditableValue.BoolValue drawGexGrid = new EditableValue.BoolValue(true);
    EditableValue.Vector2Value gridSize = new EditableValue.Vector2Value(new Vector2(1, 1));

    @Override
    public Array<EditableValue> getValues() {
        Array<EditableValue> values = super.getValues();
        values.add(gridSize);

        return values;
    }

    public GridComp(GObject gObject) {
        super(gObject);
    }


    @Override
    public void render(View view) {
        super.render(view);
        if(view.getCurrentRenderer() == View.Renderers.SHAPE_RENDERER) {
            view.getShapeRenderer().setColor(Color.WHITE);
            float forSpaceBetween = 0.8f;
            GridCell.gexagon.setScale(2 / ((float) Math.sqrt(3)) * gridSize.get().x * forSpaceBetween, 1f * gridSize.get().y * forSpaceBetween);
            for(GridCell c : cells)
                c.render(view);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        cells = new Array<GridCell>();
        for(int i = minXi; i <=maxXi; i++)
            for(int j = minYi; j <=maxYi; j++)
                cells.add(new GridCell(i,j,this));


        //temp
//        for(int i = -4; i < 5; i++){
//            getCell(i, 1).walkable = false;
//        }
    }

    public GridCell getCell(int xi, int yi){
        xi = MathUtils.clamp(xi, minXi, maxXi);
        yi = MathUtils.clamp(yi, minYi, maxYi);
        for(GridCell c : cells){
            if(c.xi == xi && c.yi == yi)
                return c;
        }
        return null;
    }

    public GridCell getCell(float x, float y){
        GridCell result = null;
        int yi1 = Math.round(y / 1.5f / gridSize.get().y);
        int xi1 = Math.round((x/gridSize.get().x - ((yi1%2==0)?1:-1)*0.5f)/2);
        int[] xi = new int[]{xi1-1, xi1, xi1+1};
        int[] yi = new int[]{yi1-1, yi1, yi1+1};
        float dist = Float.POSITIVE_INFINITY;
        for(int i = 0; i < 9; i++){
            int xii = xi[i/3];
            int yii = yi[i%3];
            GridCell c = getCell(xii, yii);
            float newDist = c.getCenter().dst2(x, y);
            if(newDist < dist){
                result = c;
                dist = newDist;
            }
        }
        return result;
    }
    private Vector2 getGridCenter(int xi, int yi){
        return new Vector2(xi*2+((yi%2==0)?1:-1)*0.5f, yi*1.5f).scl(gridSize.get());
    }
    int minXi = -5;
    int maxXi = 5;
    int minYi = -5;
    int maxYi = 5;

    Array<GridCell> cells;

    public List<GridCell> bfs(GridCell from, GridCell to){
        boolean found = false;
        List<GridCell> queue = new ArrayList<GridCell>();
        List<GridCell> result = new LinkedList<GridCell>();
        queue.add(from);
        from.bfsVisited = true;
        from.bfsLength = 0;
        int j = 0;
        while(!found && queue.size() > j) {
            GridCell current = queue.get(j++);
            if(current == to){
                found = true;
                while(current != from){
                    result.add(0, current);
                    for(int i = 0; i < 6; i++){
                        GridCell c = current.getAtDirection(i);
                        if((c.bfsVisited) && (c.bfsLength == current.bfsLength - 1)){
                            current = c;
                            break;
                        }
                    }
                }
                result.add(0, from);
            }
            for (int i = 0; i < 6; i++) {
                GridCell c = current.getAtDirection(i);
                if (!c.bfsVisited && c.walkable) {
                    c.bfsVisited = true;
                    c.bfsLength = current.bfsLength+1;
                    queue.add(c);
                }
            }
        }

        for(GridCell c : queue){
            c.bfsLength = 0;
            c.bfsVisited = false;
        }
        return result;
    }


    public static class GridCell{
        private boolean bfsVisited = false;
        private int bfsLength = 0;
        public boolean selected = false;
        public boolean walkable = true;
        public static final int LEFT = 0;
        public static final int LEFT_TOP = 1;
        public static final int RIGHT_TOP = 2;
        public static final int RIGHT = 3;
        public static final int RIGHT_BOTTOM = 4;
        public static final int LEFT_BOTTOM = 5;

        private static Polyline gexagon = new Polyline();
        static{
            float[] vert = new float[14];
            for(int i = 0; i <= 6; i++) {
                vert[i * 2] = MathUtils.cosDeg(i * 60f + 30);
                vert[i * 2 +1] = MathUtils.sinDeg(i * 60f+30);
            }
            gexagon.setVertices(vert);
        }
        int xi;
        int yi;
        private GridComp grid;
        Vector2 pos = new Vector2();
        private Array<GObject> objects;

        public GridCell(int xi, int yi, GridComp grid) {
            this.xi = xi;
            this.yi = yi;
            this.grid = grid;
            objects = new Array<GObject>();
        }

        public void render(View view){
            float lineWidth = 0.05f;
            if(selected) {
                view.getShapeRenderer().setColor(Color.RED);
                lineWidth = 0.1f;
            }
            if(!walkable){
                view.getShapeRenderer().setColor(Color.GRAY);
            }
            pos.set(grid.getGridCenter(xi, yi));
            gexagon.setPosition(pos.x, pos.y);
            Utils.drawPolygon(view.getShapeRenderer(),(gexagon.getTransformedVertices()), lineWidth);
            view.getShapeRenderer().setColor(Color.WHITE);
        }
        public Vector2 getCenter(){
            return pos.set(grid.getGridCenter(xi, yi));
        }

        public GridCell getAtDirection(int direction){
            switch(direction){
                case LEFT : return grid.getCell(xi-1, yi);
                case RIGHT : return grid.getCell(xi+1, yi);
                case LEFT_TOP : return grid.getCell(xi +(yi%2==0?0:-1), yi+1);
                case RIGHT_TOP : return grid.getCell(xi +(yi%2==0?1:0), yi+1);
                case LEFT_BOTTOM : return grid.getCell(xi +(yi%2==0?0:-1), yi-1);
                case RIGHT_BOTTOM : return grid.getCell(xi +(yi%2==0?1:0), yi-1);
                default: throw new IllegalArgumentException("Чет не так");
            }
        }

        public void add(GObject owner) {
            objects.add(owner);
            walkable = false;
        }

        public void removeValue(GObject value, boolean identity) {
            objects.removeValue(value, identity);
            walkable = objects.size == 0;
        }
    }
}
