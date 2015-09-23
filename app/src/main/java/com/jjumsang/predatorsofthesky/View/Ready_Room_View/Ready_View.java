package com.jjumsang.predatorsofthesky.View.Ready_Room_View;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.view.KeyEvent;

import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.games.Games;
import com.jjumsang.predatorsofthesky.GameActiviry;
import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;
import com.jjumsang.predatorsofthesky.Values.ButtonName;
import com.jjumsang.predatorsofthesky.View.ImageTestView.ImageView;
import com.jjumsang.predatorsofthesky.View.SelectStageView.SelectStageView;
import com.jjumsang.predatorsofthesky.View.Store.StoreView;
import com.jjumsang.predatorsofthesky.View.St_Battle;
import com.jjumsang.predatorsofthesky.View.Story_room.StoryView;
import com.jjumsang.predatorsofthesky.View.Test_GameView.TestView;
import com.jjumsang.predatorsofthesky.immortal.GameView;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.R;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.IState;
import com.jjumsang.predatorsofthesky.immortal.ScreenAnimation;
import com.jjumsang.predatorsofthesky.immortal.Sound;

import android.widget.Toast;

import java.io.IOException;

/**
 * Created by 경민 on 2015-04-18.
 */
public class Ready_View implements IState {


    int m_Width;
    int m_Height;
    Rect btb_startRect;
    Rect btn_sotreRect;
    Rect btn_partbattleRect;
    Rect btn_storyRect;
    Rect btn_settingRect;
    Rect btn_tropyRect;
    private SettingView m_Setting;


    UserInfo m_myImfoRender;
    SumInfo m_SumImfoRender;
    ScreenAnimation m_fadein;
    ScreenAnimation m_fadeout;
    double currentTime;

    @Override

    public void Init() {

        currentTime = System.currentTimeMillis() / 1000;
        AppManager.getInstance().state = AppManager.S_ROBBY;
        GraphicManager.getInstance().Init();
        if(Sound.getInstance().m_MediaPlayer==null) {
            Sound.getInstance().backgroundPlay(R.raw.intro_bgm);
        }
        // Sound.getInstance().backgroundPlay(R.raw.intro_bgm);
        Sound.getInstance().addList(6,R.raw.ready_tictok);
        m_Width = (int) GraphicManager.getInstance().m_Width;
        m_Height = (int) GraphicManager.getInstance().m_Height;

        //버튼 이미지 충돌 영역 생성 부분
        int btn_left = m_Width / 20 * 15;
        int btn_top = m_Height / 20 * 15;
        btb_startRect = new Rect(btn_left, btn_top, btn_left + m_Width / 20 * 5, btn_top + (int) m_Width / 20 * 2);

        int btn_story_left= m_Width / 20;
        int btn_story_top= m_Height /20*15;

        int btn_setting_left=m_Width/20*18;
        int btn_setting_top=0;
        btn_settingRect=new Rect(btn_setting_left,0,btn_setting_left+100,100);

        int btn_tropy_left=m_Width/20*18;
        int btn_tropy_top=m_Height/20*4;
        btn_tropyRect=new Rect(btn_tropy_left,btn_tropy_top,btn_tropy_left+100,btn_tropy_top+100);
        btn_storyRect=new Rect(btn_story_left/40, btn_story_top, btn_story_left+btn_story_left+m_Width/40*4,btn_top+(int)m_Width/20*2 );



        int inven_left = m_Width / 20 * 11;
        int inven_top = m_Height / 20 * 15;
        btn_partbattleRect = new Rect(inven_left, inven_top, inven_left + m_Width / 8, inven_top + (int) m_Height / 5);




        /*btn_story.resizebitmap((int) m_Width / 20* 8, (int) m_Width / 20 * 2);//button을 화면 크기의 20/10으로 초기화.
        btn_story.ButtonInit((int) m_Width / 20 * 4, (int) m_Width / 20 * 2); //ButtonSprite 만들기 위한 함수
        btn_partbattle.resizebitmap((int) m_Width / 20 * 10, (int) m_Width / 20 * 2);//button을 화면 크기의 20/10으로 초기화.
        btn_partbattle.ButtonInit((int) m_Width / 20 * 5, (int) m_Width / 20 * 2); //ButtonSprite 만들기 위한 함수

        btn_Store.resizebitmap((int) m_Width / 20 * 8, (int) m_Width / 20 * 2);//button을 화면 크기의 1/5로 줄인다 .
        btn_Store.ButtonInit((int) m_Width / 20 * 4, (int) m_Width / 20 * 2); //ButtonSprite 만들기 위한 함수*/
        int store_left = m_Width / 10 * 3;
        int store_top = m_Height / 20 * 15;
        btn_sotreRect = new Rect(store_left, store_top, store_left + m_Width / 20 * 4, store_top + (int) m_Width / 20 * 2);


        int part_lett = store_left + m_Width / 20 * 4 - 10;
        int part_top = m_Height / 20 * 15;
        btn_partbattleRect = new Rect(part_lett, part_top, part_lett + m_Width / 20 * 4, part_top + m_Width / 20 * 2);
        m_myImfoRender=new UserInfo(m_Width,m_Height);
        m_SumImfoRender=new SumInfo(m_Width,m_Height); //썸네일 구현부분
        m_fadein=new ScreenAnimation(m_Width,m_Height);
        m_fadein.InitFadeIn();
        m_fadein.setAnimationState(true);
        m_fadeout=new ScreenAnimation(m_Width,m_Height);
        m_fadeout.InitFadeOut();
        m_fadeout.setAnimationState(false);





    }

