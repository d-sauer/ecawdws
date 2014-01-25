/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.mailThread;

import web.readProperties;
import web.services.dsauer_userWS;

/**
 *
 * @author sheky
 */
public class mailThread extends Thread {

    private static boolean isThStart = false;
    private static boolean thStart = false;     
    public static Integer thTime = 10000;

    private void mailThread() {
    }

    public boolean getThreadStatus() {
        return thStart;
    }

    public void setThSleepTime(Integer time) {
        thTime = time;
    }

    public void setOptionThSleepTime() {
        readProperties readProp = new readProperties();
        String prop = readProp.readProp("thread.sleep");
        System.out.println("read properties time (sec):" + prop);
        Integer time = Integer.parseInt(prop) * 1000;
        setThSleepTime(time);
    }

    public Integer getThSleepTime() {
        return thTime;
    }

    @Override
    public void run() {
        setOptionThSleepTime();
        isThStart = true;
        while (thStart) {
            try {
                System.out.println("+ mailThread: " + this.getName());                
                mail.checkMail();

                mailThread.sleep(thTime);
            } catch (InterruptedException ex) {
                System.out.println(">> Thread ERROR");
                thStart=false;                
                ex.printStackTrace();
            }
        }
        isThStart = false;

        System.out.println(">> mailThread: STOP");
    }

    public void startThread() {
        if (thStart == false) {
            mail.loadMailProperties();
            dsauer_userWS.mailTh = new mailThread();            
            dsauer_userWS.mailTh.start();
            //TODO mislim da je dovoljno samo start(); jer je klasa extend od thread -- provjerit
            thStart = true;
            System.out.println(">> mailThread: START");
        } else {
            System.out.println(">> mailThread: ALREDY STARTED");
        }
    }

    public void stopThread() {
        thStart = false; //zaustavlja dretvu
        Integer wait = 500;
        Integer ukupno = 0;
        System.err.println("stop thread provjera: " + this.isAlive());

        if (this.isAlive()) {
            while (isThStart == true) {
                try {
                    ukupno += wait;
                    System.out.println(">> WAIT for mailThread finish (" + ukupno.toString() + " ms) " + this.getName());
                    Thread.sleep(wait);
                } catch (InterruptedException ex) {
                }
            }
        } else {
            System.out.println(">> mailThread: STOP");
        }
    }
}
