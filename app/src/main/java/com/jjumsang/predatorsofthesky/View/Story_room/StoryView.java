package com.jjumsang.predatorsofthesky.View.Story_room;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.jjumsang.predatorsofthesky.Game.ActiveCollusion;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Bounding;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.CreateUnit;
import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;
import com.jjumsang.predatorsofthesky.Values.MapState;
import com.jjumsang.predatorsofthesky.View.Ready_Room_View.Ready_View;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.Game.PathFinder;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitManager;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.R;
import com.jjumsang.predatorsofthesky.UI.UI_Create_Bottom;
import com.jjumsang.predatorsofthesky.UI.UI_Create_Imfor;
import com.jjumsang.predatorsofthesky.UI.UnitList;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.IState;
import com.jjumsang.predatorsofthesky.immortal.ScreenAnimation;
import com.jjumsang.predatorsofthesky.immortal.Sound;
import com.jjumsang.predatorsofthesky.immortal.TextEffect;

import java.io.IOException;
import java.util.ArrayList;

public class StoryView implements IState {

    private final PathFinder finderOjbect = new PathFinder();
    private int m_SaraSay = 0;
    private int m_timer = 0;
    private double m_batch_gameTime = 5;
    private final int GAMESTART = 0;
    private final int SINARIO = 1;
    private int m_WhoWIn = 0;

    private int my_view = SINARIO;
    private int m_plot = 0;


    Matrix matrix = new Matrix();

    private ArrayList<UnitList> UnitDataList;
    private UI_Create_Bottom UI;
    private UI_Create_Imfor UI_imfor;
    public int m_UI_Touch_Postion = 0;
    private Story_String m_talk;
    private ScreenAnimation fade_in;
    private ScreenAnimation fade_out;
    private double timeDelta;
    Paint paint;
    float x;
    float y;
    float m_click_x = 0; //첫번째 터치좌표 x
    float m_click_y = 0; //첫번째 터치좌표 y
    float m_click2_x = 0; //2번째 터치 좌표 x
    float m_click2_y = 0; //2번째 터치 좌표 y
    /////////////////////

    public float FrameCount=0;

    float m_matrix_x = 1.0f;//메트릭스 변화 하는 x비율
    float m_matrix_y = 1.0f;//메트릭스 변화 하는 y비율


    public float m_Width;
    public float m_Height;

    float m_movex = 0;
    float m_movey = 0;
    float m_diffX = 0;
    float m_diffY = 0;
    float m_distx = 0;
    float m_disty = 0;
    ////////////////////////////
    // 드래그 모드인지 핀치줌 모드인지 구분
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    // 드래그시 좌표 저장
    int mode = NONE;
    int posX1 = 0, posX2 = 0, posY1 = 0, posY2 = 0;
    // 핀치시 두좌표간의 거리 저장
    float oldDist = 1f;
    float newDist = 1f;
    ///////////////////////////////////////////
    double currentTime;
    double m_time = 0;
    double m_thread_tiem = 0;
    double m_OpenServerTime = 0;
    double m_checkLoader = 0;
    double fade_endTime = 0;
    UnitManager Units;

    TextEffect m_airtext, m_airtext1, m_airtextVic, m_airTextFebe;

    public void sendMessage(String a) throws IOException {
        DBManager.getInstance().connection.oos.writeObject(a);
        DBManager.getInstance().connection.oos.flush();
    }

