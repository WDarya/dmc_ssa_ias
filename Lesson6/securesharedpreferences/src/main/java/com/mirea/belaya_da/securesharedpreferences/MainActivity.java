package com.mirea.belaya_da.securesharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewPoet;
    private TextView textViewPoetName;
    private TextView textViewPoetInfo;
    private TextView textViewEncrypted;

    private static final String PREFS_NAME = "secure_portrait_prefs";
    private static final String KEY_POET_NAME = "poet_name";
    private static final String KEY_POET_INFO = "poet_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupPhoto();

        try {
            // Создание MasterKey (новый API)
            MasterKey masterKey = new MasterKey.Builder(getApplicationContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Создание EncryptedSharedPreferences
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    getApplicationContext(),
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Сохранение и загрузка данных
            saveEncryptedData(secureSharedPreferences);
            loadAndDisplayData(secureSharedPreferences);

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            textViewEncrypted.setText("Ошибка шифрования: " + e.getLocalizedMessage());
            Toast.makeText(this, "Ошибка создания защищенных настроек", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        imageViewPoet = findViewById(R.id.imageViewPoet);
        textViewPoetName = findViewById(R.id.textViewPoetName);
        textViewPoetInfo = findViewById(R.id.textViewPoetInfo);
        textViewEncrypted = findViewById(R.id.textViewEncrypted);
    }

    private void setupPhoto() {
        try {
            // Проверяем, есть ли фото в ресурсах
            int resourceId = getResources().getIdentifier("pushkin", "drawable", getPackageName());

            if (resourceId != 0) {
                // Фото найдено - загружаем его
                imageViewPoet.setImageResource(resourceId);
                imageViewPoet.setContentDescription("Портрет Александра Пушкина");
            } else {
                // Фото не найдено - используем placeholder
                imageViewPoet.setImageResource(R.drawable.poet_placeholder);
                imageViewPoet.setContentDescription("Заглушка для портрета Пушкина");
            }
        } catch (Exception e) {
            // В случае ошибки используем стандартную иконку
            imageViewPoet.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    private void saveEncryptedData(SharedPreferences securePrefs) {
        // Сохраняем информацию о поэте
        securePrefs.edit()
                .putString(KEY_POET_NAME, "Александр Сергеевич Пушкин")
                .putString(KEY_POET_INFO, "Величайший русский поэт, драматург и прозаик.\n" +
                        "✓ Дата рождения: 6 июня 1799 г.\n" +
                        "✓ Место рождения: Москва\n" +
                        "✓ Дата смерти: 10 февраля 1837 г.")
                .apply();
    }

    private void loadAndDisplayData(SharedPreferences securePrefs) {
        String poetName = securePrefs.getString(KEY_POET_NAME, "Александр Пушкин");
        String poetInfo = securePrefs.getString(KEY_POET_INFO, "Русский поэт");

        textViewPoetName.setText(poetName);
        textViewPoetInfo.setText(poetInfo);
        textViewEncrypted.setText("✓ Данные защищены AES-256\n✓ Файл настроек зашифрован");
    }
}