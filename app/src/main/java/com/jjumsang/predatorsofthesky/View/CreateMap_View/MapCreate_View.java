package com.jjumsang.predatorsofthesky.View.CreateMap_View;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.jjumsang.predatorsofthesky.Game.ActiveCollusion;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.CreateUnit;
import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;
import com.jjumsang.predatorsofthesky.Values.MapState;
import com.jjumsang.predatorsofthesky.View.Ready_Room_View.Ready_View;
import com.jjumsang.predatorsofthesky.View.Story_room.Story_String;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
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

/**
 * Created by 경민 on 2015-05-11.
 */
public class MapCreate_View implements IState {

    private double m_batch_gameTime = 5;
    private final int GAMESTART = 0;
    private final int SINARIO = 1;
    private int m_WhoWIn = 0;

    private int my_view = SINARIO;
    private int m_plot = 0;


    Matrix matrix = new Matrix();
    private ArrayList<ActiveCollusion> tileColl;
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
    Rect r;
    TextEffect m_airtext, m_airtext1, m_airtextVic, m_airTextFebe;

    public void sendMessage(String a) throws IOException {
        DBManager.getInstance().connection.oos.writeObject(a);
        DBManager.getInstance().connection.oos.flush();
    }

    @Override
    public void Init() {
        DBManager.getInstance().setNetState(NetState.SINGLEGAME);
        try {
            sendMessage("1");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        tileColl = new ArrayList<ActiveCollusion>();
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

        Unit Mtemp;
        Mtemp = new Unit(GraphicManager.getInstance().mTownHall.m_bitmap);
        GraphicManager.getInstance().m_airplane.Air(5);
        fade_in = new ScreenAnimation((int) m_Width, (int) m_Height);
        fade_out = new ScreenAnimation((int) m_Width, (int) m_Height);
        fade_out.InitFadeOut();
        fade_in.InitFadeIn();
        r = new Rect((int) m_Width / 20 * 18, 0, (int) (m_Width), (int) m_Height / 20);
    }

    @Override
    public void Destroy() {

    }

    @Override
    public void Update() {
        Units.Update(10);
        switch (m_plot) {
            case 0:
                if (DBManager.getInstance().m_StringMap != null) {

                    m_plot = 1;
                }
                break;
            case 1: //배치 시작 터치 부분
                LoadMap(DBManager.getInstance().m_StringMap);
                TouchGame(x, y);
                if (AppManager.getInstance().Collusion((int) x, (int) y, r)) {
                    m_plot = 2;
                }

                break;
            case 2://배치 완료

                saveMap();
                m_plot = 3;


                break;
            case 3:


                AppManager.getInstance().state = AppManager.S_ROBBY;
                AppManager.getInstance().getGameView().ChangeGameState(new Ready_View());
                break;
        }

    }

    @Override
    public void Render(Canvas canvas) {

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
                switch (UnitValue.m_dmap[i][j]) {
                    case UnitValue.M_GRASS1:
                        GraphicManager.getInstance().temptile1.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                        break;
                    case UnitValue.M_GRASS2:
                        GraphicManager.getInstance().temptile2.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                        break;
                    case UnitValue.M_EMPTY:
                        GraphicManager.getInstance().temptitle3.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                        break;
                    case UnitValue.M_NOTMOVE:
                        GraphicManager.getInstance().temptile5.Draw(canvas, 750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i));
                        break;


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
        for (int i = 0; i < UI.Button.size(); i++) {
            UI.Button.get(i).Draw(canvas);
            canvas.drawText("" + i, (int) (UI.Button.get(i).GetX()), (int) (UI.Button.get(i).GetY()), paint);
        }

        canvas.drawRect(m_Width / 20 * 18, 0, m_Width, m_Height / 20, paint);

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
        x = event.getX();
        y = event.getY();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: {

                m_click_x = x;
                m_click_y = y;
                //temptitle3.SetPosition((int)(m_click_x),(int)(m_click_y));
                m_movex = x;
                m_movey = y;


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

    public void UnitAdd() {
        for(int i=0;i<DBManager.getInstance().나의유닛목록.size();i++)
        {

            UnitDataList.add(new UnitList(DBManager.getInstance().나의유닛목록.get(i)));
        }

    }

    //줌인 줌 아웃을 위한 터치 이벤트 계산 함수
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    public void LoadMap(String a) {
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
                    Unit temp;
                    if (tileColl.get(count).resultCal(m_click_x / m_matrix_x - m_diffX, m_click_y / m_matrix_y - m_diffY) == true && UnitValue.m_bmap[i][j] != MapState.NotMove) {
                        //  UnitValue.m_map[i][j]=3;
                        //점핑 트랩 생산
                        switch (UI.CheckTable.get(m_UI_Touch_Postion).retruncode()) {
                            case UnitValue.F_ROCK1:
                                //CreateRock(i, j);

                                break;
                            case UnitValue.F_ROCKE2:
                               // CreateRock2(i, j);

                                break;
                            case UnitValue.F_TREE1:
                              //  CreateTree1(i, j);

                                break;
                            case UnitValue.F_TOWER:
                                CreateUnit.CreateArchorTower(i, j, Units.MyUnits, Units.EnemyUnits, true);
                                break;
                            case UnitValue.F_BOOM:
                                CreateUnit.CreateBoom(i, j, Units.MyUnits);
                                break;
                            case UnitValue.F_ELSATOWER:
                                Unit temped = new Unit(GraphicManager.getInstance().mElsa_Tower.m_bitmap);
                                CreateUnit.CreateMagicTower(i, j, temped, Units.MyUnits, Units.EnemyUnits, false);
                                break;
                            case UnitValue.F_ANNA:
                                CreateUnit.CreateAnna(i,j, Units.MyUnits, Units.EnemyUnits, false);
                                break;

                        }
                    }
                    count++;
                }


            }

        }


    }

