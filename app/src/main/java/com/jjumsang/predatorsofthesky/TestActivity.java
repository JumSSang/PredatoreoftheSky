package com.jjumsang.predatorsofthesky;

import android.app.Activity;
import android.os.Bundle;

import com.jjumsang.predatorsofthesky.GameMath.MathTestView;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.GameView;

/**
 * Created by 경민 on 2015-05-18.
 */
public class TestActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        GameView w =new GameView(this, new MathTestView());
        AppManager.getInstance().state=AppManager.S_LOADING;
        setContentView(w);

    }

}
