package solulab.demo.recyclerview.network;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import solulab.demo.recyclerview.R;
import solulab.demo.recyclerview.dialogs.ProgressDialog;
import solulab.demo.recyclerview.utility.Logger;
import solulab.demo.recyclerview.utility.Utils;

/**
 * Created by RAJESH KUSHVAHA
 */

public abstract class JSONCallback implements Callback<ResponseBody> {
    private Context context;
    private ProgressDialog dialog;

    public JSONCallback(Context context) {
        this(context, null);
    }

    public JSONCallback(Context context, ProgressDialog dialog) {
        this(context, dialog, false);
    }

    public JSONCallback(Context context, ProgressDialog dialog, boolean isLoadMore) {
        this.dialog = dialog;
        this.context = context;
        if (dialog != null && !isLoadMore && !dialog.isShowing()) dialog.show();
    }

    @Override
    public void onResponse(Call call, Response response) {
        String body = null;
        try {//Converting string to JSONObject
            Logger.e("Status Code", String.valueOf(response.code()));
            if (response.isSuccessful() || response.code() == 200) {
                body = ((ResponseBody) response.body()).string();
                JSONObject object = new JSONObject(body);
                Logger.e("Response", call.request().url().toString() + "\n" + object.toString());
                onSuccess(response.code(), object);
            } else {
                body = response.errorBody().string();
                JSONObject object = new JSONObject(body);
                if (body.isEmpty()) {
                    object.put("message", response.raw().message());
                    Logger.e("Response", call.request().url().toString() + "\n" + object);
                    onFailed(response.code(), object.optString("message"));
                } else {
                    Logger.e("Response", call.request().url().toString() + "\n" + object.toString());
                    onFailed(response.code(), object.optString("message"));
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            if (body != null) Logger.e("Response", call.request().url().toString() + "\n" + body);
            onFailed(response.code(), context.getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Logger.e("Response", call.request().url().toString() + "\n" + t.toString());
        if (!Utils.isConnectingToInternet(context)) {
            onFailed(0, context.getString(R.string.no_internet_connection));
        } else if (t instanceof ConnectException
                || t instanceof SocketTimeoutException
                || t instanceof UnknownHostException) {
            onFailed(0, context.getString(R.string.failed_to_connect_with_server));
        } else if (t instanceof IOException) {
            onFailed(0, context.getString(R.string.something_went_wrong));
        } else {
            onFailed(0, t.getMessage());
        }
    }

    protected abstract void onSuccess(int statusCode, JSONObject jsonObject);

    protected abstract void onFailed(int statusCode, String message);

}
