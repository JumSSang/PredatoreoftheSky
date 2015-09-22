package com.jjumsang.predatorsofthesky.Game.UnitDirect;

import android.graphics.Bitmap;

import com.jjumsang.predatorsofthesky.Values.DirState;
import com.jjumsang.predatorsofthesky.immortal.SpriteControl;
import com.jjumsang.predatorsofthesky.immortal.Vec2;

import java.util.ArrayList;

/**
 * Created by GyungMin on 2015-02-23.
 */
public class Unit extends SpriteControl {

    //public Vec2 Postion;
    public boolean unitMirror=true; //true 면 오른쪽 부분 스프라이트 이미지를 반전시키지 않은 부분이다.

    public ArrayList<Vec2>unitPosition;

    public Unit(Bitmap bitmap)
    {
        super(bitmap);
        unitPosition=new ArrayList<Vec2>();
        Postion=new Vec2(0,0);
    }
    public void addPosition(Vec2 a)
    {
        unitPosition.add(a);
    }


    public void SetPos(int x,int y)
    {
        Postion.x=x;
        Postion.y=y;
    }



}
