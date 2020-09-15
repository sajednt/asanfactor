package com.sajednt.html2pdf.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sajednt.html2pdf.R;
import com.sajednt.html2pdf.adapter.AdapterFactors;
import com.sajednt.html2pdf.adapter.adapterProduct;
import com.sajednt.html2pdf.database.Database;
import com.sajednt.html2pdf.function.Functions;
import com.sajednt.html2pdf.item.itemFactor;
import com.sajednt.html2pdf.item.itemListFactor;

import java.util.ArrayList;
import java.util.List;

public class fragment_list extends Fragment {

    RecyclerView recyclerView;
    List<itemFactor> listItem = new ArrayList<>();
    Functions func;
    Database db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        db = new Database(getActivity());

        recyclerView = view.findViewById(R.id.recycler_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final AdapterFactors apv = new AdapterFactors( db.getAllFactores() , getContext(), getActivity());
        recyclerView.swapAdapter(apv , false);

        return view;
    }


}
