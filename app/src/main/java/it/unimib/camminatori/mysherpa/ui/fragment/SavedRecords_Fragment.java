package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.util.GeoPoint;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.ui.recyclerview.FavRecordsRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.utils.SaveLocation;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;

public class SavedRecords_Fragment extends Fragment {
    final static public String FAVOURITES_RECORDS_SHAREDPREFS = "FAVOURITES_RECORDS";
    final static public String FAVOURITES_RECORDS = "FAVOURITE_RECORDS_LIST";

    static final private String TAG = "SavedRecordsFragment";
    protected RecyclerView favRecordsView;

    private FloatingActionButton exploreButton;

    // View Model
    private Record_ViewModel recordViewModel;

    public SavedRecords_Fragment() {
        // Required empty public constructor
    }

    public static SavedRecords_Fragment newInstance(String param1, String param2) {
        return new SavedRecords_Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_records, container, false);

        favRecordsView = v.findViewById(R.id.fav_records_recycler_view);
        LinearLayoutManager favLinearLayout = new LinearLayoutManager(getContext());
        favRecordsView.setLayoutManager(favLinearLayout);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recordViewModel = new ViewModelProvider(requireActivity()).get(Record_ViewModel.class);

        recordViewModel.getRecordInfo(requireContext());

        ArrayList<Record_ViewModel.SaveRecordInfo> favRecords = recordViewModel.getFavList();

        final FavRecordsRecyclerViewAdapter favRecordsRecyclerViewAdapter = new FavRecordsRecyclerViewAdapter(favRecords);

        favRecordsRecyclerViewAdapter.setOnItemsChangedListener(size -> {
            TextView noBookmarksTextView = view.findViewById(R.id.no_bookmarks_text_view);

            if (size == 0)
                noBookmarksTextView.setVisibility(View.VISIBLE);
            else
                noBookmarksTextView.setVisibility(View.GONE);
        });

