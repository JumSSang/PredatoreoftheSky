package com.jjumsang.predatorsofthesky.View.Store;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;
import com.jjumsang.predatorsofthesky.R;
import com.jjumsang.predatorsofthesky.Values.StoreNpcState;
import com.jjumsang.predatorsofthesky.View.Ready_Room_View.Ready_View;
import com.jjumsang.predatorsofthesky.View.Ready_Room_View.SumInfo;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.immortal.Graphic_image;
import com.jjumsang.predatorsofthesky.immortal.IState;
import com.jjumsang.predatorsofthesky.immortal.ScreenAnimation;

import java.io.IOException;

/**
 * Created by 경민 on 2015-08-03.
 */
class testRect
{
    Rect rect;
    Paint paint;
    testRect(Rect r)
    {
        this.rect=r;
        paint=new Paint();
        this.paint.setColor(Color.argb(255,(int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
    }
}
public class StoreView  implements IState {

    public CardManager cards;
    StoreNPC_Manager m_npc; //npc제어부분

    Rect InterfaceRect; //임시 인터페이스 사각형이다.
    public int m_leftCardCount=0; //카드 인터페이스에서 왼쪽으로 누적된 카드의 숫자를 의미
    public float downx;
    public float downy;
    public Graphic_image m_storeback;
    Typeface m_typeface;
    Paint mainpaint;
    SumInfo m_SumImfoRender;
    RectF 골드로유닛구매;
    RectF 크리스탈로유닛구매;
    RectF 유닛활성화버튼;
    RectF 골드충전;
    RectF 크리스탈충전;
    RectF 나가기;
    Paint m_SotreFont;
    Paint mGoldPaint;
    Paint m테두리페인트;
    Paint paint;
    private double timeDelta;
    double currentTime;
    double m_time = 0;

    ScreenAnimation m_fadein;
    @Override
    public void Init() {

        paint=new Paint();
        InterfaceRect = new Rect(100, 200, 1000, 200 + 120);
        m_storeback = new Graphic_image(AppManager.getInstance().getBitmap(R.drawable.store_background));
        m_typeface = Typeface.createFromAsset(AppManager.getInstance().getResources().getAssets(), "fonts/SangSangTitle.ttf");
        DBManager.getInstance().npc_state=StoreNpcState.s_welcome;
        GraphicManager.getInstance().Init();
        m_npc=new StoreNPC_Manager();
        //  Card=new ArrayList<CardClass>();
        cards = new CardManager();
        m_storeback.resizebitmap((int) cards.m_Width, (int) cards.m_Height / 40 * 39);
        m_SotreFont=new Paint();
        m_SotreFont.setColor(Color.BLACK);
        m_SotreFont.setTextSize(30);
        m_SotreFont.setTypeface(m_typeface);

        mainpaint = new Paint();
        mainpaint.setTextSize(30);
        mainpaint.setColor(Color.WHITE);
        mainpaint.setTypeface(m_typeface);


        mGoldPaint= new Paint();
        mGoldPaint.setTextSize(30);
        mGoldPaint.setColor(Color.YELLOW);
        mGoldPaint.setTypeface(m_typeface);

        m테두리페인트=new Paint();
        m테두리페인트.setTextSize(32);
        m테두리페인트.setColor(Color.BLACK);
        m테두리페인트.setTypeface(m_typeface);


        RectAdd();
        m_SumImfoRender = new SumInfo((int) cards.m_Width, (int) cards.m_Height); //썸네일 구현부분
        골드로유닛구매 = new RectF(cards.m_Width / 40 * 24, cards.m_Height / 20 * 14, cards.m_Width / 40 * 28, cards.m_Height / 20 * 18);
        크리스탈로유닛구매 = new RectF(cards.m_Width / 40 * 29, cards.m_Height / 20 * 14, cards.m_Width / 40 * 33, cards.m_Height / 20 * 18);
        유닛활성화버튼 = new RectF(cards.m_Width / 40 * 34, cards.m_Height / 20 * 14, cards.m_Width / 40 * 38, cards.m_Height / 20 * 18);
        나가기=new RectF(cards.m_Width/20*18, cards.m_Height/20*1,cards.m_Width/20*18+100,cards.m_Height/20*1+100);


        paint.setColor(Color.WHITE);


        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        m_fadein=new ScreenAnimation((int)GraphicManager.getInstance().m_Width,(int)GraphicManager.getInstance().m_Height);
        m_fadein.InitFadeIn();
        m_fadein.setAnimationState(true);
    }

    public void RectAdd()
    {


        for(int i=0;i< DBManager.getInstance().나의유닛목록.size();i++)
        {
            cards.cardAdd(DBManager.getInstance().나의유닛목록.get(i));
        }

    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
            cards.Update();

        double newTime = System.currentTimeMillis() / 1000.0;
        timeDelta = newTime - currentTime;
        currentTime = newTime;
        m_time += timeDelta;
        m_fadein.fadeInUpdate(timeDelta);
        if(DBManager.getInstance().shop_message_state==true)
        {
            callTexr();
        }

    }
    public void callTexr()
    {
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 Toast.makeText (AppManager.getInstance().getGameView().getContext(),""+DBManager.getInstance().shop_message, Toast.LENGTH_LONG).show();
                 DBManager.getInstance().shop_message_state=false;
                 DBManager.getInstance().shop_message=null;
                // 내용

            }
        }, 0);


    }

