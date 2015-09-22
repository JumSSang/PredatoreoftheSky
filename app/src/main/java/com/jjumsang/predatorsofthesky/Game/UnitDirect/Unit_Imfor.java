package com.jjumsang.predatorsofthesky.Game.UnitDirect;

import android.graphics.Rect;

import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.Game.PathFinder;
import com.jjumsang.predatorsofthesky.R;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.SpriteControl;
import com.jjumsang.predatorsofthesky.immortal.Vec2;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

public class Unit_Imfor {
    public Vec2F middlePosition;
    public PathFinder myPath;
    public Unit myUnitObject;
    public Unit mMyTarget;
    public int mHp;
    public int mSpeed;
    public PathFinder.Node findedPath;
    public double myTime;
    public int mType=0;
    public float unitSize=0;

    public float enemyRadius=0; //적의 각도
    public Rect originHP;
    public Rect destHP;
    public Vec2F DrawPosition;
    public Unit_Imfor my_enemy; //적정보
    public boolean b_myUnit=false;
    public double myAttackDelayTime=0;

    public SpriteControl m_effect;
    public Vec2 mVSpeed;
    private boolean mThisMove=false;
    public boolean nextstate=false; //다음 상태로 들어가기 위해 필요한 상태

    public Vec2 m_moveVector;
    public int count=0;
    public int m_range=2; //유닛이 전투 상태에 돌입하게 해주는 바운딩 영역의 반지름이 된다.
    public float m_ac_range=2.0f; //유닛이 상대를 인식하게 되는 바운딩 거리
 //   private int range=2;
    public BoundingSpear m_BoundingSpear;
    public BoundingSpear m_battleBounding;
    public BoundingSpear m_longdistance;


    public boolean m_attck=false;
    public int findingTilenumber=0;
    public int m_time_pathfinder=0;
    public Vec2F m_SpeedVecrt;
    private float m_distance=0;
    private int tempHp;

    public boolean boom_start=false;
    public boolean boom_erase=false;

    public int m_maparea=0;
    public boolean m_findMap=false;





    private int state=1; //0은 평화 1은 이동 2는 전투


    public float getdistance()
    {
        return m_distance;
    }
    public void setdistance(float distance)
    {
        m_distance=distance;
    }

    public void setMovestate(boolean a)
    {
        this.mThisMove=a;
    }
    public boolean getMovestate()
    {
        return mThisMove;
    }
    public Unit_Imfor(Unit myUnitObject, int hp, int mSpeed, int type,boolean b_myUnit) {
            myPath=new PathFinder();
            this.mHp=hp;
        this.tempHp=hp;
            this.mSpeed=mSpeed;
            this.myUnitObject=myUnitObject;
            this.myTime=0;
        this.b_myUnit=b_myUnit;

        switch(type)
        {
            case UnitValue.F_ANNA:
                this.unitSize=0.2f;
                middlePosition=  new Vec2F((float) (750 + 50 / 2 * (myUnitObject.Postion.y - myUnitObject.Postion.x))+10+ 16, (float) (-300 + 25 / 2 * (myUnitObject.Postion.y + myUnitObject.Postion.x))-40 + 50);
                DrawPosition = new Vec2F((float) (750 + 50 / 2 * (myUnitObject.Postion.y - myUnitObject.Postion.x))+10  , (float) (-300 + 25 / 2 * (myUnitObject.Postion.y + myUnitObject.Postion.x))-40);
                break;
            case UnitValue.F_ELSATOWER:
                this.unitSize=0.2f;
                DrawPosition = new Vec2F((float) (750 + 50 / 2 * (myUnitObject.Postion.y - myUnitObject.Postion.x)), (float) (-300 + 25 / 2 * (myUnitObject.Postion.y + myUnitObject.Postion.x)));
                break;
            case UnitValue.F_TOWER:
                this.unitSize=0.2f;
                DrawPosition = new Vec2F((float) (750 + 50 / 2 * (myUnitObject.Postion.y - myUnitObject.Postion.x))-15, (float) (-300 + 25 / 2 * (myUnitObject.Postion.y + myUnitObject.Postion.x))-50);
                break;
            case UnitValue.F_BOOM:
                this.unitSize=0.2f;
                DrawPosition = new Vec2F((float) (750 + 50 / 2 * (myUnitObject.Postion.y - myUnitObject.Postion.x)), (float) (-300 + 25 / 2 * (myUnitObject.Postion.y + myUnitObject.Postion.x)-25));
                myAttackDelayTime=5;
                break;
            case UnitValue.F_ARCHER:
                this.unitSize=0.2f;
                DrawPosition = new Vec2F((float) (750 + 50 / 2 * (myUnitObject.Postion.y - myUnitObject.Postion.x))-25, (float) (-300 + 25 / 2 * (myUnitObject.Postion.y + myUnitObject.Postion.x)-25));
                myAttackDelayTime=2;


                break;
            default:
                DrawPosition = new Vec2F((float) (750 + 50 / 2 * (myUnitObject.Postion.y - myUnitObject.Postion.x)), (float) (-300 + 25 / 2 * (myUnitObject.Postion.y + myUnitObject.Postion.x)));
                break;
        }

        m_moveVector=new Vec2((float)0,(float)0);

            this.mType=type;
    }
    public void setRange(int r)
    {
        this.m_range=r;
    }

