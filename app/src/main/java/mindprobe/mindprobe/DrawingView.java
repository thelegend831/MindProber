package mindprobe.mindprobe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DrawingView extends View {

    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    LinearLayout gradient;
    Context context;
    TextView score;
    private Paint circlePaint;
    private Path circlePath;
    public static int global_score;

    public DrawingView(Context c, LinearLayout gradient1, TextView score1) {
        super(c);
        context=c;
        score = score1;
        mPath = new Path();
        gradient = gradient1;
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL));
        //circlePaint.setStyle(Paint.Style.STROKE);
        //circlePaint.setStrokeJoin(Paint.Join.MITER);
        //circlePaint.setStrokeWidth(2f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);

        //canvas.drawPath( mPath,  mPaint);

        canvas.drawPath( circlePath,  circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        if (y < gradient.getHeight() && y > 0){
            score.setText(Integer.toString((int)(100*((gradient.getHeight()-y)/gradient.getHeight()))));
            global_score = (int)(100*((gradient.getHeight()-y)/gradient.getHeight()));
        }
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;

            if (y < gradient.getHeight() && y > 0){
                score.setText(Integer.toString((int)(100*((gradient.getHeight()-y)/gradient.getHeight()))));
                global_score = (int)(100*((gradient.getHeight()-y)/gradient.getHeight()));

                circlePath.reset();
                //circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
                circlePath.addRect((float)(0), (float)(mY-(0.04*gradient.getHeight())), (float)(gradient.getWidth()), (float)(mY+(0.04*gradient.getHeight())), Path.Direction.CW);
            }

				/*circlePath.reset();
				//circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
				circlePath.addRect((float)(0), (float)(mY-(0.04*gradient.getHeight())), (float)(gradient.getWidth()), (float)(mY+(0.04*gradient.getHeight())), Path.Direction.CW);*/
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        //circlePath.reset();
        // commit the path to our offscreen
        //mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }
}