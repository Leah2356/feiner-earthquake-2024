package feiner.earthquake;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import feiner.earthquake.json.FeatureCollection;

public interface EarthquakeService {

    @GET("/earthquakes/feed/v1.0/summary/1.0_hour.geojson")
    Single<FeatureCollection> oneHour();

    @GET("/earthquakes/feed/v1.0/summary/significant_month.geojson")
    Single<FeatureCollection> significantMonth();

}