    @Override
    public void Render(Canvas canvas) {


       // GraphicManager.getInstance().background1.Draw(canvas);

        //canvas.drawRect(0,cards.m_Height/20*10,cards.m_Width,cards.m_Height,paint);
try {

    canvas.drawRect(0, 0, cards.m_Width, cards.m_Height, mainpaint);


    cards.onDraw(canvas);//카드들 그려주는부분
    m_storeback.Draw(canvas);
    // canvas.drawText("상점",0,cards.m_Height/20*3,mainpaint);
    // canvas.drawRect(0,cards.m_Height/20*5,cards.m_Width/40*14,cards.m_Height/20*14,m테두리페인트); //상점 npc 모습 나올 영역
    canvas.drawRoundRect(new RectF(0, cards.m_Height / 20 * 5, cards.m_Width / 40 * 14, cards.m_Height / 40 * 27), 10, 10, m테두리페인트);//상점 npc 모습 나올 영역
    canvas.drawRoundRect(new RectF(cards.m_Width / 40 * 6, cards.m_Height / 20 * 6, cards.m_Width / 40 * 13, cards.m_Height / 40 * 26), 10, 10, mainpaint);//상점 npc 말풍선 영역
    m_npc.onDraw(canvas,DBManager.getInstance().npc_state, (int) (cards.m_Width / 40 * 7), (int) (cards.m_Height / 20 * 7), m테두리페인트); //npc 대사 부분


    m_SumImfoRender.Draw(canvas); //유저 초상화 랜더링
    canvas.drawRect(cards.m_Width / 20, cards.m_Height / 20 * 14, cards.m_Width / 20 + cards.m_Width / 20 * 10, cards.m_Height / 20 * 18, paint);//유닛 정보 나올 UI사각형
    canvas.drawRect(cards.m_Width / 20 * 18, cards.m_Height / 20 * 1, cards.m_Width / 20 * 18 + 100, cards.m_Height / 20 * 1 + 100, paint);//나개미가기 버튼 위치
    canvas.drawRect(cards.m_Width / 40 * 24, cards.m_Height / 20 * 14, cards.m_Width / 40 * 28, cards.m_Height / 20 * 18, paint);//골드 구매 및 업글 버튼 위치
    canvas.drawRect(cards.m_Width / 40 * 29, cards.m_Height / 20 * 14, cards.m_Width / 40 * 33, cards.m_Height / 20 * 18, paint);//크리스탈 구매 버튼 위치
    canvas.drawRect(cards.m_Width / 40 * 34, cards.m_Height / 20 * 14, cards.m_Width / 40 * 38, cards.m_Height / 20 * 18, paint);//유닛 활성화 버튼

    GraphicManager.getInstance().m_stroe_Hani.Draw(canvas, 50, (int) cards.m_Height / 20 * 8);
    // canvas.drawRect(cards.m_Width/40*16,cards.m_Height/40*1,cards.m_Width/40*30,cards.m_Height/40*4,paint);//골드 바 위치
    canvas.drawText("금      화 : " + DBManager.getInstance().GetGold(), cards.m_Width / 40 * 16, cards.m_Height / 40 * 2, mGoldPaint);//골드수치 출력

    // canvas.drawRect(cards.m_Width/40*16,cards.m_Height/40*5,cards.m_Width/40*30,cards.m_Height/40*8,paint);//크리스탈바 위치
    canvas.drawText("크리스탈 : " + DBManager.getInstance().GetCash(), cards.m_Width / 40 * 16, cards.m_Height / 40 * 6, mGoldPaint);//크리스탈바 출력

    //    canvas.drawRect(cards.m_Width/40*6,cards.m_Height/40,cards.m_Width/40*15,cards.m_Height/40*4,paint); //아이디 위치
    canvas.drawText("ID:" + DBManager.getInstance().GetID(), cards.m_Width / 40 * 6, cards.m_Width / 40 + 11, mainpaint);//아이디 출력

    // canvas.drawRect(cards.m_Width/40*1,cards.m_Height/40,cards.m_Width/40*1+cards.m_Height/40*6,cards.m_Height/40*8,paint); //초상화 위치
    canvas.drawText("Lv_10", cards.m_Width / 40 * 4, cards.m_Height / 40 * 7, mainpaint);//레벨 표시할 위치
    canvas.drawText("나가기", cards.m_Width / 20 * 18, cards.m_Height / 20 * 2, mainpaint); //나가기 버튼


    canvas.drawText("이름:" + cards.cardList.get(cards.m_selectCardnumber).유닛정보.name, cards.m_Width / 20, cards.m_Height / 20 * 15, m_SotreFont);
    canvas.drawText("Level" + cards.cardList.get(cards.m_selectCardnumber).유닛정보.level, cards.m_Width / 20, cards.m_Height / 20 * 17, m_SotreFont);
    canvas.drawText("공격력" + cards.cardList.get(cards.m_selectCardnumber).유닛정보.demage, cards.m_Width / 20 * 7, cards.m_Height / 20 * 17, m_SotreFont);

    canvas.drawText("골드 구매", cards.m_Width / 40 * 24, cards.m_Height / 20 * 16, m_SotreFont);
    canvas.drawText("크리스탈", cards.m_Width / 40 * 30, cards.m_Height / 20 * 16, m_SotreFont);
    canvas.drawText("구매", cards.m_Width / 40 * 30, cards.m_Height / 20 * 17, m_SotreFont);
    canvas.drawText("유닛 활성화", cards.m_Width / 40 * 34, cards.m_Height / 20 * 16, m_SotreFont);
    m_fadein.fadeDraw(canvas);
      /*  for(int i=0;i<Card.size();i++)  //모든카드를 그리기 위해서 있는 반복문 +로 카드 위치까지 검사해서 그릴지 안그릴지도 결정한다.
        {
            RectF dest = new RectF(Card.get(i).x, Card.get(i).y, Card.get(i).x + (Card.get(i).myRect.right),
                    Card.get(i).y + Card.get(i).myRect.bottom);

            if(Card.get(i).x+Card.get(i).myRect.right>InterfaceRect.left-5 && Card.get(i).x+Card.get(i).myRect.left<InterfaceRect.right) { //카드의 위치가 인터페이스의 안쪽에있으면 출력할수있도록 설정

                canvas.drawBitmap(Card.get(i).my_image.m_bitmap,  new Rect(0,0,Card.get(i).my_image.m_bitmap.getWidth(),Card.get(i).my_image.m_bitmap.getHeight()), dest, null);
                Card.get(i).state=true;
                //카드 비트맵 이미지 크기를 받고 후에 카드 이미지의 사각형 크기로 축소 확대한 이미지를 보여준다.
                //그냥 쓰면 비트맵 리소스를 많이 차지하여서 이런방식으로 출력하여야 한다.
            }
            else if(Card.get(i).state==true)
            {
                m_leftCardCount+=1;
                Card.get(i).state=false;
            }

            if(dest.right<0)
            {
             //   CardAdd(Card.get(i));
             //   Card.remove(i);

            }
        }*/
    // canvas.drawRect(InterfaceRect,paint);
    //RectF test=new RectF(cards.m_Width/20,cards.m_Height/20*10,cards.m_Width/20*19,cards.m_Height/20*19);
    //canvas.drawRoundRect(test,20,20,paint);
}
catch(RuntimeException e)
{

}

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
        boolean statetimeer = false;

        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                cards.selectCard((int)downx,(int)downy);
                if(나가기.contains(downx,downy))
                {
                    try {
                        DBManager.getInstance().connection.oos.writeObject("exit");//상점으로 나간다고 서버에 알려준다.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DBManager.getInstance().npc_state=StoreNpcState.byebye;
                    DBManager.getInstance().setNetState(NetState.ROBBY);
                    AppManager.getInstance().getGameView().ChangeGameState(new Ready_View());
                    GraphicManager.getInstance().StoreRelease();
                }
                else if(골드로유닛구매.contains(downx,downy))
                {
                    try {
                        DBManager.getInstance().sendMessage("GOLD:"+cards.cardList.get(cards.m_selectCardnumber).유닛정보.type);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*AlertDialog.Builder alert_confirm = new AlertDialog.Builder(AppManager.getInstance().getGameView().getContext());
                    alert_confirm.setMessage("골드구매 미구현").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();*/
                }
                else if(유닛활성화버튼.contains(downx,downy))
                {
                    try {
                        DBManager.getInstance().sendMessage("활성화:"+cards.cardList.get(cards.m_selectCardnumber).유닛정보.type+":"+cards.cardList.get(cards.m_selectCardnumber).유닛정보.활성화+":"+cards.cardList.get(cards.m_selectCardnumber).유닛정보.level);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText (AppManager.getInstance().getGameView().getContext(),"활성화", Toast.LENGTH_LONG).show();
                    if(cards.cardList.get(cards.m_selectCardnumber).유닛정보.활성화==0) {
                        DBManager.getInstance().npc_state = StoreNpcState.active;
                    }
                    else
                    {
                        DBManager.getInstance().npc_state = StoreNpcState.unactiv;
                    }
                    /*AlertDialog.Builder alert_confirm = new AlertDialog.Builder(AppManager.getInstance().getGameView().getContext());
                    alert_confirm.setMessage("활성화!").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'YES'
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();*/
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float m_movex = event.getX();
                //float m_movey = event.getY();
                cards.moveCard(downx-m_movex);
                break;
            case MotionEvent.ACTION_UP:
                downx=0;
                downy=0;
                break;

            }
        return true;
    }
}
