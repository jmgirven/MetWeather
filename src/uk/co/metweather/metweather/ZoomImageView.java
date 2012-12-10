package uk.co.metweather.metweather;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {
	// An ImageView with zoom capability
	
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	String savedItemClicked;
	
	
    
	// Constructors
    public ZoomImageView(Context context) {
        this(context, null, 0);
    }
    
    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    
    
    // Handle zooming
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            start.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            oldDist = spacing(event);
            if (oldDist > 30f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                // ...
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - start.x, event.getY()
                        - start.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 30f) {
                    matrix.set(savedMatrix);
                    float scale = newDist / oldDist;
                    
                    Matrix temp = new Matrix();
                    
                    // Save matrix to a temp location
                    temp.set(matrix);
                    temp.postScale(scale, scale, mid.x, mid.y);
                    
                    
                    // Check if proposed zoom http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=979changes limits beyond desired range
                    float[] f = new float[9];
                    temp.getValues(f);
                    float mScaleX = f[Matrix.MSCALE_X];
                    // float mScaleY = f[Matrix.MSCALE_Y];
                    
                    // Limit to zooming in
                    if (mScaleX < 3 && mScaleX > 0.8) {
                    	matrix.postScale(scale, scale, mid.x, mid.y);
                    	savedMatrix.set(matrix);
                    } else {
                    	matrix.set(savedMatrix);
                    }
                }
            }
            break;
        }

        this.setImageMatrix(matrix);
        return true;
    }

    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid ").append(
                    action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int iii = 0; iii < event.getPointerCount(); iii++) {
            sb.append("#").append(iii);
            sb.append("(pid ").append(event.getPointerId(iii));
            sb.append(")=").append((int) event.getX(iii));
            sb.append(",").append((int) event.getY(iii));
            if (iii + 1 < event.getPointerCount())
                sb.append(";");
        }
        sb.append("]");
    }

    /** Determine the space between the first two fingers */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
