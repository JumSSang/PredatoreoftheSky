package com.jjumsang.predatorsofthesky.GameMath;

import com.jjumsang.predatorsofthesky.Values.DirState;
import com.jjumsang.predatorsofthesky.immortal.Vec2F;

public class Vezier {
    float verzierLength;//
    float vezier50;//
    public Vec2F m_startPosition;
    public Vec2F m_UpPosition;
    public Vec2F m_targetPosition;
    public Vec2F m_middle_Line;
    public Vec2F m_danVector;
    public  Vec2F cal_mQ; //베지어 삼각형 꼭지점
    public  Vec2F mQ0;
    public   Vec2F mQ1;
    public  Vec2F m_vezier_higher;//베지어 곡선의 꼭지점 벡터
    public   Vec2F m_start_higer;//시작점 기울기에서 높이만큼 올라온 위치
    public    Vec2F m_target_higer; //타겟점 기울기에서 높이만큼 올라온 위치
    public Vec2F movePath;

    public Vezier(Vec2F startPosition, Vec2F targetPosition)
    {
        m_startPosition=startPosition;
        //m_UpPosition=upVector;
        m_targetPosition=targetPosition;
       // m_vezier_higher=vezier_higher;

    }
    public void vezier_degree(float trate)
    {

                mQ0=new Vec2F( (m_startPosition.x+(m_vezier_higher.x-m_startPosition.x)*trate),(m_startPosition.y+ (m_vezier_higher.y-m_startPosition.y)*trate)); //베지어 곡선을 움직이게 하는 점 1
                mQ1=new Vec2F( (m_vezier_higher.x+(m_targetPosition.x-m_vezier_higher.x)*trate),(m_vezier_higher.y+(m_targetPosition.y-m_vezier_higher.y)*trate)); //베지어 곡선을 움직이게 하는 점 2
                movePath=new Vec2F((mQ0.x+(mQ1.x-mQ0.x)*trate),(mQ0.y+(mQ1.y-mQ0.y)*trate));  //베지어 곡선을 이루는 점이다.




    }


    //실제로 입력된 정보를 바탕으로 베지어 곡선을 그려주는 함수이다.
    public void resultvezier()
    {

        //베지어 곡선의 꼭지점이 될 위치와 대응 되는 중앙의 포지션
        m_middle_Line=new Vec2F(((m_targetPosition.x - m_startPosition.x) / 2 + m_startPosition.x),((m_targetPosition.y - m_startPosition.y) / 2 + m_startPosition.y));
        m_danVector=new Vec2F((m_targetPosition.y-m_startPosition.y),-(m_targetPosition.x-m_startPosition.x)); //실제 길이를 구해주기 위해 만들어줌
        verzierLength = (float) Math.sqrt(m_danVector.x * m_danVector.x + m_danVector.y * m_danVector.y); //베지어 곡선이 그려질 직선의 살재 길이값
        if(m_startPosition.x>m_targetPosition.x)
        {
            vezier50 = -(m_middle_Line.y - (m_middle_Line.y - 50)) / verzierLength; //방향을 구한다.
        }
        else {
            vezier50 = (m_middle_Line.y - (m_middle_Line.y - 50)) / verzierLength; //방향을 구한다.
        }
        m_danVector.x*=vezier50; //방향벡터이다.
        m_danVector.y*=vezier50;//방향벡터
        m_start_higer=new Vec2F(m_startPosition.x+m_danVector.x,m_startPosition.y+m_danVector.y);
        m_target_higer=new Vec2F(m_targetPosition.x+m_danVector.x,m_targetPosition.y+m_danVector.y);
        m_vezier_higher=new Vec2F((m_target_higer.x-m_start_higer.x)/2+m_start_higer.x,(m_target_higer.y-m_start_higer.y)/2+m_start_higer.y); //베지어 곡선의 꼭지점



    }


}
