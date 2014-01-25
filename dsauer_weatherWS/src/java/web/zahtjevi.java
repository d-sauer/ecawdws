/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import web.weather.generateHTML;
import web.weather.generateXML;
import web.weather.weatherClient;
import web.weather.weatherData;

/**
 *
 * @author sheky
 */
public class zahtjevi {

    public static weatherData getLiveWeatherByUSZipCode(String zip) {
        weatherData wData = new weatherData();
        if (sqlQuery.isZipWeatherExist(zip) == true) {
            wData = sqlQuery.getWeatherByUSZipCode(zip);
        //System.out.println("zahtjevi: podaci vec postoje u bazi za zip code: " + zip);
        } else { //ako podaci ne postoje u bazi
            //dohvati podatke sa weatherBug servisa
            wData = weatherClient.GetLiveWeatherByUSZipCode(zip);
            //pohrani podatke u bazu
            if (wData.getZIP() != null) { //rezultati za trazeni grad
                sqlQuery.setWeatherByUSZipCode(wData);
            }
        }
        sqlQuery.logAction("getLiveWeatherByUSZipCode zip:" + zip);
        return wData;
    }

    public static weatherData updateLiveWeatherByUSZipCode(String zip) {
        weatherData wData = new weatherData();
        //dohvati podatke sa weatherBug servisa
        wData = weatherClient.GetLiveWeatherByUSZipCode(zip);
        //pohrani podatke u bazu
        sqlQuery.updateWeatherByUSZipCode(wData);

        sqlQuery.logAction("updateLiveWeatherByUSZipCode zip:"+ zip);
        return wData;
    }

    public static cityData getCityDataUSZipCode(String zip) {
        sqlQuery.logAction("getCityDataUSZipCode zip:" + zip);
        return sqlQuery.getCityDataUSZipCode(zip);
    }

