package com.ramobeko.ocsandroidapp.ui.dashboard;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ramobeko.ocsandroidapp.databinding.ActivityDashboardBinding;
import com.ramobeko.ocsandroidapp.databinding.ItemUsageBinding;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // usageMessage, usageInternet, usageCall binding tipinde, doğrudan eriş
        setupUsageItem(binding.usageMessage, "Mesaj", "50%");
        setupUsageItem(binding.usageInternet, "İnternet", "75%");
        setupUsageItem(binding.usageCall, "Arama", "30%");
    }

    private void setupUsageItem(ItemUsageBinding usageBinding, String labelText, String percentText) {
        usageBinding.usageLabel.setText(labelText);
        usageBinding.usagePercent.setText(percentText);
    }
}
