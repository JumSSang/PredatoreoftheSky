package com.jjumsang.predatorsofthesky.GameMath;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.Values.DirState;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

/**
 * Created by 경민 on 2015-07-14.
 */
public class DirectionClass {


        public static void DirectionUpdate(int x, int y, Unit_Imfor a, int type) {
        //방향 업데이트는 x좌표 y좌표 그리고 유닛의 정보 유닛의 타입을 받아서 업데이트 한다.

            if(type==UnitValue.F_ARCHER || type==UnitValue.F_MAGICAIN||type==UnitValue.F_WORRIOR)
            {

                if (x == -1 && y == 1)//left
                {

                    if(a.myUnitObject.unitMirror==true)
                    {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror=false;
                    }
                    a.myUnitObject.mDirection = DirState.LEFT;

                } else if (x == 1 && y == 1)//top
                {
                    if(a.myUnitObject.unitMirror==false) {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror = true;

                    }
                    a.myUnitObject.mDirection = DirState.TOP;

                } else if (x == 1 && y == -1)//right
                {
                    if(a.myUnitObject.unitMirror==false)
                    {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror=true;
                    }
                    a.myUnitObject.mDirection = DirState.RIGHT;


                } else if (x == -1 && y == -1)//bottom
                {
                    if(a.myUnitObject.unitMirror==false)
                    {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror=true;
                    }
                    a.myUnitObject.mDirection = DirState.BOTTOM;

                } else if (x == 0 && y == 1)//lefttop
                {
                    if(a.myUnitObject.unitMirror==true)
                    {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror=false;
                    }
                    a.myUnitObject.mDirection = DirState.LEFT_TOP;

                } else if (x == -1 && y == 0)//leftbottom
                {
                    if(a.myUnitObject.unitMirror==true)
                    {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror=false;
                    }
                    a.myUnitObject.mDirection = DirState.LEFT_BOTTOM;

                } else if (x == 1 && y == 0)//righttop
                {
                    if(a.myUnitObject.unitMirror==false)
                    {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror=true;
                    }
                    a.myUnitObject.mDirection = DirState.RIGHT_TOP;

                } else if (x == 0 && y == -1)//rightbottom
                {
                    if(a.myUnitObject.unitMirror==false)
                    {
                        a.myUnitObject.img_mirror();
                        a.myUnitObject.unitMirror=true;
                    }
                    a.myUnitObject.mDirection = DirState.RIGHT_BOTTOM;

                }
            }


            if (type == UnitValue.F_ANNA) {
                if (x == -1 && y == 1)//left
                {
                    a.myUnitObject.mDirection = 1;

                } else if (x == 1 && y == 1)//top
                {
                    a.myUnitObject.mDirection = 3;


                } else if (x == 1 && y == -1)//right
                {
                    a.myUnitObject.mDirection = 5;


                } else if (x == -1 && y == -1)//bottom
                {
                    a.myUnitObject.mDirection = 7;

                } else if (x == 0 && y == 1)//lefttop
                {
                    a.myUnitObject.mDirection = 2;

                } else if (x == -1 && y == 0)//leftbottom
                {
                    a.myUnitObject.mDirection = 0;

                } else if (x == 1 && y == 0)//righttop
                {
                    a.myUnitObject.mDirection = 4;

                } else if (x == 0 && y == -1)//rightbottom
                {
                    a.myUnitObject.mDirection = 6;

                }

            }
            if (type == UnitValue.F_ELSATOWER)
            {
            }
    }
    public static float GetAngle(Vec2F a,Vec2F b)
    {
        float x=b.x-a.x;
        float y=b.y-a.y;
        float ang=RadianToDegree((float)(Math.atan2(y,x)));
        return ang+90;
    }
    public static float RadianToDegree(float rad)
    {
        return (float)(rad * (180 / Math.PI));
    }

    public static void AngleDirectionUpdate(Unit_Imfor a)
    {
        if(0<=a.enemyRadius&&a.enemyRadius< 22.5)
        {
            //top
            if(a.myUnitObject.unitMirror==false) {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror = true;

            }
            a.myUnitObject.mDirection = DirState.TOP;
        }
        else if(a.enemyRadius>-22.5&&a.enemyRadius<0)
        {
            //top
            if(a.myUnitObject.unitMirror==false) {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror = true;

            }
            a.myUnitObject.mDirection = DirState.TOP;
            //top
        }
        else if(22.5<=a.enemyRadius&&a.enemyRadius<67.5)
        {
            if(a.myUnitObject.unitMirror==false)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=true;
            }
            a.myUnitObject.mDirection = DirState.RIGHT_TOP;
            //topright
        }
        else if(a.enemyRadius>=67.5 &&a.enemyRadius<112.5)
        {
            if(a.myUnitObject.unitMirror==false)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=true;
            }
            a.myUnitObject.mDirection = DirState.RIGHT;
            //right

        }
        else if(a.enemyRadius>=112.5 &&a.enemyRadius<157.5)
        {
            if(a.myUnitObject.unitMirror==false)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=true;
            }
            a.myUnitObject.mDirection = DirState.RIGHT_BOTTOM;
            //right_bottom
        }
        else if(a.enemyRadius>=157.5&&a.enemyRadius<202.5 )
        {
            if(a.myUnitObject.unitMirror==false)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=true;
            }
            a.myUnitObject.mDirection = DirState.BOTTOM;
            //bottom
        }
        else if(a.enemyRadius>=202.5 && a.enemyRadius<247.5)
        {
            if(a.myUnitObject.unitMirror==true)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=false;
            }
            a.myUnitObject.mDirection = DirState.LEFT_BOTTOM;
            //bottomleft
        }
        else if(a.enemyRadius>=247.5 &&a.enemyRadius<270)
        {
            if(a.myUnitObject.unitMirror==true)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=false;
            }
            a.myUnitObject.mDirection = DirState.LEFT;
            //left
        }
        else if(a.enemyRadius>=-90 &&a.enemyRadius<-67.5)
        {
            if(a.myUnitObject.unitMirror==true)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=false;
            }
            a.myUnitObject.mDirection = DirState.LEFT;
            //left
        }
        else if(a.enemyRadius>=-67.5 &&a.enemyRadius<-22.5)
        {

            if(a.myUnitObject.unitMirror==true)
            {
                a.myUnitObject.img_mirror();
                a.myUnitObject.unitMirror=false;
            }
            a.myUnitObject.mDirection = DirState.LEFT_TOP;
            //lefttop
        }

    }


}
