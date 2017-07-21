package br.com.wasys.gn.motorista.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.wasys.gn.motorista.R;

import org.apache.commons.lang3.StringUtils;

import br.com.wasys.library.utils.FieldUtils;

/**
 * Created by pascke on 01/09/16.
 */
public class CpfDialog extends Dialog implements View.OnClickListener {

    private Button mButton;
    private EditText mCpf1EditText;
    private EditText mCpf2EditText;
    private EditText mCpf3EditText;

    private OnConfirmListener mConfirmListener;

    public CpfDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cpf);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setTitle("???");

        mCpf1EditText = (EditText) findViewById(R.id.edt_cpf_1);
        mCpf2EditText = (EditText) findViewById(R.id.edt_cpf_2);
        mCpf3EditText = (EditText) findViewById(R.id.edt_cpf_3);

        mCpf1EditText.addTextChangedListener(new TextWatcher(R.id.edt_cpf_1));
        mCpf2EditText.addTextChangedListener(new TextWatcher(R.id.edt_cpf_2));
        mCpf3EditText.addTextChangedListener(new TextWatcher(R.id.edt_cpf_3));

        mButton = (Button) findViewById(R.id.btn_ok);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String value;
        StringBuilder cpf = new StringBuilder();
        // 1
        value = FieldUtils.getValue(mCpf1EditText);
        if (StringUtils.isNotBlank(value)) {
            cpf.append(value);
        }
        // 2
        value = FieldUtils.getValue(mCpf2EditText);
        if (StringUtils.isNotBlank(value)) {
            cpf.append(value);
        }
        // 3
        value = FieldUtils.getValue(mCpf3EditText);
        if (StringUtils.isNotBlank(value)) {
            cpf.append(value);
        }
        if (cpf.length() < 3) {
            Toast.makeText(getContext(), "Preencha o CPF", Toast.LENGTH_SHORT).show();
        }
        else {
            dismiss();
            if (mConfirmListener != null) {
                mConfirmListener.onCpfConfirm(String.valueOf(cpf));
            }
        }
    }

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.mConfirmListener = confirmListener;
    }

    private class TextWatcher implements android.text.TextWatcher {
        private int idRes;
        public TextWatcher(@IdRes int resId) {
            this.idRes = resId;
        }
        @Override
        public void afterTextChanged(Editable editable) {
            if (StringUtils.isNotBlank(editable)) {
                switch (idRes) {
                    case R.id.edt_cpf_1: {
                        mCpf2EditText.requestFocus();
                        break;
                    }
                    case R.id.edt_cpf_2: {
                        mCpf3EditText.requestFocus();
                        break;
                    }
                    case R.id.edt_cpf_3: {
                        break;
                    }
                }
            }
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Nothing
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Nothing
        }
    }

    public interface OnConfirmListener {
        void onCpfConfirm(String cpf);
    }
}
