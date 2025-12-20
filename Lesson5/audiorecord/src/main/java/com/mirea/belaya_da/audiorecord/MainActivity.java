package com.mirea.belaya_da.audiorecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mirea.belaya_da.audiorecord.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 200;
    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private boolean isWork = false;
    private String recordFilePath = null;

    private Button recordButton = null;
    private Button playButton = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    private boolean isStartRecording = true;
    private boolean isStartPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация кнопок через binding
        recordButton = binding.recordButton;
        playButton = binding.playButton;

        // Изначально кнопка воспроизведения отключена
        playButton.setEnabled(false);

        // Путь для сохранения аудиофайла
        recordFilePath = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "audiorecordtest.3gp").getAbsolutePath();

        Log.d(TAG, "Путь записи: " + recordFilePath);

        // Проверка разрешений
        checkPermissions();

        // Настройка обработчиков кнопок
        setupButtons();
    }

    private void checkPermissions() {
        // Проверяем наличие разрешений
        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
            Log.d(TAG, "Разрешения уже предоставлены");
        } else {
            // Запрашиваем разрешения
            String[] permissions = {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }
    }

    private void setupButtons() {
        // Обработчик кнопки записи
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartRecording) {
                    // Начинаем запись
                    if (isWork) {
                        recordButton.setText("Остановить запись");
                        playButton.setEnabled(false);
                        startRecording();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Нет разрешений для записи",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Останавливаем запись
                    recordButton.setText("Начать запись");
                    playButton.setEnabled(true);
                    stopRecording();
                }
                isStartRecording = !isStartRecording;
            }
        });

        // Обработчик кнопки воспроизведения
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartPlaying) {
                    // Начинаем воспроизведение
                    playButton.setText("Остановить воспроизведение");
                    recordButton.setEnabled(false);
                    startPlaying();
                } else {
                    // Останавливаем воспроизведение
                    playButton.setText("Воспроизвести");
                    recordButton.setEnabled(true);
                    stopPlaying();
                }
                isStartPlaying = !isStartPlaying;
            }
        });
    }

    /**
     * Начинает запись аудио
     */
    private void startRecording() {
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(recordFilePath);

            recorder.prepare();
            recorder.start();

            Log.d(TAG, "Запись начата: " + recordFilePath);
            Toast.makeText(this, "Запись начата", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.e(TAG, "Ошибка подготовки записи: " + e.getMessage());
            Toast.makeText(this, "Ошибка записи", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Останавливает запись аудио
     */
    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            Log.d(TAG, "Запись остановлена");
            Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Начинает воспроизведение аудио
     */
    private void startPlaying() {
        try {
            player = new MediaPlayer();
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();

            Log.d(TAG, "Воспроизведение начато");
            Toast.makeText(this, "Воспроизведение", Toast.LENGTH_SHORT).show();

            // Обработчик завершения воспроизведения
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playButton.setText("Воспроизвести");
                    recordButton.setEnabled(true);
                    isStartPlaying = true;
                    stopPlaying();
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Ошибка воспроизведения: " + e.getMessage());
            Toast.makeText(this, "Ошибка воспроизведения", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Останавливает воспроизведение аудио
     */
    private void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;

            Log.d(TAG, "Воспроизведение остановлено");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION) {
            // Проверяем результаты запроса разрешений
            if (grantResults.length >= 2) {
                boolean audioGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                isWork = audioGranted && storageGranted;

                if (isWork) {
                    Log.d(TAG, "Разрешения предоставлены");
                    Toast.makeText(this,
                            "Разрешения получены. Можете начать запись",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Необходимы разрешения для микрофона и хранилища",
                            Toast.LENGTH_LONG).show();
                    Log.w(TAG, "Разрешения не предоставлены");
                }
            }
        }

        // Если разрешения не получены, закрываем приложение
        if (!isWork) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Освобождаем ресурсы при выходе из приложения
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Очищаем ссылку на binding
    }
}