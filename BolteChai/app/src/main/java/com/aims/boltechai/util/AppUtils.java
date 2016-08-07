package com.aims.boltechai.util;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.common.base.Strings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hhson on 7/29/2016.
 */
public class AppUtils {

    public static File saveImageFile(Uri uri, String imageName, Context context) {

        if (uri == null || Strings.isNullOrEmpty(imageName))
            return null;

        final File imageFile = new File(
                getImageDirectory(context), imageName);
            putImageFileInLocalDir(context, imageFile, uri);

        return imageFile;
    }

    public static File saveAudioFile(Uri uri, String audioName, Context context) {

        if (uri == null || Strings.isNullOrEmpty(audioName))
            return null;

        final File audioFile = new File(
                getAudioDirectory(context), audioName);
        putImageFileInLocalDir(context, audioFile, uri);

        return audioFile;
    }

    public static File getImageDirectory(Context context) {
        return context.getDir("images", Context.MODE_PRIVATE);
    }
    public static File getAudioDirectory(Context context) {
        return context.getDir("audio", Context.MODE_PRIVATE);
    }
    public static boolean putImageFileInLocalDir(Context context, File f, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            FileOutputStream os = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            return true;
        } catch (IOException e) {
            Toast.makeText(context, "Errrrrroooooo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }
}
