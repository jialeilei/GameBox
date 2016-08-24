package com.lei.fivesonschess.game;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

/**
 * Created by lei on 2016/8/23.
 */
public class NumberGame extends GridLayout{
    private static String TAG = "NumberGame";
    private Card[][] cardsMap = new Card[4][4];

    public NumberGame(Context context) {
        super(context);
        initGameView();
    }

    public NumberGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public NumberGame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }


    private void initGameView(){
        setColumnCount(4);//指定4列
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new OnTouchListener() {
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX > 50) {
                                swipeRight();
                            } else if (offsetX < -50) {
                                swipeLeft();
                            }
                        } else {
                            if (offsetY > 50) {
                                swipeDown();
                            } else if (offsetY < -50) {
                                swipeUp();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = (Math.min(w, h)-10)/4;
        addCards(width,width);
    }

    private void addCards(int width, int height){
        Card c;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                c = new Card(getContext());
                c.setNum(2);
                addView(c,width,height);
            }
        }
    }

    private void swipeLeft(){

    }

    private void swipeRight(){

    }

    private void swipeUp(){

    }

    private void swipeDown(){

    }

}
