package com.mirea.belaya_da.mireaproject.ui.profile;

import android.content.SharedPreferences;
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

public class ProfileFragment extends Fragment {
    private EditText etName, etAge, etGroup;
    private Button btnSave;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        etName = view.findViewById(R.id.etName);
        etAge = view.findViewById(R.id.etAge);
        etGroup = view.findViewById(R.id.etGroup);
        btnSave = view.findViewById(R.id.btnSave);

        sharedPreferences = getActivity().getSharedPreferences("profile_prefs", 0);
        loadData();

        btnSave.setOnClickListener(v -> saveData());
        return view;
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", etName.getText().toString());
        editor.putInt("age", Integer.parseInt(etAge.getText().toString()));
        editor.putString("group", etGroup.getText().toString());
        editor.apply();
        Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        etName.setText(sharedPreferences.getString("name", ""));
        etAge.setText(String.valueOf(sharedPreferences.getInt("age", 0)));
        etGroup.setText(sharedPreferences.getString("group", ""));
    }
}