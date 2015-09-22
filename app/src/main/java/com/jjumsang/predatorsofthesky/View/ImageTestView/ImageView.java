package com.jjumsang.predatorsofthesky.View.ImageTestView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.immortal.IState;

/**
 * Created by gyungmin on 2015-09-07.
 */
public class ImageView implements IState {
    Rect[] r;
    @Override
    //ap(350,630);
    public void Init() {
       r=new Rect[50];
       int left=0;
        int top=0;
        int right=70;
        int bottom=70;
       for(int i=1;i<45;i++)
       {
            r[i]=new Rect(left,top,right,bottom);
           top+=70;
           bottom+=70;
           if(i%9==0 &&i!=0)
           {
               left+=70;
               right+=70;
               top=0;
               bottom=70;
           }

       }
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {

    }

    @Override
    public void Render(Canvas canvas) {
        Paint paint;
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        GraphicManager.getInstance().m_WorriorUnit.Draw(canvas);
        for(int i=1;i<45;i++)
        {
            canvas.drawRect(r[i],paint);
        }



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
