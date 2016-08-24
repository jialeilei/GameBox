package com.lei.fivesonschess;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lei.fivesonschess.game.FiveSonsGame;

public class MainActivity extends Activity implements View.OnClickListener{
    //five
    private static String TAG="MainActivity";
    FiveSonsGame chessBoard;
    TextView tvWhiteResult,tvBlackResult,tvWhite,tvBlack,tvTitle;
    Button btnAgain,btnModelPerson,btnModelEasy,btnModelCommon;
    ImageView imgSetting;
    Spinner spinnerSize;
    private int gameModel =0; // *person with person:0  *person with machine:1
    //2048
    Button btnJump;

    //others
    ImageView imgSlide;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        btnJump = (Button) findViewById(R.id.btnJump);
        btnJump.setOnClickListener(this);
        imgSlide = (ImageView) findViewById(R.id.imgLeft);
        imgSlide.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        btnAgain=(Button)findViewById(R.id.btnAgain);
        btnAgain.setOnClickListener(this);
        imgSetting=(ImageView)findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(this);
        btnModelPerson=(Button)findViewById(R.id.btnModelPerson);
        btnModelPerson.setOnClickListener(this);
        btnModelEasy=(Button)findViewById(R.id.btnModelEasy);
        btnModelEasy.setOnClickListener(this);
        btnModelCommon=(Button)findViewById(R.id.btnModelCommon);
        btnModelCommon.setOnClickListener(this);
        tvWhiteResult=(TextView)findViewById(R.id.tvWhiteResult);
        tvBlackResult=(TextView)findViewById(R.id.tvBlackResult);
        tvWhite=(TextView)findViewById(R.id.tvWhite);
        tvBlack=(TextView)findViewById(R.id.tvBlack);
        tvTitle=(TextView)findViewById(R.id.tvTitle);



        chessBoard=(FiveSonsGame)findViewById(R.id.keyBoard);
        chessBoard.setEnabled(false);
        chessBoard.setFocusable(false);
        chessBoard.setOnChessBoardListener(new FiveSonsGame.OnChessBoardListener() {
            @Override
            public void whiteVictor() {
                Toast.makeText(MainActivity.this, "白方胜利", Toast.LENGTH_LONG).show();
                tvWhiteResult.setTextColor(Color.GREEN);
                tvWhiteResult.setText("胜利!");
                tvBlackResult.setTextColor(Color.RED);
                tvBlackResult.setText("失败");
            }

            @Override
            public void blackVictor() {
                Toast.makeText(MainActivity.this, "黑方胜利", Toast.LENGTH_LONG).show();
                tvWhiteResult.setTextColor(Color.RED);
                tvWhiteResult.setText("失败");
                tvBlackResult.setTextColor(Color.GREEN);
                tvBlackResult.setText("胜利!");
            }

            @Override
            public void equal() {
                Toast.makeText(MainActivity.this, "棋盘已满，平局", Toast.LENGTH_LONG).show();
                tvWhiteResult.setTextColor(Color.RED);
                tvWhiteResult.setText("平局");
                tvBlackResult.setTextColor(Color.GREEN);
                tvBlackResult.setText("平局");
            }

            @Override
            public void number(int white, int black) {
                tvWhite.setText( white + "手");
                tvBlack.setText( black + "手");
            }
        });
    }

    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_common, null);
        final Dialog dialog = new Dialog(MainActivity.this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        //点击事件的注册
        spinnerSize=(Spinner)window.findViewById(R.id.spinnerSize);
        Button btnConfirm = (Button) window.findViewById(R.id.btnConfirm);
        //btnConfirm.setText("添加");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chessBoard.setBoardSize(spinnerSize.getSelectedItemPosition() + 10);
                dialog.dismiss();
            }
        });
        Button btnCancel = (Button) window.findViewById(R.id.btnCancle);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void reset(){
        chessBoard.resetBoard();
        tvWhite.setText(0 + "手");
        tvBlack.setText(0 + "手");
        tvBlackResult.setText("");
        tvWhiteResult.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAgain:
                reset();
                break;
            case R.id.imgSetting:
                showDialog();
                break;
            case R.id.btnModelPerson:

                gameModel=0;
                tvTitle.setText("人人对战");
                chessBoard.setGameModel(gameModel);
                reset();
                break;
            case R.id.btnModelEasy:

                gameModel=1;
                tvTitle.setText("简单模式");
                chessBoard.setGameModel(gameModel);
                reset();
                break;
            case R.id.btnModelCommon:

                gameModel=2;
                tvTitle.setText("一般模式");
                chessBoard.setGameModel(gameModel);
                reset();
                break;
            case R.id.btnJump:
                Intent intent = new Intent(MainActivity.this,NumberGameActivity.class);
                startActivity(intent);
                break;
            case R.id.imgLeft:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
    }
}
