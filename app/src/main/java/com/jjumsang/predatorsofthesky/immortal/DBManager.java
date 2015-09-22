package com.jjumsang.predatorsofthesky.immortal;

import android.widget.Toast;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;

import com.jjumsang.predatorsofthesky.NetConnect;
import com.jjumsang.predatorsofthesky.Values.StoreNpcState;
import com.jjumsang.predatorsofthesky.View.Store.StoreView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 경민 on 2015-04-19.
 */


public class DBManager {

    public class 디비유닛
    {
        public int type;
        public int 활성화=0;
        public int level;
        public int demage;
        public String name;

        디비유닛(int 타입,int 활성,int level,int demage,String name)
        {
            this.type=타입;
            this.활성화=활성;
            this.level=level;
            this.demage=demage;
            this.name=name;
        }
    }

    public static ArrayList<디비유닛>나의유닛목록;
    public static ArrayList<String> EventStack;
    public NetConnect connection;
    private static DBManager s_instance;
    private String response;
    public boolean netstate=false;

    public float m_batch_time=1000;
    public boolean m_turn_game_start=false;
    public static double readyroomtime=0;
    private String enemy="매칭을 시작하기전입니다.."; //잠시 적군의 아이디 보내줄 곳
    private String id="로딩중..";//유저의 닉네임
    private int cash; //유저의 캐시정보
    private int level; //유저의 렙레정보
    private int gold; //유저의 골드 정보
    public int victory=0; //유저의 승리 정보
    private int sum_number; //유저의 썸네일 정보
    private String guild="로딩중.."; //유저의 길드 정보



    private int enemysum;
    int state=0;
    private int go_robby=0;//변수명이 희한하지만 어떻게 패킷을 받을지 클라이언트의 통신상태를 뜻한다.
    public boolean nextlobby=false;
    public String m_StringMap;
    public String m_server_getMap;
    public static int FrameCount=0;
    public static int stackCount=30;
    public static boolean nextFrame=true;
    public static boolean b_wiatFrame=false;
    public static boolean b_create=false;
    public static String n_UnitString=null;
    public int team=0;
    public boolean shop_message_state=false;
    public String shop_message;

    public boolean m_bgm_state;
    public boolean m_sound_effect_sate;
    public float m_BleftVolume=1.0f;
    public float m_Bright_Volume=1.0f;
    public int npc_state= StoreNpcState.s_welcome;


    public static DBManager getInstance()
    {
        if(s_instance==null)
        {
            s_instance =new DBManager();
            EventStack=new ArrayList<String>();
            나의유닛목록=new ArrayList<디비유닛>();
    }
        return s_instance;
    }
    public String GetEnemy()
    {
        return enemy;
    }

    public void SetID(String a)
    {
        this.id=a;
    }
    public String GetID()
    {
        return id;
    }
    public String getResponse() //접속이라던가 모든 문자열을 보내준다.
    {
        return response;
    }
    public void sendMessage(String a) throws IOException {
        connection.oos.writeObject(a);
        connection.oos.flush();
    }
    public int getNetState()
    {
        return go_robby;
    }

    public void setNetState(int state)
    {
        go_robby=0;
        go_robby=state;
    }

    public void setImforDB(String a)
    {
        if(a!=null) {
            String[] result2 = a.split(":");
            this.enemy = result2[0];
            this.enemysum = Integer.parseInt(result2[1]);
            this.team = Integer.parseInt(result2[2]);
        }
    }

    public void 유닛감별함수(String a,int 디비순서)
    {
        String[] result=a.split("a");
        int level=Integer.parseInt(result[0]);
        int state=Integer.parseInt(result[1]);
            switch (디비순서) {
                case UnitValue.F_ANNA://안나
                    나의유닛목록.add(new 디비유닛(UnitValue.F_ANNA,state,level,30+(int)(level*3.5),"힐러안나"));
                    break;
                case UnitValue.F_ARCHER: //아처
                    나의유닛목록.add(new 디비유닛(UnitValue.F_ARCHER,state,level,5+(int)(level*1.5),"아처"));
                    break;
                case UnitValue.F_BOOM: //폭탄
                    나의유닛목록.add(new 디비유닛(UnitValue.F_BOOM,state,level,50+(int)(level*10.5),"대형폭탄"));
                    break;
                case UnitValue.F_ELSATOWER: //매직타워
                    나의유닛목록.add(new 디비유닛(UnitValue.F_ELSATOWER,state,level,10+(int)(level*30),"매직타워"));
                    break;
                case UnitValue.F_JUMPINGTRAP://점핑트랩
                    나의유닛목록.add(new 디비유닛(UnitValue.F_JUMPINGTRAP,state,level,(int)(level*5000),"점핑트랩"));
                    break;
                case UnitValue.F_MAGICAIN: //마법사
                    나의유닛목록.add(new 디비유닛(UnitValue.F_MAGICAIN,state,level,20+(int)(level*10),"마법사"));
                    break;
                case UnitValue.F_TOWER: //아처타워
                    나의유닛목록.add(new 디비유닛(UnitValue.F_TOWER,state,level,30+(int)(level*10),"아처타워"));
                    break;
                case UnitValue.F_WORRIOR://워리어
                    나의유닛목록.add(new 디비유닛(UnitValue.F_WORRIOR,state,level,22+(int)(level*1.5),"풋맨"));
                    break;
            }
        }

