package dz.tdm.esi.myapplication.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dz.tdm.esi.myapplication.Activities.AddDossier;
import dz.tdm.esi.myapplication.Activities.FolderUpdate;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Amine on 27/05/2017.
 */

public class ImageTaker {

    public static final String TAG = ImageTaker.class.getSimpleName();
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String IMAGE_DIRECTORY_NAME = "assurVoiture";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static Uri fileUri;

    private static Context context;
    private static ImageView imageView;


    public static void captureImage(Context context, ImageView imageView) {
        ImageTaker.context = context;
        ImageTaker.imageView = imageView;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = ImageTaker.getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        if (context instanceof AddDossier) {
            ((AddDossier) context).startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else if (context instanceof FolderUpdate) {
            ((FolderUpdate) context).startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            Log.e(TAG,"mContext should be an instanceof Activity.");
        }
    }

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    public static String onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                BitmapFactory.Options options = new BitmapFactory.Options();

                // down sizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

                imageView.setImageBitmap(bitmap);

                return fileUri.getPath();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(ImageTaker.context,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(ImageTaker.context,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        return null;
    }

}
