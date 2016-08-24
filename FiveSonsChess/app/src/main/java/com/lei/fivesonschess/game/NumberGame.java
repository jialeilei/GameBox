package com.lei.fivesonschess.game;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2016/8/23.
 */

public class NumberGame extends GridLayout{
    private static String TAG = "NumberGame";
    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = (Math.min(w, h)-10)/4;
        addCards(width, width);
        startGame();
    }

    private void addCards(int width, int height){
        Card c;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, width, height);
                cardsMap[x][y] = c;
            }
        }
    }

    private void startGame(){
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
    }

    private void addRandomNum(){

        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0){
                    emptyPoints.add(new Point(x,y));
                }
            }
        }

        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));//生成随机点
        cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);
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





    private void swipeLeft(){
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                for (int x1 = x+1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum() > 0){
                        if (cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x--;
                            break;

                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            break;
                        }
                    }
                }
            }
        }

    }

    private void swipeRight(){
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                for (int x1 = x-1; x1 >=0 ; x1--) {
                    if (cardsMap[x1][y].getNum() > 0){
                        if (cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;
                            break;

                        }else if (cardsMap[x][y].equals(cardsMap[x1][y])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x1][y].setNum(0);
                            break;
                        }
                    }
                }
            }
        }

    }

    private void swipeUp(){

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int y1 = y+1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum() > 0){
                        if (cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;
                            break;

                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void swipeDown(){

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >=0; y--) {
                for (int y1 = y-1; y1 >=0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0){
                        if (cardsMap[x][y].getNum() <= 0){
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y++;
                            break;

                        }else if (cardsMap[x][y].equals(cardsMap[x][y1])){
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
                            cardsMap[x][y1].setNum(0);
                            break;
                        }
                    }
                }
            }
        }
    }

}
