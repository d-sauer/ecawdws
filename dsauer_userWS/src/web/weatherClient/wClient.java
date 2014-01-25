/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.weatherClient;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import web.readProperties;

/**
 *
 * @author sheky
 */
public class wClient {

    public static URL weatherWSDL = null;
    public static String wURL = null;

    public static void getWeatherWSDL_url() {
        readProperties rp = new readProperties();
        wURL = rp.readProp("wService.WSDL");
        try {
            URL baseUrl;
            baseUrl = web.service.WeatherServiceService.class.getResource(".");
            weatherWSDL = new URL(baseUrl, wURL);
        } catch (MalformedURLException e) {
            System.out.println("Failed to create URL for the wsdl Location: '" + wURL + "', retrying as a local file");
            weatherWSDL = null;
            e.printStackTrace();
        }
        System.out.println("weather service URL: " + weatherWSDL.toString());
    }

    public static String CityZIPs(String msg_zahtjev) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }

        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService(weatherWSDL, new QName("http://service.web/", "weatherServiceService"));
            web.service.WeatherService port = service.getWeatherServicePort();



            String result = port.getCityZIPs(msg_zahtjev);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: cityZIPs; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }
    }

    public static String stateSearch(String state, String msg_zahtjev) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }
        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService();
            web.service.WeatherService port = service.getWeatherServicePort();

            java.lang.String result = port.stateSearch(state, msg_zahtjev);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: stateSearch; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }

    }

    public static String cityCounty(String county, String msg_zahtjev) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }
        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService();
            web.service.WeatherService port = service.getWeatherServicePort();
            // TODO initialize WS operation arguments here

            java.lang.String result = port.cityCounty(county, msg_zahtjev);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: cityCounty; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }
    }

    public static String citySearch(String city, String msg) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }
        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService();
            web.service.WeatherService port = service.getWeatherServicePort();

            java.lang.String result = port.citySearch(city, msg);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: citySearch; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }
    }

    public static String data(String zip, String msg) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }
        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService();
            web.service.WeatherService port = service.getWeatherServicePort();

            java.lang.String result = port.weatherData(zip, msg);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: data; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }
    }

    public static String distance(String zip1, String zip2, String msg) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }
        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService();
            web.service.WeatherService port = service.getWeatherServicePort();

            java.lang.String result = port.distance(zip1, zip2, msg);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: distance; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }

    }

    public static String cityCountry(String country, String msg) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }
        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService();
            web.service.WeatherService port = service.getWeatherServicePort();

            java.lang.String result = port.worldCountry(country, msg);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: cityCountry; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }

    }

    public static String worldCity(String city, String msg) {
        if (weatherWSDL == null) {
            getWeatherWSDL_url();
        }
        try { // Call Web Service Operation
            web.service.WeatherServiceService service = new web.service.WeatherServiceService();
            web.service.WeatherService port = service.getWeatherServicePort();

            java.lang.String result = port.worldCity(city, msg);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error: worldCity; Došlo je do pogreške prilikom komunikacije sa web servisom: " + weatherWSDL.toString();
        }

    }
}
