package com.jjumsang.predatorsofthesky.View.Store;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.jjumsang.predatorsofthesky.Game.Card.CardClass;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Values.UnitState;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;

import java.util.ArrayList;

/**
 * Created by 경민 on 2015-08-25.
 */
public class CardManager {
    public float m_Width;
    public float m_Height;
    Matrix matrix = new Matrix();
    DisplayMetrics metrics = AppManager.getInstance().getResources().getDisplayMetrics();
    float m_diffX = 0;
    float m_diffY = 0;
    float m_matrix_x = 1.0f;//메트릭스 변화 하는 x비율
    float m_matrix_y = 1.0f;//메트릭스 변화 하는 y비율
    ArrayList<CardClass> cardList;
    public float card_left = 0;
    public int m_selectCardnumber;


    public CardManager() {
        m_Width = metrics.widthPixels;
        m_Height = metrics.heightPixels;
        cardList = new ArrayList<CardClass>();
        m_diffX = m_Width/40*15;

    }

    //카드를 추가 해주는 함수이다.
    public void cardAdd(DBManager.디비유닛 유닛정보) //type을 넘겨주면 그 타입으로 카드를 생성하여 추가한다.
    {

            cardList.add(new CardClass(GraphicManager.getInstance().m_Card, new Rect(0, 0, 200, 300), card_left, m_Height / 20 * 6,유닛정보));
            card_left += 220;

    }

    public void Update() {
        GraphicManager.getInstance().m_ArcherUnit.Update(System.currentTimeMillis());
        GraphicManager.getInstance().m_WorriorUnit.Update(System.currentTimeMillis());
        GraphicManager.getInstance().m_Magician.Update(System.currentTimeMillis());
        GraphicManager.getInstance().m_elsatower.PatrolUpdate(System.currentTimeMillis());
        GraphicManager.getInstance().m_anna.PatrolUpdate(System.currentTimeMillis());



        //   m_diffX-=1;
    }

    public void selectCard(int x,int y)
    {
        for(int i=0;i<cardList.size();i++)
        {

            RectF check=new RectF( m_diffX+cardList.get(i).x, cardList.get(i).y, m_diffX+ cardList.get(i).x + (cardList.get(i).myRect.right),
                    cardList.get(i).y + cardList.get(i).myRect.bottom);
            if(check.contains(x,y))
            {
                cardList.get(i).state=true;
                m_selectCardnumber=i;
            }
        }

    }
    public void moveCard(float x) {
        if (cardList.size() > 6) {
            if (m_diffX < m_Width/40*16) {
                m_diffX += (-1 * (x * 0.03));
            }
            else {
                m_diffX = m_Width/40*15;
            }
            if (cardList.get(cardList.size() - 1).x + cardList.get(0).myRect.right + m_diffX < m_Width - 50) {
                m_diffX +=30;
            }
        }
    }

    public void onDraw(Canvas canvas) {
        try {
            Paint paint;
            paint = new Paint();
            Paint textpaint = new Paint();
            textpaint.setColor(Color.BLACK);

            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            canvas.save();
            canvas.setMatrix(matrix);
            canvas.translate(m_diffX, m_diffY);
            for (int i = 0; i < cardList.size(); i++) {
                RectF dest = new RectF(cardList.get(i).x, cardList.get(i).y, cardList.get(i).x + (cardList.get(i).myRect.right),
                        cardList.get(i).y + cardList.get(i).myRect.bottom);
                canvas.drawBitmap(cardList.get(i).my_image.m_bitmap, new Rect(0, 0, cardList.get(i).my_image.m_bitmap.getWidth(), cardList.get(i).my_image.m_bitmap.getHeight()), dest, null);
                if (cardList.get(i).유닛정보.level == 0) {
                    canvas.drawText("" + cardList.get(i).유닛정보.type + "유닛의 타입번호", cardList.get(i).x + 50, cardList.get(i).y - 20, textpaint);
                }
                if (cardList.get(i).유닛정보.활성화 == 1) {
                    canvas.drawText("활성화", cardList.get(i).x + 50, cardList.get(i).y + m_Height / 40 * 12, textpaint);
                } else {
                    canvas.drawText("비활성화", cardList.get(i).x + 50, cardList.get(i).y + m_Height / 40 * 12, textpaint);
                }

                유닛초상화그리기(canvas, cardList.get(i).유닛정보.type, (int) cardList.get(i).x + 50, (int) (cardList.get(i).y + m_Height / 40 * 4));

                if (i == m_selectCardnumber) {
                    canvas.drawRoundRect(dest, 10, 10, paint);
                }
            }
            canvas.restore();
        }
        catch(NullPointerException e)
        {
            //그려주는 리소스가 없어 생기는 문제임
          //  Log.e("그려줄 이미지가 없성",null);
            Log.e(null,"그려줄이미지가 없성");
        }
    }
    public void 유닛초상화그리기(Canvas canvas,int type,int x,int y)
    {
           switch(type)
           {
               case UnitValue.F_ARCHER:
                   GraphicManager.getInstance().m_ArcherUnit.초상화에서그리기(canvas, x,y);
                   break;
               case UnitValue.F_WORRIOR:
                   GraphicManager.getInstance().m_WorriorUnit.초상화에서그리기(canvas, x,y);
                   break;
               case UnitValue.F_MAGICAIN:
                 //  GraphicManager.getInstance().m_Magician.초상화에서그리기(canvas, x,y);
                   break;
               case UnitValue.F_ANNA:
                   GraphicManager.getInstance().m_anna.초상화에서그리기(canvas,x,y);
                   break;
               case UnitValue.F_BOOM:
                   GraphicManager.getInstance().m_Boom.초상화에서그리기(canvas,x,y);
                   break;
               case UnitValue.F_TOWER:
                   GraphicManager.getInstance().m_ArchorTower.초상화에서그리기(canvas,x,y);
                   break;
               case UnitValue.F_JUMPINGTRAP:
                 //  GraphicManager.getInstance()..Draw(canvas,UnitState.MOVE,x,y);
                   break;
               case UnitValue.F_ZOMBIE:
                   break;
               case UnitValue.F_ELSATOWER:
                   GraphicManager.getInstance().m_elsatower.초상화에서그리기(canvas,x,y);
                   break;
               default:
                   break;
           }
    }
}
