/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.weather;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author sheky
 */
public class parseXML {

    public static weatherData parseXML_GetLiveWeatherByUSZipCode(String xml_data, String tagName) {
        weatherData wData = new weatherData();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            //parser.parse(new InputSource(new java.io.StringReader(xml_data)));
            Document doc = db.parse(new InputSource(new java.io.StringReader(xml_data)));

            doc.getDocumentElement().normalize();
            
            //postavljanje na element koji sadrži ostale elemente
            NodeList nodeList = doc.getElementsByTagName(tagName);


            NodeList child = null;


            //ucitavanje elemenata
            for (int index = 0; index < nodeList.getLength(); index++) {
                Node frstNode = nodeList.item(index);

                if (frstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element frstElmnt = (Element) frstNode;

                    child = frstElmnt.getElementsByTagName("ZipCode");
                    wData.setZIP(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("CurrDesc");
                    wData.setCurrDesc(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("CurrIcon");
                    wData.setCurrIcon(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("Humidity");
                    wData.setHumidity(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("HumidityHigh");
                    wData.setHumidityHigh(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("HumidityLow");
                    wData.setHumidityLow(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("HumidityUnit");
                    wData.setHumidityUni(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("MoonPhaseImage");
                    wData.setMoonPhaseImage(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("Pressure");
                    wData.setPressure(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("PressureHigh");
                    wData.setPressureHigh(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("PressureLow");
                    wData.setPressureLow(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("PressureUnit");
                    wData.setPressureUnit(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("RainRate");
                    wData.setRainRate(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("RainRateMax");
                    wData.setRainRateMax(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("RainRateUnit");
                    wData.setRainRateUnit(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("RainToday");
                    wData.setRainToday(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("RainUnit");
                    wData.setRainUnit(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("Sunrise");
                    wData.setSunrise(parseToTimeStamp(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("Sunset");
                    wData.setSunset(parseToTimeStamp(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("Temperature");
                    wData.setTemperature(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("TemperatureHigh");
                    wData.setTemperatureHigh(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("TemperatureLow");
                    wData.setTemperatureLow(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("TemperatureUnit");
                    wData.setTemperatureUnit(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("TimeZone");
                    wData.setTimeZone(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("TimeZoneOffset");
                    wData.setTimeZoneOffset(Integer.parseInt(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("WindDirection");
                    wData.setWindDirection(child.item(0).getTextContent());

                    child = frstElmnt.getElementsByTagName("WindSpeed");
                    wData.setWindSpeed(parseToFloat(child.item(0).getTextContent()));

                    child = frstElmnt.getElementsByTagName("WindSpeedUnit");
                    wData.setWindSpeedUnit(child.item(0).getTextContent());



                //ispis djece i vrijednosti
                    /*Node childNode = null;
                for(int i=0;i<frstNode.getChildNodes().getLength();i++){
                childNode = frstNode.getChildNodes().item(i);
                System.out.println("child: " + childNode.getNodeName() + " var:" + childNode.getTextContent());
                }*/
                /*
                NodeList fstNmElmntLst = frstElmnt.getElementsByTagName("City");
                Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                fstNm = fstNmElmnt.getChildNodes();
                System.out.println("First Name : "  + ((Node) fstNm.item(0)).getNodeValue());
                 */
                }

            }



        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        return wData;
    }

    public static Float parseToFloat(String var) {
        var = var.replace(" ", "");
        var = var.replace(",", "");
        if(var.contains("N/A")) {
            var = "0.0";
        }
        if(var.indexOf(".")==-1) {
            var+=".0";
        }       
        return Float.parseFloat(var);
    }

    public static Timestamp parseToTimeStamp(String var) {
        String rep = var.replace("T", " ");        
        return Timestamp.valueOf(rep);
    }
}
