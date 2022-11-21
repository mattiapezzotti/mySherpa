package it.unimib.camminatori.mysherpa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.StorageUtils;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity{
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    public MainActivity(){
        super(R.layout.activity_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){

            MapFragment map = new MapFragment();
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, map, null)
                    .commit();

            String[] permissions = new String[3];
            permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            permissions[2] = Manifest.permission.ACCESS_FINE_LOCATION;
            requestPermissionsIfNecessary(permissions);

            //load/initialize the osmdroid configuration
            Context context = getApplicationContext();
            Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
            Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
            //if(StorageUtils.isWritable(Configuration.getInstance().getOsmdroidTileCache()))
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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