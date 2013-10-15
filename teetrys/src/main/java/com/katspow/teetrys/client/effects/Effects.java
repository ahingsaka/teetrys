package com.katspow.teetrys.client.effects;

import com.katspow.caatja.behavior.AlphaBehavior;
import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.BehaviorListener;
import com.katspow.caatja.behavior.ContainerBehavior;
import com.katspow.caatja.behavior.GenericBehavior;
import com.katspow.caatja.behavior.SetForTimeReturnValue;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.teetrys.client.core.Teetrymino;

public class Effects {
    
    public static double blinkAndDisappear(Actor actor, double time) {
        
        ContainerBehavior cb = new ContainerBehavior().setFrameTime(time, 1000);
        
        GenericBehavior changeColorBehavior = new GenericBehavior().setFrameTime(0, 500);
        changeColorBehavior.addListener(new BehaviorListener() {

            public void behaviorStarted(BaseBehavior behavior, double time, Actor actor) {
                
            }
            
            public void behaviorExpired(BaseBehavior behavior, double time, Actor actor) {
                actor.setDiscardable(true);
                actor.setExpired(true);
            }
            
            public void behaviorApplied(BaseBehavior behavior, double time, double normalizeTime, Actor actor,
                    SetForTimeReturnValue value) throws Exception {
                actor.setFillStyle(Teetrymino.getRandomColor());
            }
        });
        
        AlphaBehavior alphaBehavior = new AlphaBehavior().
                setValues(1, 0).
                setFrameTime(500, 500);
        
        cb.addBehavior(changeColorBehavior);
        cb.addBehavior(alphaBehavior);
        
        actor.addBehavior(cb);
        
        return time + 1000;
    }

}
