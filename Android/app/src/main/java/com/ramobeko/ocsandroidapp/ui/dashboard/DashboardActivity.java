package com.ramobeko.ocsandroidapp.ui.dashboard;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ramobeko.ocsandroidapp.data.model.Balance;
import com.ramobeko.ocsandroidapp.data.model.Subscriber;
import com.ramobeko.ocsandroidapp.databinding.ActivityDashboardBinding;
import com.ramobeko.ocsandroidapp.databinding.ItemUsageBinding;

import java.util.ArrayList;
import java.util.List;

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

        Subscriber subscriber = (Subscriber) getIntent().getSerializableExtra("subscriber");

        if (subscriber != null) {
            setupDashboard(subscriber);
        } else {
            Toast.makeText(this, "Abone bilgileri bulunamadı.", Toast.LENGTH_SHORT).show();
            Log.e("DashboardActivity", "Subscriber is null");
        }
    }

    private void setupDashboard(Subscriber subscriber) {
        // Hoşgeldin mesajını ayarla
        String welcomeMessage = "Hoş geldin, " + subscriber.getCustomer().getName();
        binding.welcomeText.setText(welcomeMessage);

        // Paket Detayları
        binding.detailTitle.setText(subscriber.getPackagePlan().getName());

        String packageDescription = String.format(
                "Aylık %d dakika, %d SMS ve %d GB internet hakkınız bulunmaktadır.",
                subscriber.getPackagePlan().getAmountMinutes(),
                subscriber.getPackagePlan().getAmountSms(),
                subscriber.getPackagePlan().getAmountData()
        );
        binding.detailDescription.setText(packageDescription);

        // Kullanım detaylarını ayarla (Balance listesinin ilk elemanına göre örnek)
        if (!subscriber.getBalances().isEmpty()) {
            Balance currentBalance = subscriber.getBalances().get(0);

            // Mesaj Kullanımı
            setUsageData(binding.usageMessage,
                    currentBalance.getLevelSms(),
                    subscriber.getPackagePlan().getAmountSms(),
                    "SMS");

            // Internet Kullanımı
            setUsageData(binding.usageInternet,
                    currentBalance.getLevelData(),
                    subscriber.getPackagePlan().getAmountData(),
                    "GB");

            // Arama Kullanımı
            setUsageData(binding.usageCall,
                    currentBalance.getLevelMinutes(),
                    subscriber.getPackagePlan().getAmountMinutes(),
                    "Dk");
        }


    }

    private void setUsageData(ItemUsageBinding usageBinding, int usedAmount, int totalAmount, String unit) {
        usageBinding.usageLabel.setText(unit);
        usageBinding.usagePercent.setText(String.format("%d/%d", usedAmount, totalAmount));

        int percentage = (int) ((usedAmount / (double) totalAmount) * 100);

        // ProgressBar'a animasyon uygula
        ObjectAnimator animation = ObjectAnimator.ofInt(usageBinding.usageProgress, "progress", 0, percentage);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

}
