package com.example.kirillovaes.mireaproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kirillovaes.mireaproject.databinding.ActivityMainBinding;
import com.example.kirillovaes.mireaproject.databinding.FragmentMapBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private MapView mapView = null;
    private FragmentMapBinding binding;
    private String text;
    MyLocationNewOverlay locationNewOverlay;
    double latitude;
    double longitude;


    public MapFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Получаем текст из аргументов фрагмента
        if (getArguments() != null) {
            text = getArguments().getString("text");
            Log.d("gettext", text);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Configuration.getInstance().load(this.getContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()));
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        mapView = binding.mapView;

        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.794229, 37.700772);
        mapController.setCenter(startPoint);

        locationNewOverlay = new MyLocationNewOverlay(new
                GpsMyLocationProvider(requireContext()),mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationNewOverlay);

        Marker marker = new Marker(mapView);
        if (Objects.equals(text, "Mary Wong")) {
            latitude = 55.762308;
            longitude = 37.625837;
        } else if (Objects.equals(text, "Ресторан корейской кухни Hite")) {
            latitude = 55.755008;
            longitude = 37.557713;
        } else if (Objects.equals(text, "Китайская грамота")) {
            latitude = 55.766532;
            longitude = 37.627890;
        }
        marker.setPosition(new GeoPoint(latitude, longitude));
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(requireContext(), text,
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Логирование
        Log.d("MapFragment", "MapView is null: " + (mapView == null));
        Log.d("MapFragment", "Map overlays count: " + mapView.getOverlays().size());
        mapView.getOverlays().add(marker);
        Log.d("MapFragment", "Map overlays count: " + mapView.getOverlays().size());
        marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        Log.d("work", "onCreateView() вызван");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("work", "onResume() вызван");
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
        mapView.setBackgroundColor(Color.RED);
        if (mapView != null) {
            mapView.onResume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("work", "onPause() вызван");
        Configuration.getInstance().save(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }
}