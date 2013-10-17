package com.katspow.teetrys.client.core;

import java.util.ArrayList;
import java.util.List;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.Cube.Full;
import com.katspow.teetrys.client.core.GameController.Direction;

public class GameWorld {

    private static final String WALL_COLOR = "black";
    private Cube[][] gameboard;
    
    public GameWorld() {
        init();
    }

    // Initialize the array that will contain cubes.
    // There is a line surrounding the gameboard.
    // This will be for collisions.
    public void init() {

        gameboard = new Cube[getGameboardLinesNb()][getGameboardColumnsNb()];

        for (int i = 0; i < getGameboardLinesNb(); i++) {
            
            // Init with default values
            for (int j = 0; j < getGameboardColumnsNb(); j++) {
                gameboard[i][j] = Cube.Fixed.EMPTY;
            }

            // Upper and lower lines contain only collision blocks
            if (i == 0 || i == getGameboardLinesNb() - 1) {
                for (int j = 0; j < getGameboardColumnsNb(); j++) {
                    gameboard[i][j] = Cube.Fixed.BRICK;
                }

            } else {
                gameboard[i][0] = Cube.Fixed.BRICK;
                gameboard[i][getGameboardColumnsNb() - 1] = Cube.Fixed.BRICK;
            }
        }

    }

    public int getGameboardLinesNb() {
        return (Constants.GAME_HEIGHT / Constants.CUBE_SIDE) + 2;
    }

    public int getGameboardColumnsNb() {
        return ((Constants.GAME_WIDTH - Constants.LEFT_SPACE - Constants.RIGHT_SPACE) / Constants.CUBE_SIDE) + 2;
    }
    
