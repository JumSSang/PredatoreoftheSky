package com.jjumsang.predatorsofthesky.View.Ready_Room_View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.jjumsang.predatorsofthesky.R;
/**
 * Created by gyungmin on 2015-09-02.
 */
public class SettingView extends Dialog{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.popup);
        setLayout();
        setTitle(mTitle);
        setContent(mContent);
        setClickListener(mLeftClickListener , mRightClickListener);
    }

    public SettingView(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
    }

    public SettingView(Context context , String title ,
                        View.OnClickListener singleListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mLeftClickListener = singleListener;
    }

    public SettingView(Context context , String title  ,
                        View.OnClickListener leftListener ,	View.OnClickListener rightListener) {
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    private void setTitle(String title){
        mTitleView.setText(title);
    }

    private void setContent(String content){
        mContentView.setText(content);
    }

    private void setClickListener(View.OnClickListener left , View.OnClickListener right){
        if(left!=null && right!=null){
            mBackSound.setOnClickListener(left);
            mEffecSound.setOnClickListener(right);
        }else if(left!=null && right==null){
            mBackSound.setOnClickListener(left);
        }else {

        }
    }

    /*
     * Layout
     */
    private TextView mTitleView;
    private TextView mContentView;
    private Button mBackSound;
    private Button mEffecSound;
    private Button mLogOut;
    private Button mCancel;
    private String mTitle;
    private String mContent;


    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    /*
     * Layout
     */
    private void setLayout(){
        mTitleView = (TextView) findViewById(R.id.tv_title);
        mContentView = (TextView) findViewById(R.id.tv_content);
        mBackSound = (Button) findViewById(R.id.bt_bsound);
        mEffecSound = (Button) findViewById(R.id.bt_esound);
        mLogOut = (Button) findViewById(R.id.bt_logout);
        mCancel = (Button) findViewById(R.id.bt_cancel);
}

}









