package br.com.wasys.library.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import br.com.wasys.library.text.MoneyTextWatcher;

/**
 * Created by pascke on 22/05/16.
 */
public class MoneyEditText extends EditText {

    public MoneyEditText(Context context) {
        super(context);
        construct(context);
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        construct(context);
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        construct(context);
    }

    private void construct(Context context) {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        new MoneyTextWatcher(this);
    }
}
