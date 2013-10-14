package com.katspow.teetrys.client;

import com.katspow.caatja.core.canvas.CaatjaColor;

public class Constants {
    
    public static final CaatjaColor BACKGROUND_COLOR = CaatjaColor.valueOf("#272822");
    
    // Value used in gameboard for collision
    public static final int COLLISION = 1;
    
    // GAME DIMENSIONS
    //////////////////
    
    // Window size
    public static final int GAME_HEIGHT = 800;
    public static final int GAME_WIDTH = 480;
    
    // Falling blocks are builded with cubes, this variable fixes the cube side size
    public static final int CUBE_SIDE = 40; 
    
    // Define the margins on left and right
    public static final int LEFT_SPACE = 40;
    public static final int RIGHT_SPACE = 40;

    // Represents the column number where falling blocks will appear. There are 10 columns in the game
    public static final int START_POINT_X = 3;
    public static final int START_POINT_Y = 0;
    

}
