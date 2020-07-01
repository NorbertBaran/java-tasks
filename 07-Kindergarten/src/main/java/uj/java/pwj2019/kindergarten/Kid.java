package uj.java.pwj2019.kindergarten;

public class Kid extends Child{
    private Fork leftFork;
    private Fork rightFork;
    public Kid(String name, int hungerSpeedMs, Fork leftFork, Fork rightFork) {
        super(name, hungerSpeedMs);
        this.leftFork=leftFork;
        this.rightFork=rightFork;
    }

    public void setRightFork(Fork rightFork) {
        this.rightFork = rightFork;
    }

    private void feedChildThread(){
        eat();
        leftFork.free();
        rightFork.free();
    }

    public void feedChild(){
        boolean result = false;
        while(!result) {
            if (leftFork.use()) {
                if (rightFork.use()) {
                    Thread feedingChild = new Thread(this::feedChildThread);
                    feedingChild.start();
                    result = true;
                } else {
                    leftFork.free();
                }
            }
        }
    }
}