    public void 유닛업데이트(String a,int 유닛타입)
    {
        String[] result=a.split("a");
        int level=Integer.parseInt(result[0]);
        int state=Integer.parseInt(result[1]);

        for(int i=0;i<나의유닛목록.size();i++)
        {
            if(나의유닛목록.get(i).type==유닛타입)
            {
                나의유닛목록.get(i).level=level;
                나의유닛목록.get(i).활성화=state;
                나의유닛목록.get(i).demage=30+(int)(level*3.5);
            }
        }
    }

    public void SetResponse(String a) throws IOException //서버로부터 신호들어온 문자열을 셋팅한다.
    {
        switch(go_robby)
        {
            case NetState.READY:
               // go_robby=NetState.USERLOAD;

                response=a;
                break;

            case NetState.USERLOAD://처음 들어왔을경우 정보셋팅하는 부분이다.
                /*
                1. 닉네임
                2.골드
                3.캐시
                4.레벨
                5.승리횟수
                6.썸네일 번호
                7.길드
                 */
                String[] result=a.split(":");
                this.id=result[0];
                this.gold= Integer.parseInt(result[1]);
                this.cash=Integer.parseInt(result[2]);
                this.level=Integer.parseInt(result[3]);
                this.victory=Integer.parseInt(result[4]);
                this.sum_number=Integer.parseInt(result[5]);
                this.guild=result[6];
                유닛감별함수(result[7], UnitValue.F_ANNA);
                유닛감별함수(result[8], UnitValue.F_ARCHER);
                유닛감별함수(result[9],UnitValue.F_TOWER);
                유닛감별함수(result[10],UnitValue.F_BOOM);
                유닛감별함수(result[11],UnitValue.F_JUMPINGTRAP);
               // 유닛감별함수(result[11],UnitValue.F_BOOM);
                유닛감별함수(result[13],UnitValue.F_ELSATOWER);
                유닛감별함수(result[14],UnitValue.F_MAGICAIN);
               // 유닛감별함수(result[14],UnitValue.F_BOOM);
                유닛감별함수(result[16],UnitValue.F_WORRIOR);




                go_robby=NetState.ROBBY;
                break;
            case NetState.ROBBY:
                response=a;
                if(team==0) {
                    this.enemy = a;
                }

                break;
            case NetState.SHOP:
                response=a;
                String[] shop_result=a.split(":");
                if(Integer.parseInt(shop_result[0])==1) //업그레이드나 상점 구매 성공
                {
                    this.gold=Integer.parseInt(shop_result[2]);
                    this.cash=Integer.parseInt(shop_result[3]);
                    shop_message=shop_result[1];
                    shop_message_state=true;
                    유닛업데이트(shop_result[4],Integer.parseInt(shop_result[5]));
                    npc_state=StoreNpcState.s_buy;
                }
                else if(Integer.parseInt(shop_result[0])==0) //구매 실패
                {
                    shop_message_state=true;
                    shop_message=shop_result[1];
                    npc_state=StoreNpcState.s_buy_false;
                    //Toast.makeText(AppManager.getInstance().getGameView().getContext(), "돈부족", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(shop_result[0])==3) //활성화 또는 비활성화
                {

                    //1번째 패킷정보는 활성화시킬 유닛의 타입
                    //2번째는 상태를 어떻게 바꿀껀지에 관한 정보
                    int type=Integer.parseInt(shop_result[1]);
                    int state=Integer.parseInt(shop_result[2]);
                    for(int i=0;i<나의유닛목록.size();i++)
                    {
                        if(type==나의유닛목록.get(i).type)
                        {
                            나의유닛목록.get(i).활성화=state;
                            return;
                        }
                    }

                }

                break;
            case NetState.MULTIGAMESTART: //적의 아이디와 썸네일 분리해서 정보를 넘겨준다.

                    //setImforDB(a);
                     response=a;
                    sendMessage("Commit");
            break;
            case NetState.MUTI_TRUN:
                m_batch_time=Integer.parseInt(a);
                if(m_batch_time<500)
                {
                    DBManager.getInstance().setNetState(NetState.MUTI_TRUN_READY);
                }


                break;
            case NetState.MUTI_TRUN_READY:
                n_UnitString=a;
                DBManager.getInstance().m_turn_game_start=true;
                DBManager.b_create=true;
                break;

            case NetState.SINGLEGAME: //싱글게임 시작시 맵정보를 불러오는 부분이다.
                m_StringMap=a;
                //go_robby=6;
                break;
            case NetState.MULTIGAME:
                String[] gamepacket =a.split(":");
                if(gamepacket[0].equals("nextFrame"))
                {
                    nextFrame=true;
                    if(!gamepacket[1].equals("null"))
                    {
                        n_UnitString=gamepacket[1];
                        b_create=true;
                    }
                    else
                    {
                        n_UnitString="null";
                        b_create=false;
                    }
                }
                break;
            case 7:

                break;
        }

       // this.response=a;
    }
    public int GetGold()
    {
        return gold;
    }
    public int GetVictory()
    {
        return victory;
    }
    public int GetCash()
    {
        return cash;
    }
    public String getGuild()
    {
        return guild;
    }
    public int GetLevel()
    {
        return level;
    }
    public int GetSumnumber()
    {
        return sum_number;
    }
    public void SetEnemy(String s)
    {
        // 적의 정보가 어떻게 변화하는지 체크해주는 함수  적아이디가 들어오면 더이상 호출 해주지 않는걸로
        this.enemy=s;
    }
    public int GetEnemySum()
    {
        return enemysum;
    }
}

