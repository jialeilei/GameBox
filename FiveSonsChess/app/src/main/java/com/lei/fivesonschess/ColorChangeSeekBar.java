package com.lei.fivesonschess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * create  by lei
 */
public class ColorChangeSeekBar extends View {

	private final String TAG="ColorChangeSeekBar";
	private final Paint paint = new Paint();
	private final Path sPath = new Path();
	private float sLeft, sTop, sRight, sBottom;
	private float sWidth,sHeight;
	private LinearGradient linearGradient;
	private float x;
	private float mRadius;
	private float progress;
	private OnStateChangeListener onStateChangeListener;
	private int startColor=0xff222222;
	private int endColor=0xffeeeeee;//灰色
	private int thumbColor=0xff222222;
	private int thumbBorderColor=Color.WHITE;
	private int colorArray[]={startColor,endColor};


	public ColorChangeSeekBar(Context context) {
		this(context, null);
	}

	public ColorChangeSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayerType(LAYER_TYPE_SOFTWARE, null);
	}

	public void setColor(int startColor,int endColor,int thumbColor,int thumbBorderColor){
		this.startColor=startColor;
		this.endColor=endColor;
		this.thumbColor=thumbColor;
		this.thumbBorderColor=thumbBorderColor;
		colorArray[0]=startColor;
		colorArray[1]=endColor;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec*2);
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mRadius=h;
		x=h/2;//圆的横坐标
		sLeft = 0; // 田径场左的坐标
		sTop = h*0.25f;//top位置
		sRight = w; // 田径场 右占自身的全部
		sBottom = h*0.75f; // 田径场底部
		sWidth = sRight - sLeft; // 田径场的宽度
		sHeight = sBottom - sTop; // 田径场的高度
		RectF sRectF = new RectF(sLeft, sTop, sBottom, sBottom);
		sPath.arcTo(sRectF, 90, 180);
		sRectF.left = sRight - sBottom;
		sRectF.right = sRight;
		sPath.arcTo(sRectF, 270, 180);
		sPath.close();    // path准备田径场的路径
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBackground(canvas);
		drawCircle(canvas);
		paint.reset();
	}

	public boolean onTouchEvent(MotionEvent event) {
		this.x = event.getX();
		progress =(x-mRadius)/(sWidth-mRadius*2)*100;
		switch(event.getAction()) {
			case 0://ACTION_DOWN
				Log.i(TAG, "onTouchEvent: x: "+x +" max : "+event.getSize()+" "+" "+sWidth);
				invalidate();
				break;
			case 1://ACTION_UP
				if (onStateChangeListener!=null){
					onStateChangeListener.onStopTrackingTouch(progress);
				}
				break;
			case 2://ACTION_MOVE
				if (onStateChangeListener!=null){
					onStateChangeListener.OnStateChangeListener(progress);
				}
				invalidate();
				break;
		}
		return true;
	}

	private void drawBackground(Canvas canvas){
		linearGradient=new LinearGradient(sLeft,sTop,sWidth,sHeight,colorArray,null, Shader.TileMode.REPEAT);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		//设置渲染器
		paint.setShader(linearGradient);
		canvas.drawPath(sPath, paint);
	}

	private void drawCircle(Canvas canvas){
		Paint thumbPaint = new Paint();
		x =x<(mRadius/2)?(mRadius/2):x;//判断thumb边界
		x=x>sWidth-mRadius/2?sWidth-mRadius/2:x;
		thumbPaint.setStyle(Style.FILL);
		thumbPaint.setColor(thumbColor);
		canvas.drawCircle(x, mRadius / 2, mRadius / 2, thumbPaint);
		thumbPaint.setStyle(Style.STROKE);
		thumbPaint.setColor(thumbBorderColor);
		thumbPaint.setStrokeWidth(2);
		canvas.drawCircle(x, mRadius / 2, mRadius / 2, thumbPaint);
	}

	public interface OnStateChangeListener{
		void OnStateChangeListener(float progress);
		void onStopTrackingTouch(float progress);
	}

	public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener1){
		this.onStateChangeListener=onStateChangeListener1;
	}

}
