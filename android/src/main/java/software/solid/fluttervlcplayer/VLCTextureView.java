package software.solid.fluttervlcplayer;



import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.view.View;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.flutter.view.TextureRegistry;

public class VLCTextureView extends TextureView implements TextureView.SurfaceTextureListener, View.OnLayoutChangeListener {

     private TextureRegistry.SurfaceTextureEntry mTextureEntry = null;
    protected Context mContext;
    private SurfaceTexture mSurfaceTexture = null;
    private boolean wasPlaying = false;

    private Handler mHandler;
    private Runnable mLayoutChangeRunnable = null;

    public VLCTextureView(final Context context) {
        super(context);
        mContext = context;
        initVideoView();
    }

    public VLCTextureView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initVideoView();
    }

    public VLCTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initVideoView();
    }

    public void dispose() {
        setSurfaceTextureListener(null);
        removeOnLayoutChangeListener(this);

        if (mLayoutChangeRunnable != null) {
            mHandler.removeCallbacks(mLayoutChangeRunnable);
            mLayoutChangeRunnable = null;
        }

        if (mSurfaceTexture != null) {
            if (!mSurfaceTexture.isReleased()) {
                mSurfaceTexture.release();
            }
            mSurfaceTexture = null;
        }
        mTextureEntry = null;
         mContext = null;
    }

    private void initVideoView() {
        mHandler = new Handler(Looper.getMainLooper());

        setFocusable(false);
        setSurfaceTextureListener(this);
        addOnLayoutChangeListener(this);
    }


    public void setTextureEntry(TextureRegistry.SurfaceTextureEntry textureEntry) {
        this.mTextureEntry = textureEntry;
        this.updateSurfaceTexture();
    }

    private void updateSurfaceTexture() {
        if (this.mTextureEntry != null) {
            final SurfaceTexture texture = this.mTextureEntry.surfaceTexture();
            if (!texture.isReleased() && (getSurfaceTexture() != texture)) {
                setSurfaceTexture(texture);
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mSurfaceTexture == null || mSurfaceTexture.isReleased()) {
            mSurfaceTexture = surface;


            wasPlaying = false;

        } else {
            if (getSurfaceTexture() != mSurfaceTexture) {
                setSurfaceTexture(mSurfaceTexture);
            }
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        setSize(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {


        if (mSurfaceTexture != surface) {
            if (mSurfaceTexture != null) {
                if (!mSurfaceTexture.isReleased()) {
                    mSurfaceTexture.release();
                }
            }
            mSurfaceTexture = surface;
        }

        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }



    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
            updateLayoutSize(view);
        }
    }

    public void updateLayoutSize(View view) {

    }

    private void setSize(int width, int height) {
        int mVideoWidth = 0;
        int mVideoHeight = 0;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1) return;

        // Screen size
        int w = this.getWidth();
        int h = this.getHeight();

        // Size
        if (w > h && w < h) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR) {
            h = (int) (w / videoAR);
        } else {
            w = (int) (h * videoAR);
        }

        // Layout fit
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = h;
        this.setLayoutParams(lp);
        this.invalidate();
    }

}