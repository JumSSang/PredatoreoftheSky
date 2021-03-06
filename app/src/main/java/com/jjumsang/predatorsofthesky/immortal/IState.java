package com.jjumsang.predatorsofthesky.immortal;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.io.IOException;

public interface IState {
    public void Init();

    public void Destroy();

    public void Update();

    public void Render(Canvas canvas);

    public boolean onKeyDown(int keyCode, KeyEvent event);

    public boolean onTouchEvent(MotionEvent event);


}
