package br.rgb.openweatherreaderapp.view;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.rgb.openweatherreaderapp.R;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;
import br.rgb.openweatherreaderapp.util.GpsUtil;

public class WeatherInfoView extends FrameLayout {
    private final TextView cityName, condition, predictions, temperature, from;
    private final ImageView ivCondition;
    private HomeActivityState.PresentationValue presentation;

    public WeatherInfoView(Context context, HomeActivityState.PresentationValue presentation) {
        super(context);
        this.presentation = presentation;

        LayoutInflater.from(context).inflate(R.layout.view_weather_item, this, true);

        cityName = fvbi(R.id.tv_city);
        condition = fvbi(R.id.tv_condition);
        predictions = fvbi(R.id.tv_prediction);
        temperature = fvbi(R.id.tv_temperature);
        ivCondition = fvbi(R.id.iv_condition);
        from = fvbi(R.id.tv_from);
    }

    public void setObject(WeatherInfo object, PointF from) {
        cityName.setText(object.cityName);
        condition.setText(object.condition);
        predictions.setText(object.predictions(presentation));
        temperature.setText(String.valueOf(presentation.convert(object.temperature))+"°");
        this.from.setText("(" + (int) GpsUtil.calculateDistanceInKilometer(from.x, from.y, object.lat, object.lon) + "km de distancia)");
        Picasso.with(getContext()).load("http://openweathermap.org/img/w/" + object.weatherIcon + ".png").into(ivCondition);
    }

    <T> T fvbi(int resId){ return (T) findViewById(resId); }
}
