package com.katspow.teetrys.client.effects;

import java.util.List;

import com.katspow.caatja.behavior.AlphaBehavior;
import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.BehaviorListener;
import com.katspow.caatja.behavior.ContainerBehavior;
import com.katspow.caatja.behavior.GenericBehavior;
import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.behavior.PathBehavior;
import com.katspow.caatja.behavior.ScaleBehavior;
import com.katspow.caatja.behavior.SetForTimeReturnValue;
import com.katspow.caatja.behavior.listener.BehaviorAppliedListener;
import com.katspow.caatja.behavior.listener.BehaviorExpiredListener;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.pathutil.Path;
import com.katspow.teetrys.client.Constants;
import com.katspow.teetrys.client.core.Cube;
import com.katspow.teetrys.client.core.Cube.Full;
import com.katspow.teetrys.client.core.Teetrymino;

public class Effects {
    
    public static double blinkAndDisappear(Actor actor, double time) {
        
        ContainerBehavior cb = new ContainerBehavior().setFrameTime(time, 1000);
        
        GenericBehavior changeColorBehavior = new GenericBehavior().setFrameTime(0, 500);
        
        changeColorBehavior.addListener(BehaviorListener.valueOfExpiredAndApplied(
                
           new BehaviorExpiredListener() {
            public void call(BaseBehavior behavior, double time, Actor actor) {
                actor.setDiscardable(true);
                actor.setExpired(true);
                
            }
            
        }, new BehaviorAppliedListener() {
            public void call(BaseBehavior behavior, double time, double normalizeTime, Actor actor, SetForTimeReturnValue value)
                    throws Exception {
                actor.setFillStyle(Teetrymino.getRandomColor());
            }
        }));
        
        AlphaBehavior alphaBehavior = new AlphaBehavior().
                setValues(1, 0).
                setFrameTime(500, 500);
        
        cb.addBehavior(changeColorBehavior);
        cb.addBehavior(alphaBehavior);
        
        actor.addBehavior(cb);
        
        return time + 1100;
    }

    public static double fall(List<Integer> linesToMoveIndexes, final Cube[][] gameboard, double time, int size) {
        
        for (Integer lineIndex : linesToMoveIndexes) {
            
            System.out.println(lineIndex);
            
            final Cube[] line = gameboard[lineIndex];
            
            for (int i = 1; i < line.length - 1; i++) {
                
                if (line[i] != Cube.Fixed.EMPTY) {
                    
                    Full cube = (Full) line[i];
                    Actor actor = cube.getValue();
                    
                    int indexLineToCheck = i + size;
                    int futureY = Constants.CUBE_SIDE + size;
                    
                    Path p = new Path();
                    p.setLinear(actor.x, actor.y, actor.x, actor.y + futureY);
                    
                    PathBehavior translationBehavior = new PathBehavior().setFrameTime(time, 300);
                    translationBehavior.setPath(p);
                    actor.addBehavior(translationBehavior);
                    
                    actor.moveTo(actor.x, actor.y + futureY, 300, time, Interpolator.createLinearInterpolator(false, false), 
                            BehaviorListener.valueOfExpired(new BehaviorExpiredListener() {
                        public void call(BaseBehavior behavior, double time, Actor actor) {
                            
                        }
                    }));
                    
                    Cube[] futureLine = gameboard[indexLineToCheck];
                    futureLine[i] = line[i];
                    line[i] = Cube.Fixed.EMPTY;
                    
                    
                    System.out.println("fall effect added ");
                    
                }
            }
            
        }
        
        return time + 300;
    }

    public static double fall(List<Actor> cubes, int futureY, double time) {
        for (Actor actor : cubes) {
            Path p = new Path();
            p.setLinear(actor.x, actor.y, actor.x, actor.y + futureY);
            
            PathBehavior translationBehavior = new PathBehavior().setFrameTime(time, 300);
            translationBehavior.setPath(p);
            
            actor.addBehavior(translationBehavior);
            
        }
        
        return time + 300;
    }
    
    public static void scale(Actor actor, double time) {
        ScaleBehavior scaleBehavior = new ScaleBehavior().
                setFrameTime(time, 400).
                setValues(0, 1.5, 0, 1.5, 0, 0);
        
        actor.addBehavior(scaleBehavior);
    }

    public static void scaleOutAndDisappear(Actor cube, double time) {
        
        ScaleBehavior sb = new ScaleBehavior().
                setFrameTime(time, 400).
                setValues(1.5, 0, 1.5, 0, 0, 0);
        
        sb.addListener(BehaviorListener.valueOfExpired(new BehaviorExpiredListener() {
            public void call(BaseBehavior behavior, double time, Actor actor) {
                actor.setAlpha(1);
            }
        }));

        cube.addBehavior(sb);
        
    }
    
}
