package br.rgb.openweatherreaderapp.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.rgb.openweatherreaderapp.WebRequestService;
import br.rgb.openweatherreaderapp.lang.Listener;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.util.GpsUtil;
import br.rgb.openweatherreaderapp.web.ServicesParser;

public class HomeActivityWorker extends Fragment {
    private final ArrayList<WeatherInfo> currentData = new ArrayList<>();
    private final PointF from = new PointF();
    private Listener<List<WeatherInfo>> onResultChangedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startLocationUpdates();
    }

    private void stopLocationUpdates() {
        if(!requesting)
            return;

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        manager.removeUpdates(listener);
    }
    LocationListener listener;
    volatile boolean requesting = false;
    private void startLocationUpdates() {
        if(requesting)
            return;

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 30);
            return;
        }

        requesting = true;

        Criteria qry = new Criteria();
        qry.setAccuracy(Criteria.ACCURACY_FINE);
        qry.setAltitudeRequired(false);
        qry.setBearingRequired(false);
        qry.setCostAllowed(true);
        qry.setPowerRequirement(Criteria.POWER_LOW);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                requestListFrom(location);
            }

            @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) {
                if (provider.equals(LocationManager.GPS_PROVIDER))
                    Toast.makeText(getActivity(), "Ative o GPS para receber atualizações", Toast.LENGTH_LONG).show();
            }
        };
        manager.requestSingleUpdate(qry, listener, null);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, listener);
    }

    private void requestListFrom(final Location loc){
        from.set((float)loc.getLatitude(), (float)loc.getLongitude());
        RectD area = getArea(loc.getLatitude(), loc.getLongitude(), 50);//kilometers
        StringBuilder request = new StringBuilder("http://api.openweathermap.org/data/2.5/box/city?");
        request.append("bbox=").append(area.lbrt()).append(",10");//10 = zoom
        request.append("&APPID=fed6cff3fd63212bf1b16bef38e79390");
        request.append("&lang=pt");
        WebRequestService.request(getActivity(), request.toString(), new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);

                if(resultCode != WebRequestService.RESULT_OK)
                    return;

                String result = resultData.getString(WebRequestService.RESULT_KEY);
                try {
                    currentData.clear();
                    currentData.addAll(ServicesParser.parseWeatherList(result));

                    Collections.sort(currentData, new Comparator<WeatherInfo>() {
                        @Override
                        public int compare(WeatherInfo o1, WeatherInfo o2) {
                            double d1 = GpsUtil.calculateDistanceInKilometer(loc.getLatitude(), loc.getLongitude(), o1.lat, o1.lon);
                            double d2 = GpsUtil.calculateDistanceInKilometer(loc.getLatitude(), loc.getLongitude(), o2.lat, o2.lon);
                            return (int) (d1 - d2);
                        }
                    });
                    onResultChangedListener.on(currentData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setOnResultChangedListener(Listener<List<WeatherInfo>> onResultChangedListener) {
        this.onResultChangedListener = onResultChangedListener;
    }

    public boolean hasResults() {
        return currentData.size() > 0;
    }

    /**
     *
     * @param lat
     * @param lng
     * @param distance in kilometers
     * @return
     */
    private static RectD getArea(double lat, double lng, double distance) {
        // From: http://stackoverflow.com/questions/238260/how-to-calculate-the-bounding-box-for-a-given-lat-lng-location
        // http://stackoverflow.com/questions/1253499/simple-calculations-for-working-with-lat-lon-km-distance
        double lat_change = distance/111.2d;
        double lon_change = Math.abs(Math.cos(lat*(Math.PI/180)));

        lat_change /= 2d;
        lon_change /= 2d;

        return new RectD(lat - lat_change, lng - lon_change, lat + lat_change, lng + lon_change);
    }

    public ArrayList<WeatherInfo> data() {
        return currentData;
    }

    public void destroy() {
        stopLocationUpdates();
    }

    public PointF from() {
        return from;
    }

    static class RectD {
        private final double x1;
        private final double y1;
        private final double x2;
        private final double y2;

        public RectD(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public String lbrt() {
            return y1+","+x2+","+y2+","+x1;
        }
    }
}
