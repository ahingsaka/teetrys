package com.katspow.teetrys.client.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.ui.ShapeActor;
import com.katspow.caatja.foundation.ui.ShapeActor.Shape;
import com.katspow.teetrys.client.Constants;

/**
 * This represents the pieces of the game.<br>
 * There are 7 possible.
 *
 */
public class Teetrymino {
    
    public enum Form {
        
        I(new ArrayList<Transformation>() {{
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {0,1,1,1,1},
                    {0,0,0,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {1,1,1,1,0},
                    {0,0,0,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
        }}),
        
        O(new ArrayList<Transformation>() {{
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {0,0,1,1,0},
                    {0,0,1,1,0},
                    {0,0,0,0,0}
            }));
        }}),
        
        T(new ArrayList<Transformation>() {{
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,1,0,0},
                    {0,1,1,1,0},
                    {0,0,0,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,1,0,0},
                    {0,1,1,0,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {0,1,1,1,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,1,0,0},
                    {0,0,1,1,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
        }}),
        
        J(new ArrayList<Transformation>() {{
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,1,1,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,1,0,0,0},
                    {0,1,1,1,0},
                    {0,0,0,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,1,1,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {0,1,1,1,0},
                    {0,0,0,1,0},
                    {0,0,0,0,0}
            }));
        }}),
        
        L(new ArrayList<Transformation>() {{
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,1,1,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,1,0},
                    {0,1,1,1,0},
                    {0,0,0,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,1,1,0,0},
                    {0,0,1,0,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {0,1,1,1,0},
                    {0,1,0,0,0},
                    {0,0,0,0,0}
            }));
        }}),
        
        Z(new ArrayList<Transformation>() {{
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {0,1,1,0,0},
                    {0,0,1,1,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,1,0},
                    {0,0,1,1,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
        }}),
        
        S(new ArrayList<Transformation>() {{
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,0,0,0,0},
                    {0,0,1,1,0},
                    {0,1,1,0,0},
                    {0,0,0,0,0}
            }));
            
            add(new Transformation(new int[][] {
                    {0,0,0,0,0},
                    {0,1,0,0,0},
                    {0,1,1,0,0},
                    {0,0,1,0,0},
                    {0,0,0,0,0}
            }));
        }});
        
        private List<Transformation> transformations;

        private Form(List<Transformation> transformations) {
            this.transformations = transformations;
        }
        
        public List<Transformation> getTransformations() {
            return transformations;
        }
    }
    
    /**
     * Returns the smallest shape possible for a teetrymino : a cube.
     * 
     * @param x
     * @param y
     * @param color
     * @param strokeColor
     * @return
     */
    public static Actor createCube(double x, double y, String color, String strokeColor) {
        
        ShapeActor cube = new ShapeActor().
            setShape(Shape.RECTANGLE).
            setLocation(x, y).
            setSize(Constants.CUBE_SIDE, Constants.CUBE_SIDE).
            setFillStyle(color).
            setStrokeStyle(CaatjaColor.valueOf(strokeColor));
        
        return cube;
    }
    
    public static void createNewTeetrymino(double x, double y) {
        int randomValue = new Random().nextInt(Form.values().length);
        Form chosenForm = Form.values()[randomValue];
        
        
        
    }
    
    public static CaatjaColor getRandomColor() {
        String[] letters = "0123456789ABCDEF".split("");
        String color = "#";
        
        for (int i = 0; i < 6; i++) {
            color += letters[new Random().nextInt(15)];
        }
        
        return CaatjaColor.valueOf(color);
    }
    
}
