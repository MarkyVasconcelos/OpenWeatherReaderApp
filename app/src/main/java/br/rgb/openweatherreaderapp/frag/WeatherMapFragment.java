package br.rgb.openweatherreaderapp.frag;

import android.graphics.PointF;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;
import br.rgb.openweatherreaderapp.view.WeatherInfoView;

public class WeatherMapFragment extends SupportMapFragment {
    private static final String OBJECT_KEY = "object_key";
    private static final String FROM_X_KEY = "from_x_key";
    private static final String FROM_Y_KEY = "from_y_key";
    private static final String PRESENTATION_KEY = "presentation_key";

    //GOOGLE_MAPS_API_KEY=AIzaSyCJ4vpTB9zl5Ae0YZIzXRklfL5Gn-e8PN4
    public static WeatherMapFragment newInstance(PointF userPos, ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues) {
        WeatherMapFragment instance = new WeatherMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(OBJECT_KEY, data);
        args.putInt(PRESENTATION_KEY, viewPresentationValues.ordinal());
        args.putFloat(FROM_X_KEY, userPos.x);
        args.putFloat(FROM_Y_KEY, userPos.y);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setupMap(googleMap);
                populate(googleMap);
            }
        });

        return root;
    }

    private void setupMap(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                HomeActivityState.PresentationValue presenter = HomeActivityState.PresentationValue.values()[getArguments().getInt(PRESENTATION_KEY)];
                PointF from = new PointF(getArguments().getFloat(FROM_X_KEY), getArguments().getFloat(FROM_Y_KEY));
                WeatherInfoView view = new WeatherInfoView(getContext(), presenter);
                view.setObject(population.get(marker), from);
                return view;
            }
        });
    }

    private void populate(GoogleMap googleMap) {
        LatLng pos = new LatLng(getArguments().getFloat(FROM_X_KEY), getArguments().getFloat(FROM_Y_KEY));
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos, 9.5f, 0 , 0)));// TODO: zoom 0 at world in 256, each 1 increases size by *2
        CircleOptions middle = new CircleOptions();
        middle.center(pos);
        googleMap.addCircle(middle);

        HomeActivityState.PresentationValue presenter = HomeActivityState.PresentationValue.values()[getArguments().getInt(PRESENTATION_KEY)];
        for(WeatherInfo o : getArguments().<WeatherInfo>getParcelableArrayList(OBJECT_KEY)) {
            MarkerOptions marker = new MarkerOptions();
            marker.title(presenter.convert(o.temperature) +"°");
            marker.position(new LatLng(o.lat, o.lon));
            population.put(googleMap.addMarker(marker), o);
        }
    }

    private Map<Marker, WeatherInfo> population = new HashMap<>();
}