    public void saveMap() {
        Units.Enviroment.addAll(Units.EnemyUnits);
        Units.Enviroment.addAll(Units.MyUnits);
        int number = Units.Enviroment.size();
        String[] map;
        map = new String[number];
        String text = "";
        for (int i = 0; i < Units.Enviroment.size(); i++) {
            String a;
            if (i != 0)
                a = String.valueOf("=" + Units.Enviroment.get(i).myUnitObject.Postion.x + "a" + Units.Enviroment.get(i).myUnitObject.Postion.y + "a" + "0" + "a" + Units.Enviroment.get(i).mType);
            else {
                a = String.valueOf(Units.Enviroment.get(i).myUnitObject.Postion.x + "a" + Units.Enviroment.get(i).myUnitObject.Postion.y + "a" + "0" + "a" + Units.Enviroment.get(i).mType);
            }
            map[i] = a;
        }
        for (int i = 0; i < map.length; i++) {
            text += map[i];
        }
        DBManager.getInstance().m_server_getMap = text;

        try {
            DBManager.getInstance().connection.oos.writeObject(text);
            DBManager.getInstance().connection.oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*

    public void CreateRock(int i, int j) {
        if (UnitValue.m_map[i][j] != UnitValue.M_NOTMOVE) {
            Unit temp;
            temp = new Unit(GraphicManager.getInstance().rock1.m_bitmap);
            temp.SetPos(i, j);
            Units.Enviroment.add(new Unit_Imfor(temp, 5000, 0, UnitValue.F_ROCK1, true));
            UnitValue.m_map[i][j] = UnitValue.M_NOTMOVE;
        }


    }

    public void CreateTree1(int i, int j) {
        if (UnitValue.m_map[i][j] != UnitValue.M_NOTMOVE) {
            Unit temp;
            temp = new Unit(GraphicManager.getInstance().tree1.m_bitmap);
            temp.SetPos(i, j);
            Units.Enviroment.add(new Unit_Imfor(temp, 5000, 0, UnitValue.F_TREE1, true));
            UnitValue.m_map[i][j] = UnitValue.M_NOTMOVE;
        }

    }

    public void CreateRock2(int i, int j) {
        if (UnitValue.m_map[i][j] != UnitValue.M_NOTMOVE) {
            Unit temp;
            temp = new Unit(GraphicManager.getInstance().rock2.m_bitmap);
            temp.SetPos(i, j);
            Units.Enviroment.add(new Unit_Imfor(temp, 5000, 0, UnitValue.F_ROCKE2, true));
            UnitValue.m_map[i][j] = UnitValue.M_NOTMOVE;
        }

    }
*/

    public void InitMap() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                ActiveCollusion temp = new ActiveCollusion();
                temp.addSpot(750 + 50 / 2 * (j - i), -300 + 25 / 2 * (j + i) + 12);//p1 왼쪽
                temp.addSpot(750 + 50 / 2 * (j - i) + 25, -300 + 25 / 2 * (j + i));//p2 //위
                temp.addSpot(750 + 50 / 2 * (j - i) + 50, -300 + 25 / 2 * (j + i) + 12);//p3 오른쪽
                temp.addSpot(750 + 50 / 2 * (j - i) + 25, -300 + 25 / 2 * (j + i) + 25);//p4 아래
                temp.distanceCal();
                tileColl.add(temp);

                if ((i + j) % 2 == 0) {
                    UnitValue.m_dmap[i][j] = 1;
                } else {
                    UnitValue.m_dmap[i][j] = 2;
                }
                if (i == 0 || i == 49 || j == 0 || j == 49) {
                    UnitValue.m_dmap[i][j] = 4;
                }

            }
        }
    }
}
