package com.example.firebasedemo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView text;
    Button button;
    FirebaseRemoteConfig firebaseRemoteConfig;
    FirebaseRemoteConfigSettings firebaseRemoteConfigSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFirebaseRemoteConfig();
    }

    private void getValueFirebaseConfig() {
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            Log.i("TAG", "onComplete: " + String.valueOf(task.getResult()));
                            text.setText(firebaseRemoteConfig.getString("text_str"));
                            text.setTextSize(Float.parseFloat(firebaseRemoteConfig.getString("text_size") + ""));
                            text.setTextColor(Color.parseColor(firebaseRemoteConfig.getString("text_color")));
                        } else {
                            Toast.makeText(MainActivity.this, "Fetch failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initFirebaseRemoteConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0).build();
        firebaseRemoteConfig.setConfigSettingsAsync(firebaseRemoteConfigSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.default_map);

        text.setText(firebaseRemoteConfig.getString("text_str"));
        text.setTextSize(Float.parseFloat(firebaseRemoteConfig.getString("text_size") + ""));
        text.setTextColor(Color.parseColor(firebaseRemoteConfig.getString("text_color")));
    }

    private void initView() {
        text = findViewById(R.id.text);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                getValueFirebaseConfig();
                break;
        }
    }
}
