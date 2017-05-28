package dz.tdm.esi.myapplication.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dz.tdm.esi.myapplication.Activities.AddDossier;
import dz.tdm.esi.myapplication.Activities.FolderUpdate;

import static android.app.Activity.RESULT_OK;
import static dz.tdm.esi.myapplication.Util.ImageTaker.TAG;

/**
 * Created by Amine on 27/05/2017.
 */

public class VideoTaker {

    public static final int REQUEST_VIDEO_CAPTURE = 1;

    private static Context context;
    private static VideoView videoView;
    public static final String IMAGE_DIRECTORY_NAME = "assurVoiture";

    private static Uri fileUri;

    public static void dispatchTakeVideoIntent(Context context, VideoView videoView) {
        VideoTaker.context = context;
        VideoTaker.videoView = videoView;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = VideoTaker.getOutputMediaFileUri(REQUEST_VIDEO_CAPTURE);

        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        if (takeVideoIntent.resolveActivity((context).getPackageManager()) != null) {
            if (context instanceof AddDossier) {
                ((AddDossier) context).startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            } else if (context instanceof FolderUpdate) {
                ((FolderUpdate) context).startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            } else {
                Log.e(TAG,"mContext should be an instanceof Activity.");
            }
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
        if (type == REQUEST_VIDEO_CAPTURE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VIDEO_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            VideoTaker.videoView.setVideoURI(fileUri);
            return fileUri.getPath();
        }

        return null;
    }

}
