package com.example.medicinealarm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link medicine_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class medicine_list extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public medicine_list() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment medicine_list.
     */
    // TODO: Rename and change types and number of parameters
    public static medicine_list newInstance(String param1, String param2) {
        medicine_list fragment = new medicine_list();
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

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        myDbAdapter helper = new myDbAdapter(getContext());
        Cursor cursor = helper.getData();
        ListView listView = view.findViewById(R.id.listView1);
        List<Model> list = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    @SuppressLint("Range")
                    String name = cursor.getString(cursor.getColumnIndex("medicine_name"));
                    int index = cursor.getColumnIndex("image");
                    @SuppressLint("Range")
                    String tag = cursor.getString(cursor.getColumnIndex("id"));
                    byte[] imgByte = cursor.getBlob(index);
                    Bitmap bm;
                    if (imgByte == null) {
                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.medicine);
                    }
                    else {
                        bm = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                    }

                    list.add(new Model(name, tag, bm));
                    cursor.moveToNext();
                }
            }
        } catch (Exception ignored) {
        }
        myAdapter adapter = new myAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener((adapterView, viewed, i, l) -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage(getText(R.string.delete));
            alert.setCancelable(true);
            alert.setPositiveButton(getText(R.string.yes), (dialog, id) -> {
                helper.deleteMedicine(Integer.parseInt(String.valueOf(viewed.findViewById(R.id.listview_item_title).getTag())));
                helper.deleteTime(Integer.parseInt(String.valueOf(viewed.findViewById(R.id.listview_item_title).getTag())));
                Cursor cursor1 = helper.getData();
                List<Model> list1 = new ArrayList<>();
                try {
                    if (cursor1.moveToFirst()) {
                        while (!cursor1.isAfterLast()) {
                            @SuppressLint("Range")
                            String name = cursor1.getString(cursor1.getColumnIndex("medicine_name"));
                            int index = cursor1.getColumnIndex("image");
                            @SuppressLint("Range")
                            String tag = cursor1.getString(cursor1.getColumnIndex("id"));
                            byte[] imgByte = cursor1.getBlob(index);
                            Bitmap bm;
                            if (imgByte == null) {
                                bm = BitmapFactory.decodeResource(getResources(), R.drawable.medicine);
                            }
                            else {
                                bm = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                            }

                            list1.add(new Model(name, tag, bm));
                            cursor1.moveToNext();
                        }
                    }
                } catch (Exception ignored) {
                }
                myAdapter adapter1 = new myAdapter(getActivity(), list1);
                listView.setAdapter(adapter1);
            });
            alert.setNegativeButton(getText(R.string.no), (dialog, id) -> dialog.cancel());
            AlertDialog dialog = alert.create();
            dialog.show();
            return true;
        });
//        listView.setOnItemClickListener((parent, viewed, position, id) -> {
//            Cursor cursor2 = helper.getNotify(Integer.parseInt(String.valueOf(viewed.findViewById(R.id.listview_item_title).getTag())));
//            EditText editText = getContext().findViewById(R.id.textInputEditText);
//            cursor2.moveToFirst();
//            editText.setText("dvdsv");
//            Navigation.findNavController(viewed).navigate(R.id.add_medicine);
//
//        });
        return view;
    }
}