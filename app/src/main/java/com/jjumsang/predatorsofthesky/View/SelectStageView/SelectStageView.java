package com.jjumsang.predatorsofthesky.View.SelectStageView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.google.android.gms.maps.model.Circle;
import com.jjumsang.predatorsofthesky.Values.ColorState;
import com.jjumsang.predatorsofthesky.View.Store.StoreView;
import com.jjumsang.predatorsofthesky.View.Story_room.StoryView;
import com.jjumsang.predatorsofthesky.View.Test_GameView.TestView;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.immortal.Graphic_image;
import com.jjumsang.predatorsofthesky.immortal.IState;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

/**
 * Created by gyungmin on 2015-09-21.
 */
public class SelectStageView implements IState {

    Vec2F m_cposition;
    Matrix matrix = new Matrix();
    DisplayMetrics metrics = AppManager.getInstance().getResources().getDisplayMetrics();
    float m_x = 0.0f; //현재 클릭된 x좌표
    float m_y = 0.0f; //현재 클릭된 y좌표
    Paint[] paint;
    RectF[] stage;

    float m_Width;
    float m_Height;
    public int selectstatge=0;

    @Override
    public void Init() {
        m_cposition = new Vec2F(0, 0);
        m_cposition.y = -(GraphicManager.getInstance().m_Height + GraphicManager.getInstance().m_Height / 40);
        GraphicManager.getInstance().SelectStageInit();
        paint = new Paint[20];
        for (int i = 0; i < 20; i++) {
            paint[i] = new Paint();
        }
        stage = new RectF[20];
        int a;
        paint[ColorState.BLACK].setColor(Color.BLACK);
        paint[ColorState.RED].setColor(Color.RED);
        paint[ColorState.BLUE].setColor(Color.BLUE);
        paint[ColorState.WHITE].setColor(Color.WHITE);
        paint[ColorState.YELLOW].setColor(Color.YELLOW);
        paint[ColorState.GREEND].setColor(Color.GREEN);


        m_Width = GraphicManager.getInstance().m_Width;
        m_Height = GraphicManager.getInstance().m_Height;
        stage[0] = new RectF(m_Width / 2, (m_Height + m_Height / 40 * 30), m_Width / 2 + 40, (m_Height + m_Height / 40 * 30) + 40);
        stage[1] = new RectF(m_Width / 3, (m_Height + m_Height / 40 * 20), m_Width / 3 + 40, (m_Height + m_Height / 40 * 20) + 40);
        stage[2] = new RectF(m_Width / 1.5f, (m_Height + m_Height / 40 * 10), m_Width / 1.5f + 40, (m_Height + m_Height / 40 * 10) + 40);
        stage[3] = new RectF(m_Width / 4, (m_Height + m_Height / 40), m_Width / 4 + 40, (m_Height + m_Height / 40) + 40);
        stage[4] = new RectF(m_Width / 20, (m_Height + m_Height / 40 * 35), m_Width / 20 + 40, (m_Height + m_Height / 40 * 35) + 40); //4번은 Test뷰이다.
        stage[5] = new RectF(m_Width / 2, (m_Height + m_Height / 40), m_Width / 2 + 40, (m_Height + m_Height / 40) + 40);
        stage[6] = new RectF(m_Width / 2, (m_Height - m_Height / 40 * 10), m_Width / 2 + 40, (m_Height - m_Height / 40 * 10) + 40);
        stage[7] = new RectF(m_Width / 1.2f, (m_Height - m_Height / 40 * 10), m_Width / 1.2f + 40, (m_Height - m_Height / 40 * 10) + 40);
        stage[8] = new RectF(m_Width / 4, (m_Height - m_Height / 40 * 10), m_Width / 4 + 40, (m_Height - m_Height / 40 * 10) + 40);

        stage[9] = new RectF(m_Width / 2, (m_Height - m_Height / 40 * 20), m_Width / 2 + 40, (m_Height - m_Height / 40 * 20) + 40);
        stage[10] = new RectF(m_Width / 3, (m_Height - m_Height / 40 * 20), m_Width / 3 + 40, (m_Height - m_Height / 40 * 20) + 40);


    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {

    }

    @Override
    public void Render(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.save();
        canvas.setMatrix(matrix);
        canvas.translate(m_cposition.x, m_cposition.y);
        GraphicManager.getInstance().m_StageMap.Draw(canvas);
        canvas.drawRect(stage[0], paint[ColorState.WHITE]);
        canvas.drawRect(stage[1], paint[ColorState.RED]);
        canvas.drawRect(stage[2], paint[ColorState.BLACK]);
        canvas.drawRect(stage[3], paint[ColorState.BLUE]);
        canvas.drawRect(stage[4], paint[ColorState.GREEND]);
        canvas.drawRect(stage[5], paint[ColorState.GREEND]);
        canvas.drawRect(stage[6], paint[ColorState.GREEND]);
        canvas.drawRect(stage[7], paint[ColorState.GREEND]);
        canvas.drawRect(stage[8], paint[ColorState.GREEND]);
        canvas.drawRect(stage[9], paint[ColorState.GREEND]);
        canvas.drawRect(stage[10], paint[ColorState.GREEND]);
        canvas.restore();
        canvas.drawText(""+selectstatge,0,100,paint[0]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        String strMsg = "";
        Log.i("액션" + strMsg, "" + strMsg);
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                m_x = event.getX();
                m_y = event.getY();


                break;
            case MotionEvent.ACTION_MOVE:
                float m_movey = event.getY();
                //float m_movey = event.getY();
                StageMapMove(m_y - m_movey);
                break;
            case MotionEvent.ACTION_UP:
                SelectStage(m_x, m_y);
                m_x = 0;
                m_y = 0;
                break;
        }
        return true;
    }

    public void StageMapMove(float y) //스테이지맵을 이동시켜주는 함수
    {
        m_cposition.y += (-1 * (y * 0.03));
    }

    public void SelectStage(float x, float y) //스테이지를 선택하게 해주는 함수
    {
        for(int i=0;i<10;i++)
        {
            if(stage[i].contains(m_x , m_y - m_cposition.y))
            {
                selectstatge=i;
                if(i==4) //TestView이다.
                {
                    AppManager.getInstance().getGameView().ChangeGameState(new TestView());
                }
                else if(i==0)
                {
                    AppManager.getInstance().getGameView().ChangeGameState(new StoryView());
                }
            }
        }

    }


}
