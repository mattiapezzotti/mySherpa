package it.unimib.camminatori.mysherpa.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import it.unimib.camminatori.mysherpa.FavRecordsRecyclerViewAdapter;
import it.unimib.camminatori.mysherpa.R;
import it.unimib.camminatori.mysherpa.viewmodel.Record_ViewModel;

public class SavedRecords_Fragment extends Fragment {
    final private String TAG = "SavedRecordsFragment";
    protected RecyclerView favRecordsView;

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

        favRecordsView = (RecyclerView) v.findViewById(R.id.fav_records_recycler_view);
        LinearLayoutManager favLinearLayout = new LinearLayoutManager(getContext());
        favRecordsView.setLayoutManager(favLinearLayout);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Record_ViewModel recordViewModel = new ViewModelProvider(requireActivity()).get(Record_ViewModel.class);

        ArrayList<Record_ViewModel.SaveRecordInfo> favRecords = recordViewModel.getFavList();

        final FavRecordsRecyclerViewAdapter favRecordsRecyclerViewAdapter = new FavRecordsRecyclerViewAdapter(favRecords);
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
}