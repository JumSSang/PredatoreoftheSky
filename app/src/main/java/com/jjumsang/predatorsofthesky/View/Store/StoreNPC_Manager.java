package com.jjumsang.predatorsofthesky.View.Store;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jjumsang.predatorsofthesky.Values.StoreNpcState;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.immortal.TextEffect;

/**
 * Created by gyungmin on 2015-09-20.
 */


public class StoreNPC_Manager {
    private  String[] m_npc_talk;
    int m_talknumber=StoreNpcState.s_welcome;
    TextEffect m_airtext;
    public StoreNPC_Manager()
    {
        m_npc_talk=new String[20];
        InitTalk();
    }

    public void InitTalk()
    {
        m_npc_talk[StoreNpcState.byebye]="이용해 주셔서& 감사합니다~";
        m_npc_talk[StoreNpcState.s_buy]="유닛을 구매 하는데& 성공 하였습니다.";
        m_npc_talk[StoreNpcState.s_buy_false]="골드가 & 부족해요. & -3-";
        m_npc_talk[StoreNpcState.s_cash_buy_confirm]="크리스탈을 이용하여 &유닛을 구매하였습니다";
        m_npc_talk[StoreNpcState.s_cash_buy_false]="크리스탈이 모자랍니다.&  충전후 이용해 주세요";
        m_npc_talk[StoreNpcState.s_cash_up_confirm]="유닛 업그레이드에 & 성공하였습니다.";
        m_npc_talk[StoreNpcState.s_up_false]="골드가 모자랍니다. & -3-";
        m_npc_talk[StoreNpcState.s_upgradeok]="업그레이드에 &성공하였습니다";
        m_npc_talk[StoreNpcState.s_welcome]="안녕하세요 & 상점에 오신것을 &환영합니다~~~ ";
        m_npc_talk[StoreNpcState.active]="유닛이 활성화 & 되었습니다. ";
        m_npc_talk[StoreNpcState.unactiv]="유닛이 비활성화& 되었습니다.. ";

    }
    void onDraw(Canvas canvas,int state,int x,int y,Paint paint)
    {
        String[] talk=m_npc_talk[state].split("&");

        for(int i=0;i<talk.length;i++)
        {
            canvas.drawText(""+talk[i],x,y+i*100,paint);
         //   m_airtext=new TextEffect(talk[i]);
         //   m_airtext.SlowText(canvas, GraphicManager.getInstance().m_Width,GraphicManager.getInstance().m_Height,dt);


        }
    }
}
