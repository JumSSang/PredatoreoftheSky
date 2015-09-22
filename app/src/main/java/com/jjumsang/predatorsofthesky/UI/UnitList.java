package com.jjumsang.predatorsofthesky.UI;

import com.jjumsang.predatorsofthesky.immortal.DBManager;

/**
 * Created by GyungMin on 2015-02-03.
 */



public class UnitList
{
    private DBManager.디비유닛 m_Unit;

   public UnitList(DBManager.디비유닛 유닛명)
    {
        m_Unit=유닛명;
    }


    public int retruncode()
    {
        return m_Unit.type;
    }

}

