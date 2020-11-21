package com.mcdonald;

import java.awt.*;
import java.util.Random;

public class WorkerThread extends Thread {

    public static final int FIVE_SECONDS = 5000;
    public static final int MAX_Y = 500;
    public static final int MAX_X = 500;

    @Override
    public void run() {

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        Random random = new Random();
        while (true) {
            robot.mouseMove(random.nextInt(MAX_X), random.nextInt(MAX_Y));
            try {
                Thread.sleep(FIVE_SECONDS);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }
}
