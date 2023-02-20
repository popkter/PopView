package com.popkter.roundedimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.popkter.colorpickerview.R;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * 引用自 https://blog.csdn.net/explorerqp/article/details/95336023
 */
public class RoundedImageView extends AppCompatImageView {

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;


    private final int mBorderColor;
    private final int mBorderWidth;

    private final Paint paintImage;
    private final Paint paintBorder;
    /**
     * 圆角的幅度
     **/
    private final float mRadius;
    /**
     * 是否是圆形
     **/
    private final boolean mIsCircle;

    public RoundedImageView(final Context context) {
        this(context, null);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setScaleType(ScaleType.FIT_XY);
        // 获取自定属性配置
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.rounded_image_view, defStyle, 0);
        mRadius = ta.getDimensionPixelSize(R.styleable.rounded_image_view_radius, 0);
        mIsCircle = ta.getBoolean(R.styleable.rounded_image_view_circle, false);
        mBorderColor = ta.getColor(R.styleable.rounded_image_view_border_color, DEFAULT_BORDER_COLOR);
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.rounded_image_view_border_width, DEFAULT_BORDER_WIDTH);
        ta.recycle();
        paintImage = new Paint();
        paintImage.setAntiAlias(true);
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
    }


    @Override
    public void onDraw(Canvas canvas) {
        // 去点padding值
        int viewWidth = canvas.getWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = canvas.getHeight() - getPaddingTop() - getPaddingBottom();
        // 获取iamgeView 中的drawable 并转成bitmap
        Bitmap image = drawableToBitmap(getDrawable());
        // 对获取到的bitmap 按照当前imageView的宽高进行缩放
        Bitmap reSizeImage = reSizeImage(image, viewWidth, viewHeight);
        int imgWidth = reSizeImage.getWidth();
        int imgHight = reSizeImage.getHeight();

        paintBorder.setColor(mBorderColor);
        paintBorder.setStrokeWidth(mBorderWidth);
        paintBorder.setStyle(Paint.Style.FILL_AND_STROKE);

        // 判断当前需要绘制圆角还是圆
        if (mIsCircle) {
            //画边线
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, (Math.min(viewWidth, viewHeight) / 2) - mBorderWidth / 2, paintBorder);
            //设置画笔
            BitmapShader bitmapShader = new BitmapShader(reSizeImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paintImage.setShader(bitmapShader);
            //画布移动(x,y),移动量为边框宽度
            canvas.translate(mBorderWidth, mBorderWidth);
            canvas.drawCircle(imgWidth / 2, imgHight / 2, Math.min(imgWidth, imgHight) / 2, paintImage);

        } else {
            //画边线
            RectF rectB = new RectF(mBorderWidth / 2, mBorderWidth / 2, viewWidth - mBorderWidth / 2, viewHeight - mBorderWidth / 2);
            canvas.drawRoundRect(rectB, mRadius, mRadius, paintBorder);

            BitmapShader bitmapShader = new BitmapShader(reSizeImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paintImage.setShader(bitmapShader);

            RectF rect = new RectF(0, 0, imgWidth, imgHight);
            //弧度半径(有边框无圆角)
            float radius = mBorderWidth == 0 ? mRadius : 0;
            //画布移动(x,y),移动量为边框宽度
            canvas.translate(mBorderWidth, mBorderWidth);
            canvas.drawRoundRect(rect, radius, radius, paintImage);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicHeight(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 重设Bitmap的宽高
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap reSizeImage(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //取得正方形边长
        int sideL = Math.min(width, height);
        //取得原点轴坐标
        int x = 0;
        int y = 0;
        if (width > height) {
            x = (width - height) / 2;
        } else {
            y = (height - width) / 2;
        }

        Bitmap bitmapSquare = Bitmap.createBitmap(bitmap, x, y, sideL, sideL);

        width = bitmapSquare.getWidth();
        height = bitmapSquare.getHeight();

        // 计算出缩放比
        float scaleHeight = (float) (newHeight - (mBorderWidth * 2)) / height;
        float scaleWidth = (float) (newWidth - (mBorderWidth * 2)) / width;
        // 矩阵缩放bitmap
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmapSquare, 0, 0, width, height, matrix, true);
    }
}