    @Override
    public void Init() {
        DBManager.getInstance().setNetState(NetState.SINGLEGAME);;
        try {
            sendMessage("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bounding.tileColl = new ArrayList<ActiveCollusion>();
        m_talk = new Story_String();
        AppManager.getInstance().state = AppManager.S_STORY1;
        GraphicManager.getInstance().Init();
        Sound.getInstance().addList(1, R.raw.buildingsaw);
        Sound.getInstance().backgroundPlay(R.raw.story_1);
        Sound.getInstance().addList(2, R.raw.smallchain);
        Sound.getInstance().addList(3, R.raw.zombie_create_sound);
        Sound.getInstance().addList(4, R.raw.hello);
        Sound.getInstance().addList(5, R.raw.smash);
        Sound.getInstance().addList(6, R.raw.text_call);

        currentTime = System.currentTimeMillis() / 1000;

        UnitDataList = new ArrayList<UnitList>();
        DisplayMetrics metrics = AppManager.getInstance().getResources().getDisplayMetrics();
        m_Width = metrics.widthPixels;
        m_Height = metrics.heightPixels;
        Units = new UnitManager();
        matrix.setScale(m_matrix_x, m_matrix_y);
        InitMap();
        GraphicManager.getInstance().background.resizebitmap((int) (m_Width * 2), (int) (m_Height * 2));
        UnitAdd(); //데이터 베이스로 부터 유닛 목록 받아온다.
        UI = new UI_Create_Bottom(m_Width, m_Height, UnitDataList.size(), 0, UnitDataList);
        //id,gold,gname,log,glogo
        UI_imfor = new UI_Create_Imfor(m_Width, m_Height);
        Unit Mtemp;
        Mtemp = new Unit(GraphicManager.getInstance().mTownHall.m_bitmap);


        GraphicManager.getInstance().m_airplane.Air(5);
        fade_in = new ScreenAnimation((int) m_Width, (int) m_Height);
        fade_out = new ScreenAnimation((int) m_Width, (int) m_Height);
        fade_out.InitFadeOut();
        fade_in.InitFadeIn();
        m_airtext = new TextEffect("배치시작!");
        m_airtext1 = new TextEffect("전투시작!");
        m_airtextVic = new TextEffect("전투 승리!", 0);
        m_airTextFebe = new TextEffect("전투 패배...", 0);
        LoadingDBMap(DBManager.getInstance().m_StringMap);
        LoadEnemy();
    }

    public void InitMap() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                ActiveCollusion temp = new ActiveCollusion();
                temp.addSpot(750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i) + 12);//p1 왼쪽
                temp.addSpot(750 + 50 / 2 * (j - i) + 25, -300 + 25 / 2 * (j + i));//p2 //위
                temp.addSpot(750 + 50 / 2 * (j - i) + 50, -300 + 25 / 2 * (j + i) + 12);//p3 오른쪽
                temp.addSpot(750 + 50 / 2 * (j - i) + 25, -300 + 25 / 2 * (j + i) + 25);//p4 아래
                temp.distanceCal();
                Bounding.tileColl.add(temp);

                if ((i + j) % 2 == 0) {
                    UnitValue.m_bmap[i][j]= MapState.Move;
                    UnitValue.m_dmap[i][j] = 1;
                } else {
                    UnitValue.m_bmap[i][j]= MapState.Move;
                    UnitValue.m_dmap[i][j] = 2;
                }
                if (i == 0 || i == 49 || j == 0 || j == 49) {
                    UnitValue.m_dmap[i][j] = 4;
                }

            }
        }
    }

    //덱 창안에 유닛을 더하는 역할을 한다.
    public void UnitAdd() {
        for(int i=0;i<DBManager.getInstance().나의유닛목록.size();i++)
        {
            if(DBManager.getInstance().나의유닛목록.get(i).활성화==1) {
                UnitDataList.add(new UnitList(DBManager.getInstance().나의유닛목록.get(i)));
            }
        }


    }

    public void LoadingDBMap(String a) {

        //맵에 내용 있을 경우에만 셋팅을 한다.

        try
        {
            String[] result = a.split("=");
            String[] imfors;
            for (int i = 0; i < result.length; i++) {

                int x = 0;
                int y = 0;
                int level = 0;
                int type = 0;
                imfors = result[i].split("a");

                x = Integer.parseInt(imfors[0]);
                y = Integer.parseInt(imfors[1]);
                level = Integer.parseInt(imfors[2]);
                type = Integer.parseInt(imfors[3]);
                switch (type) {
                    case UnitValue.F_TOWER:
                        CreateUnit.CreateArchorTower(x, y, Units.MyUnits, Units.EnemyUnits, false);
                        break;
                    case UnitValue.F_JUMPINGTRAP:
                        break;
                    case UnitValue.F_ZOMBIE:
                        break;
                    case UnitValue.F_GOLDRUN:
                        break;
                    case UnitValue.F_ELSATOWER:
                        Unit temp = new Unit(GraphicManager.getInstance().mElsa_Tower.m_bitmap);
                        CreateUnit.CreateMagicTower(x, y, temp, Units.MyUnits, Units.EnemyUnits, false);
                        break;
                    case UnitValue.F_ANNA:

                        CreateUnit.CreateAnna(x, y, Units.MyUnits, Units.EnemyUnits, false);
                        break;
                    case UnitValue.F_TOWNHALL:
                        break;
                    case UnitValue.F_TREE1:
                        CreateUnit.CreateTree1(x, y, Units.Enviroment);
                        break;

                    case UnitValue.F_ROCK1:
                        CreateUnit.CreateRock(x, y, Units.Enviroment);

                        break;
                    case UnitValue.F_ROCKE2:
                        CreateUnit.CreateRock2(x, y, Units.Enviroment);
                        break;
                    case UnitValue.F_BOOM:
                        CreateUnit.CreateBoom(x, y, Units.EnemyUnits);

                        break;
                }
            }


        }
        catch(NullPointerException e)
        {

        }


    }

    @Override
    public void Destroy() {
    }

    @Override
    public void Update() {

        if (AppManager.getInstance().state != AppManager.S_LOADING) {
            // long frameEndTime = System.currentTimeMillis();
            //long delta = frameEndTime - frameStartTime;
            double newTime = System.currentTimeMillis() / 1000.0;
            timeDelta = newTime - currentTime;
            currentTime = newTime;
            m_time += timeDelta;
            m_thread_tiem += timeDelta;

            if (my_view == SINARIO) {
                GraphicManager.getInstance().m_airplane.Update(System.currentTimeMillis());
                if (m_SaraSay > 11) {
                    fade_out.fadeOutUpdate(timeDelta);
                }
            }
            if (my_view == GAMESTART) {

                if (fade_endTime > 2)
                    fade_in.fadeInUpdate(timeDelta);
                else {
                    fade_endTime += timeDelta;
                }

                Units.Update(timeDelta);


            }
            // fade_in.fadeOutUpdate(timeDelta);

            //GraphicManager.getInstance().m_effect.Update(System.currentTimeMillis());
            if (m_airtext.getState() == true) {
                m_batch_gameTime -= timeDelta;
            }

        }
    }

    //m_time=m_time*1000;

    public void SenariDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        GraphicManager.getInstance().background.Draw(canvas, -750, -450);
        GraphicManager.getInstance().m_Chat_View.Draw(canvas, 0, (int) m_Height / 20 * 15);
        GraphicManager.getInstance().m_Sara.Draw(canvas, (int) m_Width / 20 * 14, (int) m_Height / 20 * 10);

        if (m_SaraSay > 11) {
            //canvas.drawRect(0,0,m_Width,m_Height,paint);
            fade_out.fadeDraw(canvas);

            if (!fade_out.getAnimationState()) {
                my_view = GAMESTART;
                my_view = GAMESTART;
                Sound.getInstance().backgroundRelease();
              // Sound.getInstance().backgroundPlay(R.raw.battle_bgm);
                m_SaraSay = 0;
                return;
            }
        } else {
            String temp = m_talk.getSara1(m_SaraSay);
            String[] result = temp.split("#");
            for (int i = 0; i < result.length; i++) {
                if (i == 0)
                    canvas.drawText(result[i], m_Width / 20, m_Height / 20 * 17, paint);
                else if (i == 1) {
                    canvas.drawText(result[i], m_Width / 20, m_Height / 20 * 18, paint);
                } else if (i == 2) {
                    canvas.drawText(result[i], m_Width / 20, m_Height / 20 * 19, paint);
                }
            }
        }


    }

    public void GameDraw(Canvas canvas) {
        Paint paint;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.save();
        canvas.setMatrix(matrix);
        canvas.translate(m_diffX, m_diffY);
        //타일 한번 깔아준다.
        GraphicManager.getInstance().background.Draw(canvas, -750, -450);
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (UnitValue.m_dmap[i][j] == 1) {
                    GraphicManager.getInstance().temptile1.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));

                } else if (UnitValue.m_dmap[i][j] == 2) {
                    GraphicManager.getInstance().temptile2.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                } else if (UnitValue.m_dmap[i][j] == 3) {
                    GraphicManager.getInstance().temptitle4.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                } else if (UnitValue.m_dmap[i][j] == UnitValue.M_NOTMOVE) {
                    GraphicManager.getInstance().temptile5.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                }

            }
        }

        //나무가 타일에 겹쳐지지 않게 그려주기 위해 한번더 연산해 주었다.
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (UnitValue.m_dmap[i][j] == 4) {
                    GraphicManager.getInstance().temptile2.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                    GraphicManager.getInstance().temptitle4.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i) - 25);
                }
            }
        }
        Units.RenderUnit(canvas);
        canvas.restore();

        GraphicManager.getInstance().ButtonView_Image.Draw(canvas, 0, (int) m_Height - (int) m_Height / 6);
        //유저 정보를 뿌려주는 인스턴스의 Draw
        UI_imfor.Draw(canvas);
        //UI 버튼위치마다 번호를 매겨서 글자 출력 해준다.
        for (int i = 0; i < UI.Button.size(); i++) {
            UI.Button.get(i).Draw(canvas);
            canvas.drawText("" + i, (int) (UI.Button.get(i).GetX()), (int) (UI.Button.get(i).GetY()), paint);
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        talkUnit(canvas);
        //클릭 위치마다 사각형을 그려준다 오브젝트에 사각형
        canvas.drawRect((m_UI_Touch_Postion * 5) + m_UI_Touch_Postion * m_Width / 12, m_Height - m_Height / 6, (m_UI_Touch_Postion * 5) + m_UI_Touch_Postion * m_Width / 12 + m_Width / 12, m_Height - m_Height / 18, paint);

    }


    //유닛의 대화화
   public void talkUnit(Canvas canvas) {

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);

        switch (m_plot) {
            case 0: //처음으로 간단한 설명을 하는 장면의 대화 시뮬 부분이다.

                GraphicManager.getInstance().m_Sara.Draw(canvas, (int) m_Width / 20 * 14, (int) m_Height / 20 * 10);
                GraphicManager.getInstance().ballon_talk.Draw(canvas, (int) m_Width / 20 * 12, (int) m_Height / 20 * 2);
                String temp = m_talk.getSara2(m_SaraSay);
                String[] result = temp.split("#");
                for (int i = 0; i < result.length; i++) {
                    if (i == 0)
                        canvas.drawText(result[i], m_Width / 20 * 12, (int) m_Height / 20 * 3, paint);
                    else if (i == 1) {
                        canvas.drawText(result[i], m_Width / 20 * 12, (int) m_Height / 20 * 4, paint);
                    } else if (i == 2) {
                        canvas.drawText(result[i], m_Width / 20 * 12, (int) m_Height / 20 * 5, paint);
                    }
                }
                if (m_SaraSay > 6) {

                  //  LoadingDBMap(DBManager.getInstance().m_StringMap);
                    m_plot = 1;
                }
                break;
            case 1:   //배치시작이라는 글자가 휙휙 날라오는 장면이다.
                m_airtext.SlowText(canvas, m_Width, m_Height, timeDelta);
                if (m_airtext.getState() == true) {
                    m_plot = 2;
                }
                break;
            case 2: //이 장면은 배치 제한 시간 30초가 나오는 장면이다.
                canvas.drawText("" + (int) m_batch_gameTime, m_Width / 2, m_Height / 20, paint);
                if (m_batch_gameTime <= 0) {
                    if (m_SaraSay < 9) {

                        m_plot = 3;
                    }
                }
                break;
            case 3: //배치가 끝난 이후 사라가 말을 시작한 장면
            {
                GraphicManager.getInstance().m_Sara.Draw(canvas, (int) m_Width / 20 * 14, (int) m_Height / 20 * 10);
                GraphicManager.getInstance().ballon_talk.Draw(canvas, (int) m_Width / 20 * 12, (int) m_Height / 20 * 2);
                temp = m_talk.getSara2(m_SaraSay);
                result = temp.split("#");
                for (int i = 0; i < result.length; i++) {
                    if (i == 0)
                        canvas.drawText(result[i], m_Width / 20 * 12, (int) m_Height / 20 * 3, paint);
                    else if (i == 1) {
                        canvas.drawText(result[i], m_Width / 20 * 12, (int) m_Height / 20 * 4, paint);
                    } else if (i == 2) {
                        canvas.drawText(result[i], m_Width / 20 * 12, (int) m_Height / 20 * 5, paint);
                    }
                }

            }
            if (m_SaraSay > 8) {

                m_plot = 4;
            }
            break;
            case 4:
              //  LoadEnemy();
                m_airtext1.SlowText(canvas, m_Width, m_Height, timeDelta);
                // m_airtext1.SlowText(canvas, m_Width, m_Height, timeDelta);
                if (m_airtext1.getState() == true) {
                    Units.setRoundState(true);
                   m_plot = 5;
                }
                break;
            case 5:
                if (Units.EnemyUnits.size() == 0) {
                    Units.setRoundTheEnd(true);
                    Units.setRoundState(false);
                    m_plot = 6;
                    m_WhoWIn = 1;
                    SetMap();
                } else if (Units.MyUnits.size() == 0) {
                    Units.setRoundTheEnd(true);
                    Units.setRoundState(false);
                    m_plot = 6;
                    m_WhoWIn = 2;
                    SetMap();
                }

                // m_plot=6;
                break;
            case 6:

                // canvas.drawText("게임 끝",100,100,paint);
                if (m_WhoWIn == 1) {
                    m_airtextVic.FadeOutText(canvas, m_Width, m_Height, timeDelta);
                    if (m_airtextVic.getState() == true) {
                        DBManager.getInstance().setNetState(NetState.ROBBY);
                        m_plot = 7;
                    }
                    //아군 승리
                } else if (m_WhoWIn == 2) {
                    m_airTextFebe.FadeOutText(canvas, m_Width, m_Height, timeDelta);
                    m_plot = 7;
                    //적 승리
                } else {
                    //무 승부
                }
                break;
            case 7:
                Sound.getInstance().backgroundRelease();
                AppManager.getInstance().state = AppManager.S_LOADING;
                AppManager.getInstance().getGameView().ChangeGameState(new Ready_View());

                break;
        }
    }

    /*  public void AirText(Canvas canvas,String a) throws InterruptedException {
          for(int i=0;i<a.length();i++) {
              for(int j=0;j<=i;j++)
              {
                  String aa=a.toUpperCase();
                  canvas.drawText(""+aa.charAt(j),m_Width/20*(9+j),m_Height/2,paint);
              }
      }*/
    @Override
    public void Render(Canvas canvas) {
        if (AppManager.getInstance().state != AppManager.S_LOADING) {
            switch (my_view) {
                case GAMESTART:
                    GraphicManager.getInstance().background.Draw(canvas, -750, -450);
                    GameDraw(canvas);
                    fade_in.fadeDraw(canvas);

                    break;
                case SINARIO:
                    GraphicManager.getInstance().background.Draw(canvas, -750, -450);
                    SenariDraw(canvas);
                    GraphicManager.getInstance().m_airplane.Draw(canvas, 1, m_Width / 2, m_Height / 2);
                    break;
            }
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void TouchGame(float x, float y) {
        int count = 0;
        if (y > (int) m_Height - (int) m_Height / 6) {
            for (int i = 0; i < UI.Button.size(); i++) {
                if (UI.Button.get(i).Collusion(m_click_x, m_click_y, UI.rsizex, UI.rsizey)) {
                    m_UI_Touch_Postion = i;
                }
            }
        } else {
            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 50; j++) {

                        if (Bounding.tileColl.get(count).resultCal(m_click_x / m_matrix_x - m_diffX, m_click_y / m_matrix_y - m_diffY) == true && UnitValue.m_bmap[i][j] != MapState.NotMove) {
                            switch (UI.CheckTable.get(m_UI_Touch_Postion).retruncode()) {
                                case UnitValue.F_ANNA:
                                    CreateUnit.CreateAnna(i, j, Units.MyUnits, Units.EnemyUnits, true);
                                    break;
                                case UnitValue.F_ROCK1:
                                    CreateUnit.CreateRock2(i, j, Units.Enviroment);
                                    break;
                                case UnitValue.F_TREE1:
                                    CreateUnit.CreateTree1(i, j, Units.Enviroment);
                                    break;
                                case UnitValue.F_TOWER:
                                    CreateUnit.CreateArchorTower(i,j,Units.MyUnits,Units.EnemyUnits,true);
                                    break;
                                case UnitValue.F_BOOM:
                                    CreateUnit.CreateBoom(i, j, Units.MyUnits);
                                    Units.MyUnits.get(Units.MyUnits.size()-1).b_myUnit=true;
                                    break;
                                case UnitValue.F_ARCHER:
                                    CreateUnit.CreateArcher(i, j,Units.MyUnits,Units.EnemyUnits,true);
                                    break;
                            }
                        }
                        count++;

                    }
                }


            }

        }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        String strMsg = "";
        Log.i("액션" + strMsg, "" + strMsg);
        boolean statetimeer = false;
        x = event.getX();
        y = event.getY();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: {

                m_click_x = x;
                m_click_y = y;
                //temptitle3.SetPosition((int)(m_click_x),(int)(m_click_y));
                m_movex = x;
                m_movey = y;

                switch (m_plot) {
                    case 2: //배치 시작 터치 부분
                        TouchGame(x, y);
                        break;
                }
                // TouchGame(x, y);
                if (my_view == GAMESTART) {
                    if (m_SaraSay < 7 || m_batch_gameTime <= 0)
                        m_SaraSay += 1;
                }
                if (my_view == SINARIO) {
                    if (m_SaraSay < 12)
                        m_SaraSay += 1;
                }
                mode = DRAG;
                Log.d("zoom", "mode=DRAG");
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                m_time += 1;
                if (mode == DRAG) {
                    m_distx = x - m_movex;
                    m_disty = y - m_movey;

                    m_movex = event.getX();
                    m_movey = event.getY();
                    if (m_time > 2) {
                        if (m_diffY + m_disty > -400 && m_diffY + m_disty < 400) {
                            m_diffY += m_disty;
                        }
                        if (m_diffX + m_distx > -700 && m_diffX + m_distx < 500) {
                            m_diffX += m_distx;
                        }
                    }
                } else if (mode == ZOOM) {
                    m_click_x = event.getX(0);
                    m_click_y = event.getY(0);

                    //  newDist = spacing(event);
                    m_click2_x = event.getX(1);
                    m_click2_y = event.getY(1);
                    newDist = spacing(event);

                    if (newDist - oldDist > 20) { // zoom in
                        if (m_matrix_x < 2) {
                            m_matrix_x = m_matrix_x + (m_matrix_x * 0.02f);
                            m_matrix_y = m_matrix_y + (m_matrix_y * 0.02f);
                        }
                        oldDist = newDist;

                        matrix.setScale(m_matrix_x, m_matrix_y);
                    } else if (oldDist - newDist > 20) { // zoom out
                        oldDist = newDist;

                        m_matrix_x = m_matrix_x - (m_matrix_x * 0.02f);
                        m_matrix_y = m_matrix_y - (m_matrix_y * 0.02f);

                        matrix.setScale(m_matrix_x, m_matrix_y);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP: {


                m_time = 0;
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                //두번째 손가락 터치(손가락 2개를 인식하였기 때문에 핀치줌으로 판별
                mode = ZOOM;

                newDist = spacing(event);
                oldDist = spacing(event);
                m_click2_x = event.getX(1);
                m_click2_y = event.getY(1);
                Log.d("zoom", "newDist=" + newDist);
                Log.d("zoom", "oldDist=" + oldDist);
                Log.d("zoom", "mode=ZOOM");
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_CANCEL:
            default:
                break;


        }
        return true;
    }

    //줌인 줌 아웃을 위한 터치 이벤트 계산 함수
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }










    //안나생성 부분  whounit.1번은 아군 2번은 적


    public void SetMap() {
        for (int i = 0; i < Units.MyUnits.size(); i++) {
            if (Units.MyUnits.get(i).mType == UnitValue.F_JUMPINGTRAP) {
                UnitValue.m_testmap[Units.MyUnits.get(i).myUnitObject.Postion.x][Units.MyUnits.get(i).myUnitObject.Postion.y] = UnitValue.F_ROCK1;
            }
        }

    }

    public void LoadEnemy() {


        CreateUnit.CreateHall(45,5,Units.MyUnits,true);
        CreateUnit.CreateHall(5, 45,  Units.EnemyUnits, false);
        CreateUnit.CreateAnna(3,3,Units.MyUnits,Units.EnemyUnits,false);
        CreateUnit.CreateAnna(3,10,Units.MyUnits,Units.EnemyUnits,false);
        CreateUnit.CreateAnna(3,12,Units.MyUnits,Units.EnemyUnits,false);
        CreateUnit.CreateAnna(5,9,Units.MyUnits,Units.EnemyUnits,false);
        CreateUnit.CreateAnna(10,3,Units.MyUnits,Units.EnemyUnits,false);
        CreateUnit.CreateMagican(10, 10, Units.MyUnits, Units.EnemyUnits, false);


    }


}


