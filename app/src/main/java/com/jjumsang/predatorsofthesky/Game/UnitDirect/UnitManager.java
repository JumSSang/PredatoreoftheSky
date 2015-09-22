package com.jjumsang.predatorsofthesky.Game.UnitDirect;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.Pattern;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitMove;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern.UnitThread;
import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;
import com.jjumsang.predatorsofthesky.Values.MapState;
import com.jjumsang.predatorsofthesky.Values.UnitState;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.Vec2;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Md by 경민 on 2015-04-27.
 */

/*유닛의 추가 삭제 모든것을 관리한다.
적군의 유닛도 관리해 준다.
유닛의 출력 순서도 여기서 담당한다.

 */

public class UnitManager {

    private boolean m_roundStart = false;
    private boolean m_Round_end = false;
    public ArrayList<Unit_Imfor> MyUnits;
    public ArrayList<Unit_Imfor> EnemyUnits;
    public ArrayList<Unit_Imfor> Enviroment;
    public ArrayList <Explosive>ExplosiveList;
    public static ArrayList<MissleManager> BulletList;
  //  public SpriteControl TestSprite;
    public float test_count=0;
    Paint paint;
    UnitThread map_findThread;

    public ArrayList<Unit_Imfor> UnitList;
    public boolean attack = false;

