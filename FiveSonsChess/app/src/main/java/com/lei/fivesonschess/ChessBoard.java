package com.lei.fivesonschess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lei on 2016/7/3.
 */

public class ChessBoard extends View {
    private static String TAG="ChessBoard";
    private int maxX,xOffset,distance;
    private int pointSize=13;
    private int[][] allPointArray =new int[pointSize][pointSize];   // 1为白方，2为黑方,储存着棋盘上的所有点
    private float radius;//半径
    private float positionX,positionY;
    private boolean circleKey=false;//第一次画棋盘时不画棋子
    private boolean colorWhite=true;
    private boolean victor =false;//是否有人获胜
    private int width;//边界

    private int gameModel=0;
    private int point[]={0,0};
    private OnChessBoardListener mOnChessBoardListener;
    //count
    private int whiteCountX=0,blackCountX=0,whiteCountY=0,blackCountY=0;
    private int downWhiteCountSlantToRightBottom =0, downBlackCountSlantToRightBottom =0,upBlackCountSlantToRightBottom=0,upWhiteCountSlantToRightBottom=0;
    private int downWhiteCountSlantToLeftBottom =0, downBlackCountSlantToLeftBottom =0,upBlackCountSlantToLeftBottom=0,upWhiteCountSlantToLeftBottom=0;
    private int whiteNumber=0,blackNumber=0;


    public ChessBoard(Context context) {
        this(context, null);
    }

    public ChessBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setGameModel(int num){
        gameModel=num;
    }

    public void setBoardSize(int size){
        pointSize=size;
        allPointArray =new int[pointSize][pointSize];
        maxX= ((int) Math.floor(width / pointSize));
        xOffset=(width-pointSize*maxX+maxX)/2;  //boarder
        radius=maxX*0.4f;
        distance=maxX/2;
        resetBoard();
    }


    public void resetBoard(){
        circleKey=false;//第一次画棋盘时不画棋子
        colorWhite=true;
        victor =false;//是否有人获胜
        whiteNumber=0;
        blackNumber=0;
        clearChessCount();
        resetData();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure:  width: " + widthMeasureSpec + " height: " + heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        maxX= ((int) Math.floor(w / pointSize));
        xOffset=(w-pointSize*maxX+maxX)/2;  //boarder
        radius=maxX*0.4f;
        distance=maxX/2;
        Log.i(TAG, "onSizeChanged  width: " + w + " height: " + h + " maxX: " + maxX);
        resetData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawKeyBoard(canvas);
        if (circleKey){
            drawCircle(canvas);
        }else {
            circleKey=true;
        }
    }

