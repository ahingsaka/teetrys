package com.katspow.teetrys.client.core;

public class Score {
    
    private static int level;
    private static int lines;
    private static int score;
    private static int oldScore;
    private static int linesInCurrentLevel;

    public static void init() {
        level = 19;
        lines = 0;
        score = 0;
        oldScore = 0;
        
        linesInCurrentLevel = 9;
    }
    
    public static void addLines(int nbLines) {
        oldScore = score;
        lines += nbLines;
        linesInCurrentLevel += nbLines;

        switch (nbLines) {
        case 1:
            score += 100;
            break;

        case 2:
            score += 300;
            break;

        case 3:
            score += 600;
            break;

        case 4:
            score += 1000;
            break;
        }

    }
    
    public static boolean checkForNextLevel() {
        if (linesInCurrentLevel >= 10) {
            level += 1;
            linesInCurrentLevel -= 10;
            return true;
            
        } else {
            return false;
        }
    }
    
    public static int getLevel() {
        return level;
    }
    
    public static int getLines() {
        return lines;
    }

    public static int getScore() {
        return score;
    }
    
    
//    init: ->
//@level = 1
//@lines = 0
//@score = 0
//@old_score = 0
//@lines_in_current_level = 0
//
//# FIXME statut must = 0
//@statut = 3
//
//increaseStatut: ->
//@statut += 1
//



}
