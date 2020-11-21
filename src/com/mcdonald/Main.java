package com.mcdonald;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.Random;
import javax.swing.*;

class MouseMover {
    public static final int TEN_SECONDS = 10000;
    public static final int MAX_Y = 400;
    public static final int MAX_X = 400;
    private static boolean isOn = true;

    public static void main(String[] args) {

        final Logger logger = Logger.getLogger(MouseMover.class.getName());

        /* Declare a scanner to get user input... */
        final Scanner scanner = new Scanner(System.in);

        /* ...and make sure it gets closed under all circumstances */
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Closing Scanner.");
                scanner.close();
            }
        });

        /* Our runnable which does the actual work */
        Runnable runner = new Runnable() {

            @Override
            public void run() {

                /* We want to run at least once */
                do {

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        logger.info("Got interrupted first run");

                        /* Edit: we need to return here, as we don't want
                         * to execute the payload code in case
                         * we are interrupted. Replaces stopped variable
                         */
                        logger.info("Doing Stuff");
                        Robot robot = null;
                        try {
                            robot = new Robot();
                        } catch (AWTException err) {
                            err.printStackTrace();
                        }
                        Random random = new Random();

                        while (true) {
                            robot.mouseMove(random.nextInt(MAX_X), random.nextInt(MAX_Y));
                            try {
                                Thread.sleep(TEN_SECONDS);
                            } catch (InterruptedException err) {
                                logger.info("Got interrupted move loop");
                            }
                        }
                    }

                    /*
                     * Do here whatever you want.
                     */



                } while (true);
            }
        };

        /* We initialize to prevent ugly null checks */
        Thread t = new Thread(runner);

        /* You can actually enter whatever you want for stopping the thread */
        System.out.println("Enter \"s\" to start/stop thread, \"q\" to exit");
        JFrame frame = new JFrame("Wigglr");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(260,100);
        JLabel button1 = new JLabel("     Running... Close Application to Stop.");
        frame.getContentPane().add(BorderLayout.CENTER, button1);
        frame.setVisible(true);

        while (true) {
            /* Blocking, our thread runs anyway if started */
            //String input = scanner.next();

            if (!t.isAlive() && isOn) {

                /*
                 * We ensure that we have a new thread because stopped threads
                 * can't be reused.
                 */
                t = new Thread(runner);
                logger.info("Starting thread");
                t.start();

            } else if (t.isAlive()) {

                /*
                 * If the thread is alive, we want to stop it on user input
                 * regardless wether it is q or s
                 */
                logger.info("Shutting down thread...");

                /* We tell the thread to shut itself down */
                t.interrupt();

                try {
                    /* We wait for our thread to finish cleanly */
                    t.join();
                } catch (InterruptedException e) {
                    /* Something is severely wrong here */
                    logger.severe("Got interrupted during thread shutdown:");
                    e.printStackTrace();
                    logger.severe("Unclean exit!");
                    System.exit(5);
                }

                logger.info("Thread stopped");

            }

            /*
             * If the thread was running on user input
             * it is cleanly shut down by now
             * Next, we want to find out wether we should exit
             * or wait for the next user input of "s" to start.
             */
            /*if (input.equals("q")) {
                logger.info("Exiting...");
                System.exit(0);
            }
            *//* Clear the input line *//*
            scanner.nextLine();*/

        }
    }
}
