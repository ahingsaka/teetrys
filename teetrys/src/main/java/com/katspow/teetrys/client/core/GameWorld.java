package com.katspow.teetrys.client.core;

import com.katspow.teetrys.client.Constants;

public class GameWorld {

    private int[][] gameboard;

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
