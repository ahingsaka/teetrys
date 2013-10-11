package com.katspow.teetrys.client.core;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.teetrys.client.Constants;

public class GameWorld {

    private static final String WALL_COLOR = "black";
    private int[][] gameboard;
    
    public GameWorld() {
        init();
    }

    // Initialize the array that will contain cubes.
    // There is a line surrounding the gameboard.
    // This will be for collisions.
    public void init() {

        gameboard = new int[getGameboardLinesNb()][getGameboardColumnsNb()];

        for (int i = 0; i < getGameboardLinesNb(); i++) {

            // Upper and lower lines contain only collision blocks
            if (i == 0 || i == getGameboardLinesNb() - 1) {
                for (int j = 0; j < getGameboardColumnsNb(); j++) {
                    gameboard[i][j] = Constants.COLLISION;
                }

            } else {
                gameboard[i][0] = Constants.COLLISION;
                gameboard[i][getGameboardColumnsNb() - 1] = Constants.COLLISION;
            }
        }

    }

    private int getGameboardLinesNb() {
        return (Constants.GAME_HEIGHT / Constants.CUBE_SIDE) + 2;
    }

    private int getGameboardColumnsNb() {
        return ((Constants.GAME_WIDTH - Constants.LEFT_SPACE - Constants.RIGHT_SPACE) / Constants.CUBE_SIDE) + 2;
    }
    
    public int[][] getGameboard() {
        return gameboard;
    }
    
    /**
     * Returns a list of actors that represent the walls cubes
     * @return
     */
    public List<Actor> createWalls() {
        
        int x = 0;
        int y = Constants.CUBE_SIDE;
        List<Actor> walls = new ArrayList<Actor>();
        
        // Begin at 1, we do NOT represent the TOP line of walls (2 cubes are added later in the code)
        for (int i = 1; i < getGameboardLinesNb(); i++) {
            int[] line = gameboard[i];
            
            for (int j = 0; j < line.length; j++) {
                int value = line[j];
                
                if (value == Constants.COLLISION) {
                    Actor cube = Teetrymino.createCube(x, y, WALL_COLOR, WALL_COLOR);
                    walls.add(cube);
                }
                
                x += Constants.CUBE_SIDE;
                
            }
            
            y += Constants.CUBE_SIDE;
            x = 0;
        }
        
        // add 2 cubes and icons on upper left and right
        Actor upperLeftCube = Teetrymino.createCube(0, 0, WALL_COLOR, WALL_COLOR);
        Actor upperRightCube = Teetrymino.createCube(Constants.GAME_WIDTH - Constants.CUBE_SIDE, 0, WALL_COLOR, WALL_COLOR);

        walls.add(upperLeftCube);
        walls.add(upperRightCube);
        
        return walls;
        
    }
    
    public static void main(String[] args) {
        GameWorld gw = new GameWorld();
        gw.init();
        
        int[][] gameboard = gw.getGameboard();
        
        for (int i = 0; i < gw.getGameboardLinesNb(); i++) {
            for (int j = 0; j < gw.getGameboardColumnsNb(); j++) {
                System.out.print(gameboard[i][j]);
            }
            
            System.out.println("\n");
        }
        
        
    }

}
