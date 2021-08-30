package com.example.medicinealarm;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.work.Data;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeScreen extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeScreen() {
    }

    public static HomeScreen newInstance(String param1, String param2) {
        HomeScreen fragment = new HomeScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        myDbAdapter helper = new myDbAdapter(getContext());
        Cursor cursor = helper.getData();
        ListView listView = view.findViewById(R.id.listView1);
        List<Model> list = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("medicine_name"));
                    int index = cursor.getColumnIndexOrThrow("image");
                    byte[] imgByte = cursor.getBlob(index);
                    Bitmap bm;
                    if (imgByte == null) {
                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.medicine);
                    }
                    else {
                        bm = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    }

                    list.add(new Model(name, bm));
                    cursor.moveToNext();
                }
            }
        } catch (Exception ignored) {
        }
        myAdapter adapter = new myAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }
}