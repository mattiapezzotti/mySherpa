package it.unimib.camminatori.mysherpa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;

public class ExploreActivity extends AppCompatActivity{
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map;
    private MyLocationNewOverlay myLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] permissions = new String[3];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
        permissions[2] = Manifest.permission.ACCESS_FINE_LOCATION;
        requestPermissionsIfNecessary(permissions);

        //load/initialize the osmdroid configuration
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        //inflate and create the map
        setContentView(R.layout.activity_main);

        this.map = (MapView) findViewById(R.id.map);
        this.map.setTileSource(TileSourceFactory.WIKIMEDIA);
        this.map.setMultiTouchControls(true);
        this.map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        RotationGestureOverlay rotationController = new RotationGestureOverlay(map);
        rotationController.setEnabled(true);
        map.getOverlays().add(rotationController);

        myLocation = new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
        myLocation.enableMyLocation();
        map.getOverlays().add(myLocation);

        IMapController mapController = this.map.getController();
        mapController.setZoom(17.0);
        map.setMinZoomLevel(6.5);
        //mapController.setCenter(location.getMyLocation());

        GeoPoint provaglio = new GeoPoint(45.6374,10.0430);
        mapController.setCenter(provaglio);

    }

    @Override
    public void onResume() {
        super.onResume();
        this.map.onResume();
        this.myLocation.enableMyLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.map.onPause();
        this.myLocation.disableMyLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest =
                new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));

        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions
                    (this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }

        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this, permissionsToRequest.toArray(
                            new String[0]), REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}