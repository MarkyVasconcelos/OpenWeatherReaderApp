package br.rgb.openweatherreaderapp.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;

public class WeatherMapFragment extends SupportMapFragment {
    private static final String OBJECT_KEY = "object_key";
    private static final String PRESENTATION_KEY = "presentation_key";

    //GOOGLE_MAPS_API_KEY=AIzaSyCJ4vpTB9zl5Ae0YZIzXRklfL5Gn-e8PN4
    public static WeatherMapFragment newInstance(ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues) {
        WeatherMapFragment instance = new WeatherMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(OBJECT_KEY, data);
        args.putInt(PRESENTATION_KEY, viewPresentationValues.ordinal());
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                populate(googleMap);
                googleMap.getUiSettings().setAllGesturesEnabled(false);
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    Marker currentShown;
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.equals(currentShown)) {
                            marker.hideInfoWindow();
                            currentShown = null;
                        } else {
                            marker.showInfoWindow();
                            currentShown = marker;
                        }
                        return true;
                    }
                });
            }
        });

        return root;
    }

    private void populate(GoogleMap googleMap) {
        WeatherInfo center = getArguments().<WeatherInfo>getParcelableArrayList(OBJECT_KEY).get(0);
        LatLng pos = new LatLng(center.lat, center.lon);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos, 9.8f, 0 , 0)));

        HomeActivityState.PresentationValue presenter = HomeActivityState.PresentationValue.values()[getArguments().getInt(PRESENTATION_KEY)];
        for(WeatherInfo o : getArguments().<WeatherInfo>getParcelableArrayList(OBJECT_KEY)) {
            MarkerOptions marker = new MarkerOptions();
            marker.title(presenter.convert(o.temperature) +"°");
            marker.position(new LatLng(o.lat, o.lon));
            googleMap.addMarker(marker);
        }
    }
}
