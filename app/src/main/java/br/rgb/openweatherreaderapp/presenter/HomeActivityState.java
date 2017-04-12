package br.rgb.openweatherreaderapp.presenter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by INAC on 11/04/2017.
 */

public class HomeActivityState implements Parcelable {
    public int viewPresentationData;
    public PresentationValue viewPresentationValues;

    public static final int VIEW_LIST = 0, VIEW_MAP = 1;

    protected HomeActivityState(Parcel in) {
        viewPresentationData = in.readInt();
        viewPresentationValues = PresentationValue.values()[in.readInt()];
    }

    public static final Creator<HomeActivityState> CREATOR = new Creator<HomeActivityState>() {
        @Override
        public HomeActivityState createFromParcel(Parcel in) {
            return new HomeActivityState(in);
        }

        @Override
        public HomeActivityState[] newArray(int size) {
            return new HomeActivityState[size];
        }
    };

    public HomeActivityState() {
        
    }

    public static HomeActivityState newInitialState() {
        HomeActivityState state = new HomeActivityState();
        state.viewPresentationData = VIEW_LIST;
        state.viewPresentationValues = PresentationValue.C;
        return state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewPresentationData);
        dest.writeInt(viewPresentationValues.ordinal());
    }

    public enum PresentationValue {
        C {
            @Override
            public double convert(double temp) {
                return temp; // C is the default
            }
        }, F {
            @Override
            public double convert(double temp) {
                return temp * 1.8 + 32; // from: http://www.rapidtables.com/convert/temperature/how-celsius-to-fahrenheit.htm
            }
        };

        public abstract double convert(double maxDay);
    }
}
