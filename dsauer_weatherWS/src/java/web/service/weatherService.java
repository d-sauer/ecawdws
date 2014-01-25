/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.service;

import java.sql.ResultSet;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import web.cityData;
import web.sqlQuery;
import web.weather.weatherData;
import web.zahtjevi;

/**
 *
 * @author sheky
 */
@WebService()
public class weatherService {

    /**
     * lista svih djelova gradova sa ZIP kodovima
     */
    @WebMethod(operationName = "getCityZIPs")
    public String getCityZIPs(@WebParam(name = "msg") String msg) {
        return zahtjevi.generateURLCityZIPs(msg);

    }

    /**
     * countrySearch
     * lista USA država i gradova prema predlošku naziva države
     */
    @WebMethod(operationName = "stateSearch")
    public String stateSearch(@WebParam(name = "state") String state, @WebParam(name = "msg") String msg) {
        return zahtjevi.generateURLStateSearch(state, msg);
    }

    /**
     * lista USa gradova prema predlošku naziva okruga
     */
    @WebMethod(operationName = "cityCounty")
    public String cityCounty(@WebParam(name = "county") String county, @WebParam(name = "msg") String msg) {
        return zahtjevi.generateURLCountySearch(county, msg);
    }

    /**
     * lista USa gradova prema predlošku
     */
    @WebMethod(operationName = "citySearch")
    public String citySearch(@WebParam(name = "city") String city, @WebParam(name = "msg") String msg) {
        return zahtjevi.generateURLCitySearch(city, msg);
    }

    @WebMethod(operationName = "WeatherData")
    public String WeatherData(@WebParam(name = "zip") String zip, @WebParam(name = "msg") String msg) {
        return zahtjevi.generateURL_USAWeatherData(zip, msg);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "distance")
    public String distance(@WebParam(name = "zip1") String zip1, @WebParam(name = "zip2") String zip2, @WebParam(name = "msg") String msg) {
        //TODO write your implementation code here:
        return zahtjevi.generateClacDistanceKM(zip1, zip2, msg);
    }

    @WebMethod(operationName = "worldCountry")
    public String worldCountry(@WebParam(name = "country") String country, @WebParam(name = "msg") String msg) {
        //TODO write your implementation code here:
        return zahtjevi.generateWorldCountrySearch(country, msg);
    }

    @WebMethod(operationName = "worldCity")
    public String worldCity(@WebParam(name = "city") String city, @WebParam(name = "msg") String msg) {
        //TODO write your implementation code here:
        return zahtjevi.generateWorldCity(city, msg);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "searchCityUSA")
    public String searchCityUSA(@WebParam(name = "zip") String zip, @WebParam(name = "city") String city, @WebParam(name = "state") String state) {
        if (zip == null) {
            zip = "";
        }
        if (city == null) {
            city = "";
        }
        if (state == null) {
            state = "";
        }

        ResultSet rs = sqlQuery.searchCityUSA(zip, city, state);
        return web.weather.generateXML.generateXMLfromResultSet(rs, "", "");
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getWeatherByUSZipCode")
    public String getWeatherByUSZipCode(@WebParam(name = "zip") String zip) {
        if (zip != null) {
            weatherData wData = sqlQuery.getWeatherByUSZipCode(zip);
            cityData cData = sqlQuery.getCityDataUSZipCode(zip);
            return web.weather.generateXML.generateWeatherXMLFromData(cData, wData, "", "");
        } else {
            return null;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllCityZIPs")
    public String getAllCityZIPs() {
        ResultSet rs = sqlQuery.getAllCityZIPs();
        return web.weather.generateXML.generateXMLfromResultSet(rs, "", "");
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getUSAState")
    public String getUSAState(@WebParam(name = "state") String state) {
        if (state == null) {
            return null;
        } else {
            ResultSet rs = sqlQuery.getUSAState(state);
            return web.weather.generateXML.generateXMLfromResultSet(rs, "", "");
        }
    }


     /**
     * Web service operation
     */
    @WebMethod(operationName = "getUSACounty")
    public String getUSACounty(@WebParam(name = "county") String county) {
        if (county == null) {
            return null;
        } else {
            ResultSet rs = sqlQuery.getUSAState(county);
            return web.weather.generateXML.generateXMLfromResultSet(rs, "", "");
        }
    }


    /**
     * Web service operation
     */
    @WebMethod(operationName = "getUSACity")
    public String getUSACity(@WebParam(name = "city") String city) {
        if (city == null) {
            return null;
        } else {
            ResultSet rs = sqlQuery.getUSAState(city);
            return web.weather.generateXML.generateXMLfromResultSet(rs, "", "");
        }
    }


    /**
     * Web service operation
     */
    @WebMethod(operationName = "calculateDistance")
    public String calculateDistance(@WebParam(name = "zip1") String zip1, @WebParam(name = "zip2") String zip2) {
        if (zip1 == null) {
            return null;
        }else if (zip2==null) {
            return null;
        }else {
            Double dist = zahtjevi.calculateDistance(zip1, zip2);
            return dist.toString();
        }
    }

     /**
     * Web service operation
     */
    @WebMethod(operationName = "searchWorld")
    public String searchWorld(@WebParam(name = "city") String city, @WebParam(name = "prov") String prov,
            @WebParam(name = "country") String country, @WebParam(name = "code") String code,@WebParam(name = "cont") String cont,
            @WebParam(name = "reg") String reg,@WebParam(name = "lang") String lang) {
        if (city == null) {
            city="";
        }
        if (prov == null) {
            prov="";
        }
        if (country == null) {
            country="";
        }
        if (code == null) {
            code="";
        }
        if (cont == null) {
            cont="";
        }
        if (reg == null) {
            reg="";
        }
        if (lang == null) {
            lang="";
        }
            ResultSet rs = sqlQuery.searchWorld(city, prov, country, code, cont, reg, lang);
            return web.weather.generateXML.generateXMLfromResultSet(rs, "", "");        
    }


     /**
     * Web service operation
     */
    @WebMethod(operationName = "getWorldCountryLang")
    public String getWorldCountryLang(@WebParam(name = "countryCode") String countryCode) {
        if (countryCode == null) {
            return null;
        }else {
            ResultSet rs = sqlQuery.getWorldCountryLang(countryCode);
            return web.weather.generateXML.generateXMLfromResultSet(rs, "", "");
        }
    }

}
