package com.jjumsang.predatorsofthesky.Game.UnitDirect;

import android.graphics.Canvas;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.BoundingSpear;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.UnitValue;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.GameMath.Vezier;
import com.jjumsang.predatorsofthesky.immortal.GraphicManager;
import com.jjumsang.predatorsofthesky.immortal.SpriteControl;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

import static com.jjumsang.predatorsofthesky.GameMath.DirectionClass.GetAngle;

/**
 * Created by 경민 on 2015-06-09.
 */
public class MissleManager {
    public SpriteControl img_Sprite;
    Unit_Imfor m_parents;
    Vec2F m_Target; //미사일 타겟
    Vec2F m_StartPosition; //미사일 시작 지점
    Vec2F m_SpeedVector;//방향성을 가지는 벡터 미사일 이동 할때 add해줄값
    public Vec2F DrawPosition;
    float m_Damage; //미사일의 데미지
    float m_Speed; //미사일의 속도
    BoundingSpear m_BoundgArea; //미사일을 감싸고있는 바운딩 영역이다.
    boolean b_team = false;
    int type = 0;
    float m_distance = 0;
    boolean state=false;
    float move=0.0f; //베지어 이동 비율
    Vezier track; //활이나 곡선운동하는 물체에 사용되는 변수이다.
    float m_degree=0.0f;

    public MissleManager(Vec2F StartPosition, Vec2F target, float damage, float speed, boolean team, int type, Unit_Imfor parents) {

        this.DrawPosition =new Vec2F(StartPosition.x,StartPosition.y-120);
        this.m_parents=parents;
        this.m_Target = target;
        this.m_Damage = damage;
        this.m_Speed = speed;
        this.b_team = team;
        this.type = type;
        switch (type) {
            case UnitValue.BU_SPPOT:
                this.m_StartPosition=new Vec2F( StartPosition.x, StartPosition.y-120);
                img_Sprite = GraphicManager.getInstance().m_bulletSprite;
                directionVector(); //방향및 위치 거리 계산 해주는 함수
                break;
            case UnitValue.BU_ARROW:
                this.m_StartPosition=new Vec2F( StartPosition.x+35, StartPosition.y-50);
                img_Sprite = GraphicManager.getInstance().m_bulletArrow;
                track =new Vezier(m_StartPosition,m_Target);

                if(m_StartPosition.x<m_Target.x) {
                    track.resultvezier();
                }
                else
                {
                    track.resultvezier();
                }

                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
        state=true;

    }

    public void directionVector() {
        m_SpeedVector = new Vec2F(m_Target.x, m_Target.y);
        m_SpeedVector.sub(DrawPosition);
        m_SpeedVector.normalize();
        m_SpeedVector.multiply(m_Speed);
        float jegop = ((m_Target.x - m_StartPosition.x) * (m_Target.x - m_StartPosition.x) + (m_Target.y - m_StartPosition.y) * (m_Target.y - m_StartPosition.y));
        m_distance = (float) Math.sqrt(jegop);
    }


    public void moveSpotBullet()
    {
        if (m_distance <= 0) {
            state=false;
        } else {
            DrawPosition.add(m_SpeedVector);
            m_distance -= m_Speed;
        }
    }
    public void moveArrowUpdate(double go) //go만큼 미슬이 이동한다
    {
        move+=go;
        if(move<1) {
               track.vezier_degree(move);
               m_degree = GetAngle(new Vec2F(track.mQ0.x, track.mQ0.y), new Vec2F(track.mQ1.x, track.mQ1.y)); //활의 각도 업데이트

        }
        else
        {
            state=false;
        }
    }

    public void draw(Canvas canvas) {
       switch(type)
       {
           case UnitValue.BU_SPPOT:
               drawBullet(canvas);
               break;
           case UnitValue.BU_ARROW:
               drawArroaw(canvas);
               break;
       }
    }
    public void drawBullet(Canvas canvas)
    {
        img_Sprite.Update(System.currentTimeMillis());
        img_Sprite.EffectDraw(canvas, DrawPosition.x, DrawPosition.y);
    }
    public void drawArroaw(Canvas canvas)
    {
        canvas.save();
        canvas.rotate(m_degree,track.movePath.x,track.movePath.y);
       //  RotateBitmap.rotate(img_Sprite.m_bitmap,45);
        img_Sprite.Draw(canvas,(int)track.movePath.x,(int)track.movePath.y);
        canvas.restore();
    }


}
