package it.unimib.camminatori.mysherpa.model.map;



import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.repository.WeatherRepository;
import it.unimib.camminatori.mysherpa.viewmodel.Location_ViewModel;
import it.unimib.camminatori.mysherpa.viewmodel.Weather_ViewModel;

public class ExploreMap extends Map {

    private final Marker marker;
    private final Location_ViewModel location_viewModel;
    private final Weather_ViewModel weather_viewModel;

    public ExploreMap(MapView map){
        super(map);

        Drawable markerIcon = AppCompatResources.getDrawable(mapView.getContext(), R.drawable.ic_baseline_circle_24_marker_red);

        location_viewModel = new Location_ViewModel();
        weather_viewModel = new Weather_ViewModel();

        this.marker = new Marker(mapView);
        this.marker.setIcon(markerIcon);
        this.marker.setInfoWindow(null);
    }

    // Un single tap rimuove tutti i marker rossi
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        this.mapView.getOverlays().remove(marker);
        return true;
    }

    // Un long tap inserisce un marker rosso nel punto premuto, centra la mappa su quello e
    // aggiorna la location in focus in modo da comunicarlo alle altre classi
    @Override
    public boolean longPressHelper(GeoPoint p) {
        setMarkerPosition(p);
        updateLabelLocation(p);
        return true;
    }

    public void setMarkerPosition(GeoPoint p){
        mapView.getOverlays().remove(marker);

        marker.setPosition(p);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        mapView.getOverlays().add(marker);
        mapController.animateTo(marker.getPosition());
    }

    public void updateLabelLocation(GeoPoint position){
        if(position == null)
            position = myLocationOverlay.getMyLocation();
        location_viewModel.reverseGeocode(position.getLatitude(), position.getLongitude());
        weather_viewModel.getCoordinatesWeather(position.getLatitude(), position.getLongitude());
    }
}