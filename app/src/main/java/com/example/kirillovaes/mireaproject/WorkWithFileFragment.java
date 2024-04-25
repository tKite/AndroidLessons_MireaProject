package com.example.kirillovaes.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kirillovaes.mireaproject.databinding.FragmentWorkWithFileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkWithFileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkWithFileFragment extends Fragment {
    private FragmentWorkWithFileBinding binding;

    public WorkWithFileFragment() {
    }

    public static WorkWithFileFragment newInstance(String param1, String param2) {
        WorkWithFileFragment fragment = new WorkWithFileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private EditText inputField;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkWithFileBinding.inflate(inflater, container, false);
        inputField = binding.inputField;
        FloatingActionButton fabButton = binding.fabButton;
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveDialog();
            }
        });
        Button readButton = binding.readButton;
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = getTextFromFile();
                if (text != null) {
                    inputField.setText(text);
                }
            }
        });
        return binding.getRoot();
    }
    private String fileName = "encrypted_file.txt";
    String key = "secretkey";
    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Сохранить файл");
        View viewInflated = LayoutInflater.from(requireContext()).inflate(R.layout.alertdialog, (ViewGroup) getView(), false);
        final EditText input = viewInflated.findViewById(R.id.input);
        String encryptedText = encrypt(inputField.getText().toString(), key);
        input.setText(encryptedText);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveToFile(encryptedText, fileName);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    private void saveToFile(String content, String fileName) {
        FileOutputStream outputStream;
        try {
            outputStream = requireActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
            Toast.makeText(requireContext(), "Файл сохранен", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
        }
    }

    private String getTextFromFile() {
        FileInputStream file_reader = null;
        try {
            file_reader = requireActivity().openFileInput(fileName);
            byte[] bytes = new byte[file_reader.available()];
            file_reader.read(bytes);
            String encryptedText = new String(bytes);
            return decrypt(encryptedText, key);
        } catch (IOException ex) {
            Toast.makeText(requireContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (file_reader != null)
                    file_reader.close();
            } catch (IOException ex) {
                Toast.makeText(requireContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
    private String encrypt(String text, String key) {
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                int shiftAmount = key.charAt(i % key.length()) - 'a';
                if (c + shiftAmount > 'z') {
                    encryptedText.append((char) (c + shiftAmount - 26));
                } else {
                    encryptedText.append((char) (c + shiftAmount));
                }
            }
            else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    private String decrypt(String text, String key) {
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                int shiftAmount = key.charAt(i % key.length()) - 'a';
                if (c - shiftAmount < 'a') {
                    decryptedText.append((char) (c - shiftAmount + 26));
                } else {
                    decryptedText.append((char) (c - shiftAmount));
                }

            }
            else{
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }
}