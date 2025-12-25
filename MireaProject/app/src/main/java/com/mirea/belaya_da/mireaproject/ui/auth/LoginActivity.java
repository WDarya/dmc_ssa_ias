package com.mirea.belaya_da.mireaproject.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.mirea.belaya_da.mireaproject.MainActivity;
import com.mirea.belaya_da.mireaproject.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Инициализация Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            if (validateForm()) {
                login();
            }
        });

        btnRegister.setOnClickListener(v -> {
            if (validateForm()) {
                register();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Введите email");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Введите пароль");
            valid = false;
        } else if (password.length() < 6) {
            etPassword.setError("Пароль должен содержать минимум 6 символов");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // Блокируем кнопки на время выполнения запроса
        setButtonsEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Вход выполнен!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        // Конкретные сообщения об ошибках
                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(LoginActivity.this,
                                    "Аккаунт с таким email не найден",
                                    Toast.LENGTH_LONG).show();
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginActivity.this,
                                    "Неверный пароль",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Ошибка входа: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        setButtonsEnabled(true);
                    }
                });
    }

    private void register() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        // Блокируем кнопки на время выполнения запроса
        setButtonsEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this,
                                "Регистрация успешна!",
                                Toast.LENGTH_SHORT).show();

                        // После регистрации сразу входим
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        // Конкретные сообщения об ошибках
                        if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(LoginActivity.this,
                                    "Слабый пароль. Используйте минимум 6 символов",
                                    Toast.LENGTH_LONG).show();
                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginActivity.this,
                                    "Неверный формат email",
                                    Toast.LENGTH_LONG).show();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(LoginActivity.this,
                                    "Аккаунт с таким email уже существует",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Ошибка регистрации: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        setButtonsEnabled(true);
                    }
                });
    }

    private void setButtonsEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
        btnRegister.setEnabled(enabled);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Пользователь уже вошёл, переходим на главный экран
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}