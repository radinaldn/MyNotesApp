package id.topapp.radinaldn.mynotesapp.listener;

import android.view.View;

/**
 * Created by radinaldn on 26/01/19.
 */

public class CustomOnItemClickListener implements View.OnClickListener {

    private int position;
    private OnItemClickCallBack onItemCallback;
    public CustomOnItemClickListener(int position, OnItemClickCallBack onItemCallBack) {
        this.position = position;
        this.onItemCallback = onItemCallBack;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnItemClickCallBack{
        void OnItemClicked(View view, int position);
    }
}
