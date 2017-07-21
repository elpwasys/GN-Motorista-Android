package br.com.wasys.library.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import br.com.wasys.library.R;

/**
 * Created by pascke on 04/05/16.
 */
public class AsyncTask extends android.os.AsyncTask<AsyncTask.Task, Void, Boolean> {

    private final Task task;
    private final Activity activity;

    private boolean loading;
    private Throwable throwable;
    private ProgressDialog progress;

    private static final String TAG = AsyncTask.class.getSimpleName();

    public AsyncTask(Task task, Activity activity) {
        this.task = task;
        this.activity = activity;
        this.loading = true;
    }

    public AsyncTask(Task task, Activity activity, boolean loading) {
        this(task, activity);
        this.loading = loading;
    }

    @Override
    protected void onPreExecute() {
        if (this.loading) {
            this.progress = ProgressDialog.show(activity, null, activity.getString(R.string.aguarde), false, false);
        }
    }

    @Override
    protected Boolean doInBackground(Task... params) {
        try {
            Log.d(TAG, "Starting " + this.getClass().getName() + ".doInBackground method...");
            task.perform();
        } catch (Throwable e) {
            throwable = e;
            Log.e(TAG, ExceptionUtils.getRootCauseMessage(e), e);
            return false;
        } finally {
            Log.d(TAG, "Ending " + this.getClass().getName() + ".doInBackground method...");
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (this.loading) {
            progress.dismiss();
        }
        if (BooleanUtils.isTrue(result)) {
            task.update();
        }
        else {
            onThowable(throwable);
        }
    }

    protected void onThowable(Throwable throwable) {
        String message;
        if (throwable instanceof OutOfMemoryError) {
            message = activity.getString(R.string.msg_out_of_memory_error);
        }
        else {
            message = throwable.getMessage();
            Throwable rootCause = ExceptionUtils.getRootCause(throwable);
            if (rootCause != null) {
                message = rootCause.getMessage();
            }
        }
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public interface Task {
        abstract void update();
        abstract void perform() throws Throwable;
    }
}
