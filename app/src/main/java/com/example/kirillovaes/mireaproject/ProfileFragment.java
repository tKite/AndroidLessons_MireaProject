package com.example.kirillovaes.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kirillovaes.mireaproject.databinding.FragmentProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {



    public ProfileFragment() {
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private SharedPreferences sharedPreferences;
    private FragmentProfileBinding binding;
    private String PREF_NAME = "preferences";
    private String KEY_NAME = "name";
    private String KEY_SURNAME = "surname";
    private String KEY_EMAIL = "e-mail";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    private EditText editName;
    private EditText editSurname;
    private EditText editEmail;
    private Button buttonConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        editName = binding.editName;
        editSurname = binding.editSurname;
        editEmail = binding.editEmail;
        buttonConfirm = binding.button;

        String savedName = sharedPreferences.getString(KEY_NAME, "");
        String savedSurname = sharedPreferences.getString(KEY_SURNAME, "");
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");

        editName.setText(savedName);
        editSurname.setText(savedSurname);
        editEmail.setText(savedEmail);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        return binding.getRoot();
    }

    public void save() {
        String name = editName.getText().toString();
        String surname = editSurname.getText().toString();
        String email = editEmail.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SURNAME, surname);
        editor.putString(KEY_EMAIL, email);
        editor.apply();

        if (sharedPreferences.contains(KEY_NAME)
                && sharedPreferences.contains(KEY_SURNAME)
                && sharedPreferences.contains(KEY_EMAIL)) {
            Toast.makeText(requireContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }
}