    public static boolean refreshWeatherDataUSA() {
        sqlQuery.logAction("refreshWeatherDataUSA");
        ResultSet rs = sqlQuery.getIndexedCity();
        try {
            
            if(rs==null) {
                return false;
            }

            weatherData wData = new weatherData();
            String zip = "";
            while (rs.next()) {
                zip = rs.getString(1);
                System.out.println("UPDATE ZIP:" + zip);
                //dohvati podatke sa weatherBug servisa
                wData = weatherClient.GetLiveWeatherByUSZipCode(zip);
                //pohrani podatke u bazu
                if (wData != null) {
                    sqlQuery.updateWeatherByUSZipCode(wData);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static String generateURLCityZIPs(String msg) {
        sqlQuery.logAction("generateURLCityZIPs, zahtjev:" + msg);
        ResultSet rs = sqlQuery.getAllCityZIPs();
        String fname = generateFileName();
        String returnURL;
        String url1 = saveFile(generateXML.generateXMLfromResultSet(rs, "USA - Popis svih gradova", msg), fname + ".xml");
        String url2 = saveFile(generateHTML.generateHTMLfromResultSet(rs, "USA - Popis svih gradova", msg), fname + ".html");
        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    public static String generateURLStateSearch(String state, String msg) {
        sqlQuery.logAction("generateURLStateSearch zahtjev:" + msg);
        ResultSet rs = sqlQuery.getUSAState(state);
        String fname = generateFileName();
        String returnURL;
        String url1 = saveFile(generateXML.generateXMLfromResultSet(rs, "USA - popis gradova prema nazivu države", msg), fname + ".xml");
        String url2 = saveFile(generateHTML.generateHTMLfromResultSet(rs, "USA - popis gradova prema nazivu države", msg), fname + ".html");
        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    public static String generateURLCountySearch(String city, String msg) {
        sqlQuery.logAction("generateURLCountySearch city:" + city + "   zahtjev:" + msg);
        ResultSet rs = sqlQuery.getUSACity(city);
        String fname = generateFileName();
        String returnURL;
        String url1 = saveFile(generateXML.generateXMLfromResultSet(rs, "USA - popis gradova prema nazivu okruga", msg), fname + ".xml");
        String url2 = saveFile(generateHTML.generateHTMLfromResultSet(rs, "USA - popis gradova prema nazivu okruga", msg), fname + ".html");
        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    public static String generateURLCitySearch(String city, String msg) {
        sqlQuery.logAction("generateURLCitySearch city:" + city + "     zahtjev:" + msg);
        ResultSet rs = sqlQuery.getUSACity(city);
        String fname = generateFileName();
        String returnURL;
        String url1 = saveFile(generateXML.generateXMLfromResultSet(rs, "USA - popis gradova prema nazivu okruga", msg), fname + ".xml");
        String url2 = saveFile(generateHTML.generateHTMLfromResultSet(rs, "USA - popis gradova prema nazivu okruga", msg), fname + ".html");
        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    public static String generateURL_USAWeatherData(String zip, String msg) {
        sqlQuery.logAction("generateURL_USAWeatherData zip:" + zip + "    zahtjev:"+ msg);
        cityData cData = new cityData();
        weatherData wData = new weatherData();
        cData = getCityDataUSZipCode(zip);
        wData = getLiveWeatherByUSZipCode(zip);

        String fname = generateFileName();
        String returnURL;
        String url1 = saveFile(generateXML.generateWeatherXMLFromData(cData, wData, "Podaci o vremenskoj prognozi", msg), fname + ".xml");
        String url2 = saveFile(generateHTML.generateWeatherHTMLfromData(cData, wData, "Podaci o vremenskoj prognozi", msg), fname + ".html");
        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    private static String generateFileName() {
        Date date = new Date();
        Long msec = date.getTime();
        return "file_" + msec.toString();
    }

    private static String saveFile(String content, String fname) {
        readProperties rp = new readProperties();

        String path = rp.getRoot();//ClassLoader.getSystemClassLoader().getResource(".").toString();
        path = path.substring(0, path.lastIndexOf("/"));
        path = path.substring(0, path.lastIndexOf("/"));
        path = path.substring(0, path.lastIndexOf("/")) + "/generate/";
        path += fname;

        FileOutputStream fos = null;
        try {
            URL url = new URL(path);
            fos = new FileOutputStream(new File(url.toURI()));
            fos.write(content.getBytes());

            System.out.println("Save XML/HTML file to url:" + url.toString());
            fos.close();
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fos.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return path;
    }

    public static Double calculateDistance(String zip1, String zip2) {
        sqlQuery.logAction("calculateDistance (" + zip1 + "," + zip2 + ")");
        Double lat1 = 0.0, lng1 = 0.0, lat2 = 0.0, lng2 = 0.0;
        Vector<Double> coodriantes;
        coodriantes = sqlQuery.getUSACityCoordinates(zip1);
        if (coodriantes != null) {
            lat1 = coodriantes.elementAt(0); //LATITUDE
            lng1 = coodriantes.elementAt(1); //LONGITUDE
        } else {
            return -1.0;
        }

        coodriantes = sqlQuery.getUSACityCoordinates(zip2);
        if (coodriantes != null) {
            lat2 = coodriantes.elementAt(0); //LATITUDE
            lng2 = coodriantes.elementAt(1); //LONGITUDE
        } else {
            return -1.0;
        }

        System.out.println("zip1:" + zip1 + "  lat:" + lat1.toString() + "  lng:" + lng1.toString());
        System.out.println("zip2:" + zip2 + "  lat:" + lat2.toString() + "  lng:" + lng2.toString());

        Double e;
        e = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1));
        e *= 6378.137;  //KM, množi se s radijusom ekvatora

        return e;
    }

    public static String generateClacDistanceKM(String zip1, String zip2, String msg) {
         sqlQuery.logAction("generateClacDistanceKM (" + zip1 + "," + zip2 + ")    zahtjev:" + msg);
        Double lat1 = 0.0, lng1 = 0.0, lat2 = 0.0, lng2 = 0.0;
        Vector<Double> coodriantes;
        String err = "";
        coodriantes = sqlQuery.getUSACityCoordinates(zip1);
        if (coodriantes != null) {
            lat1 = coodriantes.elementAt(0); //LATITUDE
            lng1 = coodriantes.elementAt(1); //LONGITUDE
        } else {
            err += "Grad sa zip kodom " + zip1 + " nije pronađen\n";
        }

        coodriantes = sqlQuery.getUSACityCoordinates(zip2);
        if (coodriantes != null) {
            lat2 = coodriantes.elementAt(0); //LATITUDE
            lng2 = coodriantes.elementAt(1); //LONGITUDE
        } else {
            err += "Grad sa zip kodom " + zip2 + " nije pronađen\n";
        }

        System.out.println("zip1:" + zip1 + "  lat:" + lat1.toString() + "  lng:" + lng1.toString());
        System.out.println("zip2:" + zip2 + "  lat:" + lat2.toString() + "  lng:" + lng2.toString());

        Double e;
        e = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1));
        e *= 6378.137;  //KM, množi se s radijusom ekvatora

        System.out.println("Udaljnost (km):" + e.toString());

        String fname = generateFileName();
        String returnURL = null;

        String url1 = saveFile(generateXML.generateDistanceXML(zip1, zip2, lat1, lng1, lat2, lng2, e, "Udaljenost između gradova", msg, err), fname + ".xml");
        String url2 = saveFile(generateHTML.generateDistanceHTML(zip1, zip2, lat1, lng1, lat2, lng2, e, "Udaljenost između gradova", msg, err), fname + ".html");


        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    public static String generateWorldCountrySearch(String country, String msg) {
         sqlQuery.logAction("generateWorldCountrySearch country:" + country + "   zahtjev:" + msg);
        ResultSet rs = sqlQuery.searchWorld("", "", country, "", "", "", "");
        String fname = generateFileName();
        String returnURL;
        String url1 = saveFile(generateXML.generateXMLfromResultSet(rs, "Popis svjetskih gradova i država prema nazivu države", msg), fname + ".xml");
        String url2 = saveFile(generateHTML.generateHTMLfromResultSet(rs, "Popis svjetskih gradova i država prema nazivu države", msg), fname + ".html");
        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    public static String generateWorldCity(String city, String msg) {
         sqlQuery.logAction("generateWorldCity city:" + city + "    zahtjev:" + msg);
        ResultSet rs = sqlQuery.searchWorld(city, "", "", "", "", "", "");
        String fname = generateFileName();
        String returnURL;
        String url1 = saveFile(generateXML.generateXMLfromResultSet(rs, "Popis svjetskih gradova i država prema nazivu grada", msg), fname + ".xml");
        String url2 = saveFile(generateHTML.generateHTMLfromResultSet(rs, "Popis svjetskih gradova i država prema nazivu grada", msg), fname + ".html");
        returnURL = "<a href=\"" + url1 + "\">" + fname + ".xml</a>\n";
        returnURL += "<a href=\"" + url2 + "\">" + fname + ".html</a>\n";

        return returnURL;
    }

    //public static void main(String[] args) {
    //weatherData wData = new weatherData();
    //wData = getLiveWeatherByUSZipCode("01452");
    //System.out.println(wData.toString());
    //refreshWeatherDataUSA();
    //generateURLCityZIPs();
    //System.out.println(generateURL_USAWeatherData("44333"));
    //generateClacDistanceKM("00605", "00606","");
    //}
}
