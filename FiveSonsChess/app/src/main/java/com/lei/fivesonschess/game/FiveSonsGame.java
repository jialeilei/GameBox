package com.lei.fivesonschess.game;

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

public class FiveSonsGame extends View {
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
    private int gameModel=0;//1:easy   2:common   3:difficult
    private int point[]={0,0};
    private OnChessBoardListener mOnChessBoardListener;
    //private int whiteBetter,blackBetter;
    //count
    private int whiteCountX=0,blackCountX=0,whiteCountY=0,blackCountY=0;
    private int downWhiteCountSlantToRightBottom =0, downBlackCountSlantToRightBottom =0,upBlackCountSlantToRightBottom=0,upWhiteCountSlantToRightBottom=0;
    private int downWhiteCountSlantToLeftBottom =0, downBlackCountSlantToLeftBottom =0,upBlackCountSlantToLeftBottom=0,upWhiteCountSlantToLeftBottom=0;
    private int whiteNumber=0,blackNumber=0;   //步数
    //artificial intelligence
    private int scoreTable[]={7,35,800,15000,800000,5000000,0};
    private int whiteResultScore=0,blackResultScore=0, fiveArrayWhiteCount =0, fiveArrayBlackCount =0;
    private int defensePoint[]={0,0},attackPoint[]={0,0};
    private int whiteBigger =0,blackBigger=0;
    private boolean attack=false;


    public FiveSonsGame(Context context) {
        this(context, null);
    }

