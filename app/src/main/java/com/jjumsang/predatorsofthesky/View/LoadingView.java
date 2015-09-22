package com.jjumsang.predatorsofthesky.View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.jjumsang.predatorsofthesky.View.Ready_Room_View.Ready_View;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.R;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.IState;
import com.jjumsang.predatorsofthesky.immortal.ScreenAnimation;
import com.jjumsang.predatorsofthesky.immortal.Sound;

/**
 * Created by 경민 on 2015-05-02.
 */
public class LoadingView implements IState {

    ScreenAnimation m_fadeOut;

    double m_ReadyTime=0;
    double currentTime;
    RectF m_loadingRect_dest;
    RectF m_loadingRect_src;
    int m_Width;
    int m_Height;
    Paint[] paint;
    float loadingleft;
    float loadingTop;
    String[] m_readyText;

    private ScreenAnimation fadeOut;

    @Override
    public void Init() {
        GraphicManager.getInstance().Init();
        Sound.getInstance().backgroundPlay(R.raw.intro_bgm);
        DisplayMetrics metrics = AppManager.getInstance().getResources().getDisplayMetrics();
        m_Width = metrics.widthPixels;
        m_Height = metrics.heightPixels;
        AppManager.getInstance().state=AppManager.S_STORY1;
        currentTime = System.currentTimeMillis() / 1000;
        loadingleft=(int)m_Width/40*1;
        loadingTop=(int)m_Height/40*35;
        m_loadingRect_src=new RectF(loadingleft,loadingTop,(float)(loadingleft+m_Width/20*19),loadingTop+20);
        m_loadingRect_dest= new RectF(loadingleft,loadingTop,(float)(loadingleft+m_Width/20*m_ReadyTime),loadingTop+20);


        fadeOut=new ScreenAnimation(m_Width,m_Height);
        fadeOut.InitFadeOut();

        paint=new Paint[5];
        m_readyText=new String[5];
        m_readyText[0]="짐을 꾸리는중..";
        m_readyText[1]="전투 준비중..";
        m_readyText[2]="Tip. 전사타입은 마법사 타입에게 취약합니다.";
        m_readyText[3]="전투 준비가 완료 되었습니다.";

        paint[0]=new Paint();
        paint[1]=new Paint();
        paint[2]=new Paint();
        paint[3]=new Paint();
        paint[4]=new Paint();
        paint[0].setColor(Color.argb(50,200,10,10));
        paint[1].setColor(Color.DKGRAY);

        paint[2].setTextSize(25);
        paint[2].setStrokeWidth(2);
        paint[2].setColor(Color.WHITE);

        paint[3].setTextSize(25);
        paint[3].setColor(Color.BLACK);
        paint[3].setAntiAlias(true);
        paint[3].setStyle(Paint.Style.STROKE);
        paint[3].setStrokeWidth(4);

    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        if(AppManager.getInstance().state==AppManager.S_LOADING) {
            double newTime = System.currentTimeMillis() / 1000.0;
            double timeDelta = newTime - currentTime;
            currentTime = newTime;

            if(m_ReadyTime<19) {
                m_ReadyTime += timeDelta;
            }
            if (!DBManager.getInstance().GetID().equals("로딩중..")) {
                m_ReadyTime = 18;
            }
            if (m_ReadyTime >=18) {
                fadeOut.fadeOutUpdate(timeDelta);
            }

            if (!fadeOut.getAnimationState()) {
                AppManager.getInstance().state=AppManager.S_ROBBY;
                AppManager.getInstance().getGameView().ChangeGameState(new Ready_View());
            }

        }

    }

    @Override
    public void Render(Canvas canvas) {

        if(AppManager.getInstance().state==AppManager.S_LOADING) {


            GraphicManager.getInstance().background1.Draw(canvas);
            canvas.drawRoundRect(m_loadingRect_src, 5, 5, paint[1]);
            canvas.drawRoundRect(m_loadingRect_dest, 5, 5, paint[0]);

            m_loadingRect_dest = new RectF(loadingleft, loadingTop, (float) (loadingleft + m_Width / 20 * m_ReadyTime), loadingTop + 20);



            fadeOut.fadeDraw(canvas);

            if (m_ReadyTime < 5) {
                canvas.drawText(m_readyText[0], m_Width / 2, loadingTop - 20, paint[3]);
                canvas.drawText(m_readyText[0], m_Width / 2, loadingTop - 20, paint[2]);
            } else if (m_ReadyTime < 10) {

                canvas.drawText(m_readyText[1], m_Width / 2, loadingTop - 20, paint[3]);
                canvas.drawText(m_readyText[1], m_Width / 2, loadingTop - 20, paint[2]);
            } else if (m_ReadyTime < 13) {
                canvas.drawText(m_readyText[2], m_Width / 2, loadingTop - 20, paint[3]);
                canvas.drawText(m_readyText[2], m_Width / 2, loadingTop - 20, paint[2]);
            } else if (m_ReadyTime < 19) {
                canvas.drawText(m_readyText[3], m_Width / 2, loadingTop - 20, paint[3]);
                canvas.drawText(m_readyText[3], m_Width / 2, loadingTop - 20, paint[2]);

            }
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
