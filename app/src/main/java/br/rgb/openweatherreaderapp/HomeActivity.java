package br.rgb.openweatherreaderapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import br.rgb.openweatherreaderapp.activity.HomeActivityWorker;
import br.rgb.openweatherreaderapp.frag.WeatherListFragment;
import br.rgb.openweatherreaderapp.frag.WeatherMapFragment;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityPresentationListener;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;

public class HomeActivity extends AppCompatActivity implements HomeActivityPresentationContract.View {
    private HomeActivityPresentationContract.Listener listener;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listener = new HomeActivityPresentationListener(this);
        listener.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.opt_c)
            listener.onDisplayCelsiusMeasureClicked();

        if(item.getItemId() == R.id.opt_f)
            listener.onDisplayFahrenheitMeasureClicked();

        if(item.getItemId() == R.id.opt_list)
            listener.onListOptionSelected();

        if(item.getItemId() == R.id.opt_map)
            listener.onMapOptionSelected();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        listener.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void displayMapPerspective(ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, WeatherMapFragment.newInstance(data, viewPresentationValues));
        ft.commit();
    }

    @Override
    public void displayListPerspective(ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, WeatherListFragment.newInstance(data, viewPresentationValues));
        ft.commit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listener.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listener.onSaveInstanceInstate(outState);
    }

    private static final String WORKER_TAG = "worker_tag_key";
    @Override
    public HomeActivityWorker getWorker() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(WORKER_TAG);
        if(f != null)
            return (HomeActivityWorker) f;

        f = new HomeActivityWorker();
        fm.beginTransaction().add(f, WORKER_TAG).commit();
        return (HomeActivityWorker) f;
    }

    @Override
    public Menu getMenu() {
        return mMenu;
    }

    @Override
    public void finish() {
        super.finish();

        getWorker().destroy();
    }
}