    public UnitManager() {
        MyUnits = new ArrayList<Unit_Imfor>();
        EnemyUnits = new ArrayList<Unit_Imfor>();
        BulletList = new ArrayList<MissleManager>();
        UnitList = new ArrayList<Unit_Imfor>();
        Enviroment = new ArrayList<Unit_Imfor>();
       // boomer_test = new Explosive(new Vec2F(500, 500), 50, 10, true, 0);
        ExplosiveList=new ArrayList<Explosive>();


        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.STROKE);
        map_findThread= new UnitThread(UnitList);
        map_findThread.start();
        //map_findThread.run();
        //   TestSprite = new SpriteControl(AppManager.getInstance().getBitmap(R.drawable.point_bullet));
      //  TestSprite.SetBulletRect(10); //업데이트 아니고 업데이트 갱신 주기 설정이다.


    }

    public void unitSort() {
        new UnitSort(this.UnitList);
    }

    public void remove(ArrayList<Unit_Imfor> list, Unit_Imfor a) {
        if (a.mHp <= 0 || a.boom_erase==true) {
            UnitValue.m_bmap[a.myUnitObject.Postion.x][a.myUnitObject.Postion.y]=MapState.Move;
            list.remove(a);

        }
    }

    public void setRoundState(boolean state) {
        m_roundStart = state;
    }



    public void RenderUnit(Canvas canvas) {

        canvas.drawText(""+UnitValue.testcount,100,100,paint);

        if(DBManager.getInstance().victory==0) {
            for (int i = 0; i < UnitList.size(); i++) {
                int x = UnitList.get(i).myUnitObject.Postion.x;
                int y = UnitList.get(i).myUnitObject.Postion.y;
                //750 + 50 / 2 * (y - x), -300 + 30 / 2 * (y+ x)
                switch (UnitList.get(i).mType) {
                    case UnitValue.F_ELSATOWER:
                        // paint.setARGB(50, 255, 0, 0);

                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }
                        paint.setColor(Color.GREEN);

                        //바운딩영역 확인하는 원
                        paint.setColor(Color.WHITE);
                        UnitList.get(i).myUnitObject.ElsaTowerDraw(canvas, UnitState.MOVE, UnitList.get(i).DrawPosition.x, UnitList.get(i).DrawPosition.y - 110);

                        paint.setARGB(50, 100, 100, 100);


                        UnitList.get(i).Hpbar();
                        UnitList.get(i).originHP.top -= 110;
                        UnitList.get(i).originHP.bottom -= 110;
                        UnitList.get(i).m_effect.Effect(30);
//                        UnitList.get(i).m_effect.Draw(canvas, UnitState.ATTACK, UnitList.get(i).DrawPosition.x - 30, UnitList.get(i).DrawPosition.y - 50);

                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }
                        canvas.drawCircle(UnitList.get(i).m_BoundingSpear.GetX(), UnitList.get(i).m_BoundingSpear.GetY(), UnitList.get(i).m_BoundingSpear.GetRadius(), paint);
                        canvas.drawCircle(UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY(), UnitList.get(i).m_battleBounding.GetRadius(), paint);

/*

                        float top1 = (float) (247.5 * Math.PI / 180);    //top1
                        float top2 = (float) (292.5 * Math.PI / 180);    //top1//top2

                        float topright1 = (float) (292.5 * Math.PI / 180);    //top1
                        float topright2 = (float) (337.5 * Math.PI / 180);    //top1

                        float right1 = (float) (337.5 * Math.PI / 180);    //top1
                        float right2 = (float) (22.5 * Math.PI / 180);    //top1


                        //leftbottom1
                        //leftbottom2

                        //bottom1
                        //bottom2

                        float t1x = (float) (Math.cos(top1) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetX();
                        float t1y = (float) (Math.sin(top1) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetY();
                        float t2x = (float) (Math.cos(top2) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetX();
                        float t2y = (float) (Math.sin(top2) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetY();


                        float t3x = (float) (Math.cos(topright2) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetX();
                        float t3y = (float) (Math.sin(topright2) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetY();

                        float t4x = (float) (Math.cos(right2) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetX();
                        float t4y = (float) (Math.sin(right2) * UnitList.get(i).m_BoundingSpear.GetRadius()) + UnitList.get(i).m_battleBounding.GetY();


                        canvas.drawLine(UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY(), t1x, t1y, paint);
                        canvas.drawLine(UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY(), t2x, t2y, paint);
                        canvas.drawLine(UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY(), t3x, t3y, paint);
                        canvas.drawLine(UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY(), t4x, t4y, paint);
*/

                        canvas.drawRect(UnitList.get(i).originHP, paint);
                        paint.setColor(Color.WHITE);
                        break;


                    case UnitValue.F_ANNA:
                        UnitList.get(i).myUnitObject.Draw(canvas, UnitState.MOVE, UnitList.get(i).DrawPosition.x, UnitList.get(i).DrawPosition.y);
                        paint.setARGB(50, 100, 100, 100);
                        UnitList.get(i).Hpbar();

                        UnitList.get(i).originHP.top -= 40;
                        UnitList.get(i).originHP.bottom -= 40;

                        UnitList.get(i).destHP.top -= 40;
                        UnitList.get(i).destHP.bottom -= 40;
                        // canvas.drawCircle(UnitList.get(i).m_BoundingSpear.GetX(),UnitList.get(i).m_BoundingSpear.GetY(),UnitList.get(i).m_BoundingSpear.GetRadius(),paint);


                        paint.setColor(Color.WHITE);
                        canvas.drawRect(UnitList.get(i).destHP, paint);
                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }
                        // canvas.drawCircle(UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY(), UnitList.get(i).m_battleBounding.GetRadius(), paint);
                        canvas.drawRect(UnitList.get(i).originHP, paint);
                        paint.setColor(Color.WHITE);
                        switch (UnitList.get(i).getState()) {
                            case UnitValue.S_MOVE:
                                canvas.drawText("타운홀"+UnitList.get(i).myUnitObject.Postion, UnitList.get(i).DrawPosition.x + 10, UnitList.get(i).DrawPosition.y - 50, paint);
                                break;
                            case UnitValue.S_BATTLE_MOVE:
                                canvas.drawText("바운딩", UnitList.get(i).DrawPosition.x + 10, UnitList.get(i).DrawPosition.y - 50, paint);
                                break;
                            case UnitValue.S_REMOVE:
                                canvas.drawText("적제거", UnitList.get(i).DrawPosition.x + 10, UnitList.get(i).DrawPosition.y - 50, paint);
                                break;

                        }


                        if (UnitList.get(i).m_attck) {
                            UnitList.get(i).m_effect.EffectDraw(canvas, UnitList.get(i).my_enemy.DrawPosition.x - 30, UnitList.get(i).my_enemy.DrawPosition.y - 60); // 검색해라!
                            if (UnitList.get(i).m_effect.mbEnd) {
                                UnitList.get(i).m_attck = false;
                                UnitList.get(i).m_effect.mbEnd = false;
                            }
                        }
                        break;
                    case UnitValue.F_ROCK1:
                        UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y - 25);
                        break;
                    case UnitValue.F_JUMPINGTRAP:
                        UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y);
                        break;
                    case UnitValue.F_TOWNHALL:
                        if (UnitList.get(i).mHp > 0)
                            UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x + 25, (int) UnitList.get(i).DrawPosition.y - 175);
                        break;
                    case UnitValue.F_TREE1:
                        UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x - 25, (int) UnitList.get(i).DrawPosition.y - 50);
                        break;
                    case UnitValue.F_ROCKE2:
                        UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y - 25);
                        break;
                    case UnitValue.F_TOWER:
                        UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y);
                        //canvas.drawCircle(UnitList.get(i).m_battleBounding.GetX(),UnitList.get(i).m_battleBounding.GetY(),UnitList.get(i).m_battleBounding.GetRadius(),paint);
                        UnitList.get(i).Hpbar();
                        paint.setColor(Color.GREEN);
                        canvas.drawRect(UnitList.get(i).originHP, paint);
                        paint.setColor(Color.WHITE);
                        break;
                    case UnitValue.F_BOOM:
                        UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y);
                        //   canvas.drawCircle(UnitList.get(i).m_battleBounding.GetX(),UnitList.get(i).m_battleBounding.GetY(),UnitList.get(i).m_battleBounding.GetRadius(),paint);
                        UnitList.get(i).Hpbar();
                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }
                        canvas.drawRect(UnitList.get(i).originHP, paint);
                        paint.setColor(Color.WHITE);
                        if (UnitList.get(i).boom_start) {
                            canvas.drawText("" + (int) UnitList.get(i).myAttackDelayTime, UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY() - 50, paint);
                        }
                        break;
                    case UnitValue.F_ARCHER:

                        if (UnitList.get(i).getState() == UnitValue.S_MOVE || UnitList.get(i).getState() == UnitValue.S_BATTLE_MOVE) //걷는 모션
                        {
                            UnitList.get(i).myUnitObject.WorkUnitDraw(canvas, UnitState.MOVE, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y); //유닛을 그려준다.
                        }
                        else if (UnitList.get(i).getState() == UnitValue.S_BATTLE) //공격 모션
                        {
                            UnitList.get(i).myUnitObject.WorkUnitDraw(canvas, UnitState.ATTACK, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y); //전투 상태를 그려준다.
                        }

                       // canvas.drawCircle(UnitList.get(i).m_battleBounding.GetX(), UnitList.get(i).m_battleBounding.GetY(), UnitList.get(i).m_battleBounding.GetRadius(), paint);
                        UnitList.get(i).Hpbar();
                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }
                        switch (UnitList.get(i).getState()) {
                            case UnitValue.S_MOVE:
                                //canvas.drawText("이동", UnitList.get(i).DrawPosition.x + 10, UnitList.get(i).DrawPosition.y - 50, paint);
                                break;
                            case UnitValue.S_BATTLE_MOVE:
                               // canvas.drawText("전투이동", UnitList.get(i).DrawPosition.x + 10, UnitList.get(i).DrawPosition.y - 50, paint);
                                break;
                            case UnitValue.S_BATTLE:
                               // canvas.drawText("전투중", UnitList.get(i).DrawPosition.x + 10, UnitList.get(i).DrawPosition.y - 50, paint);
                                break;
                            case UnitValue.S_REMOVE:
                               // canvas.drawText("적제거", UnitList.get(i).DrawPosition.x + 10, UnitList.get(i).DrawPosition.y - 50, paint);
                                break;

                        }
                        canvas.drawRect(UnitList.get(i).originHP, paint);
                        paint.setColor(Color.WHITE);


                        break;
                    case UnitValue.F_WORRIOR:

                        if (UnitList.get(i).getState() == UnitValue.S_MOVE || UnitList.get(i).getState() == UnitValue.S_BATTLE_MOVE)
                        {
                            UnitList.get(i).myUnitObject.WorkUnitDraw(canvas, UnitState.MOVE, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y); //유닛을 그려준다.
                         }
                        else if (UnitList.get(i).getState() == UnitValue.S_BATTLE)
                        {
                            UnitList.get(i).myUnitObject.WorkUnitDraw(canvas, UnitState.ATTACK, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y); //전투 상태를 그려준다.
                        }


                        canvas.drawCircle(UnitList.get(i).m_battleBounding.GetX(),UnitList.get(i).m_battleBounding.GetY(),UnitList.get(i).m_battleBounding.GetRadius(),paint);
                        UnitList.get(i).Hpbar();
                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }
                        canvas.drawRect(UnitList.get(i).originHP, paint);
                        paint.setColor(Color.WHITE);
                        break;
                    case UnitValue.F_MAGICAIN:

                        UnitList.get(i).myUnitObject.WorkUnitDraw(canvas,UnitState.MOVE, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y);
                        canvas.drawCircle(UnitList.get(i).m_battleBounding.GetX(),UnitList.get(i).m_battleBounding.GetY(),UnitList.get(i).m_battleBounding.GetRadius(),paint);
                        UnitList.get(i).Hpbar();
                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }
                        canvas.drawRect(UnitList.get(i).originHP, paint);
                        paint.setColor(Color.WHITE);
                        break;


                    default:

                        UnitList.get(i).myUnitObject.Draw(canvas, (int) UnitList.get(i).DrawPosition.x, (int) UnitList.get(i).DrawPosition.y);
                        UnitList.get(i).Hpbar();
                        if (UnitList.get(i).b_myUnit) {
                            paint.setColor(Color.GREEN);
                        } else {
                            paint.setColor(Color.RED);
                        }

                        canvas.drawRect(UnitList.get(i).originHP, paint);

                        paint.setColor(Color.WHITE);
                        break;
                }
            }
            //  TestSprite.Update(System.currentTimeMillis());
            // TestSprite.EffectDraw(canvas, 100, 100);
            for (int i = 0; i < BulletList.size(); i++) {
                BulletList.get(i).draw(canvas);
            }

            canvas.drawText("" + BulletList.size(), 500, 500, paint);
            ExplosionDraw(canvas);
        }
        else if(DBManager.getInstance().victory==2)
        {
            //canvas.drawText("승리",500,500,paint);
        }
        else if(DBManager.getInstance().victory==1)
        {
           // canvas.drawText("패배",500,500,paint);
        }


    }


    //에니메이션과 바운딩 포지션 업데이트를 한다.
    public void animationUpdate(Unit_Imfor a) {

        switch (a.mType) {
            case UnitValue.F_ELSATOWER:
                a.myUnitObject.PatrolUpdate(System.currentTimeMillis());
                a.m_effect.Update(System.currentTimeMillis());
                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                break;
            case UnitValue.F_ANNA:
                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                break;
            case UnitValue.F_ZOMBIE:
                break;
            case UnitValue.F_TOWER:
                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                break;
            case UnitValue.F_BOOM:
                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                break;
            case UnitValue.F_JUMPINGTRAP:
                break;
            case UnitValue.F_TOWNHALL:
                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                break;
            case UnitValue.F_ARCHER:

                if(a.getState()!=UnitValue.S_BATTLE)
                {
                    a.myUnitObject.UpdatePlus(System.currentTimeMillis(),false);
                }
                else
                {
                    a.myUnitObject.UpdatePlus(System.currentTimeMillis(),true);
                }


                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                break;
            case UnitValue.F_WORRIOR:
                a.myUnitObject.Update(System.currentTimeMillis());
                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                    break;
            case UnitValue.F_MAGICAIN:
                a.myUnitObject.Update(System.currentTimeMillis());
                a.boundingPositionUpdate(a.DrawPosition.x, a.DrawPosition.y);
                break;

            default:

                break;
        }
    }


    //라운드 상태를 설정해준다. 시작하면 고고고
    public void setRound() {
        m_roundStart = !m_roundStart;
    }

    //유닛에 관환 업데이트 부분

    public void UnitUpdates(double dt)
    {
        for (int i = 0; i < MyUnits.size(); i++) {

            //unitSort(MyUnits);//유닛의 출력순서 정렬 부분
            animationUpdate(MyUnits.get(i)); //유닛 애니메이션 업데이트 구현부분
            if(!DBManager.b_wiatFrame) {
                if (m_roundStart == true) {
                    if (EnemyUnits.size() != 0)
                        //Bounding.pushObject()
                        UnitMonitor(MyUnits.get(i), dt, true);
                }

                remove(MyUnits, MyUnits.get(i));//유닛 제거 체크 부분

            }
        }
        for (int i = 0; i < EnemyUnits.size(); i++) {
            // unitSort(EnemyUnits);//적유닛의 출력순서 정렬 부분

            animationUpdate(EnemyUnits.get(i));//적 에니메이션 구현 부분
            if(!DBManager.b_wiatFrame) {
                if (m_roundStart == true) {
                    if (MyUnits.size() != 0) {
                        UnitMonitor(EnemyUnits.get(i), dt, false);
                    }
                }

                remove(EnemyUnits, EnemyUnits.get(i));//적 유닛 제거 체크 부분

            }
        }
        //if(UnitValue.getInstance().getGameStart()==true)
        if(!DBManager.b_wiatFrame) {
            if (m_roundStart == true) {
                MoveUpdate(dt);
                MissleUpdate(dt);
                MissleErase();
                for (int i = 0; i < ExplosiveList.size(); i++) {
                    if (ExplosiveList.get(i).life == false) {
                        ExplosiveList.remove(i);
                    }
                }
            }
        }
    }
    public void Update(double dt) {


        UnitList.clear();
        UnitList.addAll(MyUnits);
        UnitList.addAll(EnemyUnits);
        UnitList.addAll(Enviroment);
        unitSort();//유닛의 출력순서 정렬 부분
        if(DBManager.getInstance().getNetState()==NetState.SINGLEGAME)
        {
            UnitUpdates(dt);
        }
        else if(DBManager.getInstance().getNetState()== NetState.MULTIGAME) {

            UnitUpdates(dt);
            DBManager.getInstance().stackCount+=1;
            if( DBManager.getInstance().stackCount>=1) {

                if (DBManager.nextFrame == true) {
                    DBManager.FrameCount += 1;
                    DBManager.b_wiatFrame=false;
                }
                else
                {
                    DBManager.b_wiatFrame=true;
                }
                try {
                    String sendString=null;
                    if(DBManager.EventStack.size()>0)
                    {
                        sendString="";
                    }
                    for(int i=0;i<DBManager.EventStack.size();i++)
                    {
                        sendString+=DBManager.EventStack.get(i);
                    }
                    DBManager.getInstance().sendMessage("true:" + DBManager.FrameCount+":"+sendString+":"+DBManager.getInstance().team);
                    DBManager.EventStack.clear();
                    DBManager.nextFrame = false;
                    DBManager.getInstance().stackCount=0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(DBManager.getInstance().getNetState()==NetState.MUTI_TRUN_READY)
        {
            if(MyUnits.size()==0)
            {
                DBManager.getInstance().victory=1;
            }
            if(EnemyUnits.size()==0)
            {
                DBManager.getInstance().victory=2;
            }
            if(DBManager.getInstance().m_turn_game_start==true) {
                UnitUpdates(dt);
            }
        }






    }

    public void ExplosionDraw(Canvas canvas) {
        for(int i=0;i<ExplosiveList.size();i++) {
            ExplosiveList.get(i).ExplosiveDraw(canvas);
        }
    }
    public void MissleErase() {
        for (int i = 0; i < BulletList.size(); i++) {


            switch(BulletList.get(i).type)
            {
                case UnitValue.BU_ARROW:
                    if (BulletList.get(i).state == false) {
                        BulletList.remove(i);
                        try {
                            BulletList.get(i).m_parents.mHp -= BulletList.get(i).m_Damage; //Try Catch 로 오류 잡아두어 후에 문제 소재있음
                        }
                        catch(IndexOutOfBoundsException e)
                        {

                        }
                        catch(NullPointerException e)
                        {

                        }
                    }
                    break;
                case UnitValue.BU_SPPOT:
                    if (BulletList.get(i).state == false) {
                        if (BulletList.get(i).m_parents.my_enemy != null) {
                            ExplosiveList.add(new Explosive(new Vec2F(BulletList.get(i).DrawPosition.x+20,BulletList.get(i).DrawPosition.y),40,10,true,0));
                            if(BulletList.get(i).m_parents.b_myUnit) {
                                ExplosiveList.get(ExplosiveList.size() - 1).boom_attack(EnemyUnits);
                            }
                            else
                            {
                                ExplosiveList.get(ExplosiveList.size() - 1).boom_attack(MyUnits);
                            }
                        }
                        BulletList.remove(i);
                    }
                    break;
            }


            //return;
        }
    }



    public void MissleUpdate(double dt) {
        for (int i = 0; i < BulletList.size(); i++) {
            switch(BulletList.get(i).type)
            {
                case UnitValue.BU_ARROW:
                    BulletList.get(i).moveArrowUpdate(dt);
                    break;
                case UnitValue.BU_SPPOT:
                    BulletList.get(i).moveSpotBullet();
                    break;
            }

        }
    }

    //폭탄 제어 부분
    public void BoomPattern(Unit_Imfor a, boolean myUnit, double dt)
    {
        //아군부분
        if (myUnit == true) {
            if(a.boom_start)
            {
                a.myAttackDelayTime-=dt;
                if(a.myAttackDelayTime<=0)
                {
                    a.boom_erase=true;
                    ExplosiveList.add(new Explosive(new Vec2F(a.m_battleBounding.GetX(),a.m_battleBounding.GetY()),40,10,true,0));
                    ExplosiveList.get(ExplosiveList.size()-1).boom_attack(EnemyUnits);


                }

                return;
            }

            for (int i = 0; i < EnemyUnits.size(); i++) {
                if(a!=null &&EnemyUnits.get(i)!=null) {
                    if (a.m_battleBounding != null && EnemyUnits.get(i).m_battleBounding != null) {
                        if (Bounding.UnitAABB(a.m_BoundingSpear.m_position, EnemyUnits.get(i).m_battleBounding.m_position, a.m_battleBounding.GetRadius(), EnemyUnits.get(i).m_battleBounding.GetRadius())) {
                            a.boom_start = true;
                            return;
                        }
                    }
                }
            }

        }

        //적부분
        else
        {
            if(a.boom_start)
            {
                a.myAttackDelayTime-=dt;
                if(a.myAttackDelayTime<=0)
                {
                    a.boom_erase=true;
                    ExplosiveList.add(new Explosive(new Vec2F(a.m_battleBounding.GetX(),a.m_battleBounding.GetY()),40,10,true,0));
                    ExplosiveList.get(ExplosiveList.size()-1).boom_attack(MyUnits);

                }

                return;
            }

            for (int i = 0; i < MyUnits.size(); i++) {
                if (MyUnits.get(i) != null &&a!=null) {
                    try {
                        if (Bounding.UnitAABB(a.m_BoundingSpear.m_position, MyUnits.get(i).m_battleBounding.m_position, a.m_battleBounding.GetRadius(), MyUnits.get(i).m_battleBounding.GetRadius())) {
                            a.boom_start = true;
                            return;
                        }
                    }
                    catch(NullPointerException e)
                    {

                    }

                }
            }
        }
    }

    //엘사타워 제어부분분


    //유닛 움직임을 제어한다.
    public void MoveUpdate(double dt) {


        for (int i = 0; i < UnitList.size(); i++) {
            UnitMove.MoveUpdate(dt,UnitList.get(i));

        }
    }



    public Vec2 enemyPositionTarget(Vec2 a, ArrayList<Vec2> blist, Unit_Imfor b) {
        double dist = 9999;
        int number = 0;
        Vec2 result = null;
        double tempdist;

        for (int i = 0; i < blist.size(); i++) {
            tempdist = ((Math.pow((a.x - blist.get(i).x), 2) + Math.pow((a.y - blist.get(i).y), 2)));
            if (tempdist < dist) {
                dist = tempdist;
                result = blist.get(i);
                number = i;

            }
        }
        b.findingTilenumber = number;
        return result;
    }



    //Agoon은 적인지 아군인지 업데이트할 대상을 의미한다. 1번은 아군 2번은 적
    public void UnitMonitor(Unit_Imfor a, double dt, boolean Agoon) {

        if (!m_Round_end) {
            switch (a.mType) {

                case UnitValue.F_ELSATOWER:
                    if (Agoon == true) {
                        Pattern.Tower(a, dt, EnemyUnits);
                    } else if (Agoon == false) {
                        Pattern.Tower(a, dt, MyUnits);
                    }
                    break;
                case UnitValue.F_ANNA:
                    if (Agoon == true) {
                        Pattern.Worrior(a, dt, EnemyUnits);
                    } else if (Agoon == false) {
                        Pattern.Worrior(a, dt, MyUnits);
                    }
                    break;
                case UnitValue.F_BOOM:
                    if(Agoon==true)
                    {
                        BoomPattern(a,true,dt);
                    }
                    else
                    {
                        BoomPattern(a,false,dt);
                    }
                    break;
                case UnitValue.F_ARCHER:
                    if (Agoon == true) {
                        Pattern.Archer(a, dt, EnemyUnits); //나에게는 적의 정보들을 넘겨준다.
                    } else if (Agoon == false) {
                        Pattern.Archer(a, dt, MyUnits); //적에게 내 유닛 정보 넘겨줌
                    }
                    break;
                case UnitValue.F_WORRIOR:
                    if (Agoon == true) {
                        Pattern.Worrior(a, dt, EnemyUnits);
                    } else if (Agoon == false) {
                        Pattern.Worrior(a, dt, MyUnits);
                    }
                    break;
                case UnitValue.F_MAGICAIN:
                    break;
                default:
                    break;
            }
        }
    }

    public void setRoundTheEnd(boolean state) {
        m_Round_end = state;
    }



}


class UnitSort {
    UnitSort(ArrayList<Unit_Imfor> thisUnit) {
        // Collections.sort(thisUnit, new XNoAscCompare());
        Collections.sort(thisUnit, new NoAscCompare());
    }

    //X값으로 정렬


    static class XNoAscCompare implements Comparator<Unit_Imfor> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(Unit_Imfor arg0, Unit_Imfor arg1) {

            return arg0.myUnitObject.Postion.x < arg1.myUnitObject.Postion.x ? -1 : arg0.myUnitObject.Postion.x > arg1.myUnitObject.Postion.x ? 1 : 0;
        }
    }


    static class XNoDescCompare implements Comparator<Unit_Imfor> {
        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(Unit_Imfor arg0, Unit_Imfor arg1) {
            // TODO Auto-generated method stub
            return arg0.myUnitObject.Postion.x > arg1.myUnitObject.Postion.x ? -1 : arg0.myUnitObject.Postion.x < arg1.myUnitObject.Postion.x ? 1 : 0;
        }

    }


    //Y값으로 정렬
    static class NoAscCompare implements Comparator<Unit_Imfor> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(Unit_Imfor arg0, Unit_Imfor arg1) {

            return arg0.myUnitObject.Postion.y + arg0.myUnitObject.Postion.x < arg1.myUnitObject.Postion.y + arg1.myUnitObject.Postion.x ? -1 : arg0.myUnitObject.Postion.y + arg0.myUnitObject.Postion.x >
                    arg1.myUnitObject.Postion.y + arg1.myUnitObject.Postion.x ? 1 : 0;
        }
    }

    static class NoDescCompare implements Comparator<Unit_Imfor> {
        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(Unit_Imfor arg0, Unit_Imfor arg1) {
            // TODO Auto-generated method stub
            return arg0.myUnitObject.Postion.y > arg1.myUnitObject.Postion.y ? -1 : arg0.myUnitObject.Postion.y < arg1.myUnitObject.Postion.y ? 1 : 0;
        }

    }

}
