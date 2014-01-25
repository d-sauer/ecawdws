/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

/**
 *
 * @author sheky
 */
public class readProperties {

    public static String propFile = "conf/options.properties";

    public String getRoot() {
        URL url = null;
        String path = null;
        try {
            url = this.getClass().getClassLoader().getResource("/");
            path = url.toString();
        } catch (Exception ex) {
            url = this.getClass().getResource("/");
            path = url.toString();
        }
        return path;
    }

    public Properties readPropFile() {
        Properties prop = new Properties();
        URL url = null;
        String path = null;
        url = this.getClass().getResource("/");
        path = url.toString();
        System.out.println("root url: " + path);
        path = path.substring(0, path.lastIndexOf("/"));
        path = path.substring(0, path.lastIndexOf("/")) + "/" + propFile;
        System.out.println("config url: " + path);

        File file = null;

        try {
            url = new URL(path);
            file = new File(url.toURI());
        } catch (URISyntaxException ex) {
            System.out.println("error: file -- uri");
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            System.out.println("error: url");
        }

        FileInputStream fis = null;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                //System.out.println("file size: " + fis.available());
                prop.load(fis);
                fis.close();
                return prop;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("error: Properties file don't exist!");
        }
        return null;
    }

    public String readProp(String naziv) {
        Properties prop = readPropFile();
        return prop.getProperty(naziv).toString();
    }

    public Collection readPropKey(String naziv) {
        Collection col = new LinkedList();

        Properties prop = readPropFile();
        Iterator it = prop.keySet().iterator();
        while (it.hasNext()) {
            String propStr = it.next().toString();
            if (propStr.contains(naziv)) {
                col.add(propStr);
            }
        }
        return col;
    }

    /*public static void main(String[] args) {

    readProperties rp = new readProperties();
    System.out.println("prop:" + rp.readPropKey("cost.").toString());


    }*/
}
