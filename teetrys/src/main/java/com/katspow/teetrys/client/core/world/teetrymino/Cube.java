package com.katspow.teetrys.client.core.world.teetrymino;

import com.katspow.caatja.foundation.actor.Actor;

public interface Cube {
    
    public Teetrymino getParent();
    
    public enum Fixed implements Cube {
        EMPTY {
            public Teetrymino getParent() {
                return null;
            }

			@Override
			public String toString() {
				return "0";
			}
        }, 
        
        BRICK {
            public Teetrymino getParent() {
                return null;
            }

			@Override
			public String toString() {
				return "B";
			}
        }
    }
    
    public class Full implements Cube {
       
        private Actor value;
        
        private Teetrymino parent;
        
        private Full(Actor value, Teetrymino parent) {
            this.value = value;
            this.parent = parent;
        }
        
        public Actor getValue() {
            return value;
        }
        
        public void setValue(Actor value) {
            this.value = value;
        }
        
        public static Full valueOf(Actor value, Teetrymino parent) {
            return new Full(value, parent);
        }
        
        public void setParent(Teetrymino parent) {
            this.parent = parent;
        }
        
        public Teetrymino getParent() {
            return parent;
        }

		@Override
		public String toString() {
			return parent.getColor();
		}
        
        
        
    }

}
