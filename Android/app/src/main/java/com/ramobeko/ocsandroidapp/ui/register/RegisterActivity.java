package com.ramobeko.ocsandroidapp.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ramobeko.ocsandroidapp.OCSAndroidApp;
import com.ramobeko.ocsandroidapp.R;
import com.ramobeko.ocsandroidapp.data.model.auth.RegisterRequest;
import com.ramobeko.ocsandroidapp.data.repository.RegisterRepository;
import com.ramobeko.ocsandroidapp.databinding.ActivityRegisterBinding;
import com.ramobeko.ocsandroidapp.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterRepository registerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inject RegisterRepository from AppContainer
        OCSAndroidApp app = (OCSAndroidApp) getApplication();
        registerRepository = app.appContainer.registerRepository;

        EdgeToEdge.enable(this);
        applyEdgeToEdgeInsets();

        initViews();
        setupListeners();
    }

    private void applyEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        // Optional setup logic
    }

    private void setupListeners() {
        binding.loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.registerButton.setOnClickListener(v -> {
            if (validateInput()) {
                registerUser();
            }
        });
    }

    private boolean validateInput() {
        String name = binding.inputName.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String confirmPassword = binding.inputConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerUser() {
        String name = binding.inputName.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        String address = binding.inputAddress.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();

        RegisterRequest request = new RegisterRequest(name, email, address, password);

        registerRepository.registerUser(this, request, () -> {
            // On success
            Toast.makeText(this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, () -> {
            // On failure
            Toast.makeText(this, "Kayıt işlemi başarısız oldu. Lütfen tekrar deneyin.", Toast.LENGTH_LONG).show();

            // Optionally: highlight the failed fields, reset form, log, etc.
        });
    }

}
