package com.katspow.teetrys.client.core.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.katspow.caatja.behavior.PathBehavior;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.pathutil.Path;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.GameController.Direction;
import com.katspow.teetrys.client.core.world.teetrymino.Cube;
import com.katspow.teetrys.client.core.world.teetrymino.Cube.Full;
import com.katspow.teetrys.client.core.world.teetrymino.Teetrymino;

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
            }
            
        }
        
        System.out.println(indexes.size());
        
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
        
        dumpGameWorld(gw);
        
    }

	public static void dumpGameWorld(GameWorld gw) {
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
    
    private static Comparator<List<Full>> comparator = new Comparator<List<Full>>() {
		@Override
		public int compare(List<Full> o1, List<Full> o2) {
			double o1y = returnMinY(o1, Constants.GAME_HEIGHT);
			double o2y = returnMinY(o2, Constants.GAME_HEIGHT);
			return Double.compare(o2y, o1y);
		}
	};
	
//	private static Comparator<Full> cubeComparator = new Comparator<Cube.Full>() {
//		@Override
//		public int compare(Full o1, Full o2) {
//			double o1y = o1.getValue().y;
//			double o2y = o2.getValue().y;
//			return Double.compare(o2y, o1y);
//		}
//	};
	
	private static double returnMinY(List<Full> o1, double o1y) {
		for (Full full : o1) {
			double y = full.getValue().y;
			if (y < o1y) {
				o1y = y;
			}
		}
		return o1y;
	}

    public void makeAllCubesFall(List<Integer> fullLinesIndexes, double newReturnTime) {
    	
    	// Deplacement de chaque ligne qui disparait
//    	int substractValue = 0;
//    	for (int i = 0; i < fullLinesIndexes.size(); i++) {
//    		Integer index = fullLinesIndexes.get(i);
//    		makeLineFall(newReturnTime, index, substractValue);
//    		substractValue += 1;
//    	}
    	
    	
    	// Gravity
    	List<Full> alreadyVisited = new ArrayList<Cube.Full>();
    	List<List<Full>> teetryminosSaves = new ArrayList<List<Full>>();
    	
        for (int i = getGameboardLinesNb() - 2; i > 0; i--) {
//            if (!fullLinesIndexes.contains(i)) {
              processLine(i, gameboard[i], alreadyVisited, teetryminosSaves, newReturnTime);
//            }
        }
        
        // Trier les teetryminos en fonction de leur hauteur (plus bas d abord)
		Collections.sort(teetryminosSaves, comparator);
        
        for (List<Full> teetryminoSave : teetryminosSaves) {
        	newGravityFall(teetryminoSave, newReturnTime);
		}
        
    }
    
	private double makeLineFall(double returnTime, int index, int substractValue) {
		double newReturnTime = returnTime + 500;
		
		for (int i = index + substractValue; i > 0; i--) {
			for (int j = 1; j < getGameboardColumnsNb() - 1; j++) {
				Cube cube = gameboard[i][j];
				if (cube.getParent() != null) {
					Full f = (Full) cube;
					Actor a = f.getValue();

					// FIXME 
					a.addBehavior(new PathBehavior().setFrameTime(
							returnTime, 300).setPath(
							new Path().setLinear(a.x, a.y, a.x, a.y
									+ Constants.CUBE_SIDE)));
					
					gameboard[i + 1][j] = cube; 
					gameboard[i][j] = Cube.Fixed.EMPTY;
				}
			}
		}

		return newReturnTime;
	}

	private void newGravityFall(List<Full> fullCubesFound, double newReturnTime) {
		int nbLines = 0;

		if (!fullCubesFound.isEmpty()) {

			boolean collisionFound = false;
			int y = 0;
			String color = fullCubesFound.get(0).getParent().getColor();
			;

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

				collisionFound = Collision.checkCollisionsForAllCubes(
						clonedCubes, color, Direction.DOWN,
						Constants.CUBE_SIDE, gameboard);

				if (!collisionFound) {
					nbLines += 1;
					y = Constants.CUBE_SIDE;
				}

			}

			if (y > 0) {
				for (int i = 0; i < clonedCubes.size(); i++) {
					Actor clonedCube = clonedCubes.get(i);
					Actor a = fullCubesFound.get(i).getValue();

					a.addBehavior(new PathBehavior().setFrameTime(
							newReturnTime, 300).setPath(
							new Path().setLinear(a.x, a.y, a.x, clonedCube.y)));
					// a.moveTo(clonedCube.x, clonedCube.y, 500, newReturnTime +
					// 100, Interpolator.createLinearInterpolator(false,
					// false));

					// Change gameboard
					int cubeAbscisse = (int) (clonedCube.x / Constants.CUBE_SIDE);
					int cubeOrdonnee = (int) (clonedCube.y / Constants.CUBE_SIDE);
					
					 Cube cube = gameboard[cubeOrdonnee][cubeAbscisse];
					 gameboard[cubeOrdonnee + nbLines][cubeAbscisse] = cube;
					 gameboard[cubeOrdonnee][cubeAbscisse] = Cube.Fixed.EMPTY;
				}
			}
		}
		
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
    
    private void processNew(int lineNumber, int index, Teetrymino previousParent, List<Full> alreadyVisited, List<List<Full>> teetryminosSaves, List<Full> fullCubes, double newReturnTime) {
    	
    	System.out.println("Line number " + lineNumber);
        
        Cube[] line = gameboard[lineNumber];
        
        while (index < line.length - 1) {
            Cube cube = line[index];
            List<Full> teetryminos = new ArrayList<Full>();
			processCube(lineNumber, index, cube.getParent(), alreadyVisited, teetryminos);
			
			if (teetryminos.size() > 0) {
//				Collections.sort(teetryminos, cubeComparator );
//				System.out.println("Found " + teetryminos.size() + " of color " + teetryminos.get(0).getParent().getColor());
				teetryminosSaves.add(teetryminos);
			}
			
			//gravityFall(lineNumber, teetryminos, newReturnTime);
			
			index++;
        }
        
    }

    
    private void searchAdjacentCubes(Cube cube, int row, int col, List<Full> alreadyVisited, List<Full> teetryminos) {
    	Teetrymino parent = cube.getParent();
		if (parent != null) {
    		processCube(row, col - 1, parent, alreadyVisited, teetryminos);
    		processCube(row, col + 1, parent, alreadyVisited, teetryminos);
    		processCube(row - 1, col, parent, alreadyVisited, teetryminos);
    	}
	}
    
    private void processCube(int row, int col, Teetrymino parentToCompare, List<Full> alreadyVisited, List<Full> teetryminos) {
    	
    	Cube cube = getGameboard()[row][col];
    	
    	Teetrymino parent = cube.getParent();
    	if (parent != null) {
	    	if (parent == parentToCompare && !teetryminos.contains(cube) && !alreadyVisited.contains(cube)) {
	    		teetryminos.add((Full)cube);
	    		alreadyVisited.add((Full)cube);
	    		searchAdjacentCubes(cube, row, col, alreadyVisited, teetryminos);
	    	}
    	}
    	
    }
    
    

	private void processLine(int lineNumber, Cube[] lineOfCubes, List<Full> alreadyVisited, List<List<Full>> teetryminosSaves, double newReturnTime) {
//       process(lineNumber, 1, null, new ArrayList<Cube.Full>(), newReturnTime);
       processNew(lineNumber, 1, null, alreadyVisited, teetryminosSaves,new ArrayList<Full>(), newReturnTime);
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
            
            boolean collisionFound = false;
            int y = 0;
            String color = fullCubesFound.get(0).getParent().getColor();;

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
                
                collisionFound = Collision.checkCollisionsForAllCubes(clonedCubes, color, Direction.DOWN, Constants.CUBE_SIDE,
                        gameboard);
                
                if (!collisionFound) {
                    nbLines += 1;
                    y = Constants.CUBE_SIDE;
                }

            }
            
            if (y > 0) {
                for (int i = 0; i < clonedCubes.size(); i++) {
                    Actor clonedCube = clonedCubes.get(i);
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
