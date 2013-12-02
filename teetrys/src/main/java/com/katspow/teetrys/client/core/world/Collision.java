package com.katspow.teetrys.client.core.world;

import java.util.List;

import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.GameController.Direction;
import com.katspow.teetrys.client.core.world.teetrymino.Cube;
import com.katspow.teetrys.client.core.world.teetrymino.Cube.Full;

public class Collision {
    
    private static boolean checkCollisionsForOneCube(Actor cube, String color, Direction dir, int increment, Cube[][] gameboard, List<Actor> currentTeetrymino) {
        boolean collisionFound = false;
        
        int case_courante_x = (int) (cube.x / Constants.CUBE_SIDE);
        int case_courante_y = (int) (cube.y / Constants.CUBE_SIDE);
        
        switch (dir) {
        
        case DOWN:
            
            case_courante_y = (int) (cube.y / Constants.CUBE_SIDE);
            int calcul_reste_y = (int) ((cube.y + Constants.CUBE_SIDE + increment) % Constants.CUBE_SIDE);
            int future_case_courante_y = (int) ((cube.y + Constants.CUBE_SIDE + increment) / Constants.CUBE_SIDE);
            
            if (calcul_reste_y > 0) {
              future_case_courante_y += 1;
            }
            
            Cube cubeDownside = gameboard[future_case_courante_y][case_courante_x];
            
			if (cubeDownside != Cube.Fixed.EMPTY) {
				
				if (cubeDownside.getParent() != null) {
					Full f = (Full) cubeDownside;
//					Actor value = f.getValue();

					if (currentTeetrymino.contains(f.getValue())) {
					    return false;
					}
					
//					double x = value.x;
//					double y = value.y;
//
//					for (Actor actor : currentTeetrymino) {
//						if (actor.x == x && actor.y == y) {
//							return false;
//						}
//					}

				}
				
				collisionFound = true;
			}
            
            break;
            
        case LEFT:
            int future_case_courante_x = case_courante_x - 1;
            Cube cubeOnLeft = gameboard[case_courante_y + 1][future_case_courante_x];
			if (cubeOnLeft != Cube.Fixed.EMPTY) {
				
				if (cubeOnLeft.getParent() != null) {
					Full f = (Full) cubeOnLeft;
					if (currentTeetrymino.contains(f.getValue())) {
						return false;
					}
				}
				
					collisionFound = true;
            }
            
            break;
                                
        case RIGHT:
            future_case_courante_x = case_courante_x + 1;
            Cube cubeOnRight = gameboard[case_courante_y + 1][future_case_courante_x];
			if (cubeOnRight != Cube.Fixed.EMPTY) {
				
				if (cubeOnRight.getParent() != null) {
					Full f = (Full) cubeOnRight;
					if (currentTeetrymino.contains(f.getValue())) {
						return false;
					}
				}
				
					collisionFound = true;
            }
            
            break;
        }
        
        return collisionFound;
    }

    public static boolean checkCollisionsForAllCubes(List<Actor> currentTeetrymino, String color, Direction dir, int cubeSide,
            Cube[][] gameboard) {
        
        boolean collisionFound = false;
        
        for (Actor cube : currentTeetrymino) {
            if (checkCollisionsForOneCube(cube, color, dir, cubeSide, gameboard, currentTeetrymino)) {
                collisionFound = true;
                break;
            }
        }
        
        return collisionFound;
    }

}
