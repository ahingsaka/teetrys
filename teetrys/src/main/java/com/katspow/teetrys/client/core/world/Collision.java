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
					Actor value = f.getValue();

					double x = value.x;
					double y = value.y;

					for (Actor actor : currentTeetrymino) {
						if (actor.x == x && actor.y == y) {
							return false;
						}
					}

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

//    # NOTE : for left and right, I assume only a CUBE movement translation
//    # Note that in line 257, 263, +1 is added to y, don't know exactly why
//    check_collisions_for_one_cube: (cube, direction, increment, gameboard) ->
//        collision_found = false
//
//        case_courante_x = Math.floor(cube.x / Globals.CUBE_SIDE)
//        case_courante_y = Math.floor(cube.y / Globals.CUBE_SIDE)
//        #log("case_courante_y " + case_courante_y)
//
//        if (direction == 'DOWN')
//            case_courante_y = Math.floor(cube.y / Globals.CUBE_SIDE) + 1
//            calcul_reste_y = (cube.y + Globals.CUBE_SIDE + increment) % Globals.CUBE_SIDE
//            future_case_courante_y = Math.floor((cube.y + Globals.CUBE_SIDE + increment) / Globals.CUBE_SIDE)
//
//            if (calcul_reste_y > 0)
//                future_case_courante_y += 1
//
//            if (gameboard[future_case_courante_y][case_courante_x] != undefined)
//                log("collision found at " + case_courante_y + " _ " + case_courante_x + "_ cube.x " + cube.y)
//                return true     
//
//        else if (direction == 'LEFT')
//            #log('case courante ' + case_courante_x)
//            future_case_courante_x = case_courante_x - 1
//            #log('future_case_courante_x ' + future_case_courante_x)
//            if (gameboard[case_courante_y + 1][future_case_courante_x] != undefined)
//                log("collision found at " + case_courante_y + " _ " + case_courante_x + "_ cube.x " + cube.y)
//                return true
//
//        else 
//            future_case_courante_x = case_courante_x + 1
//            if (gameboard[case_courante_y + 1][future_case_courante_x] != undefined)
//                log("collision found at " + case_courante_y + " _ " + case_courante_x + "_ cube.x " + cube.y)
//                return true
//
//        return collision_found
//
//    check_collisions_for_all_cubes: (cube_list, direction, increment, gameboard) ->
//        collision_found = false
//        for cube in cube_list
//            if (Collision.check_collisions_for_one_cube(cube, direction, increment, gameboard))
//                collision_found = true
//                break
//        return collision_found
    

}
