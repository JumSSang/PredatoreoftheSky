package com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.Bounding;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.CreateUnit;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.MissleManager;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitManager;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.GameMath.DirectionClass;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.Pattern;
import com.jjumsang.predatorsofthesky.Values.MapState;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

import java.util.ArrayList;

/**
 * Created by 경민 on 2015-07-16.
 */
public class ArcherPattern {

    public static void Move(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy)
    {

        ///바운딩 영역을 체크하는 부분은 무브에서만 한다.
        if (tempEnemy.size() != 0) {
            for (int i = 0; i < tempEnemy.size(); i++) {
                if (Bounding.AABB(a.m_battleBounding.m_position, tempEnemy.get(i).m_battleBounding.m_position, a.m_BoundingSpear.GetRadius())) {
                    a.my_enemy = tempEnemy.get(i);
                    if (tempEnemy.get(i).myUnitObject.unitPosition.size() > 0) {
                        a.setMovestate(false);
                        //   a.myPath.LoadMap(UnitValue.m_bmap);
                        try {
                            //길앞에 뭔가있는지 찾는 알고리즘
                            Vec2F checkBoundingPosition = new Vec2F(a.findedPath.m_pos.x, a.findedPath.m_pos.y);
                            if (Bounding.BoundingTile(checkBoundingPosition)) {
                                a.m_findMap=true;
                              //  a.findedPath = a.myPath.find(Pattern.enemyPositionTarget(a.myUnitObject.Postion, tempEnemy.get(i).myUnitObject.unitPosition, a), a.myUnitObject.Postion);
                            }
                        }
                        catch(NullPointerException e)
                        {

                        }
                        a.setState(UnitValue.S_BATTLE_MOVE);
                        a.myUnitObject.mCurrentFrame=0;
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
        else if(Bounding.AABB(a.m_battleBounding.m_position,a.my_enemy.m_battleBounding.m_position, a.m_BoundingSpear.GetRadius()))
        {
            a.setState(UnitValue.S_BATTLE);
        }
    }
    public static void Battle(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy)
    {
        int count=0;
        if(a.my_enemy!=null) {
            if (a.my_enemy.mHp <= 0) {
                a.setState(UnitValue.S_REMOVE);
                return;
            }
        }
        else
        {
            a.setState(UnitValue.S_REMOVE);
            return;
        }
        a.enemyRadius = DirectionClass.GetAngle(a.m_battleBounding.m_position, a.my_enemy.m_battleBounding.m_position); //적과의 각도를 구한다.
        DirectionClass.AngleDirectionUpdate(a); //각도를 기반으로 우닛 방향을 바꿔준다.
        if(a.myUnitObject.attack_state==true) {
            CreateUnit.CreateArrow(a);
            a.myUnitObject.attack_state=false;
            a.myUnitObject.mCurrentFrame=0;
            a.setState(UnitValue.S_MOVE);
        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                Unit temp;
                if (Bounding.tileColl.get(count).resultCal(a.m_battleBounding.GetX() , a.m_battleBounding.GetY()) == true && UnitValue.m_bmap[i][j] != MapState.NotMove) {
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
        /*    for (int i = 0; i < tempEnemy.size(); i++) {
                //바운딩 영역안에 적이있는지 한번 조사해주는 부분이다.  적이 존재하면 그 적을 싸워야할 적으로 인식하고 덤비게 된다.
                if (Bounding.AABB(a.m_battleBounding.m_position, tempEnemy.get(i).m_battleBounding.m_position, a.m_BoundingSpear.GetRadius()) && a.getState() != UnitValue.S_BATTLE && tempEnemy.get(i).mHp > 0) {
                    a.my_enemy = tempEnemy.get(i); //적을 누구로 할지 설정하는 부분
                    //    a.myPath.LoadMap(UnitValue.m_bmap);
                    a.setState(UnitValue.S_BATTLE_MOVE); //상태는 베틀 무브라는 상태로 변경된다.
                    a.findedPath = a.myPath.find(Pattern.enemyPositionTarget(a.myUnitObject.Postion, tempEnemy.get(i).myUnitObject.unitPosition, a), a.myUnitObject.Postion);
                    return;
                }
            }*/
            a.myUnitObject.mCurrentFrame=0;
            a.setState(UnitValue.S_MOVE);//앞쪽에서 이상없이 걸리는게 없다면 타운홀로 이동하는 상태로 변경된다.
            a.my_enemy = tempEnemy.get(0); //적은 타운홀로 설정된다.
            //  a.myPath.LoadMap(UnitValue.m_bmap);
            if (tempEnemy.get(0).myUnitObject.unitPosition.size() > 0) {
               a.m_findMap=true;
              // a.findedPath = a.myPath.find(Pattern.enemyPositionTarget(a.myUnitObject.Postion, tempEnemy.get(0).myUnitObject.unitPosition, a), a.myUnitObject.Postion); //적의 위치로 달려가게 길을 찾아준다.
                return;
            }
        }

    }
}
