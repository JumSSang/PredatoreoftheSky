package com.jjumsang.predatorsofthesky.Game.UnitDirect;

import com.jjumsang.predatorsofthesky.immortal.Vec2F;

/**
 * Created by GyungMin on 2015-03-29.
 */
public class BoundingSpear
{
    private float m_radius;
    public Vec2F m_position;
    private float m_x;
    private float m_y;


    BoundingSpear(float x,float y,float r)
    {
        m_radius=r*100;
        m_x=x;
        m_y=y;

        m_position=new Vec2F(m_x,m_y);
    }

    public float GetX()
    {
        return m_x;
    }
    public float GetY()
    {
        return m_y;
    }
    public float GetRadius()
    {
        return m_radius;
    }
    public boolean collision(int x,int y)
    {

     return false;
    }
}
