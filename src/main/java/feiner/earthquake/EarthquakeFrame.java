package feiner.earthquake;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import feiner.earthquake.json.Feature;
import feiner.earthquake.json.FeatureCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class EarthquakeFrame extends JFrame {

    private JList<String> jlist = new JList<>();
    private JRadioButton oneHrRadioButton;
    private JRadioButton oneMonthRadioButton;
    private EarthquakeService service;
    private double latitude;
    private double longitude;


    public EarthquakeFrame() {

        setTitle("EarthquakeFrame");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        final JPanel radioPanel = new JPanel();
        oneHrRadioButton = new JRadioButton("One Hour");
        oneMonthRadioButton = new JRadioButton("Thirty Days");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(oneHrRadioButton);
        buttonGroup.add(oneMonthRadioButton);
        radioPanel.add(oneHrRadioButton);
        radioPanel.add(oneMonthRadioButton);
        add(radioPanel, BorderLayout.NORTH);
        add(jlist, BorderLayout.CENTER);
        service = new EarthquakeServiceFactory().getService();
        oneHrRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchOneHour();
            }
        });

        oneMonthRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchMonth();
            }
        });
    }

    private void fetchOneHour() {

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

    private void fetchMonth() {
        Disposable disposable = service.oneMonth()
                // tells Rx to request the data on a background Thread
                .subscribeOn(Schedulers.io())
                // tells Rx to handle the response on Swing's main Thread
                .observeOn(SwingSchedulers.edt())
                //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                .subscribe(response -> handleResponse(response),
                        Throwable::printStackTrace);
    }

    private void handleResponse(FeatureCollection response) {

        String[] listData = new String[response.features.length];
        for (int i = 0; i < response.features.length; i++) {
            Feature feature = response.features[i];
            listData[i] = feature.properties.mag + " " + feature.properties.place;
        }
        jlist.setListData(listData);

        // Add a selection listener and implement google maps
        jlist.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = jlist.getSelectedIndex();
                if (index >= 0 && index < listData.length) {
                    Feature selectedFeature = response.features[index];
                    latitude = selectedFeature.geometry.coordinates[1];
                    longitude = selectedFeature.geometry.coordinates[0];
                    String url = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
    new EarthquakeFrame().setVisible(true);
}

}