package kozinski.marcin.blurbgdemo;

import com.squareup.picasso.Transformation;

import android.content.Context;
import android.graphics.Bitmap;
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
        Bitmap blurredBitmap = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // Initialize RenderScript and the script to be used
        RenderScript renderScript = RenderScript.create(context);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur
                .create(renderScript, Element.U8_4(renderScript));
        // Allocate memory for Renderscript to work with
        Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation output = Allocation.createFromBitmap(renderScript, blurredBitmap);

        script.setInput(input);
        script.setRadius(radius);
        script.forEach(output);
        output.copyTo(blurredBitmap);

        renderScript.destroy();
        bitmap.recycle();
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blur(" + String.valueOf(radius) + ")";
    }
}
