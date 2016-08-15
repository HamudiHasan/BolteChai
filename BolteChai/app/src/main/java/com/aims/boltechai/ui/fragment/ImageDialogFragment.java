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
public class ImageDialogFragment extends DialogFragment implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int GALLERY_REQUEST_CODE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_CAMERA = 0;
    private View view;
    private Button btnOpenGallery;
    private Button btnOpenCamera;
    private Context context;
    private OnPhotoSelectionFromGalleryListener onPhotoSelectionFromGalleryListener;
    private Uri uri;
    public static final int REQUEST_CAMERA_PERMISSION = 5;

    public ImageDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_photo_chooser, container, false);
        btnOpenCamera = (Button) view.findViewById(R.id.btnTakePhotoFromCamera);
        btnOpenGallery = (Button) view.findViewById(R.id.btnUploadFromGallery);
        context = getActivity();
       // getDialog().setTitle("Select Photo");

        btnOpenCamera.setOnClickListener(this);
        btnOpenGallery.setOnClickListener(this);

        return view;
    }

    public void setOnPhotoSelectionFromGalleryListener(OnPhotoSelectionFromGalleryListener onPhotoSelectionFromGalleryListener) {
        this.onPhotoSelectionFromGalleryListener = onPhotoSelectionFromGalleryListener;
    }

    @Override
    public void onClick(View v) {

        if (btnOpenCamera == v) {
            Toast.makeText(context, "Opening Camera....", Toast.LENGTH_SHORT).show();

                final String permission = Manifest.permission.CAMERA;
                if (ContextCompat.checkSelfPermission(getActivity(), permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                        // Show permission rationale
                    } else {
                        // Handle the result in Activity#onRequestPermissionResult(int, String[], int[])
                        ActivityCompat.requestPermissions(getActivity(), new String[]{
                                permission,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                    }
                } else {
                    // Start CameraActivity
                    openCameraForPhoto();
                }
        } else if (btnOpenGallery == v) {
            Toast.makeText(context, "Opening Gallery....", Toast.LENGTH_SHORT).show();
            openGalleryForPhoto();
        }
    }

    private void openCameraForPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent startCustomCameraIntent = new Intent(getActivity(), CameraActivity.class);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void openGalleryForPhoto() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select photo"), GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {

            if (requestCode == GALLERY_REQUEST_CODE || requestCode == REQUEST_CAMERA) {
                uri = data.getData();
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE ) {
                uri = data.getData();
            }
            onPhotoSelectionFromGalleryListener.onPhotoSelect(this, uri);
            this.dismiss();
        }


    }

    public interface OnPhotoSelectionFromGalleryListener {
        void onPhotoSelect(DialogFragment tag, Uri uri);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ImageDialogFragment.REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCameraForPhoto();
                } else {
                    //code for deny
                    Toast.makeText(getContext(),"Couldn't open camera. Permission denied by the user",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
