package com.katspow.teetrys.client.core.world;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.behavior.PathBehavior;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.pathutil.Path;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.GameController.Direction;
import com.katspow.teetrys.client.core.world.teetrymino.Cube;
import com.katspow.teetrys.client.core.world.teetrymino.Teetrymino;
import com.katspow.teetrys.client.core.world.teetrymino.Cube.Fixed;
import com.katspow.teetrys.client.core.world.teetrymino.Cube.Full;

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
    
    // Store the cubes in gameboard
    // Disable the mouse event
    public void storeCubes(List<Actor> cubes, Teetrymino teetryminoParent) {
        for (Actor cube : cubes) {
            
            // FIXME Disable touch/mouse events
            
            int cube_x = (int) (cube.x / Constants.CUBE_SIDE);
            int cube_y = ((int) cube.y / Constants.CUBE_SIDE) + 1;
            
            getGameboard()[cube_y][cube_x] = Cube.Full.valueOf(cube, teetryminoParent);
        }
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

    public List<Actor> makeAllCubesFall(List<Integer> fullLinesIndexes, double newReturnTime) {
        
        List<Actor> cubes = new ArrayList<Actor>();
        
        for (int i = getGameboardLinesNb() - 2; i > 0; i--) {
            if (!fullLinesIndexes.contains(i)) {
//                System.out.println("Line " + i + " process : ");
                processLine(i, gameboard[i], newReturnTime);
//                System.out.println("Line " + i + " END OF PROCESS ");
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
     * @param newReturnTime 
     */
    private void process(int lineNumber, int index, Teetrymino previousParent, List<Full> fullCubes, double newReturnTime) {
        
        Cube[] line = gameboard[lineNumber];
        
//        System.out.println("index " + index);
        if (index < line.length - 1) {
        
            Cube cube = line[index];
            Teetrymino parent = cube.getParent();
            
            if (parent == null) {
                gravityFall(lineNumber, fullCubes, newReturnTime);
                fullCubes.clear();
                process(lineNumber, index + 1, null, fullCubes, newReturnTime);
            } else {
                
                if (previousParent == parent) {
                    fullCubes.add((Full) cube);
                    process(lineNumber, index + 1, parent, fullCubes, newReturnTime);
                    
                } else {
                    gravityFall(lineNumber, fullCubes, newReturnTime);
                    fullCubes.clear();
                    fullCubes.add((Full) cube);
                    process(lineNumber, index + 1, parent, fullCubes, newReturnTime);
                }
                
            }
        
        } else {
            gravityFall(lineNumber, fullCubes, newReturnTime);
        }
        
    }

    
    private void processLine(int lineNumber, Cube[] lineOfCubes, double newReturnTime) {
       process(lineNumber, 1, null, new ArrayList<Cube.Full>(), newReturnTime);
    }

    /**
     * Return of how many lines we fall.
     * 
     * @param lineNumber 
     * 
     * @param fullCubesFound
     * @param newReturnTime 
     * @return
     */
    private int gravityFall(int lineNumber, List<Full> fullCubesFound, double newReturnTime) {
        
        int nbLines = 0;
        
        if (!fullCubesFound.isEmpty()) {
            
//            System.out.println("nb of cubes to gravity fall " + fullCubesFound.size());

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
                    y = Constants.CUBE_SIDE;
                }

            }
            
            if (y > 0) {
                for (int i = 0; i < clonedCubes.size(); i++) {
                    Actor clonedCube = clonedCubes.get(i);
                    
                    // Good line
//                    fullCubesFound.get(i).getValue().y = clonedCube.y;
                    
                    Actor a = fullCubesFound.get(i).getValue();
                    
                    a.addBehavior(new PathBehavior().setFrameTime(newReturnTime, 300).setPath(new Path().setLinear(a.x, a.y, a.x, clonedCube.y)));
//					a.moveTo(clonedCube.x, clonedCube.y, 500, newReturnTime + 100, Interpolator.createLinearInterpolator(false, false));

                    // Change gameboard
                    int cubeAbscisse = (int) (clonedCube.x / Constants.CUBE_SIDE);
                    Cube cube = gameboard[lineNumber][cubeAbscisse];
                    gameboard[lineNumber + nbLines][cubeAbscisse] = cube;  
                    gameboard[lineNumber][cubeAbscisse] = Cube.Fixed.EMPTY;
                }
            }
            
        }
        
//        System.out.println("nbLines to fall " + nbLines);
        
        return nbLines;

    }

}
