package com.example.mireaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mireaproject.databinding.FragmentCameraBinding;


public class CameraFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public CameraFragment() { }

    @NonNull
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ImageView imageView;
    Button takePhotoButton;
    Button Descriprion_Button;
    EditText description;
    Bitmap photo;
    private FragmentCameraBinding binding;
    private boolean is_permissions_granted = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("check", "onCreateView");
        binding = FragmentCameraBinding.inflate(getLayoutInflater());

        imageView = binding.profilePhotoView;
        takePhotoButton = binding.updateProfilePhotoButton;
        description = binding.descriptionText;
        Descriprion_Button = binding.DescriptionBut;

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    takePhotoButtonClicked();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            200);
                }
            }
        });
        Descriprion_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        return binding.getRoot();
    }

    private void saveNote() {
        String noteText = description.getText().toString().trim();
        if (!noteText.isEmpty() && photo != null) {
            Toast.makeText(getActivity(), "Описание сохранено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Вы решили не добавлять описание", Toast.LENGTH_SHORT).show();
        }
    }
    public void takePhotoButtonClicked() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 200);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            is_permissions_granted = (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            takePhotoButtonClicked();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,  @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == getActivity().RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }
}