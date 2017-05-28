package dz.tdm.esi.myapplication.Util;

import android.view.View;

/**
 * Created by Amine on 03/04/2017.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
