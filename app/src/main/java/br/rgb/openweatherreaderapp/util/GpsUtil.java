package br.rgb.openweatherreaderapp.util;

public class GpsUtil {
    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

    /**
     * From: http://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
     * @param userLat
     * @param userLng
     * @param venueLat
     * @param venueLng
     * @return
     */
    public static double calculateDistanceInKilometer(double userLat, double userLng, double venueLat, double venueLng) {
        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return AVERAGE_RADIUS_OF_EARTH_KM * c;
    }
}
