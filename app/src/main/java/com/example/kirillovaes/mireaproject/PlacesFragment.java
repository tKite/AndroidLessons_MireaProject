package com.example.kirillovaes.mireaproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kirillovaes.mireaproject.databinding.FragmentPlacesBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacesFragment extends Fragment {
    private FragmentPlacesBinding binding;

    public PlacesFragment() {
    }

    public static PlacesFragment newInstance() {
        PlacesFragment fragment = new PlacesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlacesBinding.inflate(inflater, container, false);
        binding.gotobutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.place1.getText().toString();
                TranslateToSecondFragment(text);
            }
        });
        binding.gotobutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.place2.getText().toString();
                TranslateToSecondFragment(text);
            }
        });
        binding.gotobutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.place3.getText().toString();
                TranslateToSecondFragment(text);
            }
        });
        return binding.getRoot();
    }
    @SuppressLint("ResourceType")
    public void TranslateToSecondFragment(String text){
        MapFragment map = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_firstFragment_to_secondFragment, bundle);
    }
}