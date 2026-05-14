package com.example.tayang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Animasi slide up saat halaman login muncul
        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.fade_slide_up);
        view.startAnimation(slideUp);

        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPassword = view.findViewById(R.id.et_password);
        Button btnLogin = view.findViewById(R.id.btn_login);
        TextView tvRegister = view.findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Email dan password harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cek kredensial dari SharedPreferences
            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences("tayang_prefs", getActivity().MODE_PRIVATE);
            String savedEmail = prefs.getString("email", "");
            String savedPassword = prefs.getString("password", "");

            if (email.equals(savedEmail) && password.equals(savedPassword)) {
                // Simpan status login
                prefs.edit().putBoolean("is_logged_in", true).apply();
                // Pindah ke MainActivity
                startActivity(new Intent(getActivity(), MainActivity.class));
                requireActivity().finish();
            } else {
                Toast.makeText(getContext(), "Email atau password salah!", Toast.LENGTH_SHORT).show();
            }
        });

        // Pindah ke halaman Register
        tvRegister.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_login_to_register)
        );
    }
}