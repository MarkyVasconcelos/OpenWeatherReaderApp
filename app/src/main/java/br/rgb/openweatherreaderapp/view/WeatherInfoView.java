package br.rgb.openweatherreaderapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import br.rgb.openweatherreaderapp.R;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;

public class WeatherInfoView extends FrameLayout {
    private final TextView cityName, condition, predictions;
    private HomeActivityState.PresentationValue presentation;

    public WeatherInfoView(Context context, HomeActivityState.PresentationValue presentation) {
        super(context);
        this.presentation = presentation;

        LayoutInflater.from(context).inflate(R.layout.view_weather_item, this, true);

        cityName = fvbi(R.id.tv_city);
        condition = fvbi(R.id.tv_condition);
        predictions = fvbi(R.id.tv_prediction);
    }

    public void setObject(WeatherInfo object) {
        cityName.setText(object.cityName);
        condition.setText(object.conditions(presentation));
        predictions.setText(object.predictions(presentation));
    }

    <T> T fvbi(int resId){ return (T) findViewById(resId); }
}