    public FiveSonsGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setGameModel(int num){
        gameModel=num;
        resetBoard();
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
        resetAllPoint();
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
        resetAllPoint();
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

    private void resetAllPoint(){
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
        whiteResultScore=0;
        blackResultScore=0;
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

    private void getScoreSlideToRightBottom(int down, int i){

        for (int c=0;c<5;c++){
            //up
            if (allPointArray[pointSize-1-down+i+c][i+c]==1){
                fiveArrayWhiteCount++;
            }else if (allPointArray[pointSize-1-down+i+c][i+c]==2){
                fiveArrayBlackCount++;
            }
        }
        countScore();

        for (int c=0;c<5;c++){
            //down
            if (allPointArray[i+c][pointSize-1-down+i+c]==1){
                fiveArrayWhiteCount++;
            }else if (allPointArray[i+c][pointSize-1-down+i+c]==2){
                fiveArrayBlackCount++;
            }
        }
        countScore();
    }

    private void getScoreSlideToLeftBottom(int down, int i){

        for (int c=0;c<5;c++){
            //up
            if (allPointArray[down-i-c][i+c]==1){
                fiveArrayWhiteCount++;
            }else if (allPointArray[down-i-c][i+c]==2){
                fiveArrayBlackCount++;
            }
        }

        countScore();

        for (int c=0;c<5;c++){
            //down
            if (allPointArray[pointSize-1-i-c][pointSize-1-down+i+c]==1){
                fiveArrayWhiteCount++;
            }else if (allPointArray[pointSize-1-i-c][pointSize-1-down+i+c]==2){
                fiveArrayBlackCount++;
            }
        }
        countScore();
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

    private void countScore(){
        switch (fiveArrayWhiteCount){//white chess number
            case 0:
                switch (fiveArrayBlackCount){
                    case 0:
                        whiteResultScore=whiteResultScore+7;
                        blackResultScore=blackResultScore+7;
                        break;
                    case 1:
                        blackResultScore=blackResultScore+35;
                        break;
                    case 2:
                        blackResultScore=blackResultScore+800;
                        break;
                    case 3:
                        blackResultScore=blackResultScore+15000;
                        break;
                    case 4:
                        blackResultScore=blackResultScore+800000;
                        break;
                    case 5:
                        blackResultScore=blackResultScore+8000000;
                        break;
                }
                break;
            case 1:
                if (fiveArrayBlackCount ==0){
                    whiteResultScore=whiteResultScore+35;
                }
                break;
            case 2:
                if (fiveArrayBlackCount ==0){
                    whiteResultScore=whiteResultScore+800;
                }
                break;
            case 3:
                if (fiveArrayBlackCount ==0){
                    whiteResultScore=whiteResultScore+15000;
                }
                break;
            case 4:
                if (fiveArrayBlackCount ==0){
                    whiteResultScore=whiteResultScore+800000;
                }
                break;
            case 5:
                if (fiveArrayBlackCount ==0){
                    whiteResultScore=whiteResultScore+8000000;
                }
                break;
        }
        fiveArrayWhiteCount =0;
        fiveArrayBlackCount =0;
    }

    private void getXYScore(){

        for (int i=0;i<pointSize;i++){
            for (int j=0;j<pointSize-4;j++){
                //Y
                for (int c=0;c<5;c++){
                    if (allPointArray[i][j+c]==1){//to get the white chess number in array
                        fiveArrayWhiteCount++;
                    }else if (allPointArray[i][j+c]==2){//to get the black chess number in array
                        fiveArrayBlackCount++;
                    }
                }
                countScore();

                //X
                for (int c=0;c<5;c++){
                    if (allPointArray[j+c][i]==1){//to get the white chess number in array
                        fiveArrayWhiteCount++;
                    }else if (allPointArray[j+c][i]==2){//to get the black chess number in array
                        fiveArrayBlackCount++;
                    }
                }
                countScore();
            }
        }
    }

    private void getSlantScore() {
        for (int down=4;down<pointSize;down++){
            for (int i=0;i<=down-4;i++){
                getScoreSlideToRightBottom(down, i);//slide from leftTop to rightBottom
                getScoreSlideToLeftBottom(down, i);//slide from rightTop to leftBottom
            }
        }
    }

    private void intelligenceGetScore(){
        getXYScore();
        getSlantScore();
    }

    private void intelligence(){
        whiteBigger =0;
        blackBigger =0;
        for (int i=0;i<pointSize;i++){
            for (int j=0;j<pointSize;j++){
                if (allPointArray[i][j]==0){//Y
                    allPointArray[i][j]=1;
                    whiteResultScore=0;
                    blackResultScore=0;
                    intelligenceGetScore();
                    if (whiteBigger <whiteResultScore){
                        whiteBigger =whiteResultScore;
                        defensePoint[0]=i;
                        defensePoint[1]=j;
                        //Log.i(TAG, "whiteScore: " + whiteResultScore + " blackScore: " + blackResultScore);
                    }
                    allPointArray[i][j]=0;
                    clearChessCount();
                }
            }
        }

        for (int i=0;i<pointSize;i++){
            for (int j=0;j<pointSize;j++){
                if (allPointArray[i][j]==0){//Y
                    allPointArray[i][j]=2;
                    whiteResultScore=0;
                    blackResultScore=0;
                    intelligenceGetScore();
                    if (blackBigger<blackResultScore){
                        blackBigger=blackResultScore;
                        attackPoint[0]=i;
                        attackPoint[1]=j;
                    }
                    if (gameModel==2){
                        //3   4   5
                        if ((blackBigger>45000&&whiteBigger<800000)||(blackBigger>1600000&&whiteBigger<8000000)||blackBigger>8000000){
                            attack=true;
                            //Log.i(TAG, "true : ");
                        }else {
                            //Log.i(TAG, "attack  : ");
                            attack=false;
                        }
                    }else if(gameModel==1){
                        if ((blackBigger>800000&&whiteBigger<800000)||blackBigger>8000000){
                            attack=true;
                        }else {
                            attack=false;
                        }
                    }
                    allPointArray[i][j]=0;
                    clearChessCount();
                }
            }
        }
        if (whiteBigger==0&&blackBigger==0){
            if (mOnChessBoardListener!=null){
                mOnChessBoardListener.equal();
            }
            victor=true;
        }
        Log.i(TAG, "white: "+whiteBigger+"  black: "+blackBigger);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        positionX=event.getX();
        positionY=event.getY();

        switch (event.getAction()){
            case 0://ACTION_DOWN
                break;
            case 1://ACTION_UP
                clearChessCount();
                attack=false;//defence
                if (!victor){
                        getPosition();
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
                            }else { //person with machine
                                if (colorWhite){
                                    allPointArray[point[0]][point[1]]=1;
                                    colorWhite=!colorWhite;
                                    whiteNumber++;
                                    if (mOnChessBoardListener!=null){
                                        mOnChessBoardListener.number(whiteNumber,blackNumber);
                                    }
                                    invalidate();
                                    findVictor();
                                    if (!victor){
                                        intelligence();
                                        if (attack){
                                            allPointArray[attackPoint[0]][attackPoint[1]]=2;
                                        }else {
                                            allPointArray[defensePoint[0]][defensePoint[1]]=2;
                                        }
                                        colorWhite=!colorWhite;
                                        blackNumber++;
                                        if (mOnChessBoardListener!=null){
                                            mOnChessBoardListener.number(whiteNumber,blackNumber);
                                        }
                                        invalidate();
                                        findVictor();
                                    }
                                }

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
        void equal();
        void number(int white,int black);
    }

    public void setOnChessBoardListener(OnChessBoardListener onChessBoardListener){
        this.mOnChessBoardListener=onChessBoardListener;
    }
}
