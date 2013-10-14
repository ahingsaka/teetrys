package com.katspow.teetrys.client.core;

import com.katspow.caatja.foundation.actor.Actor;

public interface Cube {
    
    public enum Fixed implements Cube {
        EMPTY, BRICK
    }
    
    public class Full implements Cube {
       
        private Actor value;
        
        public Actor getValue() {
            return value;
        }
        
        public void setValue(Actor value) {
            this.value = value;
        }
        
        public static Full valueOf(Actor value) {
            return new Full(value);
        }
        
        private Full(Actor value) {
            this.value = value;
        }
        
    }

}
