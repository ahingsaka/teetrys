package com.katspow.teetrys.client.core.world.teetrymino;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Random;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.ui.ShapeActor;
import com.katspow.caatja.foundation.ui.ShapeActor.Shape;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.GameController.Direction;
import com.katspow.teetrys.client.core.world.Collision;
import com.katspow.teetrys.client.core.world.teetrymino.Cube.Full;

/**
 * This represents the pieces of the game.<br>
 * There are 7 possible.
 *
 */
public class Teetrymino {
    
    private List<Cube> cubes;
    private Form form;
    private int transfoIndex;
    private String color;
    
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
    
    public Teetrymino(Form form, List<Cube> cubes, String color) {
        this.form = form;
        this.cubes = cubes;
        this.color = color;
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
    public static Cube createCube(double x, double y, String color, String strokeColor) {
    	return createCube(x, y, Constants.CUBE_SIDE, Constants.CUBE_SIDE, color, strokeColor);
    }
    
    public static Cube createCube(double x, double y, int size_x, int size_y, String color, String strokeColor) {
        
    	ShapeActor actor = new ShapeActor().
                setShape(Shape.RECTANGLE).
                setLocation(x, y).
                setSize(size_y, size_x).
                setFillStyle(color).
                setStrokeStyle(CaatjaColor.valueOf(strokeColor));
            
    	actor.disableDrag();
            
        Full cube = Full.valueOf(actor);
        return cube;
    }
    
    public static Teetrymino createNewTeetrymino(double x, double y) {
        int randomValue = Random.nextInt(Form.values().length);
        Form chosenForm = Form.values()[randomValue];
        return createTeetrymino(x, y, chosenForm, 0, null);
    }
    
    public static void setPosition(Teetrymino teetrymino, double x, double y) {
        Transformation baseTransformation = teetrymino.form.transformations.get(0);
        
        double i = x;
        double j = y;
        
        int[][] matrix = baseTransformation.getMatrix();
        List<Cube> cubes = teetrymino.getCubes();
        
        int c = 0;
        
        for (int line = 0; line < matrix.length; line ++) {
            for (int col = 0; col < matrix[line].length; col ++) {
                int val = matrix[line][col];
                
                if (val != 0) {
                    Actor actor = cubes.get(c).getValue();
                    //actor.setLocation(i, j);
                    actor.x = i;
                    actor.y = j;
                    c += 1;
                }
                
                i += Constants.CUBE_SIDE;
            }
         
            i = x;
            j += Constants.CUBE_SIDE;
        }
        
        
    }
    
    public static Teetrymino createTeetrymino(double x, double y, Form chosenForm, int transfoIndex, String color) {
        List<Cube> cubes = new ArrayList<Cube>();
        
        Transformation baseTransformation = chosenForm.getTransformations().get(transfoIndex);
        
        String randomColor = color;
        
        if (randomColor == null) {
            randomColor = getRandomColor();
        }
        
        double i = x;
        double j = y;
        
        int[][] matrix = baseTransformation.getMatrix();
        
        for (int line = 0; line < matrix.length; line ++) {
            for (int col = 0; col < matrix[line].length; col ++) {
                int val = matrix[line][col];
                
                if (val != 0) {
                    Cube cube = Teetrymino.createCube(i, j, randomColor, "#000000");
                    cubes.add(cube);
                }
                
                i += Constants.CUBE_SIDE;
            }
         
            i = x;
            j += Constants.CUBE_SIDE;
        }
        
        return new Teetrymino(chosenForm, cubes, randomColor);
    }
    
    
    public static String getRandomColor() {
        String[] letters = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        String color = "#";
        
        for (int i = 0; i < 6; i++) {
            int nextInt = Random.nextInt(16);
            color += letters[nextInt];
        }
        
        return color;
    }
    
//    public Transformation nextTransformation() {
//        List<Transformation> transformations = form.getTransformations();
//        transfoIndex += 1;
//        
//        if (transfoIndex > transformations.size() - 1) {
//            transfoIndex = 0;
//        }
//        
//        Transformation transformation = transformations.get(transfoIndex);
//        return transformation;
//    }
    
    // Move somewhere else ?, coz it needs gameworld
    public boolean rotate(double x, double y, Cube[][] world) {
        int nextIndex = nextIndex(form);
        Teetrymino newTeetrymino = createTeetrymino(x, y, form, nextIndex, color);
        boolean collision = Collision.checkCollisionsForAllCubes(newTeetrymino.getCubes(), color, Direction.UP, Constants.CUBE_SIDE, world);
        
        if (!collision) {
            
            for (int i = 0; i < newTeetrymino.getCubes().size(); i++) {
                Cube c = newTeetrymino.getCubes().get(i);
                Actor rotatedCube = c.getValue();
                Actor currentCube = this.cubes.get(i).getValue();
                currentCube.x = rotatedCube.x;
                currentCube.y = rotatedCube.y;
            }
            
            this.transfoIndex = nextIndex;
            
            System.out.println("rotate done");
        }
        
        return !collision;
        
    }
    
    private int nextIndex(Form form) {
        int nextIndex = transfoIndex + 1;
        
        if (nextIndex > form.getTransformations().size() - 1) {
            nextIndex = 0;
        }
        
        return nextIndex;
    }
    
    
    public void expire() {
    	for (Cube cube : getCubes()) {
    	    Actor actor = cube.getValue();
    		actor.setExpired(true);
    		actor.setDiscardable(true);
		}
	}

//    rotate_shape: (x, y, cube_list, check_collision, transformation_nb)->
//    transformations = @cube_shape_list[@current_shape_number]
//    keep = @current_transformation
//    shape = transformations[@current_transformation]
//    i = x
//    j = y
//    @current_max_y = y
//
//    for line in shape
//        for value in line
//            if (value != 0)
//                cube = cube_list[value - 1]
//                cube.setLocation(i, j)
//
//                if (@current_left_bound == 0) or (i < @current_left_bound)
//                    @current_left_bound = i
//
//                if (@current_right_bound == 0) or (i > @current_right_bound)
//                    @current_right_bound = i + Globals.CUBE_SIDE
//
//                if (j > @current_max_y)
//                    @current_max_y = j
//
//            i += Globals.CUBE_SIDE
//        i = x
//        j += Globals.CUBE_SIDE
//
//    # Rotation is final only if there is no collision !
//    collision_found = false
//    if (check_collision)
//
//        # Check if we have reached the bottom of gameboard
//        for cube in cube_list
//            if (cube.y >= Globals.GAME_HEIGHT)
//                collision_found = true
//                break
//
//        if (!collision_found)
//            collision_found = Collision.check_collisions_with_other_cubes(cube_list, @gameboard)
//
//    # If so rotate back 
//    if (collision_found)
//        log('collide' + keep)
//        this.previous_transformation()
//        this.rotate_shape(x, y, cube_list, false, @current_transformation)
//
//    else 
//        @shape_rotating = false
//        @current_max_y += Globals.CUBE_SIDE
    
    
    
    public List<Cube> getCubes() {
        return cubes;
    }
    
    public String getColor() {
		return color;
	}

}
