package kozinski.marcin.blurbgdemo;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class BlurredBackgroundActivity extends Activity implements Target {

    private static final String BACKGROUND_IMAGE_URL
            = "https://raw.githubusercontent.com/MarieSchweiz/lorem-ipsum-illustration/master/png/food_drink_lemon_orange_360.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWindowBackground();
    }

    @Override
    public void onBitmapFailed(Drawable drawable) {
        getWindow().setBackgroundDrawable(drawable);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
        getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        // just prepare mentally
    }

    private void updateWindowBackground() {
        String url = getUrlToTheImage();
        Picasso.with(this).load(url).error(R.drawable.background_default).into(this);
    }

    private String getUrlToTheImage() {
        return BACKGROUND_IMAGE_URL;
    }

}
