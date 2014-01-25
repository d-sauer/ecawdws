/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package web.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import web.readProperties;

/**
 *
 * @author sheky
 */
public class weatherClient {

    private static String weatherWSDL = null;
    private static String ACode = null;

    private static void readProperties() {
        readProperties rp = new readProperties();
        ACode = rp.readProp("weather.acode");
        
        weatherWSDL = rp.readProp("weather.wsdl");
        weatherWSDL = weatherWSDL.substring(0, weatherWSDL.indexOf("?WSDL"));
    }

    //SOAP 1.2
    public static String sendRequest(String xml) throws Exception {
        if(weatherWSDL==null) {
            readProperties();
        }
        String SOAPUrl = weatherWSDL;//"http://api.wxbug.net/webservice-v1.asmx";        
        System.out.println("Povezivanje sa servisom: " + weatherWSDL);
        // Create the connection where we're going to send the file.
        URL url = new URL(SOAPUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;

        byte[] b = xml.getBytes();

        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        //httpConn.setRequestProperty("SOAPAction", SOAPAction);
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);

        // Everything's set up; send the XML that was read in to b.
        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();

        // Read the response and write it to standard out.

        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        String inputLine;
        String ResponseXML = "";
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
            ResponseXML+=inputLine;
        }

        in.close();
        return ResponseXML;
    }

    public static void copy(InputStream in, OutputStream out)  throws IOException {
        // do not allow other threads to read from the
        // input or write to the output while copying is
        // taking place

        synchronized (in) {
            synchronized (out) {

                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }


    public static weatherData GetLiveWeatherByUSZipCode(String zip) {
        weatherData wData = new weatherData();
        if(ACode==null) {
            readProperties();
        }
        try {
            String result = sendRequest(generateXML.XML_GetLiveWeatherByUSZipCode(zip, ACode));
            wData = parseXML.parseXML_GetLiveWeatherByUSZipCode(result,"GetLiveWeatherByUSZipCodeResult");
            System.out.println("weatherClient: trazeni ZIP:" + zip + "  dobiveni podaci za ZIP:" + wData.getZIP());
            if(zip.equals(wData.getZIP())) {
                return wData;
            }else {
                //nema podataka za traženi grad!
                //servis vraca informacije za ALTERNTIVNI grad, prvi najblizi za koji ima informacije
                wData.makeZipAsAlternativeZIP();
                return wData; //nema podataka za trazeni grad
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*public static void main(String [] args) {
        String result = GetLiveWeatherByUSZipCode("01452");           
    }*/

}
