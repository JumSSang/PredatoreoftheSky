package com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.Bounding;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.Pattern;
import com.jjumsang.predatorsofthesky.Values.MapState;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

import java.util.ArrayList;

/**
 * Created by 경민 on 2015-07-16.
 */
public class MagicianPattern {

    public static void Move(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy)
    {
        ///바운딩 영역을 체크하는 부분은 무브에서만 한다.
        if (tempEnemy.size() != 0) {
            for (int i = 0; i < tempEnemy.size(); i++) {
                if (Bounding.AABB(a.DrawPosition, tempEnemy.get(i).DrawPosition, a.m_BoundingSpear.GetRadius())) {
                    a.my_enemy = tempEnemy.get(i);
                    if (tempEnemy.get(i).myUnitObject.unitPosition.size() > 0) {
                        a.setMovestate(false);
                        //   a.myPath.LoadMap(UnitValue.m_bmap);
                        try {
                            //길앞에 뭔가있는지 찾는 알고리즘
                            Vec2F checkBoundingPosition = new Vec2F(a.findedPath.m_pos.x, a.findedPath.m_pos.y);
                            if (Bounding.BoundingTile(checkBoundingPosition)) {
                                a.findedPath = a.myPath.find(Pattern.enemyPositionTarget(a.myUnitObject.Postion, tempEnemy.get(i).myUnitObject.unitPosition, a), a.myUnitObject.Postion);
                            }
                        }
                        catch(NullPointerException e)
                        {

                        }
                        a.setState(UnitValue.S_BATTLE_MOVE);
                    }
                    break;
                }
            }
        }
        if(a.my_enemy!=null) {
            if (a.my_enemy.mHp <= 0) {
                a.setState(UnitValue.S_REMOVE);
            }
        }

    }

    public static void BattleMove(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy)
    {
        if(a.my_enemy.mHp<=0)
        {
            a.setState(UnitValue.S_REMOVE);
        }
    }
    public static void Battle(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy)
    {
        int count=0;
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                Unit temp;
                if (Bounding.tileColl.get(count).resultCal(a.DrawPosition.x , a.DrawPosition.y) == true && UnitValue.m_bmap[i][j] != MapState.NotMove) {
                    a.myUnitObject.SetPos(i,j);
                }
            }
            count++;
        }

        if (a.my_enemy.mType == UnitValue.F_ANNA) {
            a.my_enemy.myUnitObject.unitPosition.clear();
            a.my_enemy.myUnitObject.unitPosition.add(a.my_enemy.myUnitObject.Postion);
        }
        // UnitBattle(a, dt, 1);


    }
    public static void EnemyRemove(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy)
    {
        if (tempEnemy.size() != 0) {
            for (int i = 0; i < tempEnemy.size(); i++) {
                if (Bounding.AABB(a.DrawPosition, tempEnemy.get(i).DrawPosition, a.m_BoundingSpear.GetRadius()) && a.getState() != UnitValue.S_BATTLE && tempEnemy.get(i).mHp > 0) {
                    a.my_enemy = tempEnemy.get(i);
                    //    a.myPath.LoadMap(UnitValue.m_bmap);
                    a.setState(UnitValue.S_BATTLE_MOVE);
                    a.findedPath = a.myPath.find(Pattern.enemyPositionTarget(a.myUnitObject.Postion, tempEnemy.get(i).myUnitObject.unitPosition, a), a.myUnitObject.Postion);
                    return;
                }
            }
            a.setState(UnitValue.S_MOVE);
            a.my_enemy = tempEnemy.get(0);
            //  a.myPath.LoadMap(UnitValue.m_bmap);
            if (tempEnemy.get(0).myUnitObject.unitPosition.size() > 0) {
                a.findedPath = a.myPath.find(Pattern.enemyPositionTarget(a.myUnitObject.Postion, tempEnemy.get(0).myUnitObject.unitPosition, a), a.myUnitObject.Postion);
                return;
            }
        }

    }
}
