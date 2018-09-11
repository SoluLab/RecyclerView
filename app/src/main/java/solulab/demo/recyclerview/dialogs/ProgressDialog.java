package solulab.demo.recyclerview.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import solulab.demo.recyclerview.R;

/**
 * Created by Rajesh Kushvaha
 */

public class ProgressDialog extends Dialog {
    private String message;
    private TextView tvMessage;

    public ProgressDialog(Context context) {
        this(context, null);
    }

    public ProgressDialog(Context context, String message) {
        super(context, R.style.ProgressDialog);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_progress);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        tvMessage = findViewById(R.id.tvMessage);
        if (message != null) tvMessage.setText(message);
    }
}
