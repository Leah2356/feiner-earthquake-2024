package feiner.earthquake;

import org.junit.jupiter.api.Test;
import feiner.earthquake.json.FeatureCollection;
import feiner.earthquake.json.Properties;

import static org.junit.jupiter.api.Assertions.*;

class EarthquakeServiceTest {

    @Test
    void oneHour() {
        // given
        EarthquakeService service = new EarthquakeServiceFactory().getService();

        // when
        FeatureCollection collection = service.oneHour().blockingGet();

        // then
        Properties properties = collection.features[0].properties;
        assertNotNull(properties.place);
        assertNotEquals(0, properties.mag);
        assertNotEquals(0, properties.time);
    }

    @Test
    void significantMonth() {
        // given
        EarthquakeService service = new EarthquakeServiceFactory().getService();

        // when
        FeatureCollection collection = service.significantMonth().blockingGet();

        // then
        Properties properties = collection.features[0].properties;
        assertNotNull(properties.place);
        assertNotEquals(0, properties.mag);
        assertNotEquals(0, properties.time);
    }
}