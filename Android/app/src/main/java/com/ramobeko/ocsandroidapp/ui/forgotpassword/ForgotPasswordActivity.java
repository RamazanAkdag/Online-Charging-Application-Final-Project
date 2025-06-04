package com.ramobeko.ocsandroidapp.ui.forgotpassword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ramobeko.ocsandroidapp.OCSAndroidApp;
import com.ramobeko.ocsandroidapp.data.repository.ForgotPasswordRepository;
import com.ramobeko.ocsandroidapp.databinding.ActivityForgotPasswordBinding;
import com.ramobeko.ocsandroidapp.ui.login.LoginActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // ViewBinding başlat
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Sistem içeri girintilerini uygula
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        forgotPasswordRepository = ((OCSAndroidApp) getApplication()).appContainer.forgotPasswordRepository;

        binding.btnSendReset.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Lütfen e-posta adresinizi girin.", Toast.LENGTH_SHORT).show();
                return;
            }

            forgotPasswordRepository.sendForgotPasswordRequest(
                    this,
                    email,
                    () -> {
                        Toast.makeText(this, "Şifre sıfırlama bağlantısı e-posta adresinize gönderildi.", Toast.LENGTH_LONG).show();


                        binding.getRoot().postDelayed(() -> {
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }, 2000);
                    },
                    () -> Toast.makeText(this, "E-posta bulunamadı veya gönderilemedi.", Toast.LENGTH_SHORT).show()
            );
        });

    }
}
