package uj.java.pwj2019.kindergarten;

import java.io.IOException;

class Fork {
    private boolean isInUsage=false;

    synchronized boolean use(){
        if(!isInUsage) {
            isInUsage = true;
            return true;
        }
        return false;
    }

    synchronized void free() {
        /*if(!isInUsage)
            new IllegalArgumentException("Widelec nie jest używany");*/
        isInUsage = false;
    }
}
