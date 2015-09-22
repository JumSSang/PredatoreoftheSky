package com.jjumsang.predatorsofthesky.Game.UnitDirect.Units;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern.ArcherPattern;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern.MagicianPattern;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern.TowerPattern;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern.UnitThread;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern.WorriorPattern;
import com.jjumsang.predatorsofthesky.immortal.Vec2;

import java.util.ArrayList;

/**
 * Created by 경민 on 2015-07-15.
 */
public class Pattern {

    public static Vec2 enemyPositionTarget(Vec2 a, ArrayList<Vec2> blist, Unit_Imfor b) {
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


    public static void Archer(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy)
    {
        switch(a.getState())
        {
            case UnitValue.S_MOVE:
                a.myUnitObject.mNoOfFrames=4;//이동부분은 4개
               ArcherPattern.Move(a, dt, tempEnemy);
                break;
            case UnitValue.S_BATTLE_MOVE: //타겟을 잡고 이동할때 작동되는 패턴
                ArcherPattern.BattleMove(a, dt, tempEnemy);
                break;
            case UnitValue.S_BATTLE: //전투중일때 작동되는 패턴
                a.myUnitObject.setFPS(5);
                a.myUnitObject.mNoOfFrames=2; //전투부분은 3개를 차지하고있다.
                ArcherPattern.Battle(a, dt, tempEnemy);
                break;
            case UnitValue.S_REMOVE: //나의 타겟이 되는 적을 제거했을때 패턴
                ArcherPattern.EnemyRemove(a,dt,tempEnemy);
                break;
        }
    }
     public static void Worrior(Unit_Imfor a,double dt,ArrayList<Unit_Imfor>tempEnemy)
     {

         switch(a.getState())
         {
             case UnitValue.S_MOVE:
                 WorriorPattern.Move(a, dt, tempEnemy);
                 break;
             case UnitValue.S_BATTLE_MOVE: //타겟을 잡고 이동할때 작동되는 패턴
                 WorriorPattern.BattleMove(a, dt, tempEnemy);
                 break;
             case UnitValue.S_BATTLE: //전투중일때 작동되는 패턴
                 WorriorPattern.Battle(a, dt, tempEnemy);
                 break;
             case UnitValue.S_REMOVE: //나의 타겟이 되는 적을 제거했을때 패턴
                 break;
         }
     }

    public static void Magician(Unit_Imfor a,double dt,ArrayList<Unit_Imfor>tempEnemy)
    {

        switch(a.getState())
        {
            case UnitValue.S_MOVE:
                MagicianPattern.Move(a, dt, tempEnemy);
                break;
            case UnitValue.S_BATTLE_MOVE: //타겟을 잡고 이동할때 작동되는 패턴
                MagicianPattern.BattleMove(a, dt, tempEnemy);
                break;
            case UnitValue.S_BATTLE: //전투중일때 작동되는 패턴
                MagicianPattern.Battle(a, dt, tempEnemy);
                break;
            case UnitValue.S_REMOVE: //나의 타겟이 되는 적을 제거했을때 패턴
                break;
        }
    }

    public static void Tower(Unit_Imfor a,double dt,ArrayList<Unit_Imfor>tempEnemy)
    {

        switch(a.getState())
        {
            case UnitValue.S_MOVE:
                TowerPattern.Move(a, dt, tempEnemy);

                break;
            case UnitValue.S_BATTLE: //전투중일때 작동되는 패턴


                TowerPattern.Battle(a,dt);
                break;
            case UnitValue.S_REMOVE: //나의 타겟이 되는 적을 제거했을때 패턴
                break;
        }
    }





}
