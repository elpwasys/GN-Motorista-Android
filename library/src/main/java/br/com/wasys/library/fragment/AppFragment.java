package br.com.wasys.library.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import br.com.wasys.library.R;
import br.com.wasys.library.http.AsyncTask;
import br.com.wasys.library.utils.AndroidUtils;

/**
 * Created by pascke on 03/08/16.
 */
public abstract class AppFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

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
        Context context = getContext();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                checked = false;
                break;
            }
        }
        return checked;
    }

    protected void openVirtualKeyboard() {
        Activity activity = getActivity();
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    protected void closeVirtualKeyboard() {
        Activity activity = getActivity();
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void startTask(AsyncTask.Task task) {
        startTask(false, false, task);
    }

    protected void startTask(boolean progress, boolean checkNetwork, AsyncTask.Task task) {
        FragmentActivity activity = this.getActivity();
        if (checkNetwork) {
            if (!AndroidUtils.isNetworkAvailable(activity)) {
                Toast.makeText(activity, getString(R.string.rede_indisponivel), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        AsyncTask asyncTask = new AsyncTask(task, activity, progress);
        asyncTask.execute();
    }
}