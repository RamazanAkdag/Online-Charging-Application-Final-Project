package com.ramobeko.ocsandroidapp.ui.subscribers;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ramobeko.ocsandroidapp.OCSAndroidApp;
import com.ramobeko.ocsandroidapp.R;
import com.ramobeko.ocsandroidapp.data.model.Subscriber;
import com.ramobeko.ocsandroidapp.data.repository.SubscriberRepository;
import com.ramobeko.ocsandroidapp.databinding.ActivitySubscribersBinding;

import java.util.List;

public class SubscribersActivity extends AppCompatActivity {

    private ActivitySubscribersBinding binding;
    private SubscriberRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscribersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = ((OCSAndroidApp) getApplication()).appContainer.subscriberRepository;

        repository.getSubscriptions(this, new SubscriberRepository.SubscriberCallback() {
            @Override
            public void onSuccess(List<Subscriber> subscribers) {
                SubscriberAdapter adapter = new SubscriberAdapter(subscribers);
                binding.subscriberRecyclerView.setAdapter(adapter);
                binding.tvCustomerName.setText(subscribers.get(0).getCustomer().getName());



            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SubscribersActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