    @Override
    public void Destroy() {


    }

    @Override
    public void Update() {
        double newTime = System.currentTimeMillis() / 1000.0;
        double timeDelta = newTime - currentTime;
        currentTime = newTime;
        double dt=timeDelta;
        //GraphicManager.getInstance().m_anna_punch.Update(30);
        m_fadein.fadeInUpdate(timeDelta);

        if(m_fadeout.getAnimationState()==true)
        {
            m_fadeout.fadeOutUpdate(timeDelta);
        }

        if(  GraphicManager.getInstance().Button.get(ButtonName.STORE).state_click ==true)
        {

            GraphicManager.getInstance().RobyRelease();
            DBManager.getInstance().setNetState(NetState.SHOP);
            AppManager.getInstance().state=AppManager.S_STORE;
            if(m_fadeout.getAnimationState()==false) {
                AppManager.getInstance().getGameView().ChangeGameState(new StoreView());
                try {
                    DBManager.getInstance().connection.oos.writeObject("상점모드");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

        }
        if( GraphicManager.getInstance().Button.get(ButtonName.TROPY).state_click==true)
        {
            //업적 버튼 눌렀을때
           //super.
            //Intent intent = new Intent(GameView.m_context, MainActivity.class);

           // GameView.m_context.startActivity(intent);

           // GameView.m_context.startActivityForResult()
            //finish();
          //  GameView.m_context.startActivityForR
           // (GameActiviry)GameView.m_context.startActivitiyResult

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            ((GameActiviry)GameView.m_context).startActivityForResult(Games.Achievements.getAchievementsIntent(AppManager.getInstance().mGoogleApiClient),0);
            GraphicManager.getInstance().Button.get(ButtonName.TROPY).state_click=false;


        }
        if(!DBManager.getInstance().GetEnemy().equals("매칭을 시작하기전입니다..") && !DBManager.getInstance().GetEnemy().equals("대전 상대 검색중입니다..") &&!DBManager.getInstance().GetEnemy().equals("검색취소") )
        {
          //  DBManager.getInstance().setNetState(NetState.MULTIGAMESTART);
        //    DBManager.getInstance().setImforDB(DBManager.getInstance().getResponse());
           // GraphicManager.getInstance().btn_start.state_click=false;
            //AppManager.getInstance().getGameView().ChangeGameState(new St_Battle());
        }
        if( GraphicManager.getInstance().Button.get(ButtonName.STORY).state_click==true)
        {

                GraphicManager.getInstance().RobyRelease();
                Sound.getInstance().backgroundRelease();
                AppManager.getInstance().state=AppManager.S_STORY1;
                if(m_fadeout.getAnimationState()==false) {
                    AppManager.getInstance().getGameView().ChangeGameState(new SelectStageView());
                    try {
                        DBManager.getInstance().connection.oos.writeObject("싱글게임");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        DBManager.getInstance().connection.oos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

        GraphicManager.getInstance().m_anna_punch.Update(System.currentTimeMillis());
    }

    @Override
    public void Render(Canvas canvas) {

        Paint redPaint;
        redPaint=new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.STROKE);
        if(AppManager.getInstance().state==AppManager.S_ROBBY) {

            int logo_left = 0;
            int logo_top = m_Height / 20 * 2 + 5; //사실 로고 탑임
            GraphicManager.getInstance().background1.Draw(canvas);
            //GraphicManager.getInstance().btn_start.ButtonDraw(canvas, GraphicManager.getInstance().btn_start.state_click, btb_startRect.left, btb_startRect.top);
            //btn_Store.ButtonDraw(canvas, btn_Store.state_click, btn_sotreRect.left, btn_sotreRect.top); //상점뷰
            //btn_partbattle.ButtonDraw(canvas, btn_partbattle.state_click, btn_partbattleRect.left, btn_partbattleRect.top); //파티 전투
            //btn_story.ButtonDraw(canvas,btn_story.state_click,btn_storyRect.left,btn_storyRect.top);  //스토리 버튼
            GraphicManager.getInstance().Button.get(ButtonName.MULTTI).ButtonDraw(canvas,GraphicManager.getInstance().Button.get(ButtonName.MULTTI).state_click,btb_startRect.left,btb_startRect.top);
            GraphicManager.getInstance().Button.get(ButtonName.STORE).ButtonDraw(canvas,GraphicManager.getInstance().Button.get(ButtonName.STORE).state_click,btn_sotreRect.left,btn_sotreRect.top);
            GraphicManager.getInstance().Button.get(ButtonName.PARTYBATTLE).ButtonDraw(canvas,GraphicManager.getInstance().Button.get(ButtonName.PARTYBATTLE).state_click,btn_partbattleRect.left,btn_partbattleRect.top);
            GraphicManager.getInstance().Button.get(ButtonName.STORY).ButtonDraw(canvas,GraphicManager.getInstance().Button.get(ButtonName.STORY).state_click,btn_storyRect.left,btn_storyRect.top);
            GraphicManager.getInstance().Button.get(ButtonName.SETTING).Draw(canvas,btn_settingRect.left,btn_settingRect.top);
            canvas.drawRect(btn_settingRect,redPaint);
            GraphicManager.getInstance().Button.get(ButtonName.TROPY).Draw(canvas,btn_tropyRect.left,btn_tropyRect.top);
            GraphicManager.getInstance().m_UserView.Draw(canvas, m_Width / 20 * 2, m_Height / 20 * 2); //유저 정보 뷰 출력
            GraphicManager.getInstance().m_Top_Bar.Draw(canvas, 0, 0);
            //gear.Draw(canvas, m_Width / 20 * 18, 0);
            m_myImfoRender.onDraw(canvas);
            m_SumImfoRender.Draw(canvas);
            // GraphicManager.getInstance().m_anna_punch.AnnaEffect(50);
            //  GraphicManager.getInstance().m_anna_punch.Draw(canvas);
            GraphicManager.getInstance().m_anna_punch.EffectDraw(canvas,m_Width/2,m_Height/2);
            if( GraphicManager.getInstance().Button.get(ButtonName.TROPY).state_click==true)
            {
                canvas.drawRect(btn_tropyRect,redPaint);
            }
            if (GraphicManager.getInstance().Button.get(ButtonName.MULTTI).state_click == true && (DBManager.getInstance().GetEnemy().equals("매칭을 시작하기전입니다..") || DBManager.getInstance().GetEnemy().equals("대전 상대 검색중입니다..") || DBManager.getInstance().GetEnemy().equals("검색취소"))) {
                DBManager.getInstance().SetEnemy("대전 상대 검색중입니다..");
            } else if (GraphicManager.getInstance().Button.get(ButtonName.MULTTI).state_click == false && DBManager.getInstance().GetEnemy().equals("대전 상대 검색중입니다..")) {
                DBManager.getInstance().SetEnemy("검색취소");
                try {
                    DBManager.getInstance().connection.oos.writeObject("서치취소");
                    DBManager.getInstance().connection.oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        canvas.drawRect(btn_storyRect,redPaint);
        m_fadein.fadeDraw(canvas);
        m_fadeout.fadeDraw(canvas);

        // canvas.drawText("승리 : 0 ",(int)GraphicManager.getInstance().m_Width/20*8,(int)GraphicManager.getInstance().m_Height/40*33,paint);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void Search() {
        try {
            DBManager.getInstance().connection.oos.writeObject("서치모드");
            DBManager.getInstance().connection.oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        // m_MediaPlayer.start();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (Collusion((int) x, (int) y, btb_startRect)) {//멀티 게임 시작 클릭 부분
                    GraphicManager.getInstance().Button.get(ButtonName.MULTTI).state_click = !GraphicManager.getInstance().Button.get(ButtonName.MULTTI).state_click;
                    if( GraphicManager.getInstance().Button.get(ButtonName.MULTTI).state_click==true)
                    {
                        Sound.getInstance().play(6);
                    }
                    else if(GraphicManager.getInstance().Button.get(ButtonName.MULTTI).state_click==false)
                    {
                        Sound.getInstance().stop(6);
                    }
                    Search();

                }
                if (Collusion((int) x, (int) y, btn_partbattleRect)) {
                    GraphicManager.getInstance().Button.get(ButtonName.PARTYBATTLE).state_click = !GraphicManager.getInstance().Button.get(ButtonName.PARTYBATTLE).state_click;


                }
                if((btn_storyRect.contains((int)x,(int)y)) )
                {
                    m_fadeout.setAnimationState(true);
                    GraphicManager.getInstance().Button.get(ButtonName.STORY).state_click = !GraphicManager.getInstance().Button.get(ButtonName.STORY).state_click;
                }
                if (Collusion((int) x, (int) y, btn_sotreRect)) {
                    m_fadeout.setAnimationState(true);
                    GraphicManager.getInstance().Button.get(ButtonName.STORE).state_click = !GraphicManager.getInstance().Button.get(ButtonName.STORE).state_click;
                }
                if(Collusion((int)x,(int)y,btn_tropyRect))
                {
                    GraphicManager.getInstance().Button.get(ButtonName.TROPY).state_click =! GraphicManager.getInstance().Button.get(ButtonName.TROPY).state_click;
                }
                if(btn_settingRect.contains((int)x,(int)y)) {

                    m_Setting = new SettingView(AppManager.getInstance().getGameView().getContext(),
                            "환경설정",
                            비지엠버튼,
                            효과음버튼);
                    m_Setting.show();

                }
            }
            case MotionEvent.ACTION_UP:


                break;
        }
        return true;
    }
    private View.OnClickListener 비지엠버튼 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(DBManager.getInstance().m_bgm_state==true) {
                DBManager.getInstance().m_bgm_state=false;

                DBManager.getInstance().m_BleftVolume=0.0f;
                DBManager.getInstance().m_Bright_Volume=0.0f;

                Sound.getInstance().backgroundRelease();
                Toast.makeText(AppManager.getInstance().getGameView().getContext(), "배경음악을 끕니다.",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                DBManager.getInstance().m_bgm_state=true;
                DBManager.getInstance().m_BleftVolume=1.0f;
                DBManager.getInstance().m_Bright_Volume=1.0f;

                if(Sound.getInstance().m_MediaPlayer==null) {
                    Sound.getInstance().backgroundPlay(R.raw.intro_bgm);
                }

                Toast.makeText(AppManager.getInstance().getGameView().getContext(), "배경음악을 켭니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener 효과음버튼 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(DBManager.getInstance().m_sound_effect_sate==true) {

                DBManager.getInstance().m_sound_effect_sate=false;


                Toast.makeText(AppManager.getInstance().getGameView().getContext(), "효과음을 끕니다.",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                DBManager.getInstance().m_sound_effect_sate=true;
                Toast.makeText(AppManager.getInstance().getGameView().getContext(), "효과음을 켭니다.",
                        Toast.LENGTH_SHORT).show();
            }

            m_Setting.dismiss();
        }
    };
    public boolean Collusion(int x, int y, Rect r) {
        if (x > r.left && x < r.right && y > r.top && r.bottom > y) {
            return true;
        } else
            return false;
    }
}
