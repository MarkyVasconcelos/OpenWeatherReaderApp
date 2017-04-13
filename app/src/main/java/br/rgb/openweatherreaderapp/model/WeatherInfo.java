package br.rgb.openweatherreaderapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import br.rgb.openweatherreaderapp.presenter.HomeActivityState;

public class WeatherInfo implements Parcelable {
    public String cityName;
    public String condition;
    public double temperature;
    public double minDay, maxDay;
    public double lon;
    public double lat;
    public String weatherIcon;

    public WeatherInfo(){}

    protected WeatherInfo(Parcel in) {
        cityName = in.readString();
        condition = in.readString();
        temperature = in.readDouble();
        minDay = in.readDouble();
        maxDay = in.readDouble();
        lat = in.readDouble();
        lon = in.readDouble();
        weatherIcon = in.readString();
    }

    public static final Creator<WeatherInfo> CREATOR = new Creator<WeatherInfo>() {
        @Override
        public WeatherInfo createFromParcel(Parcel in) {
            return new WeatherInfo(in);
        }

        @Override
        public WeatherInfo[] newArray(int size) {
            return new WeatherInfo[size];
        }
    };

    public String predictions(HomeActivityState.PresentationValue presentation){
        return "Min: " + presentation.convert(minDay) + "° Max: " + presentation.convert(maxDay)+"°";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityName);
        parcel.writeString(condition);
        parcel.writeDouble(temperature);
        parcel.writeDouble(minDay);
        parcel.writeDouble(maxDay);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
        parcel.writeString(weatherIcon);
    }
}
