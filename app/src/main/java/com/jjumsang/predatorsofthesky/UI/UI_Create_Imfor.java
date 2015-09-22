package com.jjumsang.predatorsofthesky.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jjumsang.predatorsofthesky.R;
import com.jjumsang.predatorsofthesky.View.Ready_Room_View.SumInfo;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.Graphic_image;

/**
 * Created by GyungMin on 2015-03-21.
 */
public class UI_Create_Imfor {
    private float ScreenWidth=0;
    private float ScreenHeight=0;
    private String m_ID;
    private String m_GuildID;
    private int m_myGold;

    ////
    private String m_enemyID;
    private int m_enemyGold;
    private String m_enemyGuildID;
    private int m_enemyLogo;
    private int m_enemyGuildLogo;
    SumInfo sumLogo;


    ///
    private int m_RoundPoint=0;

    public UI_Create_Imfor(float x, float y)
    {
        ScreenHeight=y;
        ScreenWidth=x;
        sumLogo=new SumInfo((int)ScreenWidth,(int)(ScreenHeight));



    }
    public void  Draw(Canvas canvas)
    {
        sumLogo.Draw(canvas);
        sumLogo.EnemyDraw(canvas);
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        /*
        1. 아이디
        2. 길드
        3. 적의 아아디
        4. 적의 길드
        5. 나의 골드
        6. 적군 골드인데 생략 예정
         */
        m_myGold=1000;
        canvas.drawText("I  D :"+ DBManager.getInstance().GetID(), (int) ScreenWidth / 30*3, (int) (ScreenHeight / 40)*1, paint); //나의 아이디
        canvas.drawText("GUILD:"+DBManager.getInstance().getGuild(),(int)ScreenWidth/30*3,(int)(ScreenHeight/40)*3,paint);  //나의 소속길드
        canvas.drawText("GOLD :"+DBManager.getInstance().GetGold(),(int)ScreenWidth/30*3,(int)(ScreenHeight/40)*5,paint);




      //  paint.setColor(Color.BLACK);

      //  canvas.drawText("???????"+m_enemyGold,(int)ScreenWidth-ScreenWidth/6,(int)(ScreenHeight/20)*3,paint);

    }
    public int GetGold()
    {
        return m_myGold;
    }

    public Graphic_image SetUserLogo(int id)
    {
        Graphic_image temp;
        switch(id)
        {
            case 1:
                temp=new Graphic_image(AppManager.getInstance().getBitmap(R.drawable.rockfish_logo));
                break;
            default:
                temp=new Graphic_image(AppManager.getInstance().getBitmap(R.drawable.rockfish_logo));
                break;
        }
        return temp;
    }
    public void BuyUnit(int id)
    {
        m_myGold-=id;
    }

    public Graphic_image SetGuildLogo(int id)
    {
        Graphic_image temp;
        switch(id)
        {
            case 1:
                temp=new Graphic_image(AppManager.getInstance().getBitmap(R.drawable.logo));
                break;
            default:
                temp=new Graphic_image(AppManager.getInstance().getBitmap(R.drawable.logo));
                break;
        }
        return temp;
    }

    public void SetGold(int _gold)
    {

    }
    public void SetRoundPoint(int _point)
    {
        m_RoundPoint=_point;
    }
    public int GetRoundPoint()
    {
        return m_RoundPoint;
    }
    public void  CreateSize(float x,float y) //UI적절하게 잡아주는 함수
    {
         float setx = 0;
        float sety = y / 6;
    }


}
