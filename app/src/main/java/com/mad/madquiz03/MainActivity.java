package com.mad.madquiz03;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView fvrtLV;
    Button addBtn, mapBtn;
    EditText etLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fvrtLV = findViewById(R.id.fvrtLV);
        addBtn = findViewById(R.id.addBtn);
        mapBtn = findViewById(R.id.mapBtn);
        etLoc = findViewById(R.id.etLoc);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        }, 1011);

        SQLLiteDB db = new SQLLiteDB(getBaseContext());
        ArrayList<String> locs;
        locs = db.readRecords();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                locs);
        fvrtLV.setAdapter(adapter);
        fvrtLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, fvrtLocMap.class);
                intent.putExtra("location", ((TextView) view).getText().toString().trim());
                startActivity(intent);
            }
        });

        ArrayList<String> finalLocs = locs;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = etLoc.getText().toString();

                if(location == null || location.equals("")){
                    Toast.makeText(MainActivity.this, "Add a location name",
                                    Toast.LENGTH_SHORT).show();
                }
                else{
                    db.insert(location);

                    finalLocs.add(location);
                    adapter.notifyDataSetChanged();

                    etLoc.setText("");
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, "Location Added Successfully",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyMap.class));
            }
        });
    }
}