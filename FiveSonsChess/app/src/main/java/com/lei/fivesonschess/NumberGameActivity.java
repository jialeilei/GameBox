package com.lei.fivesonschess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class NumberGameActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvScore;
    private int score = 0;
    public static NumberGameActivity numberGameActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);
        initView();
    }

    private void initView(){
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvScore.setOnClickListener(this);

    }

    public static NumberGameActivity getNumberGameActivity(){
        return numberGameActivity;
    }

    public NumberGameActivity (){
        numberGameActivity = this;
    }

    public void addScore(int s){
        score += s;
        showScore();
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvScore:
                break;
            default:
                break;
        }
    }

}
