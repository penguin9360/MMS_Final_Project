package com.project.plantappui;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.project.plantappui.databinding.ActivityLocationListBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationListActivity extends AppCompatActivity {
    Button btn_reset;
    List<String> locations_raw = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        btn_reset = findViewById(R.id.button_reset);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmptyFile();
                Toast.makeText(LocationListActivity.this, "Location history deleted",Toast.LENGTH_SHORT).show();
            }
        });

        for (Map.Entry<String, Double> entry : MainActivity.locationMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            locations_raw.add(key + ": " + value.toString());
        }

        for (int i = 0; i < locations_raw.size() - 1; i+=2) {
            writeToFile(locations_raw.get(i) + ", " + locations_raw.get(i+1) + " ::", LocationListActivity.this);
        }

        String read = readFromFile(LocationListActivity.this);
        System.out.println("read from file: " + read);

        List<String> locations = Arrays.asList(read.split("\\s*::\\s*"));
        System.out.println("==============================================================");
        System.out.println("Parsed locations: " + locations.toString());
        System.out.println("==============================================================");


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, locations);



        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

    }

    private void EmptyFile() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),"config.txt");
            FileOutputStream fos = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);

            outputStreamWriter.write("");
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File empty failed: " + e.toString());
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),"config.txt");
            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);

            outputStreamWriter.append(data);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream =  new FileInputStream(new File(Environment.getExternalStorageDirectory(), "config.txt"));


            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Main activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("Main activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}