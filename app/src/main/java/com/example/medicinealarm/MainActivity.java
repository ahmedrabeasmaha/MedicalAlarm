package com.example.medicinealarm;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.Navigation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    int[] daysString = {R.string.Saturday, R.string.Sunday, R.string.Monday, R.string.Tuesday, R.string.Wednesday, R.string.Thursday, R.string.Friday};
    boolean[] checkedDays = {false, false, false, false, false, false, false};
    byte[] data;
    ActivityResultLauncher<Intent> activityResultLauncher;
    myDbAdapter helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new myDbAdapter(this);
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ImageView image = findViewById(R.id.imageView);
                    image.setImageBitmap(bitmap);
                }
            }
        });
    }

    public void onClick(View view) {
        setTitle("Add medicine");
        Navigation.findNavController(view).navigate(R.id.addMedicine);
    }

    public void goBack(View view) {
        setTitle("Medicine Alarm");
        Navigation.findNavController(view).navigate(R.id.homeScreen);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onSave(View view) {
        setTitle("Medicine Alarm");
        TextInputEditText text = findViewById(R.id.textInputEditText);
        SwitchMaterial sw = findViewById(R.id.switch1);
        ImageView image = findViewById(R.id.imageView);
        if (Objects.requireNonNull(text.getText()).length() > 0) {
            if (sw.isChecked()) {
                int days = 0;
                for (int i = 0; i < 7; i++) {
                    if (checkedDays[i]) {
                        days++;
                    }
                }
                ConstraintLayout constraint = findViewById(R.id.con);
                if (days != 0) {
                    EditText count = (EditText) constraint.getChildAt(5);
                    if (count.getText().length() > 0) {
                        boolean go = true;
                        int num = Integer.parseInt(String.valueOf(count.getText()));
                        for (int i = 6; i < num + 6; i++) {
                            Button count1 = (Button) constraint.getChildAt(i);
                            String textb = (String) count1.getText();
                            if (!textb.contains(":")) {
                                go = false;
                                count1.setError(getText(R.string.pill_error));
                            }
                        }
                        if (go) {
                            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                            data = getBitmapAsByteArray(bitmap);
                            helper.insertMedicine(text.getText().toString(), data, true);
                            num = Integer.parseInt(String.valueOf(count.getText()));
                            for (int i = 6; i < num + 6; i++) {
                                Button count1 = (Button) constraint.getChildAt(i);
                                String textb = (String) count1.getText();
                                String[] separated = textb.split(" ");
                                if (separated[separated.length - 1] == getText(R.string.am)) {
                                    String[] daysod = {getString(R.string.Saturday), getString(R.string.Sunday), getString(R.string.Monday), getString(R.string.Tuesday), getString(R.string.Wednesday), getString(R.string.Thursday), getString(R.string.Friday)};
                                    String st = separated[separated.length - 2];
                                    separated = st.split(":");
                                    for (int j = 0; j < 7; j++) {
                                        if (checkedDays[j]) {
                                            helper.insertMedicineTime(separated[0].toString(), separated[1].toString(), daysod[j]);
                                        }
                                    }
                                } else {
                                    String[] daysod = {getString(R.string.Saturday), getString(R.string.Sunday), getString(R.string.Monday), getString(R.string.Tuesday), getString(R.string.Wednesday), getString(R.string.Thursday), getString(R.string.Friday)};
                                    String st = separated[separated.length - 2];
                                    separated = st.split(":");
                                    int ho = Integer.parseInt(separated[0]) + 12;
                                    separated[0] = String.valueOf(ho);
                                    for (int j = 0; j < 7; j++) {
                                        if (checkedDays[j]) {
                                            helper.insertMedicineTime(separated[0].toString(), separated[1].toString(), daysod[j]);
                                        }
                                    }
                                }
                            }
                            Navigation.findNavController(view).navigate(R.id.homeScreen);
                        }
                    } else {
                        count.setError(getText(R.string.pill_error));
                    }
                }
                else {
                    Button count = (Button) constraint.getChildAt(3);
                    count.setError(getText(R.string.select_days_error));
                }
            } else {
                int days = 0;
                boolean go = true;
                for (int i = 0; i < 7; i++) {
                    if (checkedDays[i]) {
                        days++;
                    }
                }
                if (days != 0) {
                    int sum = 0;
                    for (int i = 5; i < days + 5; i++) {
                        ConstraintLayout constraint = findViewById(R.id.con);
                        EditText count = (EditText) constraint.getChildAt(i);
                        if (!(count.getText().length() > 0)) {
                            count.setError(getText(R.string.pill_error));
                            go = false;
                        }
                        sum += Integer.parseInt(String.valueOf(count.getText()));
                    }
                    if (go) {
                        ConstraintLayout constraint = findViewById(R.id.con);
                        for (int i = 5 + days; i < sum + days + 5; i++) {
                            Button count = (Button) constraint.getChildAt(i);
                            String textb = (String) count.getText();
                            if (!textb.contains(":")) {
                                go = false;
                                count.setError(getText(R.string.pill_error));
                            }
                        }
                        if (go) {
                            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                            data = getBitmapAsByteArray(bitmap);
                            helper.insertMedicine(text.getText().toString(), data, false);
                            for (int i = 5 + days; i < sum + days + 5; i++) {
                                Button count1 = (Button) constraint.getChildAt(i);
                                String textb = (String) count1.getText();
                                String[] separated = textb.split(" ");
                                if (separated[separated.length - 1] == getText(R.string.am)) {
                                    String daysod = separated[3];
                                    System.out.println(daysod);
                                    String st = separated[separated.length - 2];
                                    separated = st.split(":");
                                    helper.insertMedicineTime(separated[0].toString(), separated[1].toString(), daysod);
                                } else {
                                    String daysod = separated[3];
                                    System.out.println(daysod);
                                    String st = separated[separated.length - 2];
                                    separated = st.split(":");
                                    int ho = Integer.parseInt(separated[0]) + 12;
                                    separated[0] = String.valueOf(ho);
                                    helper.insertMedicineTime(separated[0].toString(), separated[1].toString(), daysod);
                                }
                            }
                            Navigation.findNavController(view).navigate(R.id.homeScreen);
                        }
                    }
                } else {
                    ConstraintLayout constraint = findViewById(R.id.con);
                    Button count = (Button) constraint.getChildAt(3);
                    count.setError(getText(R.string.select_days_error));
                }
            }
        }
        else {
            text.setError(getText(R.string.medicine_name_error));
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void openCamera(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(camera_intent);
    }


    public void editText(String tag, int hint, View view) {
        ConstraintLayout constraint = findViewById(R.id.con);
        ConstraintSet cs = new ConstraintSet();
        EditText editText = new EditText(getApplicationContext());
        editText.setId(View.generateViewId());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setMaxLines(1);
        editText.setLines(1);
        editText.setHintTextColor(Color.argb(255, 82, 82, 82));
        editText.setTextColor(Color.WHITE);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setSingleLine(true);
        editText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                ConstraintLayout constraint = findViewById(R.id.con);
                count = constraint.getChildCount();
                for (int i = count - 1; i >= 6; i--) {
                    View currentChild = constraint.getChildAt(i);
                    if (currentChild.getTag().toString().contains("edittext") || currentChild.getTag().toString().equals("existential")) {
                        constraint.removeView(currentChild);
                    }
                }
                if (s.length() > 0) {
                    for (int i = 1; i <= Integer.parseInt(s.toString()); i++) {
                        Button button = new Button(getApplicationContext());
                        button.setId(View.generateViewId());
                        button.setTag("edittext" + i);
                        button.setText(getString(R.string.pill_time) + " " + i);
                        int finalI = i;
                        button.setOnClickListener(v -> {
                            button.setError(null);
                            Calendar currentTime = Calendar.getInstance();
                            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = currentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(MainActivity.this, (timePicker, selectedHour, selectedMinute) -> {
                                if (selectedHour > 12) {
                                    button.setText(getString(R.string.pill) + " " + finalI + " " + (selectedHour - 12) + ":" + selectedMinute + " " + getString(R.string.pm));
                                } else {
                                    button.setText(getString(R.string.pill) + " " + finalI + " " + selectedHour + ":" + selectedMinute + " " + getString(R.string.am));
                                }

                            }, hour, minute, false);//Yes 24 hour time
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();

                        });
                        constraint = findViewById(R.id.con);
                        count = constraint.getChildCount();
                        View v = constraint.getChildAt(count - 1);
                        ConstraintSet cs = new ConstraintSet();
                        constraint.addView(button);
                        cs.clone(constraint);
                        cs.connect(button.getId(), ConstraintSet.TOP, v.getId(), ConstraintSet.BOTTOM, 44);
                        cs.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                        cs.connect(button.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                        cs.applyTo(constraint);
                    }
                }
            }

            public void afterTextChanged(Editable editable) {

            }
        });
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

    public void editText(String tag, int hint, int hint2, View view, int ime) {
        ConstraintLayout constraint = findViewById(R.id.con);
        ConstraintSet cs = new ConstraintSet();
        EditText editText = new EditText(getApplicationContext());
        editText.setId(View.generateViewId());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setMaxLines(1);
        editText.setLines(1);
        editText.setHintTextColor(Color.argb(255, 82, 82, 82));
        editText.setTextColor(Color.WHITE);
        editText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                ConstraintLayout constraint = findViewById(R.id.con);
                count = constraint.getChildCount();
                int curr = 0;
                for (int i = count - 1; i >= 6; i--) {
                    View currentChild = constraint.getChildAt(i);
                    if (currentChild.getTag().toString().contains("n" + getString(hint2))) {
                        constraint.removeView(currentChild);
                        curr = i;
                    }
                }
                try {
                    View currentChild = constraint.getChildAt(curr);
                    View currentChild1 = constraint.getChildAt(curr - 1);
                    ConstraintSet cs = new ConstraintSet();
                    cs.clone(constraint);
                    cs.connect(currentChild.getId(), ConstraintSet.TOP, currentChild1.getId(), ConstraintSet.BOTTOM, 44);
                    cs.connect(currentChild.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                    cs.connect(currentChild.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                    cs.applyTo(constraint);
                } catch (Exception ignored) {

                }

                if (s.length() > 0) {
                    for (int i = 1; i <= Integer.parseInt(s.toString()); i++) {
                        Button button = new Button(getApplicationContext());
                        button.setId(View.generateViewId());
                        button.setTag("edittext" + "n" + getString(hint2) + i);
                        button.setText(getString(R.string.pill_time) + " " + i + " " + getString(R.string.day) + " " + getString(hint2));
                        int finalI = i;
                        button.setOnClickListener(v -> {
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    if (selectedHour > 12) {
                                        button.setText(getString(R.string.pill) + " " + finalI + " " + getString(hint2) + " " + (selectedHour - 12) + ":" + selectedMinute + " " + getString(R.string.pm));
                                    } else {
                                        button.setText(getString(R.string.pill) + " " + finalI + " " + getString(hint2) + " " + selectedHour + ":" + selectedMinute + " " + getString(R.string.am));
                                    }

                                }
                            }, hour, minute, false);
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();

                        });
                        constraint = findViewById(R.id.con);
                        count = constraint.getChildCount();
                        View v = constraint.getChildAt(count - 1);
                        ConstraintSet cs = new ConstraintSet();
                        constraint.addView(button);
                        cs.clone(constraint);
                        cs.connect(button.getId(), ConstraintSet.TOP, v.getId(), ConstraintSet.BOTTOM, 44);
                        cs.connect(button.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                        cs.connect(button.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                        cs.applyTo(constraint);
                    }
                }
            }

            public void afterTextChanged(Editable editable) {

            }
        });
        editText.setImeOptions(ime);
        editText.setSingleLine(true);
        editText.setTag(tag);
        String s = getString(hint) + " " + getString(hint2);
        editText.setHint(s);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        constraint.addView(editText);
        cs.clone(constraint);
        cs.connect(editText.getId(), ConstraintSet.TOP, view.getId(), ConstraintSet.BOTTOM, 44);
        cs.connect(editText.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        cs.connect(editText.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        cs.applyTo(constraint);
    }

    public void billsNumbs(View view) {
        String[] days = {getString(R.string.Saturday), getString(R.string.Sunday), getString(R.string.Monday), getString(R.string.Tuesday), getString(R.string.Wednesday), getString(R.string.Thursday), getString(R.string.Friday)};
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
            editText("existential", R.string.pills_num, sw);
        } else {
            int counter = 0, cur_count = 1;
            for (int i = 0; i < 7; i++) {
                if (checkedDays[i]) {
                    counter++;
                }
            }
            for (int i = 0; i < 7; i++) {
                count = constraint.getChildCount();
                View v = constraint.getChildAt(count - 1);
                if (checkedDays[i] && first) {
                    if (cur_count == counter) {
                        editText("edittext" + days[i], R.string.pills_num, daysString[i], sw, EditorInfo.IME_ACTION_GO);
                    } else {
                        cur_count++;
                        editText("edittext" + days[i], R.string.pills_num, daysString[i], sw, EditorInfo.IME_ACTION_NEXT);
                    }
                    first = false;
                } else if (checkedDays[i]) {
                    if (cur_count == counter) {
                        editText("edittext" + days[i], R.string.pills_num, daysString[i], v, EditorInfo.IME_ACTION_GO);
                    } else {
                        cur_count++;
                        editText("edittext" + days[i], R.string.pills_num, daysString[i], v, EditorInfo.IME_ACTION_NEXT);
                    }
                }
            }
        }
    }

    public void billsNum(View view) {
        billsNumbs(view);
    }

    public void showDays(View view) {
        Button bu = findViewById(R.id.button5);
        bu.setError(null);
        String[] days = {getString(R.string.Saturday), getString(R.string.Sunday), getString(R.string.Monday), getString(R.string.Tuesday), getString(R.string.Wednesday), getString(R.string.Thursday), getString(R.string.Friday)};
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