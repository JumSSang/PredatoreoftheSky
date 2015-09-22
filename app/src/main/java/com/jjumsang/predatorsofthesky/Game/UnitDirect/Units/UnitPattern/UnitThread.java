package com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.UnitPattern;

import android.util.Log;

import com.jjumsang.predatorsofthesky.Game.UnitDirect.Unit_Imfor;
import com.jjumsang.predatorsofthesky.Game.UnitDirect.Units.Pattern;

import java.util.ArrayList;

/**
 * Created by gyungmin on 2015-09-06.
 */
public class UnitThread extends Thread {
    int count=0;
    ArrayList<Unit_Imfor>m_allList;
    public UnitThread(ArrayList<Unit_Imfor> AllUnit)
    {
        m_allList=AllUnit;
    }

    public static void find(Unit_Imfor a,Unit_Imfor enemy)
    {
        a.findedPath = a.myPath.find(Pattern.enemyPositionTarget(a.myUnitObject.Postion, enemy.myUnitObject.unitPosition, a), a.myUnitObject.Postion); //적의 위치로 달려가게 길을 찾아준다.
    }

    public void run()
    {
        while(true)
        {
            for(int i=0;i<m_allList.size();i++)
            {
                try {
                    if (m_allList.get(i).m_findMap == true) {

                        find(m_allList.get(i), m_allList.get(i).my_enemy);
                        m_allList.get(i).m_findMap = false;
                    }
                }
                catch(NullPointerException e)
                {

                }
                catch(IndexOutOfBoundsException e)
                {
                    Log.e("바운딩 익셉션","에러");
                }
            }
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
