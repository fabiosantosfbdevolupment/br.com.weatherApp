package ceducarneiro.com.br.weatherapp.ws;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;

import java.net.URL;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class OpenWeatherWS {

    private static final String URL_BASE = "http://api.openweathermap.org/data/2.5/weather?units=%s&";
    private static final String URL_BASE_LIST = "http://api.openweathermap.org/data/2.5/group?units=%s&";

    private static final String URL_BY_LATLNG = URL_BASE + "lat=%.16f&lon=%.16f";
    private static final String URL_BY_PLACE = URL_BASE + "q=%s,%s";
    private static final String URL_BY_IDS = URL_BASE_LIST + "id=%s";

    private static OpenWeatherWS instance;

    private OpenWeatherWS() {
        Log.d("OpenWeatherWS", "OpenWeatherWS()");
    }

    public static synchronized OpenWeatherWS getInstance() {
        Log.d("OpenWeatherWS", "getInstance()");

        if (instance == null) {
            Log.d("OpenWeatherWS", "instance == null");

            instance = new OpenWeatherWS();
        }

        return instance;
    }

    public OpenWeather getWeatherByLatLng(String units, double latitude, double longitude) {
        Log.d("OpenWeatherWS", "getWeatherByLatLng(String units, double latitude, double longitude)");

        ObjectMapper mapper = new ObjectMapper();
        OpenWeather weather = null;

        try {
            Log.d("OpenWeatherWS", "getWeatherByLatLng(String units, double latitude, double longitude){try}");

            String uri = String.format(Locale.US, URL_BY_LATLNG, units, latitude, longitude);
            URL url = new URL(uri);

            weather = mapper.readValue(url, OpenWeather.class);
            if (weather != null && weather.retCode != 200) weather = null;
        } catch (Exception ignored) {
            Log.d("OpenWeatherWS", "getWeatherByLatLng(String units, double latitude, double longitude){catch}");

            ignored.printStackTrace();
        }

        return weather;
    }

    public OpenWeather getWeatherByPlace(String units, String place, String country) {
        Log.d("OpenWeatherWS", "getWeatherByPlace(String units, String place, String country)");

        ObjectMapper mapper = new ObjectMapper();
        OpenWeather weather = null;

        try {
            Log.d("OpenWeatherWS", "getWeatherByPlace(String units, String place, String country){try}");

            String uri = String.format(Locale.US, URL_BY_PLACE, units,
                    normalize(place), normalize(country));
            URL url = new URL(uri);

            weather = mapper.readValue(url, OpenWeather.class);
            if (weather != null && weather.retCode != 200) weather = null;
        } catch (Exception ignored) {
            Log.d("OpenWeatherWS", "getWeatherByPlace(String units, String place, String country){catch}");

            ignored.printStackTrace();
        }

        return weather;
    }

    public OpenWeatherList getWeatherByIds(String units, List<Long> ids) {
        Log.d("OpenWeatherWS", "getWeatherByIds(String units, List<Long> ids)");

        ObjectMapper mapper = new ObjectMapper();
        OpenWeatherList weathers = null;

        try {
            Log.d("OpenWeatherWS", "getWeatherByIds(String units, List<Long> ids){try}");

            String idsList = "";

            for (Long id : ids) {
                Log.d("OpenWeatherWS", "for (Long id : ids)");

                idsList = String.format("%s%s%s", idsList, idsList.length() > 0 ? "," : "",
                        String.valueOf(id));
            }

            String uri = String.format(Locale.US, URL_BY_IDS, units, idsList);
            URL url = new URL(uri);

            Log.d("URL", url.getPath());

            weathers = mapper.readValue(url, OpenWeatherList.class);
        } catch (Exception ignored) {
            Log.d("OpenWeatherWS", "getWeatherByIds(String units, List<Long> ids){catch}");

            ignored.printStackTrace();
        }

        return weathers;
    }

    private String normalize(String str) {
        try {
            Log.d("OpenWeatherWS", "normalize(String str){try}");

            str = Normalizer.normalize(str, Normalizer.Form.NFD);
            str = str.replaceAll("[^\\p{ASCII}]", "");
            str = URLEncoder.encode(str, "UTF-8");
        } catch(Exception ignored) {
            Log.d("OpenWeatherWS", "normalize(String str){catch}");

            /* Empty */
        }

        return str;
    }

}
