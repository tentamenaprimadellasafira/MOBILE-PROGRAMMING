package com.example.secretcalculator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    EditText inputDigit;
    Button btnSubmit;
    final int REQUEST_CODE = 100;
    String[] allowedDigits = {"0", "3", "5", "1", "2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputDigit = findViewById(R.id.inputDigit);
        btnSubmit = findViewById(R.id.btnSubmit);

        requestPermissions();

        btnSubmit.setOnClickListener(v -> {
            String digit = inputDigit.getText().toString().trim();

            if (digit.length() != 1 || !isAllowedDigit(digit)) {
                Toast.makeText(this, "Digit tidak valid!", Toast.LENGTH_SHORT).show();
                return;
            }

            runFeature(digit);
        });
    }

    private boolean isAllowedDigit(String d) {
        for (String s : allowedDigits) {
            if (s.equals(d)) return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                }, REQUEST_CODE);
    }

    private void runFeature(String digit) {
        switch (digit) {
            case "0":
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(camIntent);
                break;

            case "3":
                MediaPlayer mp = MediaPlayer.create(this, R.raw.audio_sample);
                mp.start();
                break;

            case "5":
                Intent imgIntent = new Intent(Intent.ACTION_VIEW);
                imgIntent.setType("image/*");
                startActivity(imgIntent);
                break;

            case "1":
                Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(gpsIntent);
                break;

            case "2":
                SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
                Sensor accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                Sensor gyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                if (accel != null && gyro != null) {
                    Toast.makeText(this, "Sensor OK: Accelerometer dan Gyroscope tersedia", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Sensor tidak ditemukan", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}