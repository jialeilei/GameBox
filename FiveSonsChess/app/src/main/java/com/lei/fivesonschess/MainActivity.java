package com.lei.fivesonschess;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{
    ChessBoard chessBoard;
    TextView tvWhiteResult,tvBlackResult,tvWhite,tvBlack;
    Button btnAgain;
    ImageView imgSetting;
    Spinner spinnerSize;
    private static String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        btnAgain=(Button)findViewById(R.id.btnAgain);
        btnAgain.setOnClickListener(this);
        imgSetting=(ImageView)findViewById(R.id.imgSetting);
        imgSetting.setOnClickListener(this);
        tvWhiteResult=(TextView)findViewById(R.id.tvWhiteResult);
        tvBlackResult=(TextView)findViewById(R.id.tvBlackResult);
        tvWhite=(TextView)findViewById(R.id.tvWhite);
        tvBlack=(TextView)findViewById(R.id.tvBlack);



        chessBoard=(ChessBoard)findViewById(R.id.keyBoard);
        chessBoard.setEnabled(false);
        chessBoard.setFocusable(false);
        chessBoard.setOnChessBoardListener(new ChessBoard.OnChessBoardListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAgain:
                chessBoard.resetBoard();
                tvWhite.setText(0 + "手");
                tvBlack.setText(0 + "手");
                tvBlackResult.setText("");
                tvWhiteResult.setText("");


                break;
            case R.id.imgSetting:
                showDialog();
                break;
            default:
                break;
        }
    }
}
