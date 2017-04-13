package br.rgb.openweatherreaderapp.presenter;

import android.os.Bundle;
import android.view.Menu;

import java.util.List;

import br.rgb.openweatherreaderapp.HomeActivityPresentationContract;
import br.rgb.openweatherreaderapp.R;
import br.rgb.openweatherreaderapp.activity.HomeActivityWorker;
import br.rgb.openweatherreaderapp.lang.Listener;
import br.rgb.openweatherreaderapp.model.WeatherInfo;

public class HomeActivityPresentationListener implements HomeActivityPresentationContract.Listener {
    private static final String ACTIVITY_STATE = "activity_state_key";

    private HomeActivityState state = HomeActivityState.newInitialState();
    private final HomeActivityPresentationContract.View view;
    private final HomeActivityWorker worker;

    public HomeActivityPresentationListener(HomeActivityPresentationContract.View view){
        this.view = view;
        worker = view.getWorker();
    }

    @Override
    public void onCreate(Bundle onSavedBundleInstance) {
        if(onSavedBundleInstance != null)
            state = onSavedBundleInstance.getParcelable(ACTIVITY_STATE);

        worker.setOnResultChangedListener(new Listener<List<WeatherInfo>>(){
            @Override
            public void on(List<WeatherInfo> obj) {
                loadState();
            }
        });
        loadState();
    }

    private void loadState() {
        if(!worker.hasResults())
            return;

        if(state.viewPresentationData == HomeActivityState.VIEW_LIST)
            view.displayListPerspective(worker.from(), worker.data(), state.viewPresentationValues);
        else
            view.displayMapPerspective(worker.from(), worker.data(), state.viewPresentationValues);

        onPrepareOptionsMenu(view.getMenu());
    }

    @Override
    public void onMapOptionSelected() {
        state.viewPresentationData = HomeActivityState.VIEW_MAP;
        loadState();
    }

    @Override
    public void onListOptionSelected() {
        state.viewPresentationData = HomeActivityState.VIEW_LIST;
        loadState();
    }

    @Override
    public void onDisplayCelsiusMeasureClicked() {
        state.viewPresentationValues = HomeActivityState.PresentationValue.C;
        loadState();
    }

    @Override
    public void onDisplayFahrenheitMeasureClicked() {
        state.viewPresentationValues = HomeActivityState.PresentationValue.F;
        loadState();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(menu == null)
            return;

        boolean viewC = state.viewPresentationValues == HomeActivityState.PresentationValue.C;
        menu.findItem(R.id.opt_presentation).setIcon(viewC ? R.drawable.celcius_icon : R.drawable.fahrenheint_icon);

        boolean viewList = state.viewPresentationData == HomeActivityState.VIEW_LIST;
        menu.findItem(R.id.opt_map).setVisible(viewList);
        menu.findItem(R.id.opt_list).setVisible(!viewList);
    }

    @Override
    public void onSaveInstanceInstate(Bundle outState) {
        outState.putParcelable(ACTIVITY_STATE, state);
    }
}
