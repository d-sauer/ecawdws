/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import web.weather.weatherData;

/**
 *
 * @author sheky
 */
public class sqlQuery {


    /**
     * Biljezenje logova
     */
    public static void logAction(String zahtjev) {
        try {
            String sql = "INSERT INTO logs (zahtjev ,vrijeme) VALUES (?,?)";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, zahtjev);

            java.util.Date sys_date = new java.util.Date();
            java.sql.Timestamp vrijeme = new java.sql.Timestamp(sys_date.getTime());
            sqlStat.setTimestamp(2, vrijeme);

            sqlStat.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    
    public static ResultSet searchCityUSA(String zip, String city, String state) {
         sqlQuery.logAction("searchCityUSA zip:" + zip + "  city:" + city + "   state:"+ state);
        String subQuery = "";
        boolean q = false;
        if (zip.length() != 0 ) {
            subQuery += " zip REGEXP '^" + zip + "' ";
            q = true;
        }

        if (city.length() != 0) {
            if (q == true) {
                subQuery += " AND CITY REGEXP '^" + city + "'";
            } else {
                subQuery += " CITY REGEXP '^" + city + "'  ";
                q = true;
            }
        }
        
        if (state.length() != 0) {
            if (q == true) {
                subQuery += " AND STATE REGEXP '^" + state + "'  ";
            } else {
                subQuery += " STATE REGEXP '^" + state + "'  ";
                q = true;
            }
        }

        try {
            String sql = "SELECT STATE, COUNTY, CITY,ZIP from zip_codes WHERE " + subQuery + " ORDER BY STATE,CITY";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            ResultSet rs = sqlStat.executeQuery(sql);
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static boolean isZipWeatherExist(String zip) {
        try {
            String sql = "SELECT COUNT(*) FROM weather WHERE zip=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, zip);
            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) != 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static weatherData getWeatherByUSZipCode(String zip) {
        weatherData wData = new weatherData();
        try {
            String sql = "SELECT * FROM weather WHERE ZIP=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, zip);
            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                wData.setCurrDesc(rs.getString("CurrDesc"));
                wData.setCurrIcon(rs.getString("CurrIcon"));
                wData.setHumidity(rs.getFloat("Humidity"));
                wData.setHumidityHigh(rs.getFloat("HumidityHigh"));
                wData.setHumidityLow(rs.getFloat("HumidityLow"));
                wData.setHumidityUni(rs.getString("HumidityUnit"));
                wData.setMoonPhaseImage(rs.getString("MoonPhaseImage"));
                wData.setPressure(rs.getFloat("Pressure"));
                wData.setPressureHigh(rs.getFloat("PressureHigh"));
                wData.setPressureLow(rs.getFloat("PressureLow"));
                wData.setPressureUnit(rs.getString("PressureUnit"));
                wData.setRainRate(rs.getFloat("RainRate"));
                wData.setRainRateMax(rs.getFloat("RainRateMax"));
                wData.setRainRateUnit(rs.getString("RainRateUnit"));
                wData.setRainToday(rs.getFloat("RainToday"));
                wData.setRainUnit(rs.getString("RainUnit"));
                wData.setSunrise(rs.getTimestamp("Sunrise"));
                wData.setSunset(rs.getTimestamp("Sunset"));
                wData.setTemperature(rs.getFloat("Temperature"));
                wData.setTemperatureHigh(rs.getFloat("TemperatureHigh"));
                wData.setTemperatureLow(rs.getFloat("TemperatureLow"));
                wData.setTemperatureUnit(rs.getString("TemperatureUnit"));
                wData.setTimeZone(rs.getString("TimeZone"));
                wData.setTimeZoneOffset(rs.getInt("TimeZoneOffset"));
                wData.setWindDirection(rs.getString("WindDirection"));
                wData.setWindSpeed(rs.getFloat("WindSpeed"));
                wData.setWindSpeedUnit(rs.getString("WindSpeedUnit"));
                wData.setZIP(rs.getString("ZIP"));

                return wData;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void setWeatherByUSZipCode(weatherData wData) {
        sqlQuery.logAction("setWeatherByUSZipCode zip:" + wData.getZIP());
        System.out.println("New weather data for:" + wData.getZIP());
        if (isZipWeatherExist(wData.getZIP()) == true) {
            System.out.println("podatci vec postoje u bazi");
        }
        try {
            String sql = "INSERT INTO weather (ZIP ,CityCode ,CurrIcon ,CurrDesc ,Temperature ,TemperatureHigh ," +
                    "TemperatureLow ,TemperatureUnit ,WindDirection ,WindSpeed ,WindSpeedUnit ," +
                    "Humidity ,HumidityUnit ,HumidityHigh ,HumidityLow ,MoonPhaseImage ," +
                    "Pressure ,PressureUnit ,PressureHigh ,PressureLow ," +
                    "RainRate ,RainRateMax ,RainRateUnit ,RainToday ,RainUnit ,TimeZone ,TimeZoneOffset ,Sunrise ,Sunset) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, wData.getZIP());
            sqlStat.setString(2, "");
            sqlStat.setString(3, wData.getCurrIcon());
            sqlStat.setString(4, wData.getCurrDesc());
            sqlStat.setFloat(5, wData.getTemperature());
            sqlStat.setFloat(6, wData.getTemperatureHigh());
            sqlStat.setFloat(7, wData.getTemperatureLow());
            sqlStat.setString(8, wData.getTemperatureUnit());
            sqlStat.setString(9, wData.getWindDirection());
            sqlStat.setFloat(10, wData.getWindSpeed());
            sqlStat.setString(11, wData.getWindSpeedUnit());
            sqlStat.setFloat(12, wData.getHumidity());
            sqlStat.setString(13, wData.getHumidityUni());
            sqlStat.setFloat(14, wData.getHumidityHigh());
            sqlStat.setFloat(15, wData.getHumidityLow());
            sqlStat.setString(16, wData.getMoonPhaseImage());
            sqlStat.setFloat(17, wData.getPressure());
            sqlStat.setString(18, wData.getPressureUnit());
            sqlStat.setFloat(19, wData.getPressureHigh());
            sqlStat.setFloat(20, wData.getPressureLow());
            sqlStat.setFloat(21, wData.getRainRate());
            sqlStat.setFloat(22, wData.getRainRateMax());
            sqlStat.setString(23, wData.getRainRateUnit());
            sqlStat.setFloat(24, wData.getRainToday());
            sqlStat.setString(25, wData.getRainUnit());
            sqlStat.setString(26, wData.getTimeZone());
            sqlStat.setInt(27, wData.getTimeZoneOffset());
            sqlStat.setTimestamp(28, wData.getSunrise());
            sqlStat.setTimestamp(29, wData.getSunset());

            sqlStat.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static void updateWeatherByUSZipCode(weatherData wData) {
        try {
            String sql = "UPDATE weather SET CurrIcon = ?, CurrDesc = ?, Temperature = ?, TemperatureHigh = ?, " +
                    "TemperatureLow = ?, TemperatureUnit = ?, WindDirection = ?, WindSpeed = ?, WindSpeedUnit = ?, " +
                    "Humidity = ?, HumidityUnit = ?, HumidityHigh = ?, HumidityLow = ?, MoonPhaseImage = ?, " +
                    "Pressure = ?, PressureUnit = ?, PressureHigh = ?, PressureLow = ?, RainRate = ?, " +
                    "RainRateMax = ?, RainRateUnit = ?, RainToday = ?, RainUnit = ?, TimeZone = ?, TimeZoneOffset = ?," +
                    " Sunrise = ?, Sunset = ? WHERE ZIP = ?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);

            sqlStat.setString(1, wData.getCurrIcon());
            sqlStat.setString(2, wData.getCurrDesc());
            sqlStat.setFloat(3, wData.getTemperature());
            sqlStat.setFloat(4, wData.getTemperatureHigh());
            sqlStat.setFloat(5, wData.getTemperatureLow());
            sqlStat.setString(6, wData.getTemperatureUnit());
            sqlStat.setString(7, wData.getWindDirection());
            sqlStat.setFloat(8, wData.getWindSpeed());
            sqlStat.setString(9, wData.getWindSpeedUnit());
            sqlStat.setFloat(10, wData.getHumidity());
            sqlStat.setString(11, wData.getHumidityUni());
            sqlStat.setFloat(12, wData.getHumidityHigh());
            sqlStat.setFloat(13, wData.getHumidityLow());
            sqlStat.setString(14, wData.getMoonPhaseImage());
            sqlStat.setFloat(15, wData.getPressure());
            sqlStat.setString(16, wData.getPressureUnit());
            sqlStat.setFloat(17, wData.getPressureHigh());
            sqlStat.setFloat(18, wData.getPressureLow());
            sqlStat.setFloat(19, wData.getRainRate());
            sqlStat.setFloat(20, wData.getRainRateMax());
            sqlStat.setString(21, wData.getRainRateUnit());
            sqlStat.setFloat(22, wData.getRainToday());
            sqlStat.setString(23, wData.getRainUnit());
            sqlStat.setString(24, wData.getTimeZone());
            sqlStat.setInt(25, wData.getTimeZoneOffset());
            sqlStat.setTimestamp(26, wData.getSunrise());
            sqlStat.setTimestamp(27, wData.getSunset());
            sqlStat.setString(28, wData.getZIP());
            sqlStat.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static cityData getCityDataUSZipCode(String zip) {
        cityData usaCityData = new cityData();
        try {
            String sql = "SELECT * FROM zip_codes WHERE zip =?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, zip);
            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                usaCityData.setCity(rs.getString("CITY"));
                usaCityData.setZipCode(rs.getString("ZIP"));
                usaCityData.setState(rs.getString("STATE"));
                usaCityData.setCounty(rs.getString("COUNTY"));
                usaCityData.setLatitude(rs.getString("LATITUDE"));
                usaCityData.setLongitude(rs.getString("LONGITUDE"));

                return usaCityData;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getIndexedCity() {
        try {
            String sql = "SELECT ZIP,CityCode FROM weather";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                return rs;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getAllCityZIPs() {
        try {
            String sql = "SELECT * FROM zip_codes";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                return rs;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getUSAState(String state) {
        try {
            String sql = "SELECT * FROM zip_codes WHERE state LIKE '" + state + "%'";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                return rs;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getUSACounty(String county) {
        try {
            String sql = "SELECT * FROM zip_codes WHERE county LIKE '" + county + "%'";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                return rs;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getUSACity(String city) {
        try {
            String sql = "SELECT * FROM zip_codes WHERE city LIKE '" + city + "%'";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                return rs;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Vector<Double> getUSACityCoordinates(String zip) {
        Vector<Double> coordinates = new Vector<Double>();
        try {
            String sql = "SELECT LATITUDE,LONGITUDE FROM zip_codes WHERE ZIP=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, zip);

            ResultSet rs = sqlStat.executeQuery();

            if (rs.next()) {
                coordinates.add(parseStringToLong(rs.getString("LATITUDE")));
                coordinates.add(parseStringToLong(rs.getString("LONGITUDE")));
                return coordinates;
            } else {
                System.out.println("error: sqlQuery, trazeni ZIP kod nepostoji, " + zip);
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Double parseStringToLong(String var) {
        var = var.replace("+", "");
        var = var.replace("\n", "");
        var = var.trim();
        return Double.parseDouble(var);
    }

    public static ResultSet searchWorld(String city, String prov, String country, String code, String cont, String reg, String lang) {
        ResultSet rs = null;
        String subQuery = "";
        boolean q = false;
        if (city.length() != 0) {
            subQuery += " ci.name REGEXP '^" + city + "' ";
            q = true;
        }
        if (prov.length() != 0) {
            if (q == true) {
                subQuery += " AND ci.District REGEXP '^" + prov + "'";
            } else {
                subQuery += " ci.District REGEXP '^" + prov + "'  ";
                q = true;
            }
        }
        if (country.length() != 0) {
            if (q == true) {
                subQuery += " AND co.Name REGEXP '^" + country + "'";
            } else {
                subQuery += " co.Name REGEXP '^" + country + "'  ";
                q = true;
            }
        }
        if (code.length() == 3) {
            if (q == true) {
                subQuery += " AND co.code ='" + code + "' ";
            } else {
                subQuery += " co.code ='" + code + "' ";
                q = true;
            }
        }
        if (code.length() == 2) {
            if (q == true) {
                subQuery += " AND co.code2 ='" + code + "' ";
            } else {
                subQuery += " co.code2 ='" + code + "' ";
                q = true;
            }
        }
        if (code.length() == 1) {
            if (q == true) {
                subQuery += " AND co.code REGEXP '^" + code + "' ";
            } else {
                subQuery += " co.code REGEXP '^" + code + "' ";
                q = true;
            }
        }
        if (cont.length() != 0) {
            if (q == true) {
                subQuery += " AND co.Continent REGEXP '^" + cont + "' ";
            } else {
                subQuery += " co.Continent REGEXP '^" + cont + "' ";
                q = true;
            }
        }
        if (reg.length() != 0) {
            if (q == true) {
                subQuery += " AND co.Region REGEXP '^" + reg + "' ";
            } else {
                subQuery += " co.Region REGEXP '^" + reg + "' ";
                q = true;
            }
        }
        if (lang.length() != 0) {
            if (q == true) {
                subQuery += " AND lang.Language REGEXP '^" + lang + "' ";
            } else {
                subQuery += " lang.Language REGEXP '^" + lang + "' ";
                q = true;
            }
        }

        try {
            String sql = "SELECT DISTINCT co.Region, co.name AS Country, ci.name AS City, co.code, co.Continent, ci.ID AS cityId FROM city ci, country co, countrylanguage lang WHERE ci.CountryCode = co.code AND lang.CountryCode = co.code AND" + subQuery + " ORDER BY co.Region";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            rs = sqlStat.executeQuery(sql);
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getWorldCity(String countryCode, String cityId) {
        String subQuery = "CountryCode='" + countryCode + "' ";
        if (cityId.length() != 0) {
            subQuery += " AND ID=" + cityId;
        }
        try {
            String sql = "SELECT Name,District, Population FROM city WHERE " + subQuery + " ORDER BY Name";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);

            ResultSet rs = sqlStat.executeQuery();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getWorldCountryLang(String countryCode) {

        try {
            String sql = "SELECT Language,IsOfficial, Percentage FROM countrylanguage WHERE CountryCode=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, countryCode);

            ResultSet rs = sqlStat.executeQuery();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ResultSet getWorldCountry(String countryCode) {

        try {
            String sql = "SELECT Code,Code2, Name,Continent, Region,SurfaceArea,IndepYear,Population,LifeExpectancy,GNP,GNPOld,LocalName,GovernmentForm,HeadOfState,Capital FROM country WHERE Code=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, countryCode);

            ResultSet rs = sqlStat.executeQuery();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
