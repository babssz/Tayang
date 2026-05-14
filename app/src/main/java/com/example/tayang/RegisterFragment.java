package com.example.tayang;

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

public class RegisterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Animasi slide up
        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.fade_slide_up);
        view.startAnimation(slideUp);

        EditText etName = view.findViewById(R.id.et_name);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPassword = view.findViewById(R.id.et_password);
        Button btnRegister = view.findViewById(R.id.btn_register);
        TextView tvLogin = view.findViewById(R.id.tv_login);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getContext(), "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data user ke SharedPreferences
            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences("tayang_prefs", getActivity().MODE_PRIVATE);
            prefs.edit()
                    .putString("name", name)
                    .putString("email", email)
                    .putString("password", password)
                    .apply();

            Toast.makeText(getContext(), "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show();

            // Kembali ke halaman login
            Navigation.findNavController(view)
                    .navigate(R.id.action_register_to_login);
        });

        // Balik ke login
        tvLogin.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_register_to_login)
        );
    }
}