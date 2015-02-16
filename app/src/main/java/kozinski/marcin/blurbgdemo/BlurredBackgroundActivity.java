package kozinski.marcin.blurbgdemo;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class BlurredBackgroundActivity extends Activity implements Target {

    /*
     * Images from Marie Schweiz's Lorem Ipsum Illustration
     * https://github.com/MarieSchweiz/lorem-ipsum-illustration
     */
    private static final String[] BACKGROUND_IMAGE_URLS = {
            "https://raw.githubusercontent.com/MarieSchweiz/lorem-ipsum-illustration/master/png/food_drink_lemon_orange_360.png",
            "https://raw.githubusercontent.com/MarieSchweiz/lorem-ipsum-illustration/master/png/landscape_mountain_forest_360.png",
            "https://raw.githubusercontent.com/MarieSchweiz/lorem-ipsum-illustration/master/png/monster_cyclop_eye_360.png",
            "https://raw.githubusercontent.com/MarieSchweiz/lorem-ipsum-illustration/master/png/monster_suitcase_spider_360.png",
            "https://raw.githubusercontent.com/MarieSchweiz/lorem-ipsum-illustration/master/png/space_monster_360.png"};
    private static final float BLUR_RADIUS = 25F;
    private final Handler handler = new Handler();

    private BlurTransformation blurTransformation;
    private int backgroundIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blurTransformation = new BlurTransformation(this, BLUR_RADIUS);
        updateWindowBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {
        getWindow().setBackgroundDrawable(drawable);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
        changeBackground(new BitmapDrawable(getResources(), bitmap));
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        // just prepare mentally
    }

    private void updateWindowBackground() {
        String url = getUrlToTheImage();
        Picasso.with(this).load(url).transform(blurTransformation)
                .error(R.drawable.background_default).into(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateWindowBackground();
            }
        }, TimeUnit.SECONDS.toMillis(10));
    }

    private String getUrlToTheImage() {
        final String imageUrl = BACKGROUND_IMAGE_URLS[backgroundIndex];
        backgroundIndex = (backgroundIndex + 1) % BACKGROUND_IMAGE_URLS.length;
        return imageUrl;
    }

    private void changeBackground(Drawable drawable) {
        View decorView = getWindow().getDecorView();
        Drawable oldBackgroundDrawable = decorView.getBackground();
        TransitionDrawable transitionDrawable = new TransitionDrawable(
                new Drawable[]{oldBackgroundDrawable, drawable});
        setBackgroundCompat(decorView, transitionDrawable);
        transitionDrawable.startTransition(1000);
    }

    private static void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        }
    }
}
