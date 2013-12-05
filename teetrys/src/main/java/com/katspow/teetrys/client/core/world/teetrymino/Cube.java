package com.katspow.teetrys.client.core.world.teetrymino;

import com.katspow.caatja.foundation.actor.Actor;

public interface Cube {

    public Teetrymino getParent();
    
    public Actor getValue();

    public enum Fixed implements Cube {
        EMPTY {
            public Teetrymino getParent() {
                return null;
            }

            @Override
            public String toString() {
                return "0";
            }

            @Override
            public Actor getValue() {
                return null;
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

            @Override
            public Actor getValue() {
                return null;
            }
            
        }
    }

    public class Full implements Cube {

        private Actor value;

        private Teetrymino parent;
        
        private Full(Actor value) {
            this.value = value;
        }

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
        
        public static Full valueOf(Actor value) {
            return new Full(value);
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

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Full other = (Full) obj;
            if (parent == null) {
                if (other.parent != null) {
                    return false;
                }
                
            } else {
                if (other.parent == null) {
                    return false;
                } else {
                    String color = parent.getColor();
                    String color2 = other.parent.getColor();
                    System.out.println("--");
                    System.out.println(color);
                    System.out.println(color2);
                    System.out.println("--");
                    boolean equals = color.equals(color2);
                    System.out.println(equals);
                    return equals;
                }
                
            }
            
            return true;
        }

    }

}
