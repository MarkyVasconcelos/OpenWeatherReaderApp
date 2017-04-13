package br.rgb.openweatherreaderapp.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.rgb.openweatherreaderapp.R;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;

public class WeatherInfoView extends FrameLayout {
    private final TextView cityName, condition, predictions, temperature;
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
    }

    public void setObject(WeatherInfo object) {
        cityName.setText(object.cityName);
        condition.setText(object.condition);
        predictions.setText(object.predictions(presentation));
        temperature.setText(String.valueOf(presentation.convert(object.temperature))+"°");
        Picasso.with(getContext()).load("http://openweathermap.org/img/w/" + object.weatherIcon + ".png").into(ivCondition);
    }

    <T> T fvbi(int resId){ return (T) findViewById(resId); }
}
