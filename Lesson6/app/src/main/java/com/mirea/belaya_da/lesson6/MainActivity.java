package com.mirea.belaya_da.lesson6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextGroup;
    private EditText editTextNumber;
    private EditText editTextMovie;
    private Button buttonSave;
    private TextView textViewStatus;

    private SharedPreferences sharedPref;
    private static final String PREFS_NAME = "user_settings";
    private static final String KEY_GROUP = "group";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        // Инициализация SharedPreferences
        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Загрузка сохраненных данных
        loadSavedData();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void initViews() {
        editTextGroup = findViewById(R.id.editTextGroup);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextMovie = findViewById(R.id.editTextMovie);
        buttonSave = findViewById(R.id.buttonSave);
        textViewStatus = findViewById(R.id.textViewStatus);
    }

    private void loadSavedData() {
        String group = sharedPref.getString(KEY_GROUP, "");
        int number = sharedPref.getInt(KEY_NUMBER, 0);
        String movie = sharedPref.getString(KEY_MOVIE, "");

        editTextGroup.setText(group);
        if (number != 0) {
            editTextNumber.setText(String.valueOf(number));
        }
        editTextMovie.setText(movie);

        if (!group.isEmpty() || number != 0 || !movie.isEmpty()) {
            textViewStatus.setText("Загружены сохраненные данные");
        }
    }

    private void saveData() {
        String group = editTextGroup.getText().toString().trim();
        String numberStr = editTextNumber.getText().toString().trim();
        String movie = editTextMovie.getText().toString().trim();

        if (group.isEmpty() || numberStr.isEmpty() || movie.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int number = Integer.parseInt(numberStr);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(KEY_GROUP, group);
            editor.putInt(KEY_NUMBER, number);
            editor.putString(KEY_MOVIE, movie);
            editor.apply();

            textViewStatus.setText("Данные сохранены!");
            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Номер по списку должен быть числом", Toast.LENGTH_SHORT).show();
        }
    }
}