/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.weather;

import java.sql.ResultSet;
import java.sql.SQLException;
import web.cityData;

/**
 *
 * @author sheky
 */
public class generateXML {

    public static String XML_GetLiveWeatherByUSZipCode(String zip, String acode) {
        String tmp = "";
        tmp += "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        tmp += "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ";
        tmp += "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">";
        tmp += "  <soap12:Body>";
        tmp += "    <GetLiveWeatherByUSZipCode xmlns=\"http://api.wxbug.net/\">";
        tmp += "      <zipCode>" + zip + "</zipCode>";
        tmp += "      <unittype>Metric</unittype>";
        tmp += "      <ACode>" + acode + "</ACode>";
        tmp += "    </GetLiveWeatherByUSZipCode>";
        tmp += "  </soap12:Body>";
        tmp += "</soap12:Envelope>";
        return tmp;
    }

    public static String generateXMLfromResultSet(ResultSet rs, String function, String msg) {
        StringBuffer sb = new StringBuffer();
        String colName = "";
        String colVar = "";
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<data>");
        sb.append("<zahtjev>");
        sb.append("<mail>" + msg + "</mail>\n");
        sb.append("<type>" + function + "</type>\n");
        sb.append("</zahtjev>");
        sb.append("<response>");
        try {
            java.sql.ResultSetMetaData rsMeta = rs.getMetaData();
            rs.beforeFirst();
            while (rs.next()) {
                sb.append("<city>\n");
                for (int i = 1; i < rsMeta.getColumnCount(); i++) {
                    colName = rsMeta.getColumnName(i);
                    colVar = rs.getString(i);
                    sb.append("<" + colName.toLowerCase() + ">");
                    sb.append(colVar);
                    sb.append("</" + colName.toLowerCase() + ">\n");
                }
                sb.append("</city>\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        sb.append("</response>");
        sb.append("</data>");
        return sb.toString();
    }

    public static String generateWeatherXMLFromData(cityData cData, weatherData wData, String funkcija, String msg) {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<data>\n");
        sb.append("<zahtjev>\n");
        sb.append(msg);
        sb.append("</zahtjev>\n");


        sb.append("<weatherData>\n");
        sb.append("<zipCode>" + cData.getZipCode().toString() + "</zipCode>");
        sb.append("<cityName>" + cData.getCity() + "</cityName>");
        sb.append("<cityCounty>" + cData.getCounty() + "</cityCounty>");
        sb.append("<cityState>" + cData.getState() + "</cityState>");
        sb.append("<cityLat>" + cData.getLatitude() + "</cityLat>");
        sb.append("<cityLng>" + cData.getLongitude() + "</cityLng>");

        sb.append("<CurrDesc>" + wData.getCurrDesc() + "</CurrDesc>");
        sb.append("<Temperature>" + wData.getTemperature().toString() + "</Temperature>");
        sb.append("<TemperatureHigh>" + wData.getTemperatureHigh().toString() + "</TemperatureHigh>");
        sb.append("<TemperatureLow>" + wData.getTemperatureLow().toString() + "</TemperatureLow>");
        sb.append("<TemperatureUnit><![CDATA[" + wData.getTemperatureUnit() + "]]></TemperatureUnit>");
        sb.append("<Humidity>" + wData.getHumidity().toString() + "</Humidity>");
        sb.append("<HumidityHigh>" + wData.getHumidityHigh().toString() + "</HumidityHigh>");
        sb.append("<HumidityLow>" + wData.getHumidityLow().toString() + "</HumidityLow>");
        sb.append("<HumidityUnit>" + wData.getHumidityUni() + "</HumidityUnit>");
        sb.append("<Pressure>" + wData.getPressure().toString() + "</Pressure>");
        sb.append("<PressureHigh>" + wData.getPressureHigh().toString() + "</PressureHigh>");
        sb.append("<PressureLow>" + wData.getPressureLow().toString() + "</PressureLow>");
        sb.append("<PressureUnit>" + wData.getPressureUnit() + "</PressureUnit>");
        sb.append("<WindSpeed>" + wData.getWindSpeed().toString() + "</WindSpeed>");
        sb.append("<WindSpeedUnit>" + wData.getWindSpeedUnit() + "</WindSpeedUnit>");
        sb.append("<WindDirection>" + wData.getWindDirection() + "</WindDirection>");
        sb.append("<RainToday>" + wData.getRainToday().toString() + "</RainToday>");
        sb.append("<RainUnit>" + wData.getRainUnit() + "</RainUnit>");
        sb.append("<RainRate>" + wData.getRainRate().toString() + "</RainRate>");
        sb.append("<RainRateMax>" + wData.getRainRateMax().toString() + "</RainRateMax>");
        sb.append("<RainRateUnit>" + wData.getRainRateUnit() + "</RainRateUnit>");
        sb.append("<TimeZone>" + wData.getTimeZone() + "</TimeZone>");
        sb.append("<Sunrise>" + wData.getSunrise().toString() + "</Sunrise>");
        sb.append("<Sunset>" + wData.getSunset().toString() + "</Sunset>");

        sb.append("</weatherData>\n");
        sb.append("</data>\n");
        return sb.toString();
    }

    public static String generateDistanceXML(String zip1, String zip2, Double lat1, Double lng1, Double lat2, Double lng2, Double udaljenost, String funkcija, String msg, String err) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<data>\n");
        sb.append("<zahtjev>\n");
        sb.append(msg);
        sb.append("</zahtjev>\n");
        sb.append("<city>");
        sb.append("<zip>" + zip1 + "</zip>");
        sb.append("<latitude>" + lat1.toString() + "</latitude>");
        sb.append("<longitude>" + lng1.toString() + "</longitude>");
        sb.append("</city>");
        sb.append("<city>");
        sb.append("<zip>" + zip2 + "</zip>");
        sb.append("<latitude>" + lat2.toString() + "</latitude>");
        sb.append("<longitude>" + lng2.toString() + "</longitude>");
        sb.append("</city>");
        sb.append("<distData>");
        sb.append("<distance>" + udaljenost.toString() + "</distance>");
        sb.append("<unit>km</unit>");
        sb.append("</distData>");
        sb.append("</data>\n");
        return sb.toString();
    }
}
