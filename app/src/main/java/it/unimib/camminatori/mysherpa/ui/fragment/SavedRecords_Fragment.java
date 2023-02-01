package it.unimib.camminatori.mysherpa.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.FavRecordsRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;

public class SavedRecords_Fragment extends Fragment {
    final static public String FAVOURITES_RECORDS_SHAREDPREFS = "FAVOURITES_RECORDS";
    final static public String FAVOURITES_RECORDS = "FAVOURITE_RECORDS_LIST";

    final private String TAG = "SavedRecordsFragment";
    protected RecyclerView favRecordsView;

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
        ArrayList<Record_ViewModel.SaveRecordInfo> savedRecords;
        recordViewModel = new ViewModelProvider(requireActivity()).get(Record_ViewModel.class);
        SharedPreferences favRecordsPreferences = requireContext().getSharedPreferences(FAVOURITES_RECORDS_SHAREDPREFS, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String saveJson = favRecordsPreferences.getString(FAVOURITES_RECORDS, "");
        if (saveJson.equals("")) {
            savedRecords = new ArrayList<>();
        } else {
            savedRecords = gson.fromJson(saveJson, new TypeToken<ArrayList<Record_ViewModel.SaveRecordInfo>>() {
            }.getType());
        }

        Log.i(TAG, "Model Fav: " + recordViewModel.getFavList());
        Log.i(TAG, "From sharedpref: " + savedRecords);

        recordViewModel.getRecordInfo(getContext(), savedRecords);

        ArrayList<Record_ViewModel.SaveRecordInfo> favRecords = recordViewModel.getFavList();


        final FavRecordsRecyclerViewAdapter favRecordsRecyclerViewAdapter = new FavRecordsRecyclerViewAdapter(favRecords);
        favRecordsRecyclerViewAdapter.setOnItemsChangedListener(size -> {
            TextView noBookmarksTextView = view.findViewById(R.id.no_bookmarks_text_view);

            if (size == 0)
                noBookmarksTextView.setVisibility(View.VISIBLE);
            else
                noBookmarksTextView.setVisibility(View.GONE);
        });

        favRecordsView.setAdapter(favRecordsRecyclerViewAdapter);

        EditText favSearch = (EditText) view.findViewById(R.id.fav_text_search);

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

        SharedPreferences favRecordsPreferences = requireContext().getSharedPreferences(FAVOURITES_RECORDS_SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = favRecordsPreferences.edit();

        ArrayList<Record_ViewModel.SaveRecordInfo> favList = recordViewModel.getFavList();

        Log.i(TAG, "saving " + favList);

        Gson gson = new Gson();

        String json = gson.toJson(favList);

        editor.putString(FAVOURITES_RECORDS, json);
        editor.apply();
    }
}