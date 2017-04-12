package br.rgb.openweatherreaderapp.frag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.rgb.openweatherreaderapp.R;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.view.WeatherInfoView;

public class WeatherMapFragment extends Fragment {
    private static final String OBJECT_KEY = "object_key";

    public static WeatherMapFragment newInstance(ArrayList<WeatherInfo> data) {
        WeatherMapFragment instance = new WeatherMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(OBJECT_KEY, data);
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.view_weather_item, container, false);
        RecyclerView data = (RecyclerView) root.findViewById(R.id.view_data);
        data.setLayoutManager(new LinearLayoutManager(getActivity()));
//        WeatherInfoAdapter adapter = new WeatherInfoAdapter();
//        data.setAdapter(adapter);

        return root;
    }
}
