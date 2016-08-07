package com.aims.boltechai.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aims.boltechai.R;
import com.desmond.squarecamera.CameraActivity;


/**
 *
 */
public class AudioDialogFragment extends DialogFragment implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int GALLERY_REQUEST_CODE = 1;
    private static final int ACTIVITY_RECORD_SOUND = 2;
    private static final int REQUEST_CAMERA = 0;
    private View view;
    private Button btnOpenGallery;
    private Button btnRecordAudio;
    private Context context;
    private OnAudioSelectionFromGalleryListener onAudioSelectionFromGalleryListener;
    private Uri uri;
    public static final int REQUEST_CAMERA_PERMISSION = 5;

    public AudioDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_audio_chooser, container, false);
        btnRecordAudio = (Button) view.findViewById(R.id.btnRecordAudio);
        btnOpenGallery = (Button) view.findViewById(R.id.btnAudioUploadFromGallery);
        context = getActivity();
        getDialog().setTitle("Select Audio");

        btnRecordAudio.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);

        return view;
    }

    public void setOnAudioSelectionFromGalleryListener(OnAudioSelectionFromGalleryListener onPhotoSelectionFromGalleryListener) {
        this.onAudioSelectionFromGalleryListener = onPhotoSelectionFromGalleryListener;
    }

    @Override
    public void onClick(View v) {

        if (btnRecordAudio == v) {
            Toast.makeText(context, "Opening Recorder....", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION );
            startActivityForResult(intent, ACTIVITY_RECORD_SOUND);


        } else if (btnOpenGallery == v) {
            Toast.makeText(context, "Opening Gallery....", Toast.LENGTH_SHORT).show();
            btnPickAudio();
        }
    }



    private void btnPickAudio() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Audio "), GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {

            if (requestCode == GALLERY_REQUEST_CODE) {
                uri = data.getData();
            }
            if(requestCode == ACTIVITY_RECORD_SOUND){
                Log.d("Resutl", "ACTIVITY_RECORD_SOUND"+data.getData());
               uri = data.getData();
            }
            onAudioSelectionFromGalleryListener.onAudioSelect(this, uri);
            this.dismiss();
        }
        else {
            Toast.makeText(context, "Select a file", Toast.LENGTH_SHORT).show();
        }


    }

    public interface OnAudioSelectionFromGalleryListener {
        void onAudioSelect(DialogFragment tag, Uri uri);
    }
}
