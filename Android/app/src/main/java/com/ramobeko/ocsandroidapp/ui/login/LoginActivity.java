package com.ramobeko.ocsandroidapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ramobeko.ocsandroidapp.R;
import com.ramobeko.ocsandroidapp.OCSAndroidApp;
import com.ramobeko.ocsandroidapp.data.model.auth.LoginRequest;
import com.ramobeko.ocsandroidapp.data.repository.LoginRepository;
import com.ramobeko.ocsandroidapp.databinding.ActivityLoginBinding;
import com.ramobeko.ocsandroidapp.ui.dashboard.DashboardActivity;
import com.ramobeko.ocsandroidapp.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Edge-to-edge fix
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 💉 Inject repository via AppContainer
        OCSAndroidApp app = (OCSAndroidApp) getApplication();
        loginRepository = app.appContainer.loginRepository;

        // 🔁 Navigate to Register
        binding.registerLink.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // ✅ Handle Login
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString().trim();
            String password = binding.inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen e-posta ve şifre girin", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);

            loginRepository.loginUser(this, request, () -> {
                Toast.makeText(this, "Hoş geldiniz!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }, () -> {
                Toast.makeText(this, "Giriş başarısız", Toast.LENGTH_SHORT).show();
            });
        });
    }
}
