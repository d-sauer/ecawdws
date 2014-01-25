/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.weather;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sheky
 */
public class weatherData {
    private String ZIP = null;
    private String CityCode = null;
    private String CurrIcon = null;
    private String CurrDesc = null;
    private Float Temperature = null;
    private Float TemperatureHigh = null;
    private Float TemperatureLow = null;
    private String TemperatureUnit = null;
    private String WindDirection = null;
    private Float WindSpeed = null;
    private String WindSpeedUnit = null;
    private Float Humidity = null;
    private String HumidityUni = null;
    private Float HumidityHigh = null;
    private Float HumidityLow = null;
    private String MoonPhaseImage = null;
    private Float Pressure = null;
    private String PressureUnit = null;
    private Float PressureHigh = null;
    private Float PressureLow = null;
    private Float RainRate = null;
    private Float RainRateMax = null;
    private String RainRateUnit = null;
    private Float RainToday = null;
    private String RainUnit = null;
    private String TimeZone = null;
    private Integer TimeZoneOffset = null;
    private Timestamp Sunrise = null;
    private Timestamp Sunset = null;
    //--alternativne informacije za grad
    private String alternative_ZIP = null;
    private String alternative_cityCode = null;


   

    public String getAlternative_ZIP() {
        return alternative_ZIP;
    }

    public void setAlternative_ZIP(String alternative_ZIP) {
        this.alternative_ZIP = alternative_ZIP;
    }

    public String getAlternative_cityCode() {
        return alternative_cityCode;
    }

    public void setAlternative_cityCode(String alternative_cityCode) {
        this.alternative_cityCode = alternative_cityCode;
    }

    public String getCityCode() {
        return CityCode;
    }

    public void setCityCode(String CityCode) {
        if (CityCode != null) {
            this.CityCode = CityCode;
        } else {
            this.CityCode = "";
        }
    }

    public String getCurrDesc() {
        return CurrDesc;
    }

    public void setCurrDesc(String CurrDesc) {
        this.CurrDesc = CurrDesc;
    }

    public String getCurrIcon() {
        return CurrIcon;
    }

    public void setCurrIcon(String CurrIcon) {
        this.CurrIcon = CurrIcon;
    }

    public Float getHumidity() {
        return Humidity;
    }

    public void setHumidity(Float Humidity) {
        this.Humidity = Humidity;
    }

    public Float getHumidityHigh() {
        return HumidityHigh;
    }

    public void setHumidityHigh(Float HumidityHigh) {
        this.HumidityHigh = HumidityHigh;
    }

    public Float getHumidityLow() {
        return HumidityLow;
    }

    public void setHumidityLow(Float HumidityLow) {
        this.HumidityLow = HumidityLow;
    }

    public String getHumidityUni() {
        return HumidityUni;
    }

    public void setHumidityUni(String HumidityUni) {
        this.HumidityUni = HumidityUni;
    }

    public String getMoonPhaseImage() {
        return MoonPhaseImage;
    }

    public void setMoonPhaseImage(String MoonPhaseImage) {
        this.MoonPhaseImage = MoonPhaseImage;
    }

    public Float getPressure() {
        return Pressure;
    }

    public void setPressure(Float Pressure) {
        this.Pressure = Pressure;
    }

    public Float getPressureHigh() {
        return PressureHigh;
    }

    public void setPressureHigh(Float PressureHigh) {
        this.PressureHigh = PressureHigh;
    }

    public Float getPressureLow() {
        return PressureLow;
    }

    public void setPressureLow(Float PressureLow) {
        this.PressureLow = PressureLow;
    }

    public String getPressureUnit() {
        return PressureUnit;
    }

    public void setPressureUnit(String PressureUnit) {
        this.PressureUnit = PressureUnit;
    }

    public Float getRainRate() {
        return RainRate;
    }

    public void setRainRate(Float RainRate) {
        this.RainRate = RainRate;
    }

    public Float getRainRateMax() {
        return RainRateMax;
    }

    public void setRainRateMax(Float RainRateMax) {
        this.RainRateMax = RainRateMax;
    }

    public String getRainRateUnit() {
        return RainRateUnit;
    }

    public void setRainRateUnit(String RainRateUnit) {
        this.RainRateUnit = RainRateUnit;
    }

    public Float getRainToday() {
        return RainToday;
    }

    public void setRainToday(Float RainToday) {
        this.RainToday = RainToday;
    }

    public String getRainUnit() {
        return RainUnit;
    }

    public void setRainUnit(String RainUnit) {
        this.RainUnit = RainUnit;
    }

    public Timestamp getSunrise() {
        return Sunrise;
    }

    public void setSunrise(Timestamp Sunrise) {
        this.Sunrise = Sunrise;
    }

    public Timestamp getSunset() {
        return Sunset;
    }

    public void setSunset(Timestamp Sunset) {
        this.Sunset = Sunset;
    }

    public Float getTemperature() {
        return Temperature;
    }

    public void setTemperature(Float Temperature) {
        this.Temperature = Temperature;
    }

    public Float getTemperatureHigh() {
        return TemperatureHigh;
    }

    public void setTemperatureHigh(Float TemperatureHigh) {
        this.TemperatureHigh = TemperatureHigh;
    }

    public Float getTemperatureLow() {
        return TemperatureLow;
    }

    public void setTemperatureLow(Float TemperatureLow) {
        this.TemperatureLow = TemperatureLow;
    }

    public String getTemperatureUnit() {
        return TemperatureUnit;
    }

    public void setTemperatureUnit(String TemperatureUnit) {
        this.TemperatureUnit = TemperatureUnit;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(String TimeZone) {
        this.TimeZone = TimeZone;
    }

    public Integer getTimeZoneOffset() {
        return TimeZoneOffset;
    }

    public void setTimeZoneOffset(Integer TimeZoneOffset) {
        this.TimeZoneOffset = TimeZoneOffset;
    }

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String WindDirection) {
        this.WindDirection = WindDirection;
    }

    public Float getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(Float WindSpeed) {
        this.WindSpeed = WindSpeed;
    }

    public String getWindSpeedUnit() {
        return WindSpeedUnit;
    }

    public void setWindSpeedUnit(String WindSpeedUnit) {
        this.WindSpeedUnit = WindSpeedUnit;
    }

    public String getZIP() {
        return ZIP;
    }

    public void setZIP(String ZIP) {
        if (ZIP != null) {
            this.ZIP = ZIP;
        } else {
            this.ZIP = "";
        }
    }

    public void makeZipAsAlternativeZIP () {
        this.alternative_ZIP = this.ZIP;
        this.ZIP= null;
        this.alternative_cityCode = this.CityCode;
        this.CityCode = null;
    }

    public String toString() {
        String data= "";
        Object obj = null;
        data+="ZIP:" + ZIP + "  >>";
        Class klasa = this.getClass();
        Field[] polja = klasa.getDeclaredFields();
        for(int i =0;i<polja.length;i++) {
            data+=polja[i].getName() + ", ";
        }
        return data;
    }


}
