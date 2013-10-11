package com.katspow.teetrys.client.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
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
    
    public static List<Actor> createNewTeetrymino(double x, double y) {
        List<Actor> teetrymino = new ArrayList<Actor>();
        int randomValue = new Random().nextInt(Form.values().length);
        Form chosenForm = Form.values()[randomValue];
        
        Transformation baseTransformation = chosenForm.getTransformations().get(0);
        CaatjaColor randomColor = getRandomColor();
        
        double i = x;
        double j = y;
        
        int[][] matrix = baseTransformation.getMatrix();
        
        for (int line = 0; line < matrix.length; line ++) {
            for (int col = 0; col < matrix[line].length; col ++) {
                int val = matrix[line][col];
                
                if (val != 0) {
                    Teetrymino.createCube(i, j, color, "#000000");
                }
            }
        }
        
        
        // TODO
//                current_shape_number = shape_number
//
//                transformations = @cube_shape_list[shape_number]
//                shape = transformations[0]
//                current_transformation = 0
//
//                color = COLOR.get_random_color()
//
//                i = x
//                j = y
//
//                mouseEnterHandler = (mouseEvent) ->
//                    Globals.ON_ENTER_SHAPE = true
//
//                mouseClick = (mouseEvent) =>
//                    @shape_rotating = true
//                    this.next_transformation()
//                    this.rotate_shape(@x, @y, @cube_list, true, @current_transformation)
//
//                for line in shape
//                    for value in line
//                        if (value != 0)
//                            rectangle = Teetryminos.create_cube(i, j, color, '#000000')
//                            rectangle.mouseEnter = mouseEnterHandler
//                            rectangle.mouseClick = mouseClick
//
//                            if (j > @current_max_y)
//                                @current_max_y = j + Globals.CUBE_SIDE
//
//                            cube_list[value - 1] = rectangle
//
//                        i += Globals.CUBE_SIDE
//                    i = x
//                    j += Globals.CUBE_SIDE
//
//                return [cube_list, current_shape_number, current_transformation]
        
        
        return teetrymino;
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
