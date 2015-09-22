package com.jjumsang.predatorsofthesky.Game.Card;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.immortal.Graphic_image;

/**
 * Created by 경민 on 2015-08-03.
 */
public class CardClass {
    public Graphic_image my_image;
    public Rect myRect;
    public float x; //카드의 x좌표
    public float y; //카드의 y좌표
    public boolean state=false; //그려주는 상태인지 상태 체크변수
    public int m_cardnumber;
    public DBManager.디비유닛 유닛정보;
    public CardClass(Graphic_image b,Rect r,float x,float y,    DBManager.디비유닛 DBLink)
    {
        my_image=b;
        myRect=r;
        this.x=x;
        this.y=y;
        유닛정보=DBLink;
    }


    public void onDraw(Canvas canvas)
    {
        Paint paint;
        paint=new Paint();
        paint.setColor(Color.BLACK);
        GraphicManager.getInstance().m_Card.Draw(canvas,0,0);
    }

}
