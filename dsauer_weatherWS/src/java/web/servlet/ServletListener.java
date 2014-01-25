/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import web.readProperties;
import web.weather.weatherThread;

/**
 * Web application lifecycle listener.
 * @author sheky
 */
public class ServletListener implements ServletContextListener {

    public static weatherThread wThread = null;

    public void contextInitialized(ServletContextEvent sce) {        
        readProperties rp = new readProperties();
        String wThreadStart = rp.readProp("weather.thread");
        if (wThreadStart.equals("true")) {
            System.out.println("Pokretanje dretve za update podataka o vremenskim prognozama");
            wThread = new weatherThread();
            wThread.startThread();
        }

        File f = new File("dado.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f);
            fos.write("davor".getBytes());
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (wThread != null) {
            wThread.stopThread();
            System.out.println("Zaustavljanje dretve za update podataka o vremenskim prognozama");
        }
    }
}