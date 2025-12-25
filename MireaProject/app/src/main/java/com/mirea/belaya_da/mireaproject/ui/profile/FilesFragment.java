package com.mirea.belaya_da.mireaproject.ui.files;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.mirea.belaya_da.mireaproject.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class FilesFragment extends Fragment {
    private EditText etFileName, etContent;
    private Button btnSave, btnLoad;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        etFileName = view.findViewById(R.id.etFileName);
        etContent = view.findViewById(R.id.etContent);
        btnSave = view.findViewById(R.id.btnSave);
        btnLoad = view.findViewById(R.id.btnLoad);

        btnSave.setOnClickListener(v -> saveFile());
        btnLoad.setOnClickListener(v -> loadFile());
        return view;
    }

    private void saveFile() {
        String fileName = etFileName.getText().toString();
        String content = etContent.getText().toString();
        try {
            FileOutputStream fos = getActivity().openFileOutput(fileName, 0);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(getContext(), "Файл сохранён", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {
        String fileName = etFileName.getText().toString();
        try {
            FileInputStream fis = getActivity().openFileInput(fileName);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            etContent.setText(new String(bytes));
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}