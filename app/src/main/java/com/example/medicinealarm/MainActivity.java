package com.example.medicinealarm;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    String[] days = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    boolean[] checkedDays = {false, false, false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(android.R.style.Theme_Material_NoActionBar);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        setTitle("Add medicine");
        Navigation.findNavController(view).navigate(R.id.addMedicine);
    }

    public void openCamera(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, 123);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView click_image_id = findViewById(R.id.imageView);
            click_image_id.setImageBitmap(photo);
        }
    }

    public void editText(String tag, String hint, View view) {
        ConstraintLayout constraint = findViewById(R.id.con);
        ConstraintSet cs = new ConstraintSet();
        EditText editText = new EditText(getApplicationContext());
        editText.setId(View.generateViewId());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setMaxLines(1);
        editText.setLines(1);
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setSingleLine(true);
        editText.setTag(tag);
        editText.setHint(hint);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        constraint.addView(editText);
        cs.clone(constraint);
        cs.connect(editText.getId(), ConstraintSet.TOP, view.getId(), ConstraintSet.BOTTOM, 44);
        cs.connect(editText.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        cs.connect(editText.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        cs.applyTo(constraint);
    }

    @SuppressLint("SetTextI18n")
    public void billsNumbs(View view) {
        ConstraintLayout constraint = findViewById(R.id.con);
        SwitchMaterial sw = findViewById(R.id.switch1);
        boolean first = true, chosen = false;
        int count = constraint.getChildCount();
        for (int i = count - 1; i >= 5; i--) {
            View currentChild = constraint.getChildAt(i);
            if (currentChild.getTag().toString().contains("edittext") || currentChild.getTag().toString().equals("existential")) {
                constraint.removeView(currentChild);
            }
        }
        for (int i = 0; i < 7; i++) {
            if (checkedDays[i]) {
                chosen = true;
                break;
            }
        }
        if (sw.isChecked() && chosen) {
            editText("existential", "Enter number of bills", sw);
            View currentChild = constraint.getChildAt(5);
            ConstraintSet cs = new ConstraintSet();
            Switch switchMaterial = new Switch(getApplicationContext());
            switchMaterial.setId(View.generateViewId());
            switchMaterial.setText("all pills in same time?");
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
            switchMaterial.setLayoutParams(params);
            constraint.addView(switchMaterial);
            cs.clone(constraint);
            cs.connect(switchMaterial.getId(), ConstraintSet.TOP, currentChild.getId(), ConstraintSet.BOTTOM, 44);
            cs.connect(switchMaterial.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            cs.connect(switchMaterial.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            cs.applyTo(constraint);
        } else {
            for (int i = 0; i < 7; i++) {
                count = constraint.getChildCount();
                View v = constraint.getChildAt(count - 1);
                if (checkedDays[i] && first) {
                    editText("edittext" + days[i], "Enter number of bills on " + days[i], sw);
                    first = false;
                } else if (checkedDays[i]) {
                    editText("edittext" + days[i], "Enter number of bills on " + days[i], v);
                }
            }
        }
    }

    public void billsNum(View view) {
        billsNumbs(view);
    }

    public void showDays(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose days");

        builder.setMultiChoiceItems(days, checkedDays, (dialogInterface, i, b) -> {
        });
        builder.setPositiveButton("OK", (dialog, which) -> billsNumbs(view));
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            for (int i = 0; i < 7; i++) {
                checkedDays[i] = false;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}