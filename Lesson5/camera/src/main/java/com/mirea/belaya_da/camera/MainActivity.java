package com.mirea.belaya_da.camera;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mirea.belaya_da.camera.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final String TAG = "CameraApp";

    private ActivityMainBinding binding;
    private boolean isWork = false; // Флаг наличия разрешений
    private Uri imageUri; // URI созданного файла

    // Launcher для получения результата от камеры
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Проверка разрешений
        checkAndRequestPermissions();

        // Инициализация launcher для камеры
        initCameraLauncher();

        // Обработчик нажатия на ImageView
        setupImageViewClickListener();
    }

    private void checkAndRequestPermissions() {
        // Проверяем наличие разрешений
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
            Log.d(TAG, "Разрешения уже предоставлены");
        } else {
            // Запрашиваем разрешения
            String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }
    }

    private void initCameraLauncher() {
        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Фотография сделана успешно
                            Log.d(TAG, "Фото сохранено: " + imageUri);

                            // Отображаем изображение в ImageView
                            binding.imageView.setImageURI(imageUri);
                            binding.textViewHint.setVisibility(View.GONE);

                            Toast.makeText(MainActivity.this,
                                    "Фото сохранено", Toast.LENGTH_SHORT).show();
                        } else {
                            // Пользователь отменил съемку
                            Log.d(TAG, "Съемка отменена");
                            Toast.makeText(MainActivity.this,
                                    "Съемка отменена", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupImageViewClickListener() {
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWork) {
                    try {
                        // Создаем файл для сохранения фото
                        File photoFile = createImageFile();

                        // Генерируем URI через FileProvider
                        String authorities = "com.mirea.belaya_da.camera.fileprovider";
                        imageUri = FileProvider.getUriForFile(
                                MainActivity.this,
                                authorities,
                                photoFile);

                        // Создаем Intent для вызова системной камеры
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        // Запускаем камеру
                        cameraActivityResultLauncher.launch(cameraIntent);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,
                                "Ошибка создания файла: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Ошибка создания файла", e);
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Нет необходимых разрешений",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * сохраняем фото в файл
     */
    private File createImageFile() throws IOException {
        // Генерируем уникальное имя файла на основе времени
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Получаем директорию для сохранения (внешнее хранилище приложения)
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Создаем временный файл
        File imageFile = File.createTempFile(
                imageFileName,  // префикс имени
                ".jpg",         // расширение
                storageDir      // директория
        );

        Log.d(TAG, "Файл создан: " + imageFile.getAbsolutePath());
        return imageFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 2) {
                // Проверяем, предоставлены ли оба разрешения
                boolean cameraGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                isWork = cameraGranted && storageGranted;

                if (isWork) {
                    Log.d(TAG, "Разрешения предоставлены");
                    Toast.makeText(this,
                            "Разрешения получены. Можете сделать фото",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Необходимы разрешения для камеры и хранилища",
                            Toast.LENGTH_LONG).show();
                    Log.w(TAG, "Разрешения не предоставлены");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Очищаем ссылку на binding
    }
}