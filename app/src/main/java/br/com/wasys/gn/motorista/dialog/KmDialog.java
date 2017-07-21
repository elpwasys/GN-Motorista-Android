package br.com.wasys.gn.motorista.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.wasys.gn.motorista.R;

import br.com.wasys.library.utils.FieldUtils;

/**
 * Created by pascke on 01/09/16.
 */
public class KmDialog extends Dialog implements View.OnClickListener {

    private Button mButton;
    private EditText mEditText;

    private OnConfirmListener mConfirmListener;

    public KmDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_km_concluir_servico);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setTitle("???");

        mButton = (Button) findViewById(R.id.btn_ok);
        mEditText = (EditText) findViewById(R.id.edt_km);

        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Double value = FieldUtils.getValue(Double.class, mEditText);
        if (value == null) {
            Toast.makeText(getContext(), "Preencha a kilometragem atual", Toast.LENGTH_SHORT).show();
        }
        else {
            dismiss();
            if (mConfirmListener != null) {
                mConfirmListener.onKmConfirm(value);
            }
        }
    }

    public void setConfirmListener(OnConfirmListener confirmListener) {
        this.mConfirmListener = confirmListener;
    }

    public interface OnConfirmListener {
        void onKmConfirm(Double km);
    }
}
