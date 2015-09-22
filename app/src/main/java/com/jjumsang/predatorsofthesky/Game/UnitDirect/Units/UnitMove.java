package com.jjumsang.predatorsofthesky.Game.UnitDirect.Units;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.Bounding;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.GameMath.DirectionClass;
import com.jjumsang.predatorsofthesky.Values.MapState;
import com.jjumsang.predatorsofthesky.immortal.Vec2;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

import java.util.ArrayList;

/**
 * Created by 경민 on 2015-07-14.
 */
public class UnitMove {
    //Unit_Imfor Unit;

    public static void AstarMove()
    {

    }
    public static void MoveUpdate(double dt, Unit_Imfor Unit) {
        Unit.myTime += dt;
        if (Unit.myTime > 0.01f) {  //유닛의 움직임 타임이 0.5보다 커지면 타일을 하나씩 움직인다.
            if (Unit.findedPath != null)  //목표타겟이 되는 부분이 Null값이 아니면 다음 문장  실행
            {
                Unit.myTime=0;
                if ((Unit.mType == UnitValue.F_ANNA || Unit.mType == UnitValue.F_ARCHER || Unit.mType == UnitValue.F_MAGICAIN || Unit.mType == UnitValue.F_WORRIOR) && Unit.getMovestate() == false && Unit.findedPath.parentNode != null && Unit.getState() != UnitValue.S_BATTLE) //안나일 경우와 무브가 끝나고 다음좌표 실행시에 호출된다.//getMoveState는 해당 좌표로 가는게 true일 경우 이동하고있다는걸 뜻한다.
                {

                    int x = Unit.findedPath.m_pos.x - Unit.findedPath.parentNode.m_pos.x; //방향을 업데이트 하기위해서 만든값
                    int y = Unit.findedPath.m_pos.y - Unit.findedPath.parentNode.m_pos.y;
                    DirectionClass.DirectionUpdate(x, y, Unit, Unit.mType);//방향 업데이트
                    //목표 좌표점이다.
                    float a = (float) (750 + 50 / 2 * (Unit.findedPath.parentNode.m_pos.y - Unit.findedPath.parentNode.m_pos.x)+15) ; //x + 15, y + 50
                    float b = (float) (-300 + 25 / 2 * (Unit.findedPath.parentNode.m_pos.y + Unit.findedPath.parentNode.m_pos.x)+25) ;
                    Unit.m_SpeedVecrt = new Vec2F(a, b);
                    Unit.m_SpeedVecrt.sub(Unit.m_battleBounding.m_position);//여기가 많은 문제있는곳이다.
                    Unit.m_SpeedVecrt.normalize();
                    Unit.m_SpeedVecrt.multiply(2);
                    //안나타일 중심좌표랑 상대타일 중심좌표를 이용하여 계산 이동좌표 설정해줌
                    float jegop = ((a - Unit.m_battleBounding.m_position.x) * (a - Unit.m_battleBounding.m_position.x) + (b - Unit.m_battleBounding.m_position.y) * (b - Unit.m_battleBounding.m_position.y));
                    Unit.setdistance((float) Math.sqrt(jegop));
                    Unit.setMovestate(true); //무브는 트루이다. 이동좌표탐색하고 이동가능하게 해준다.
                }
                //바운딩 영역이 겹치게 되면 이동 못하게 해준다.
                else if ((Bounding.UnitAABB(Unit.m_battleBounding.m_position, Unit.my_enemy.m_battleBounding.m_position, Unit.m_battleBounding.GetRadius(), Unit.my_enemy.unitSize) == true)) {
                    Unit.findedPath = null;
                    Unit.setState(UnitValue.S_BATTLE);
                    int count = 0;
                    for (int a = 0; a < 50; a++) {
                        for (int j = 0; j < 50; j++) {
                            if (Bounding.tileColl.get(count).resultCal(Unit.m_battleBounding.GetX(), Unit.m_battleBounding.GetY()) == true && UnitValue.m_bmap[a][j] != MapState.NotMove) {
                                Unit.myUnitObject.SetPos(a, j);
                                return;
                            }
                        }
                        count++;

                }
            }//이동이 끝나면 호출
            else if ((Unit.getMovestate() == true)) {
                if (Unit.getdistance() <= 0) {
                    if (Unit.findedPath.parentNode != null) {
                        if (Unit.my_enemy != null && Unit.my_enemy.myUnitObject.unitPosition.size() > 0) {
                            UnitValue.m_bmap[Unit.findedPath.m_pos.x][Unit.findedPath.m_pos.y] = MapState.Move;
                            Unit.myUnitObject.SetPos(Unit.findedPath.parentNode.m_pos.x, Unit.findedPath.parentNode.m_pos.y); //포지션 좌표값도 변경해서 후에 계산 편리성 도모
                            // UnitList.get(i).myPath.LoadMap(UnitValue.m_bmap);
                            Vec2F checkBoundingPosition = new Vec2F(Unit.findedPath.parentNode.m_pos.x, Unit.findedPath.parentNode.m_pos.y);
                            if (Bounding.BoundingTile(checkBoundingPosition)) {
                                //유닛끼리 겹칠경우 돌아가게 길을 찾아준다.
                                Unit.findedPath = Unit.myPath.find(enemyPositionTarget(Unit.myUnitObject.Postion, Unit.my_enemy.myUnitObject.unitPosition, Unit), Unit.myUnitObject.Postion);
                            } else {
                                //겹치지않으면 기존 찾은 패스경로로 움직여준다.
                                Unit.findedPath = Unit.findedPath.parentNode;
                            }
                        }
                        Unit.setMovestate(false);
                    }
                } else if (Unit.getdistance() > 0) {
                    UnitValue.m_bmap[Unit.findedPath.m_pos.x][Unit.findedPath.m_pos.y] = MapState.ElseMove;
                    Unit.DrawPosition.add(Unit.m_SpeedVecrt);
                    Unit.setdistance(Unit.getdistance() - 2);
                }


            }
        }
    }
}

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

}
