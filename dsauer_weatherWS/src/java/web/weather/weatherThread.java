/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.weather;

import java.util.Date;
import web.readProperties;
import web.zahtjevi;

/**
 *
 * @author sheky
 */
public class weatherThread extends Thread {

    private static boolean isThStart = false;
    private static boolean thStart = false;
    public static Integer thTime = 10000;
    public static boolean weatherUpdateComplete = false;

    private void weatherThread() {
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
        System.out.println("read properties time (min):" + prop);
        Integer time = Integer.parseInt(prop) * 1000 * 60; //minuta
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
                System.out.println("+ weatherThread: " + this.getName() + "   " + new Date().toString());
                weatherUpdateComplete = false;
                weatherUpdateComplete = zahtjevi.refreshWeatherDataUSA();

                weatherThread.sleep(thTime);
            } catch (InterruptedException ex) {
                System.out.println(">> Thread ERROR " + this.getName());
                thStart = false;
                ex.printStackTrace();
            }
        }
        isThStart = false;

        System.out.println(">> weatherThread: STOP " + this.getName());
    }

    public void startThread() {
        if (thStart == false) {
            //dsauer_userWS.mailTh = new mailThread();
            //dsauer_userWS.mailTh.start();

            thStart = true;
            System.out.println(">> weatherThread: START " + this.getName());
            start();
        } else {
            System.out.println(">> weatherThread: ALREDY STARTED " + this.getName());
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
                    if (weatherUpdateComplete == true) { //ako je završio UPDATe vrem. podataka, moze prekinuti dretvu i ranije
                        interrupt();
                    }
                    ukupno += wait;
                    System.out.println(">> WAIT for weatherThread finish (" + ukupno.toString() + " ms) " + this.getName());
                    

                    
                    if(ukupno>10000) {
                        this.interrupt();
                    }
                    Thread.sleep(wait);
                } catch (InterruptedException ex) {
                }
            }
        } else {
            System.out.println(">> weatherThread: STOP " + this.getName());
        }
    }
}
