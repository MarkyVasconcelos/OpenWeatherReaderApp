package br.rgb.openweatherreaderapp;

import android.os.Bundle;
import android.view.Menu;

import java.util.ArrayList;

import br.rgb.openweatherreaderapp.activity.HomeActivityWorker;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;

public interface HomeActivityPresentationContract {
    interface View {
        void displayMapPerspective(ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues);
        void displayListPerspective(ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues);
        HomeActivityWorker getWorker();
        Menu getMenu();
    }

    interface Listener {
        void onCreate(Bundle onSavedBundleInstance);
        void onMapOptionSelected();
        void onListOptionSelected();
        void onDisplayCelsiusMeasureClicked();
        void onDisplayFahrenheitMeasureClicked();
        void onPrepareOptionsMenu(Menu menu);
        void onSaveInstanceInstate(Bundle outState);
    }
}
