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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.rgb.openweatherreaderapp.R;
import br.rgb.openweatherreaderapp.model.WeatherInfo;
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;
import br.rgb.openweatherreaderapp.view.WeatherInfoView;

public class WeatherListFragment extends Fragment {
    private static final String OBJECT_KEY = "object_key";
    private static final String PRESENTATION_KEY = "presentation_key";

    public static WeatherListFragment newInstance(ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues) {
        WeatherListFragment instance = new WeatherListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(OBJECT_KEY, data);
        args.putInt(PRESENTATION_KEY, viewPresentationValues.ordinal());
        instance.setArguments(args);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_list, container, false);
        RecyclerView data = (RecyclerView) root.findViewById(R.id.view_data);
        data.setLayoutManager(new LinearLayoutManager(getActivity()));
        WeatherInfoAdapter adapter = new WeatherInfoAdapter(HomeActivityState.PresentationValue.values()[getArguments().getInt(PRESENTATION_KEY)]);
        data.setAdapter(adapter);
        adapter.addAll(getArguments().<WeatherInfo>getParcelableArrayList(OBJECT_KEY));

        return root;
    }

    class WeatherInfoAdapter extends RecyclerView.Adapter<WeatherInfoViewHolder> {
        private List<WeatherInfo> data = new ArrayList<>();
        private HomeActivityState.PresentationValue presentationType;

        public WeatherInfoAdapter(HomeActivityState.PresentationValue presentationType) {
            this.presentationType = presentationType;
        }

        @Override
        public WeatherInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeatherInfoViewHolder(parent.getContext(), presentationType);
        }

        @Override
        public void onBindViewHolder(WeatherInfoViewHolder holder, int position) {
            ((WeatherInfoView)holder.itemView).setObject(data.get(position));
        }

        @Override public int getItemCount() { return data.size(); }

        public void addAll(List<WeatherInfo> data) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    class WeatherInfoViewHolder extends RecyclerView.ViewHolder {
        public WeatherInfoViewHolder(Context ctx, HomeActivityState.PresentationValue presentation) {
            super(new WeatherInfoView(ctx, presentation));
            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        }
    }

}
