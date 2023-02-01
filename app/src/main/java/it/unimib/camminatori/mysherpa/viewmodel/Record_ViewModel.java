package it.unimib.camminatori.mysherpa.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Record_ViewModel extends ViewModel {
    final private String TAG = "RecordViewModel";

    private ModelInfo modelInfo;
    private RecordInfo localRecordInfo;
    private MutableLiveData<RecordInfo> recordInfo;
    private Context context;
    private MyLocationListener gpsLocationListener;
    private LocationManager locationManager;
    private static ArrayList<SaveRecordInfo> favList;
    private boolean favAdded;


    public void getRecordInfo(Context context, ArrayList<SaveRecordInfo> favList) {
        getRecordInfo(context);

        if ((favList != null) && !favAdded)
            Record_ViewModel.favList.addAll(favList);

        favAdded = true;
    }

    public MutableLiveData<RecordInfo> getRecordInfo(Context context) {
        if (recordInfo == null)
            recordInfo = new MutableLiveData<RecordInfo>();

        if (modelInfo == null)
            modelInfo = new ModelInfo();

        if (localRecordInfo == null)
            localRecordInfo = new RecordInfo();

        if (this.context == null)
            this.context = context;

        if (Record_ViewModel.favList == null)
            Record_ViewModel.favList = new ArrayList<>();

        return recordInfo;
    }

    public void setRecordStarted(boolean recordStarted) {
        this.modelInfo.recordStarted = recordStarted;
    }

    public void setRecordPaused(boolean recordPaused) {
        this.modelInfo.recordPaused = recordPaused;
    }

    public boolean isRecordPaused() {
        return modelInfo.recordPaused;
    }

    public boolean isRecordStarted() {
        return modelInfo.recordStarted;
    }

    public ArrayList<SaveRecordInfo> getFavList() {
        return favList;
    }

    public boolean buttonPlayClicked() {
        if (!modelInfo.recordStarted) {
            if (!runLocationListener())
                return false;

            runRecordTimer();

            modelInfo.startRecordTime = SystemClock.elapsedRealtime();
            modelInfo.recordStarted = true;
            modelInfo.recordPaused = false;
        } else {
            modelInfo.recordPaused = !modelInfo.recordPaused;
            if (modelInfo.recordPaused) {
                modelInfo.pauseRecordTime = SystemClock.elapsedRealtime();
            } else {
                modelInfo.startRecordTime += SystemClock.elapsedRealtime() - modelInfo.pauseRecordTime;
            }
        }

        return true;
    }

    public void buttonStopClicked() {
        modelInfo.recordStarted = false;
        modelInfo.recordPaused = true;

        if (locationManager != null)
            locationManager.removeUpdates(gpsLocationListener);

        locationManager = null;
        gpsLocationListener = null;
    }

    public void buttonSaveClicked(String localityName) {
        SaveRecordInfo saveRecordInfo = new SaveRecordInfo();
        saveRecordInfo.dateString = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        saveRecordInfo.locationString = localityName;
        saveRecordInfo.millisecondsTime = localRecordInfo.recordMilliseconds;
        saveRecordInfo.metersDistance = localRecordInfo.recordMeters;


        favList.add(saveRecordInfo);
    }

    public static ArrayList<SaveRecordInfo> removeRecord(int position) {
        if (position >= 0)
            favList.remove(position);

        return favList;
    }


    private void runRecordTimer() {
        final Handler timerHandler = new Handler();

        timerHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "recordTimer running");

                if (modelInfo.recordStarted) {
                    long milliseconds = SystemClock.elapsedRealtime() - modelInfo.startRecordTime;

                    if (modelInfo.recordPaused) {
                        milliseconds -= (SystemClock.elapsedRealtime() - modelInfo.pauseRecordTime);
                    } else {
                        float metersCount = (float)gpsLocationListener.getMetersCount();

                        localRecordInfo.recordMilliseconds = milliseconds;
                        localRecordInfo.recordMeters = (long)metersCount;
                        localRecordInfo.currElevation = (long)gpsLocationListener.getCurrentElevation();

                        localRecordInfo.updateMeters = metersCount - localRecordInfo.lastUpdateMeters;

                        localRecordInfo.timerText = formatTimerString(localRecordInfo.recordMilliseconds);
                        localRecordInfo.metersText = formatMetersString(localRecordInfo.recordMeters);
                        localRecordInfo.elevationText = formatMetersString(localRecordInfo.currElevation);

                        recordInfo.postValue(localRecordInfo);

                        localRecordInfo.lastUpdateMeters = metersCount;
                    }

                    timerHandler.postDelayed(this, 500);
                } else {
                    localRecordInfo.recordMilliseconds = 0;
                    localRecordInfo.recordMeters = 0;
                    localRecordInfo.timerText = null;
                    localRecordInfo.metersText = null;
                    localRecordInfo.elevationText = null;

                    recordInfo.postValue(localRecordInfo);
                }

            }
        });
    }

    private boolean runLocationListener() {
        if (gpsLocationListener != null)
            return true;

        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsLocationListener = new MyLocationListener(modelInfo);
            Log.i(TAG, "Location Listener: " + gpsLocationListener);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                locationManager = null;
                return false;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, gpsLocationListener);
        } else {
            locationManager = null;
            return false;
        }

        return true;
    }


    private String formatTimerString(long millisecond) {
        String timerString;
        long seconds = millisecond / 1000;
        long minutes = (seconds % 3600) / 60;
        long hours =  seconds / 3600;

        timerString = (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + (seconds % 60);

        return timerString;
    }


    private String formatMetersString(long meters) {
        String meterString;

        if (meters >= 1000) {
            meterString = String.format("%s %s", (float)meters / 1000.0, "Km");
        } else {
            meterString = String.format("%s %s", meters, "m");
        }

        return meterString;
    }

    public static class RecordInfo {
        public String metersText;
        public String timerText;
        public String elevationText;
        public long recordMilliseconds;
        public long recordMeters;
        public long currElevation;
        public float updateMeters;
        private float lastUpdateMeters;

    }

    private static class ModelInfo {
        public boolean recordStarted;
        public boolean recordPaused;
        public long startRecordTime;
        public long pauseRecordTime;
    }

    public static class SaveRecordInfo {
        public String dateString;
        public String locationString;
        public long millisecondsTime;
        public long metersDistance;
    }

    private static class MyLocationListener implements LocationListener {
        private final String TAG = "MyLocationListener";

        private Location previousLocation;
        private double metersCount;
        private double currElevation;
        private final ModelInfo modelInfo;

        MyLocationListener(ModelInfo modelInfo) {
            super();

            this.modelInfo = modelInfo;
            this.resetInfo();
        }

        @Override
        public void onLocationChanged(Location location) {
            double latMeters;
            double longMeters;
            double longRad;

            if (!modelInfo.recordStarted || modelInfo.recordPaused)
                return;

            if (location == null)
                return;

            if (previousLocation != null) {
                longRad = 111.32 * Math.cos(Math.abs(location.getLatitude() - previousLocation.getLatitude()));
                latMeters = 111.32 * Math.abs(location.getLatitude() - previousLocation.getLatitude());
                longMeters = longRad * Math.abs(location.getLongitude() - previousLocation.getLongitude());

                currElevation = location.getAltitude();

                metersCount += Math.sqrt(latMeters * latMeters + longMeters * longMeters) * 1000;
                metersCount += Math.abs(currElevation - previousLocation.getAltitude());

                Log.i(TAG, "Meters: " + metersCount);
            }

            previousLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled");
        }

        @Override
        public void onFlushComplete(int requestCode) {
            Log.i(TAG, "onFlushComplete");
        }

        public double getMetersCount() {
            return metersCount;
        }

        public double getCurrentElevation() {
            return currElevation;
        }

        private void resetInfo() {
            this.metersCount = 0;
            this.previousLocation = null;
        }
    }
}
