package com.katspow.teetrys.client.effects;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.Scene.Ease;
import com.katspow.caatja.foundation.actor.Actor.Anchor;

public class EaseInOut {
    
    public static void scenesFromUpToDown(Director director, Scene in, Scene out) throws Exception {
        director.easeInOut(director.getSceneIndex(in), Ease.TRANSLATE, Anchor.TOP,
                director.getSceneIndex(out), Ease.TRANSLATE, Anchor.BOTTOM, 500, false,
                new Interpolator().createLinearInterpolator(false, false),
                new Interpolator().createLinearInterpolator(false, false));
    }

}
