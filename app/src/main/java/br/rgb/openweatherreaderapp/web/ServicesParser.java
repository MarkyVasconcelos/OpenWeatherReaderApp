package br.rgb.openweatherreaderapp.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.rgb.openweatherreaderapp.model.WeatherInfo;

public class ServicesParser {
    public static List<WeatherInfo> parseWeatherList(String request) throws JSONException {
        final List<WeatherInfo> result = new ArrayList<>();
        JSONObject data = new JSONObject(request);
        JSONArray list = data.getJSONArray("list");
        for(int i =0; i < list.length(); i++)
            result.add(parseWeather(list.getJSONObject(i)));
        return result;
    }

    private static WeatherInfo parseWeather(JSONObject obj) throws JSONException {
        WeatherInfo result = new WeatherInfo();
        result.cityName = obj.getString("name");
        result.condition = obj.getJSONArray("weather").getJSONObject(0).getString("main");

        JSONObject coords = obj.getJSONObject("coord");
        result.lat = coords.getDouble("Lat");
        result.lon = coords.getDouble("Lon");

        JSONObject main = obj.getJSONObject("main");
        result.maxDay = main.getDouble("temp_max");
        result.minDay = main.getDouble("temp_min");
        result.temperature = main.getDouble("temp");

        return result;
    }
}
