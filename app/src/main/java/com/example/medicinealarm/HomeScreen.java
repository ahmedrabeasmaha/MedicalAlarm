package com.example.medicinealarm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeScreen extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    String[] listviewTitle = new String[]{
//            "ListView Title 1", "ListView Title 2", "ListView Title 3", "ListView Title 4",
//            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
//    };
//    int[] listviewImage = new int[]{
//            R.drawable.index, R.drawable.index, R.drawable.index, R.drawable.index,
//            R.drawable.index, R.drawable.index, R.drawable.index, R.drawable.index,
//    };

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
//        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
//        for (int i = 0; i < 8; i++) {
//            HashMap<String, String> hm = new HashMap<String, String>();
//            hm.put("listview_title", listviewTitle[i]);
//            hm.put("listview_image", Integer.toString(listviewImage[i]));
//            aList.add(hm);
//        }
//        String[] from = {"listview_image", "listview_title"};
//        int[] to = {R.id.listview_image, R.id.listview_item_title};
//        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), aList, R.layout.listviewactivty, from, to);
//        ListView listView = (ListView) view.findViewById(R.id.listView1);
//        listView.setAdapter(simpleAdapter);
        return view;
    }
}