    public void boundingPositionUpdate(float x,float y)
    {
        switch(this.mType)
        {
            case UnitValue.F_ANNA:
                this.m_battleBounding = new BoundingSpear(x + 16, y + 50, 0.2f); //안나의 바운딩 영역 위치 생성
                this.m_BoundingSpear = new BoundingSpear(x + 15, y + 50, 2);
                this.unitSize=0.2f;
                break;
            case UnitValue.F_ELSATOWER:
                this.m_battleBounding=new BoundingSpear(x+25,y+25,0.4f);
                this.m_BoundingSpear=new BoundingSpear(x+25,y+25,2);
                this.unitSize=0.2f;
                break;
            case UnitValue.F_TOWER:
                this.m_battleBounding=new BoundingSpear(x+35,y+75,0.4f);
                this.m_BoundingSpear=new BoundingSpear(x+35,y+75,2);
                this.unitSize=0.2f;
                break;
            case UnitValue.F_BOOM:
                this.m_battleBounding = new BoundingSpear(x + 20, y+25, 0.2f); //안나의 바운딩 영역 위치 생성
                this.m_BoundingSpear = new BoundingSpear(x + 20, y+25 , 2);
                this.unitSize=0.2f;
                break;
            case UnitValue.F_ARCHER:
                this.m_battleBounding = new BoundingSpear(x + 32, y+60, 2); //안나의 바운딩 영역 위치 생성
                this.m_BoundingSpear = new BoundingSpear(x + 32, y+60 , 2);
                this.unitSize=0.2f;

                break;
            case UnitValue.F_WORRIOR:
                this.m_battleBounding = new BoundingSpear(x + 32, y+60, 0.2f); //안나의 바운딩 영역 위치 생성
                this.m_BoundingSpear = new BoundingSpear(x + 32, y+60 , 2);
                this.unitSize=0.2f;
                break;
            case UnitValue.F_MAGICAIN:
                this.m_battleBounding = new BoundingSpear(x + 32, y+60, 2f); //안나의 바운딩 영역 위치 생성
                this.m_BoundingSpear = new BoundingSpear(x + 32, y+60 , 2);
                this.unitSize=0.2f;
                break;
            default:
                this.m_battleBounding=new BoundingSpear(x+10,y,0.2f);
                this.m_BoundingSpear=new BoundingSpear(x+10,y,2);
                this.unitSize=0.2f;
                break;
        }

    }
    public void setState(int a)
    {
        this.state=a;
    }
    public int getState()
    {
        return this.state;
    }
    public void InitEffect(int Value)
    {
        if(Value==UnitValue.F_ELSATOWER) {
            m_effect = new SpriteControl(AppManager.getInstance().getBitmap(R.drawable.buble_paritcle));
            m_effect.resizebitmap(2000, 100);
            m_effect.Effect(1);
        }
        else if(Value==UnitValue.F_ANNA)
        {
            m_effect =new SpriteControl(GraphicManager.getInstance().m_anna_punch.m_bitmap);
            m_effect.AnnaEffect(200);
         }

    }
    public void minus(Vec2 a,Vec2 b)
    {
        float x=a.fx;
        float y=a.fy;
        float x2=b.fx;
        float y2=b.fy;
        m_moveVector=new Vec2(x2-x,y2-y);
    }
    public void WhoEnemy(Unit mMyTarget)
    {
        this.mMyTarget=mMyTarget;
        Targeting();
    }
    public void Targeting()
    {
        findedPath=myPath.find(mMyTarget.Postion,myUnitObject.Postion);
    }

    public boolean boundingUnit()
    {

        return false;
    }

    //750 + 50 / 2 * (y - x)
    public void Hpbar()
    {
        int x=(int)DrawPosition.x;
        int y=(int)DrawPosition.y;
        int dx=x;
        int dy= y;

        originHP=new Rect(dx,
                dy,
                dx+mHp,
                dy+5);
        destHP =new Rect(dx,
                dy,
                dx+tempHp
                ,dy+5);

    }

    public void Speed(float x,float y)
    {
          this.mVSpeed=new Vec2(x,y);
    }






}
