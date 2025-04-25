package com.ramobeko.ocsandroidapp.ui.subscribers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramobeko.ocsandroidapp.data.model.Subscriber;
import com.ramobeko.ocsandroidapp.databinding.ItemSubscriberBinding;
import com.ramobeko.ocsandroidapp.ui.dashboard.DashboardActivity;

import java.util.List;

public class SubscriberAdapter extends RecyclerView.Adapter<SubscriberAdapter.SubscriberViewHolder> {

    private final List<Subscriber> subscribers;

    public SubscriberAdapter(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    @NonNull
    @Override
    public SubscriberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriberBinding binding = ItemSubscriberBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SubscriberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriberViewHolder holder, int position) {
        Subscriber subscriber = subscribers.get(position);
        holder.binding.tvPhoneNumber.setText(subscriber.getPhoneNumber());
        holder.binding.tvPackageName.setText(subscriber.getPackagePlan().getName());

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DashboardActivity.class);
            intent.putExtra("subscriber", subscriber);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return subscribers.size();
    }

    static class SubscriberViewHolder extends RecyclerView.ViewHolder {
        final ItemSubscriberBinding binding;

        public SubscriberViewHolder(@NonNull ItemSubscriberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
