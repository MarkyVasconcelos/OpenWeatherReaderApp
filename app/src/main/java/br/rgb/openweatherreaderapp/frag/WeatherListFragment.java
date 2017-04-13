package br.rgb.openweatherreaderapp.frag;

import android.content.Context;
import android.graphics.PointF;
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
import br.rgb.openweatherreaderapp.presenter.HomeActivityState;
import br.rgb.openweatherreaderapp.view.WeatherInfoView;

public class WeatherListFragment extends Fragment {
    private static final String OBJECT_KEY = "object_key";
    private static final String PRESENTATION_KEY = "presentation_key";
    private static final String FROM_X_KEY = "from_x_key";
    private static final String FROM_Y_KEY = "from_y_key";

    public static WeatherListFragment newInstance(ArrayList<WeatherInfo> data, HomeActivityState.PresentationValue viewPresentationValues, PointF from) {
        WeatherListFragment instance = new WeatherListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(OBJECT_KEY, data);
        args.putFloat(FROM_X_KEY, from.x);
        args.putFloat(FROM_Y_KEY, from.y);
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
            PointF from = new PointF(getArguments().getFloat(FROM_X_KEY), getArguments().getFloat(FROM_Y_KEY));
            ((WeatherInfoView)holder.itemView).setObject(data.get(position), from);
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
