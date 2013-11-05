package com.katspow.teetrys.client.core.world.teetrymino;

/**
 * One transformation is stored in a 5x5 matrix.
 * 
 */
public class Transformation {

    private int[][] matrix = new int[5][5];

    public int[][] getMatrix() {
        return matrix;
    }

    public Transformation(int[][] matrix) {
        this.matrix = matrix;
    }

}
