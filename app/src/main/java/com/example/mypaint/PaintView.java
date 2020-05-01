package com.example.mypaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {

    public static int BRUSH_SIZE = 10;
    public static final int DEFAULT_COLOR = Color.BLUE;
    public static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE  = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    public int currColor;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private float strokeWidth;
    private boolean emboss;
    private boolean blur;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    public Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAlpha(0xff);
        mEmboss = new EmbossMaskFilter(new float[] {1,1,1},0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);

    }

    public void init(DisplayMetrics displayMetrics)
    {
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        currColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;

    }

    void setStrokeWidth(float sw)
    {
        strokeWidth = sw;
        mPaint.setStrokeWidth(sw);
    }

    void setCurrColorColor(int color){
        currColor = color;
        mPaint.setColor(color);
    }

    void setbackgroundColor(int color){
        backgroundColor = color;
        mCanvas.drawColor(backgroundColor);
    }

    int getBackgroundColor(){
        return backgroundColor;
    }

    public void normal(){
        emboss = false;
        blur = false;
    }

    public void emboss(){
        emboss = true;
        blur = false;
    }

    public void blur(){
        emboss = false;
        blur = true;
    }

    public void clear()
    {
        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        paths.clear();
        normal();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        mCanvas.drawColor(backgroundColor);
        for(FingerPath fp : paths)
        {
            mPaint.setColor(fp.Color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
            if(fp.emboss)
                mPaint.setMaskFilter(mEmboss);
            else if(fp.blur)
                mPaint.setMaskFilter(mBlur);
            mCanvas.drawPath(fp.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float X, float Y)
    {
        mPath = new Path();
        FingerPath fp = new FingerPath(currColor, emboss, blur, strokeWidth, mPath);
        paths.add(fp);
        mPath.reset();
        mPath.moveTo(X,Y);
        mX = X;
        mY = Y;
    }

    private void touchMove(float X, float Y){
        float dx = Math.abs(X - mX);
        float dy = Math.abs(Y - mY);
        if( dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            mPath.quadTo(mX, mY, (X + mX)/2, (Y + mY)/2);
            mX = X;
            mY = Y;
        }
    }

    public Bitmap getBitmap()
    {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void touchUp()
    {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float X = event.getX();
        float Y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touchStart(X, Y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(X, Y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;

        }

        return true;
    }

}
