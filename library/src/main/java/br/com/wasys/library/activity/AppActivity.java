package br.com.wasys.library.activity;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import br.com.wasys.library.R;
import br.com.wasys.library.http.AsyncTask;
import br.com.wasys.library.utils.AndroidUtils;

/**
 * Created by pascke on 03/08/16.
 */
public abstract class AppActivity extends AppCompatActivity {

    private ProgressDialog mProgress;

    protected void showProgress() {
        hideProgress();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.aguarde));
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.show();
    }

    protected void hideProgress() {
        if (mProgress != null) {
            if (mProgress.isShowing()) {
                mProgress.hide();
            }
            mProgress = null;
        }
    }

    protected boolean grantedResults(int[] grantResults) {
        boolean granted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    protected boolean checkedSelfPermission(String[] permissions) {
        boolean checked = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                checked = false;
                break;
            }
        }
        return checked;
    }

    protected void startTask(AsyncTask.Task task) {
        startTask(false, false, task);
    }

    protected void startTask(boolean progress, boolean checkNetwork, AsyncTask.Task task) {
        if (checkNetwork) {
            if (!AndroidUtils.isNetworkAvailable(this)) {
                Toast.makeText(this, getString(R.string.rede_indisponivel), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        AsyncTask asyncTask = new AsyncTask(task, this, progress);
        asyncTask.execute();
    }
}