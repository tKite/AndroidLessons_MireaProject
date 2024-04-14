package com.example.mireaproject;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mireaproject.databinding.FragmentMicroBinding;
import java.io.File;
import java.io.IOException;

public class MicroFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MicroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MicrophoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MicroFragment newInstance(String param1, String param2) {
        MicroFragment fragment = new MicroFragment();
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

    private	static	final int	REQUEST_CODE_PERMISSION	=	200;
    private String recordFilePath;
    private Button recordBut;
    private Button playBut;
    private	boolean	isWork;
    boolean	isStartRecording	=	true;
    boolean	isStartPlaying	=	true;
    private	MediaRecorder	recorder	=	null;
    private MediaPlayer player	=	null;
    private FragmentMicroBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMicroBinding.inflate(inflater, container, false);
        recordBut = binding.record;
        playBut = binding.play;
        playBut.setEnabled(false);
        recordFilePath = (new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();
        int audioRecordPermissionStatus = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (audioRecordPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            //	Выполняется	запрос	к	пользователю	на	получение	необходимых	разрешений
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
        recordBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartRecording) {
                    recordBut.setText("Прекратить запись");
                    playBut.setEnabled(false);
                    startRecording();
                } else {
                    recordBut.setText("Начать запись");
                    playBut.setEnabled(true);
                    stopRecording();
                }
                isStartRecording = !isStartRecording;
            }
        });
        playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartPlaying) {
                    playBut.setText("Прекратить проигрывание");
                    recordBut.setEnabled(true);
                    startPlaying();
                } else {
                    playBut.setText("Начать проигрывание");
                    recordBut.setEnabled(false);
                    stopPlaying();
                }
                isStartPlaying = !isStartPlaying;
            }
        });
        return binding.getRoot();
    }
    private	void startRecording(){
        recorder = new	MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(recordFilePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try	{
            recorder.prepare();
        }	catch	(IOException e)	{
            Log.e("check",	"prepare()	failed");
        }
        recorder.start();
    }
    private	void	stopRecording()	{
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    private	void	startPlaying()	{
        player	=	new	MediaPlayer();
        try	{
            player.setDataSource(recordFilePath);
            player.prepare();
            player.start();
        }	catch	(IOException	e)	{
            Log.e("check",	"prepare()	failed");
        }
    }
    private	void	stopPlaying()	{
        player.release();
        player	=	null;
    }
    @Override
    public void onRequestPermissionsResult(int	requestCode, @NonNull String[]	permissions, @NonNull	int[]
            grantResults)	{
        //	производится	проверка	полученного	результата	от	пользователя	на	запрос	разрешения	Camera
        super.onRequestPermissionsResult(requestCode,	permissions,	grantResults);
        switch	(requestCode){
            case REQUEST_CODE_PERMISSION:
                isWork		=	grantResults[0]	==	PackageManager.PERMISSION_GRANTED;
                break;
        }
        if	(!isWork){
            getActivity().finish();
        };
    }

}