        favRecordsRecyclerViewAdapter.setOnExploreClickedListener(index -> {
            //System.out.println(recordViewModel.getFavList().get(index).path);

            ArrayList<SaveLocation> registeredWaypoints = recordViewModel.getFavList().get(index).path;
            ArrayList<GeoPoint> waypointsToDraw = new ArrayList<>();
            for (SaveLocation l : registeredWaypoints) {
                waypointsToDraw.add(new GeoPoint(l.getLatitude(), l.getLongitude()));
            }

            System.out.println(recordViewModel.getFavList().get(index).path);

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("waypoints", waypointsToDraw);
            Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).popBackStack();
            Navigation.findNavController(this.getActivity().findViewById(R.id.nav_host_fragment)).navigate(R.id.fragment_explore, bundle);
        });

        favRecordsRecyclerViewAdapter.setOnShareClickedListener(index -> {
            try {
                String filename = recordViewModel.getFavList().get(index).fileUUID + ".gpx";

                File gpxDir = new File(requireContext().getFilesDir(), "gpx");
                File xmlFile = new File(gpxDir, filename);
                if (!xmlFile.exists()) {
                    Log.w(TAG, filename + " does not exists");
                    return;
                }

                Uri shareUri = FileProvider.getUriForFile(requireContext(), "it.unimib.camminatori.mysherpa", xmlFile);

                Intent shareIntent = new Intent(Intent.ACTION_VIEW);
                shareIntent.setDataAndType(shareUri, "application/gpx");
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (shareIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(shareIntent);
                } else {
                    Snackbar.make(requireActivity().findViewById(R.id.container_main_activity), R.string.no_gpx_intent_fount, Snackbar.LENGTH_LONG)
                            .setAction(R.string.ok, v -> {
                            })
                            .show();
                }

            } catch (Exception e) {
                Log.w(TAG, e);
            }
        });

        favRecordsRecyclerViewAdapter.setOnDeleteClickedListener(index -> {
            String filename = recordViewModel.getFavList().get(index).fileUUID + ".gpx";

            File gpxDir = new File(requireContext().getFilesDir(), "gpx");
            File xmlFile = new File(gpxDir, filename);

            if (!xmlFile.delete()) {
                Log.e(TAG, "Failed to delete file " + filename);
            }
        });

        favRecordsView.setAdapter(favRecordsRecyclerViewAdapter);

        EditText favSearch = view.findViewById(R.id.fav_text_search);

        favSearch.addTextChangedListener(new TextWatcher() {
            final private String TAG = "favSearch TextWatcher";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                favRecordsRecyclerViewAdapter.filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Log.d(TAG, "onSaveInstanceState");
        saveFavRecords(requireContext(), recordViewModel);
    }


    public static void saveFavRecords(Context context, Record_ViewModel recordViewModel) {
        String TAG = "saveFavRecords";
        SharedPreferences favRecordsPreferences = context.getSharedPreferences(SavedRecords_Fragment.FAVOURITES_RECORDS_SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = favRecordsPreferences.edit();

        ArrayList<Record_ViewModel.SaveRecordInfo> favList = recordViewModel.getFavList();

        for (int i = 0; i < favList.size(); i++) {
            Record_ViewModel.SaveRecordInfo info = favList.get(i);

            File gpxDir = new File(context.getFilesDir(), "gpx");
            if (!gpxDir.exists()) {
                if (!gpxDir.mkdir()) {
                    Log.d(TAG, "Failed to create GPX directory");
                    return;
                } else {
                    Log.d(TAG, "Created GPX directory");
                }
            }

            File xmlFile = new File(gpxDir, info.fileUUID + ".gpx");
            boolean save = false;
            try {
                save = xmlFile.createNewFile();
            } catch (Exception e) {
                Log.w(TAG, "createNewFile() for gpx failed", e);
            }

            if (save) {
                Log.d(TAG, "Saving " + info.fileUUID + ".gpx");
                try {
                    String gpxXml = buildGpxXml(info.path, info.locationString);
                    FileOutputStream xmlStream = new FileOutputStream(xmlFile);
                    xmlStream.write(gpxXml.getBytes());

                    Log.d(TAG, "Saving " + favList.get(0));
                    Log.d(TAG, gpxXml);
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
            }
        }

        //TODO: FIX SALVATAGGIO LOCATIONS
        Type listOfFav = new TypeToken<ArrayList<Record_ViewModel.SaveRecordInfo>>() {
        }.getType();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = gson.toJson(favList, listOfFav);

        Log.d(TAG, "Saving " + json);

        editor.putString(SavedRecords_Fragment.FAVOURITES_RECORDS, json);
        editor.apply();
    }

    public static ArrayList<Record_ViewModel.SaveRecordInfo> getSavedRecords(Context context) {
        ArrayList<Record_ViewModel.SaveRecordInfo> savedRecords;
        SharedPreferences favRecordsPreferences = context.getSharedPreferences(SavedRecords_Fragment.FAVOURITES_RECORDS_SHAREDPREFS, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String saveJson = favRecordsPreferences.getString(SavedRecords_Fragment.FAVOURITES_RECORDS, "");
        if (saveJson.equals("")) {
            savedRecords = new ArrayList<>();
        } else {
            savedRecords = gson.fromJson(saveJson, new TypeToken<ArrayList<Record_ViewModel.SaveRecordInfo>>() {
            }.getType());
        }

        if (savedRecords.size() > 0)
            Log.d(TAG, "Saved path: " + savedRecords.get(0).path);

        return savedRecords;
    }

    static private String buildGpxXml(ArrayList<SaveLocation> path, String name) {
        ByteArrayOutputStream gpxXml = new ByteArrayOutputStream();
        XmlSerializer gpxSerializer = Xml.newSerializer();

        try {
            gpxSerializer.setOutput(gpxXml, "UTF-8");
            gpxSerializer.startDocument("UTF-8", true);

            gpxSerializer.startTag("", "gpx");
            gpxSerializer.attribute("", "version", "1.1");
            gpxSerializer.attribute("", "creator", "mySherpa - it.unimib.camminatori.mysherpa");
            gpxSerializer.attribute("", "xmlns", "http://www.topografix.com/GPX/1/1");
            gpxSerializer.attribute("", "xmlns:gpxx", "http://www.garmin.com/xmlschemas/GpxExtensions/v3");
            gpxSerializer.attribute("", "xmlns:gpxtpx", "http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
            gpxSerializer.attribute("", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            gpxSerializer.attribute("", "xsi:schemaLocation", "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd");

            gpxSerializer.startTag("", "metadata");

            gpxSerializer.startTag("", "author");
            gpxSerializer.text("it.unimib.camminatori.mysherpa");
            gpxSerializer.endTag("", "author");

            gpxSerializer.startTag("", "date");
            gpxSerializer.text(DateFormat.getDateInstance().toString());
            gpxSerializer.endTag("", "date");

            gpxSerializer.endTag("", "metadata");

            gpxSerializer.startTag("", "trk");

            gpxSerializer.startTag("", "name");
            gpxSerializer.text(name);
            gpxSerializer.endTag("", "name");

            gpxSerializer.startTag("", "trkseg");

            for (int i = 0; i < path.size(); i++) {
                SaveLocation trkPoint = path.get(i);

                gpxSerializer.startTag("", "trkpt");
                gpxSerializer.attribute("", "lat", String.valueOf(trkPoint.getLatitude()));
                gpxSerializer.attribute("", "lon", String.valueOf(trkPoint.getLongitude()));
                gpxSerializer.startTag("", "ele");
                gpxSerializer.text(String.valueOf(trkPoint.getAltitude()));
                gpxSerializer.endTag("", "ele");
                gpxSerializer.endTag("", "trkpt");
            }

            gpxSerializer.endTag("", "trkseg");

            gpxSerializer.endTag("", "trk");

            gpxSerializer.endTag("", "gpx");

            gpxSerializer.endDocument();

        } catch (Exception e) {
            Log.w("buildGPX", e);
            return "";
        }

        return gpxXml.toString();
    }

    static private ArrayList<Location> getGpxPath(File xmlGpx) {
        int event;
        String tag;
        String value;
        ArrayList<Location> path = new ArrayList<>();

        try {
            InputStream xmlIn = new FileInputStream(xmlGpx);
            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(xmlIn, "UTF-8");

            event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                tag = parser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                event = parser.next();
            }

        } catch (Exception e) {
            Log.d(TAG, "Failed to parse gpx");
            return null;
        }

        return path;
    }


    static private int fileCopy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            byte[] buff = new byte[1024];
            while (in.read(buff) > 0) {
                out.write(buff);
            }
        } catch (Exception e) {
            return 0;
        }

        return 1;
    }
}