package com.katspow.teetrys.client.effects;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.Scene.Ease;
import com.katspow.caatja.foundation.actor.Actor.Anchor;

public class EaseIn {
    
    public static void withBoomEffect(Director director, Scene scene) throws Exception {
        director.easeIn(director.getSceneIndex(scene), Ease.SCALE, 1000, false, Anchor.CENTER, new Interpolator().createElasticOutInterpolator(2.5, 0.4, false));
    }

}
