package com.ramobeko.ocsandroidapp.ui.dashboard;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ramobeko.ocsandroidapp.OCSAndroidApp;
import com.ramobeko.ocsandroidapp.data.model.Subscriber;
import com.ramobeko.ocsandroidapp.data.repository.SubscriberRepository;
import com.ramobeko.ocsandroidapp.databinding.ActivityDashboardBinding;
import com.ramobeko.ocsandroidapp.databinding.ItemUsageBinding;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private SubscriberRepository subscriberRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the SubscriberRepository from AppContainer
        OCSAndroidApp app = (OCSAndroidApp) getApplication();
        subscriberRepository = app.appContainer.subscriberRepository;

        // Fetch customer subscriptions
        fetchCustomerSubscriptions();
    }

    private void fetchCustomerSubscriptions() {
        subscriberRepository.getSubscriptions(this, new SubscriberRepository.SubscriberCallback() {
            @Override
            public void onSuccess(List<Subscriber> subscribers) {
                // Log the success message
                Toast.makeText(DashboardActivity.this, "Başarılı", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure
                Toast.makeText(DashboardActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
