package kozinski.marcin.blurbgdemo;

import com.squareup.picasso.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * @author Marcin Koziński
 */
public class BlurTransformation implements Transformation {

    private Context context;
    private float radius;

    public BlurTransformation(Context context, float radius) {
        this.context = context;
        this.radius = radius;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        bitmap = cropBitmapWidthToMultipleOfFour(bitmap);
        Bitmap argbBitmap = convertBitmap(bitmap, Config.ARGB_8888);

        Bitmap blurredBitmap = Bitmap.createBitmap(argbBitmap.getWidth(), argbBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Initialize RenderScript and the script to be used
        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur
                .create(renderScript, Element.U8_4(renderScript));
        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(renderScript, argbBitmap);
        Allocation output = Allocation.createFromBitmap(renderScript, blurredBitmap);

        script.setInput(input);
        script.setRadius(radius);
        script.forEach(output);
        output.copyTo(blurredBitmap);

        renderScript.destroy();
        argbBitmap.recycle();
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blur(" + String.valueOf(radius) + ")";
    }

    private static Bitmap cropBitmapWidthToMultipleOfFour(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        if (bitmapWidth % 4 != 0) {
            // This is the place to actually crop the bitmap,
            // but I don't have the necessary method in this demo project

            // bitmap = BitmapUtils.resize(bitmap, bitmapWidth - (bitmapWidth % 4), bitmap.getHeight(),
            //         ScaleType.CENTER);
        }
        return bitmap;
    }

    private static Bitmap convertBitmap(Bitmap bitmap, Config config) {
        if (bitmap.getConfig() == config) {
            return bitmap;
        } else {
            Bitmap argbBitmap;
            argbBitmap = bitmap.copy(config, false);
            bitmap.recycle();
            if (argbBitmap == null) {
                throw new UnsupportedOperationException(
                        "Couldn't convert bitmap from config " + bitmap.getConfig() + " to "
                                + config);
            }
            return argbBitmap;
        }
    }
}
