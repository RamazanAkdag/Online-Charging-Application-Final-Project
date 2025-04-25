package com.ramobeko.ocsandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ramobeko.ocsandroidapp.databinding.ActivityMainBinding;
import com.ramobeko.ocsandroidapp.ui.register.RegisterActivity;
import com.ramobeko.ocsandroidapp.ui.subscribers.SubscribersActivity;
import com.ramobeko.ocsandroidapp.utils.SecurePrefs;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = SecurePrefs.getToken(MainActivity.this);
                if (token != null && !token.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SubscribersActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }




}