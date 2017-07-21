package br.com.wasys.gn.motorista.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;

/**
 * Created by pascke on 02/09/16.
 */
public class ConfirmDialog extends Dialog implements OnClickListener {

    private int mNo;
    private int mYes;
    private int mTitle;
    private int mMessage;

    private Button mNoButton;
    private Button mYesButton;
    private TextView mTitleTextView;
    private TextView mMessageTextView;

    private OnConfirmListener mListener;

    private ConfirmDialog(Context context, Builder builder) {
        super(context);
        mNo = builder.no;
        mYes = builder.yes;
        mTitle = builder.title;
        mMessage = builder.message;
        mListener = builder.listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);

        Context context = getContext();
        mNoButton = (Button) findViewById(R.id.no_button);
        mYesButton = (Button) findViewById(R.id.yes_button);
        mTitleTextView = (TextView) findViewById(R.id.title_text_view);
        mMessageTextView = (TextView) findViewById(R.id.message_text_view);
        mNoButton.setOnClickListener(this);
        if (mNo > 0) {
            mNoButton.setText(context.getString(mNo));
        }
        mYesButton.setOnClickListener(this);
        if (mYes > 0) {
            mYesButton.setText(context.getString(mYes));
        }
        if (mTitle > 0) {
            mTitleTextView.setText(context.getString(mTitle));
        }
        if (mMessage > 0) {
            mMessageTextView.setText(context.getString(mMessage));
        }
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            int id = view.getId();
            boolean answer = false;
            switch (id) {
                case R.id.yes_button: {
                    answer = true;
                    break;
                }
            }
            mListener.onConfirm(answer);
        }
        dismiss();
    }

    public static class Builder {

        private int no;
        private int yes;
        private int title;
        private int message;
        private OnConfirmListener listener;

        public Builder no(@StringRes int no) {
            this.no = no;
            return this;
        }

        public Builder yes(@StringRes int yes) {
            this.yes = yes;
            return this;
        }

        public Builder title(@StringRes int title) {
            this.title = title;
            return this;
        }

        public Builder message(@StringRes int message) {
            this.message = message;
            return this;
        }

        public Builder listener(OnConfirmListener listener) {
            this.listener = listener;
            return this;
        }

        public ConfirmDialog build(Context context) {
           return new ConfirmDialog(context, this);
        }

        public ConfirmDialog show(Context context) {
            ConfirmDialog dialog = build(context);
            dialog.show();
            return dialog;
        }
    }

    public interface OnConfirmListener {
        void onConfirm(boolean answer);
    }
}
