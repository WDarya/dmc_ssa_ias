package com.mirea.belaya_da.internalfilestorage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDate;
    private EditText editTextDescription;
    private Button buttonSave;
    private TextView textViewStatus;

    private static final String FILE_NAME = "memorable_date.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        // Загружаем из
        loadFromFile();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
            }
        });
    }

    private void initViews() {
        editTextDate = findViewById(R.id.editTextDate);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);
        textViewStatus = findViewById(R.id.textViewStatus);
    }

    private void saveToFile() {
        String date = editTextDate.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (date.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        String content = "Памятная дата: " + date + "\n\nОписание:\n" + description;

        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(content.getBytes());
            textViewStatus.setText("Файл сохранен: " + getFilesDir() + "/" + FILE_NAME);
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            textViewStatus.setText("Ошибка сохранения: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String content = new String(bytes);

            // Парсим  файл
            String[] lines = content.split("\n");
            if (lines.length >= 3) {
                String date = lines[0].replace("Памятная дата: ", "");
                StringBuilder description = new StringBuilder();
                for (int i = 2; i < lines.length; i++) {
                    description.append(lines[i]).append("\n");
                }

                editTextDate.setText(date);
                editTextDescription.setText(description.toString().trim());
                textViewStatus.setText("Данные загружены из файла");
            }
        } catch (IOException e) {

        }
    }
}