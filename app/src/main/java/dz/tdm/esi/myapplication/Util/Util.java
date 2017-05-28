package dz.tdm.esi.myapplication.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Amine on 20/05/2017.
 */

public class Util {

    public static AlertDialog.Builder alert(Context context, String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(s)
                .setTitle("Alerte")
                .setPositiveButton("D\'accord", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        return builder;
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


}