    public Cube[][] getGameboard() {
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
            Cube[] line = gameboard[i];
            
            for (int j = 0; j < line.length; j++) {
                Cube value = line[j];
                
                if (value == Cube.Fixed.BRICK) {
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
    
    
    /**
     * Returns a list of full line indexes and the last index is the line above
     * the last full line.
     * 
     * @return
     */
    public List<Integer> findNumberOfFullLines() {
        
        List<Integer> indexes = new ArrayList<Integer>();
        int nbLines = gameboard.length - 2;
        int indexToCheckUpperLines = nbLines;
        
        for (int i = nbLines; i > 1; i--) {
            Cube[] line = gameboard[i];
            
            boolean isFullLine = true;
            
            for (int j = 1; j < line.length - 1; j++) {
                Cube cube = line[j];
                
                if (cube == Cube.Fixed.EMPTY) {
                    isFullLine = false;
                    break;
                }
            }
            
            if (isFullLine) {
                indexes.add(Integer.valueOf(i));
                indexToCheckUpperLines = i - 1;
            }
            
        }
        
        indexes.add(indexToCheckUpperLines);
        
        return indexes;
        
    }
    
    public int findEmptyLineIndexFrom(int indexToCheckUpperLines) {
        int index_found = -1;
        
        for (int i = indexToCheckUpperLines; i > 0; i--) {
            Cube[] line = gameboard[i];
            boolean isEmptyLine = true;
            
            for (int j = 1; j < line.length - 1; j++) {
                Cube value = line[j];
                
                if (value != Cube.Fixed.EMPTY) {
                    isEmptyLine = false;
                    break;
                }
            }
            
            if (isEmptyLine) {
                index_found = i;
                break;
            }
        }
        
        return index_found;
    }
    
//    find_empty_line_index_from: (index_to_check_upper_lines) ->
//    log('index to check from ' + index_to_check_upper_lines)
//    index_found = -1
//
//    for i in [index_to_check_upper_lines..1]
//        line = @gameboard[i]
//        for j in [1..line.length-2]
//            value = line[j]
//            is_empty_line = true
//            if (value != undefined)
//                is_empty_line = false
//                break
//
//        if (is_empty_line)
//            index_found = i
//            break
//    log('index of empty line found at ' + index_found)
//    
//    return index_found
    
    
    public static void main(String[] args) {
        GameWorld gw = new GameWorld();
        gw.init();
        
        Cube[][] gameboard = gw.getGameboard();
        
        for (int i = 0; i < gw.getGameboardLinesNb(); i++) {
            for (int j = 0; j < gw.getGameboardColumnsNb(); j++) {
                System.out.print(gameboard[i][j]);
            }
            
            System.out.println("\n");
        }
        
        
    }

    public void removeCubes(List<Integer> fullLinesIndexes) {
        for (Integer index : fullLinesIndexes) {
            Cube[] line = gameboard[index];
            for (int i = 1; i < line.length - 1; i++) {
                line[i] = Cube.Fixed.EMPTY; 
            }
        }
    }

    public List<Actor> makeAllCubesFall(List<Integer> fullLinesIndexes) {
        
        List<Actor> cubes = new ArrayList<Actor>();
        
        for (int i = getGameboardLinesNb() - 2; i > 0; i--) {
            if (!fullLinesIndexes.contains(i)) {
                System.out.println("Line " + i + " process : ");
                processLine(i, gameboard[i]);
                System.out.println("Line " + i + " END OF PROCESS ");
            }
        }
        
        return cubes;
    }
    
    /**
     * 
     * condition de sortie: la valeur de l'index est égale à la fin de la ligne
     * 
     * 
     * - prendre le cube à l'index courant
     * 
     * si le cube est vide, (je fais tomber la liste des cubes precedents) je
     * passe au suivant, je vide la liste
     * 
     * si le cube n'est pas vide,
     * 
     * si il est égal au précédent, j'ajoute ce cube à la liste des cubes prec
     * si il n'est pas égal, je fais tomber la liste des préc, j'ajoute ce cube
     * à la liste des cubes prec je passe à l'index suivant.
     * 
     * @param lineNumber
     * @param index
     * @param previousParent
     * @param fullCubes
     */
    private void process(int lineNumber, int index, Teetrymino previousParent, List<Full> fullCubes) {
        
        Cube[] line = gameboard[lineNumber];
        
        if (index < line.length - 1) {
        
            System.out.println("index " + index);
            Cube cube = line[index];
            Teetrymino parent = cube.getParent();
            
            if (parent == null) {
                gravityFall(lineNumber, fullCubes);
                fullCubes.clear();
                process(lineNumber, index + 1, null, fullCubes);
            } else {
                
                if (previousParent == parent) {
                    fullCubes.add((Full) cube);
                    process(lineNumber, index + 1, parent, fullCubes);
                    
                } else {
                    gravityFall(lineNumber, fullCubes);
                    fullCubes.clear();
                    fullCubes.add((Full) cube);
                    process(lineNumber, index + 1, parent, fullCubes);
                }
                
            }
        
        }
        
    }

    
    private void processLine(int lineNumber, Cube[] lineOfCubes) {
        
       process(lineNumber, 1, null, new ArrayList<Cube.Full>());
        
//        System.out.println("process line " + lineNumber);
//
//        int i = 1;
//        
//        List<Full> fullCubesFound = new ArrayList<Full>();
//        
//        while (i < lineOfCubes.length - 1) {
//            
//            System.out.println("process index " + i);
//
//            if (i == lineOfCubes.length - 1) {
//                // Move stored blocks
//                int nbLinesToFall = gravityFall(fullCubesFound);
//                
//                if (nbLinesToFall > 0) {
//                    updateFallingCubes(lineNumber, nbLinesToFall);
//                }
//                
//                fullCubesFound.clear();
//                
//                return;
//            } 
//
//            Cube cube = lineOfCubes[i];
//            Teetrymino parentOfCurrentCube = cube.getParent();
//
//            if (parentOfCurrentCube != null) {
//
//                fullCubesFound.add((Full) cube);
//
//                Cube nextCube = lineOfCubes[i + 1];
//
//                if (nextCube.getParent() != null) {
//                    
//                    Teetrymino parentOfNextCube = nextCube.getParent();
//
//                    if (parentOfCurrentCube == parentOfNextCube) {
//                        fullCubesFound.add((Full) nextCube);
//
//                    } else {
//                        // Move stored blocks
//                        int nbLinesToFall = gravityFall(fullCubesFound);
//                        if (nbLinesToFall > 0) {
//                            updateFallingCubes(lineNumber, nbLinesToFall);
//                        }
//                        fullCubesFound.clear();
//                    }
//                    
//                } else {
//                    // Move stored blocks
//                    int nbLinesToFall = gravityFall(fullCubesFound);
//                    if (nbLinesToFall > 0) {
//                        updateFallingCubes(lineNumber, nbLinesToFall);
//                    }
//                    fullCubesFound.clear();
//                }
//                
//            } else {
//                
//                int nbLinesToFall = gravityFall(fullCubesFound);
//                if (nbLinesToFall > 0) {
//                    updateFallingCubes(lineNumber, nbLinesToFall);
//                }
//                fullCubesFound.clear();
//                
//            }
//            
//            i += 1;
//            
//        }
    }

    private void updateFallingCubes(int lineNumber, int nbLinesToFall) {
        Cube[] cubes = gameboard[lineNumber];
        
        for (int i = 1; i < cubes.length - 1; i++) {
            if (cubes[i] != Cube.Fixed.EMPTY) {
                Full full = (Full) cubes[i];
                full.getValue().y += nbLinesToFall * Constants.CUBE_SIDE;
            }
        }
        
    }

    /**
     * Return of how many lines we fall.
     * 
     * FIXME Update gameboard
     * @param lineNumber 
     * 
     * @param fullCubesFound
     * @return
     */
    private int gravityFall(int lineNumber, List<Full> fullCubesFound) {
        
        int nbLines = 0;
        
        if (!fullCubesFound.isEmpty()) {
            
            System.out.println("nb of cubes to gravity fall " + fullCubesFound.size());

            boolean collisionFound = false;
            int y = 0;

            List<Actor> clonedCubes = new ArrayList<Actor>();
            
            for (Full cube : fullCubesFound) {
                Actor clonedCube = new Actor();
                clonedCube.x = cube.getValue().x;
                clonedCube.y = cube.getValue().y;
                clonedCubes.add(clonedCube);
            }

            while (!collisionFound) {
                
                for (Actor actor : clonedCubes) {
                    actor.y += y;
                }
                
                collisionFound = Collision.checkCollisionsForAllCubes(clonedCubes, Direction.DOWN, Constants.CUBE_SIDE,
                        gameboard);
                
                if (!collisionFound) {
                    nbLines += 1;
                    y += Constants.CUBE_SIDE;
                }

            }
            
            if (y > 0) {
                for (int i = 0; i < clonedCubes.size(); i++) {
                    Actor clonedCube = clonedCubes.get(i);
                    fullCubesFound.get(i).getValue().y = clonedCube.y;

                    // Change gameboard
                    int cubeAbscisse = (int) (clonedCube.x / Constants.CUBE_SIDE);
                    Cube cube = gameboard[lineNumber][cubeAbscisse];
                    gameboard[lineNumber + nbLines][cubeAbscisse] = cube;  
                    
                }
            }
            
        }
        
        System.out.println("nbLines to fall " + nbLines);
        
        return nbLines;

    }

}
