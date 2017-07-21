package br.com.wasys.gn.motorista.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import br.com.wasys.gn.motorista.R;


/**
  *
 * Can create a TextView with a customized typeface.
 * Must put all .ttf files in res/assets/fonts before use.
 *
 * Steps:
 * 1 - Use it:
 *      <br.com.gigaservices.widgets.CustomTextView
 *          android:layout_width="wrap_content"
 *          android:layout_height="match_parent"
 *          android:textSize="24sp"
 *          android:text="Hello World"
 *          app:custom_typeface="Your-font.ttf" />
 */

public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTypeface );
        String typeface = attributes.getString(R.styleable.CustomTypeface_custom_typeface);
        if (typeface != null && !typeface.isEmpty()) {
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/" + typeface);
            setTypeface(font);
        }
        attributes.recycle();
    }
}
