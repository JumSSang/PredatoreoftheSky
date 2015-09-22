package com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.Bounding;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.MissleManager;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitManager;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.GameMath.DirectionClass;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.Pattern;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

import java.util.ArrayList;

/**
 * Created by 경민 on 2015-07-16.
 */
public class TowerPattern {


    public static void Move(Unit_Imfor a,double dt,ArrayList<Unit_Imfor> tempEnemy) {
        try {
            switch (a.getState()) {
                case UnitValue.S_MOVE:
                    for (int i = 0; i < tempEnemy.size(); i++) {
                        if (Bounding.UnitAABB(a.m_BoundingSpear.m_position, tempEnemy.get(i).m_battleBounding.m_position, a.m_BoundingSpear.GetRadius(), tempEnemy.get(i).unitSize)) {
                            switch (tempEnemy.get(i).mType) {
                                case UnitValue.F_ANNA:
                                    a.my_enemy = tempEnemy.get(i);
                                    a.setState(UnitValue.S_BATTLE);

                                    return;
                                case UnitValue.F_ELSATOWER:
                                    a.my_enemy = tempEnemy.get(i);
                                    a.setState(UnitValue.S_BATTLE);
                                    return;

                                case UnitValue.F_TOWER:
                                    a.my_enemy = tempEnemy.get(i);
                                    a.setState(UnitValue.S_BATTLE);
                                    return;
                                case UnitValue.F_ARCHER:
                                    a.my_enemy = tempEnemy.get(i);
                                    a.setState(UnitValue.S_BATTLE);
                                    break;
                                default:
                                    break;

                            }

                        }
                    }

            }
        }
        catch(NullPointerException e)
        {
            //널포인트 발생 ㅡㅡ;;
        }
    }


    public static void Battle(Unit_Imfor a,double dt)
    {
        a.myAttackDelayTime += dt;
        if (a.myAttackDelayTime > 1) {

            a.enemyRadius = DirectionClass.GetAngle(a.m_battleBounding.m_position, a.my_enemy.m_battleBounding.m_position);
            DirectionClass.AngleDirectionUpdate(a);
            if (Bounding.UnitAABB(a.m_BoundingSpear.m_position, a.my_enemy.m_BoundingSpear.m_position, a.m_BoundingSpear.GetRadius(), a.my_enemy.m_BoundingSpear.GetRadius()) && a.my_enemy.mHp>0) {
                Vec2F tempVect;
                switch(a.my_enemy.mType)
                {
                    case UnitValue.F_ANNA:
                        tempVect= new Vec2F(a.my_enemy.m_battleBounding.GetX()-35, a.my_enemy.m_battleBounding.GetY()-25);
                        break;
                    case UnitValue.F_TOWER:
                        tempVect= new Vec2F(a.my_enemy.m_battleBounding.GetX(), a.my_enemy.m_battleBounding.GetY());
                        break;
                    case UnitValue.F_ARCHER:
                        tempVect= new Vec2F(a.my_enemy.m_battleBounding.GetX(), a.my_enemy.m_battleBounding.GetY());
                        break;
                    default:
                        tempVect= new Vec2F(a.my_enemy.m_battleBounding.GetX(), a.my_enemy.m_battleBounding.GetY());
                        break;
                }
                UnitManager.BulletList.add(new MissleManager(a.DrawPosition, tempVect, 3, 7, a.b_myUnit, UnitValue.BU_SPPOT, a)); //Draw포지션 위치에서 미사일이 생성된다.
                a.myAttackDelayTime = 0;
                a.setState(UnitValue.S_MOVE);
                return;
            } else {
                a.myAttackDelayTime = 0;
                a.setState(UnitValue.S_MOVE);
                return;
            }

        }


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
