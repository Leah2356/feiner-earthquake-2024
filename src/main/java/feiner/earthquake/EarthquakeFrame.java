package feiner.earthquake;

import feiner.earthquake.json.FeatureCollection;
import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.swing.*;
import java.awt.*;

public class EarthquakeFrame extends JFrame {
    private JList<String> jlist = new JList<>();
    public EarthquakeFrame(){
        setSize(300, 600);
        setTitle("Earthquake Frame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(jlist, BorderLayout.CENTER);

        EarthquakeService service = new EarthquakeServiceFactory().getService();

        Disposable disposable = service.oneHour()
                // tells Rx to request the data on a background Thread
                .subscribeOn(Schedulers.io())
                // tells Rx to handle the response on Swing's main Thread
                .observeOn(SwingSchedulers.edt())
                //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                .subscribe(
                        (response) -> handleResponse(response),
                        Throwable::printStackTrace);
    }
    private void handleResponse(FeatureCollection response) {
        String[] listData = Arrays.stream(response.features)

            }
        }

    }

    public static void main(String[] args) {

    }
}