    private void drawKeyBoard(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.BLACK);// 设置红色
        p.setStrokeWidth(4);
        for (int i=0;i<pointSize;i++){
            canvas.drawLine(xOffset,xOffset+maxX*i, xOffset+(pointSize-1)*maxX,  xOffset+maxX*i, p);// 画线
            canvas.drawLine(xOffset + maxX * i, xOffset, xOffset + maxX * i, xOffset + (pointSize - 1) * maxX, p);// 画线
        }
        p.reset();
    }

    private void drawCircle(Canvas canvas){
        Paint thumbPaint = new Paint();
        thumbPaint.setStyle(Paint.Style.FILL);

        for (int i=0;i<pointSize;i++){//扫描数组  进行画圆
            for (int j=0;j<pointSize;j++){
                if (allPointArray[i][j]>0){
                    positionX=xOffset+maxX*i;
                    positionY=xOffset+maxX*j;
                    if (allPointArray[i][j]==1){
                        thumbPaint.setColor(Color.WHITE);
                    }else {
                        thumbPaint.setColor(Color.BLACK);
                    }
                    canvas.drawCircle(positionX, positionY, radius, thumbPaint);
                }
            }
        }
    }

    private void resetData(){
        for (int i=0;i<pointSize;i++){
            for (int j=0;j<pointSize;j++){
                allPointArray[i][j]=0;
            }
        }
    }

    private void clearChessCount(){
        whiteCountX=0;blackCountX=0;whiteCountY=0;blackCountY=0;
        downWhiteCountSlantToRightBottom =0;downBlackCountSlantToRightBottom =0;upBlackCountSlantToRightBottom=0;upWhiteCountSlantToRightBottom=0;
        downWhiteCountSlantToLeftBottom =0;downBlackCountSlantToLeftBottom =0;upBlackCountSlantToLeftBottom=0;upWhiteCountSlantToLeftBottom=0;
    }

    private void getPosition(){
        int distanceX;
        point[0]=0;
        point[1]=1;
        for (int i=0;i<pointSize;i++){ //寻找最近Y坐标
            distanceX=xOffset+maxX*i;
            if (Math.abs(distanceX-positionX) <= distance){
                positionX=distanceX;
                point[0]=i;
            }
        }
        for (int h=0;h<pointSize;h++){ //寻找最近X坐标
            distanceX=xOffset+maxX*h;
            if (Math.abs(distanceX-positionY) <= distance){
                positionY=distanceX;
                point[1]=h;
            }
        }
    }

    private void getXYWhite(int i,int j){
        if (allPointArray[i][j]==1){
            whiteCountY++;
            if (whiteCountY >= 5){
                victor =true;
                //Log.i(TAG, "众向胜利:  White");
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.whiteVictor();
                }
            }
        }else {
            whiteCountY=0;
        }

        if (allPointArray[j][i]==1){
            whiteCountX++;
            if (whiteCountX==5){
                victor =true;
                //Log.i(TAG, "横向胜利:  White");
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.whiteVictor();
                }
            }
        }else {
            whiteCountX=0;
        }
    }

    private void getXYBlack(int i,int j){
        if (allPointArray[i][j]==2){
            blackCountY++;
            if (blackCountY==5){
                victor =true;
                //Log.i(TAG, "众向胜利:  Black");
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.blackVictor();
                }
            }
        }else {
            blackCountY=0;
        }

        if (allPointArray[j][i]==2){
            blackCountX++;
            if (blackCountX==5){
                victor =true;
                //Log.i(TAG, "横向胜利:  Black");
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.blackVictor();
                }
            }
        }else {
            blackCountX=0;
        }
    }

    private void slideToRightBottom(int down,int i){
        //up
        if (allPointArray[pointSize-1-down+i][i]==1){
            upWhiteCountSlantToRightBottom++;
            if (upWhiteCountSlantToRightBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.whiteVictor();
                }
            }
        }else {
            upWhiteCountSlantToRightBottom =0;
        }

        if (allPointArray[pointSize-1-down+i][i]==2){
            upBlackCountSlantToRightBottom++;
            if (upBlackCountSlantToRightBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.blackVictor();
                }
            }
        }else {
            upBlackCountSlantToRightBottom =0;
        }
        //down
        if (allPointArray[i][pointSize-1-down+i]==1){
            downWhiteCountSlantToRightBottom++;
            if (downWhiteCountSlantToRightBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.whiteVictor();
                }
            }
        }else {
            downWhiteCountSlantToRightBottom =0;
        }

        if (allPointArray[i][pointSize-1-down+i]==2){
            downBlackCountSlantToRightBottom++;
            if (downBlackCountSlantToRightBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.blackVictor();
                }
            }
        }else {
            downBlackCountSlantToRightBottom =0;
        }

    }

    private void slideToLeftBottom(int down,int i){
        //up
        if (allPointArray[down-i][i]==1){
            upWhiteCountSlantToLeftBottom++;
            if (upWhiteCountSlantToLeftBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.whiteVictor();
                }
            }
        }else {
            upWhiteCountSlantToLeftBottom =0;
        }

        if (allPointArray[down-i][i]==2){
            upBlackCountSlantToLeftBottom++;
            if (upBlackCountSlantToLeftBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.blackVictor();
                }
            }
        }else {
            upBlackCountSlantToLeftBottom =0;
        }

        //down
        if (allPointArray[pointSize-1-i][pointSize-1-down+i]==1){
            downWhiteCountSlantToLeftBottom++;
            if (downWhiteCountSlantToLeftBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.whiteVictor();
                }
            }
        }else {
            downWhiteCountSlantToLeftBottom =0;
        }

        if (allPointArray[pointSize-1-i][pointSize-1-down+i]==2){
            downBlackCountSlantToLeftBottom++;
            if (downBlackCountSlantToLeftBottom >=5){
                victor=true;
                if (mOnChessBoardListener!=null){
                    mOnChessBoardListener.blackVictor();
                }
            }
        }else {
            downBlackCountSlantToLeftBottom =0;
        }

    }

    private void findXYVictor(){
        for (int i=0;i<pointSize;i++){
            clearChessCount();
            for (int j=0;j<pointSize;j++){
                getXYWhite(i, j);
                getXYBlack(i, j);
            }
        }
    }

    private void findSlantVictor(){
        for (int down=4;down<pointSize;down++){
            clearChessCount();
            for (int i=0;i<=down;i++){
                slideToRightBottom(down, i);//slide from leftTop to rightBottom
                slideToLeftBottom(down, i);//slide from rightTop to leftBottom
            }
        }
    }

    private void findVictor(){
        findXYVictor();
        findSlantVictor();
    }

    private void intelligence(){
        allPointArray[point[0]][point[1]]=2;
        colorWhite=!colorWhite;
        blackNumber++;
        if (mOnChessBoardListener!=null){
            mOnChessBoardListener.number(whiteNumber,blackNumber);
        }
        invalidate();
        findVictor();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x= ((int) event.getX());
        float y=event.getY();
        positionX=event.getX();
        positionY=event.getY();

        switch (event.getAction()){
            case 0://ACTION_DOWN

                break;
            case 1://ACTION_UP
                clearChessCount();
                if (!victor){
                        getPosition();//找到坐标并进行绘制
                        if (allPointArray[point[0]][point[1]] > 0){
                            Log.i(TAG, "circle have been exist "+ allPointArray[point[0]][point[1]]);
                        }else {
                            if (gameModel==0){ //person with person
                                if (colorWhite){
                                    allPointArray[point[0]][point[1]]=1;
                                    colorWhite=!colorWhite;
                                    whiteNumber++;
                                    if (mOnChessBoardListener!=null){
                                        mOnChessBoardListener.number(whiteNumber,blackNumber);
                                    }
                                }else {
                                    allPointArray[point[0]][point[1]]=2;
                                    colorWhite=!colorWhite;
                                    blackNumber++;
                                    if (mOnChessBoardListener!=null){
                                        mOnChessBoardListener.number(whiteNumber,blackNumber);
                                    }
                                }
                                invalidate();
                                findVictor();
                            }else if (gameModel==1){ //person with machine

                                if (colorWhite){
                                    allPointArray[point[0]][point[1]]=1;
                                    colorWhite=!colorWhite;
                                    whiteNumber++;
                                    if (mOnChessBoardListener!=null){
                                        mOnChessBoardListener.number(whiteNumber,blackNumber);
                                    }
                                    invalidate();
                                    findVictor();
                                }
                                intelligence();
                            }
                        }

                }

                break;
            case 2://ACTION_MOVE

                break;
        }
        return true;
    }

    public interface OnChessBoardListener{
        void whiteVictor();
        void blackVictor();
        void number(int white,int black);
    }
    public void setOnChessBoardListener(OnChessBoardListener onChessBoardListener){
        this.mOnChessBoardListener=onChessBoardListener;

    }
}
