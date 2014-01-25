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
public class generateHTML {

    public static String generateHTMLfromResultSet(ResultSet rs, String function,String msg) {
        StringBuffer sb = new StringBuffer();
        String colName = "";
        String colVar = "";
        int i = 0;
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sb.append("<head>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        sb.append("<title>" + function + "</title>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<p>Korisnički zahtjev:<br/>");
        sb.append(msg);
        sb.append("</p><hr/>");
        try {
            java.sql.ResultSetMetaData rsMeta = rs.getMetaData();
            sb.append("&nbsp;&nbsp;&nbsp;<h1>" + function + "</h1><br/><hr/>");
            sb.append("<table border=\"1\" width=\"90%\">\n");
            sb.append("<tr  style=\"font-weight:bold;\">\n");
            for (i = 1; i < rsMeta.getColumnCount(); i++) {
                colName = rsMeta.getColumnName(i);
                sb.append("<td>");
                sb.append(colName);
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
            rs.beforeFirst();
            while (rs.next()) {
                sb.append("<tr>");
                for (i = 1; i < rsMeta.getColumnCount(); i++) {
                    colVar = rs.getString(i);
                    sb.append("<td>");
                    sb.append(colVar);
                    sb.append("</td>\n");
                }
                sb.append("</tr>");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        sb.append("</table>\n");
        sb.append("</body>\n</html>\n");
        return sb.toString();
    }

    public static String generateWeatherHTMLfromData(cityData cData, weatherData wData, String funkcija,String msg) {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sb.append("<head>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        sb.append("<title>" + funkcija + "</title>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<p>Korisnički zahtjev<br/>");
        sb.append(msg);
        sb.append("</p><hr/>");
        sb.append("<h1>" + funkcija + "</h1>");
        sb.append("<table width=\"100%\" align=\"left\" border=\"0\">" +
                "<tr>" +
                "<td width=\"17%\">Poštanski broj:</td>" +
                "<td width=\"83%\">" + cData.getZipCode() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Država:</td>" +
                "<td>" + cData.getState() + "</td>" +
                "</tr>" +
                "<tr>" + "<td colspan=\"2\">" + "<hr/>" + "</td>" + "</tr>" +
                "<tr>" +
                "<td>Vremenska prognoza</td>" +
                "<td>" + wData.getCurrDesc() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Temperatura</td>" +
                "<td>" + wData.getTemperature().toString() + " " + wData.getTemperatureUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Maks:" + wData.getTemperatureHigh().toString() + " " + wData.getTemperatureUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Min: " + wData.getTemperatureLow().toString() + " " + wData.getTemperatureUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Vlažnost zraka</td>" +
                "<td>" + wData.getHumidity().toString() + " " + wData.getHumidityUni() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Maks: " + wData.getHumidityHigh().toString() + " " + wData.getHumidityUni() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Min: " + wData.getHumidityLow().toString() + " " + wData.getHumidityUni() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Tlak zraka</td>" +
                "<td>" + wData.getPressure().toString() + " " + wData.getPressureUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Maks: " + wData.getPressureHigh().toString() + " " + wData.getPressureUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Min: " + wData.getPressureLow().toString() + " " + wData.getPressureUnit() + "</td>" +
                "</tr>" +
                "<tr><td colspan=\"2\"><hr/></td></tr>" +
                "<tr>" +
                "<td>Brzina vjetra</td>" +
                "<td>" + wData.getWindSpeed().toString() + " " + wData.getWindSpeedUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Smjer vjetra</td>" +
                "<td>" + wData.getWindDirection() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Količina kiše</td>" +
                "<td>" + wData.getRainToday().toString() + " " + wData.getRainUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Prosječno: " + wData.getRainRate().toString() + " " + wData.getRainRateUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>Maks: " + wData.getRainRateMax().toString() + " " + wData.getRainRateUnit() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Geografske koordinate</td>" +
                "<td>širina: " + cData.getLatitude() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td></td>" +
                "<td>dužina: " + cData.getLongitude() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Vremenska zona</td>" +
                "<td>" + wData.getTimeZone() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Izlazak sunca</td>" +
                "<td>" + wData.getSunrise().toString() + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td>Zalazak sunca</td>" +
                "<td>" + wData.getSunset().toString() + "</td>" +
                "</tr>" +
                "</table>");
        sb.append("</body>\n");
        sb.append("</html>");
        return sb.toString();
    }


    public static String generateDistanceHTML(String zip1, String zip2, Double lat1, Double lng1, Double lat2, Double lng2,Double udaljenost,String funkcija,String msg,String err ) {
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        sb.append("<head>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        sb.append("<title>" + funkcija + "</title>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<p>Korisnički zahtjev<br/>");
        sb.append(msg);
        sb.append("</p>");
        sb.append("<p><hr/>");
        sb.append(err);
        sb.append("</p>");
        sb.append("<h1>" + funkcija + "</h1>");
        sb.append("<table>");
        sb.append("<tr><td>ZIP</td><td>Geografska širina(lat)</td><td>Geografska dužina (lng)</td>");
        sb.append("<tr><td>" + zip1 + "</td><td>" + lat1.toString() + "</td><td>" + lng1.toString() + "</td>");
        sb.append("<tr><td>" + zip2 + "</td><td>" + lat2.toString() + "</td><td>" + lng2.toString() + "</td>");
        sb.append("<tr><td>Udaljenost</td><td colspan=\"2\">" + udaljenost.toString() + " km</td>");
        sb.append("</table>");
        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }
}
