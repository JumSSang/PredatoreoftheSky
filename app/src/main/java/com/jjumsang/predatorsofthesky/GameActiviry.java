package com.jjumsang.predatorsofthesky;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.games.Games;
import com.jjumsang.predatorsofthesky.Game_NetWork.NetState;
import com.jjumsang.predatorsofthesky.View.LoadingView;
import com.jjumsang.predatorsofthesky.immortal.AppManager;
import com.jjumsang.predatorsofthesky.immortal.DBManager;
import com.jjumsang.predatorsofthesky.immortal.GameView;

import java.io.IOException;

/**
 * Created by GyungMin on 2015-02-03.
 */


public class GameActiviry extends Activity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DBManager.getInstance().setNetState(NetState.USERLOAD);
        try {
            DBManager.getInstance().connection.oos.writeObject("uid_request");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DBManager.getInstance().connection.oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


        GameView w =new GameView(this, new LoadingView());
        AppManager.getInstance().state=AppManager.S_LOADING;

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics( metrics );
        int g_width = metrics.widthPixels;
        int g_height = metrics.heightPixels;





        setContentView(w);

    }
    public void popup_upjeok()
    {
        startActivityForResult(Games.Achievements.getAchievementsIntent(AppManager.getInstance().mGoogleApiClient),0);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
