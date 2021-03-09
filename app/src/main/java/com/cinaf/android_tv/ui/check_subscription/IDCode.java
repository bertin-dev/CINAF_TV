package com.cinaf.android_tv.ui.check_subscription;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cinaf.android_tv.App;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;
import com.cinaf.android_tv.data.models.CodeTv;
import com.cinaf.android_tv.provider.PrefManager;
import com.cinaf.android_tv.translate.LocaleHelper;
import com.cinaf.android_tv.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class IDCode extends Activity {

    @Inject
    TheMovieDbAPI theMovieDbAPI;
    @BindView(R.id.text_input_editor_text_activity_id_code)
    EditText text_input_editor_text_activity_id_code;
    @BindView(R.id.linear_layout_id_code_activity)
    LinearLayout linear_layout_id_code_activity;
    private PrefManager prf;
    private ProgressDialog register_progress;
    private AlertDialog.Builder build_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.instance().appComponent().inject(this);
        setContentView(R.layout.activity_id_code);
        ButterKnife.bind(this);
        prf = new PrefManager(getApplicationContext());
        build_error = new AlertDialog.Builder(this);
    }


    @OnClick(R.id.envoyer)
    void envoyer() {
        if (!validatIDCode())
            return;
        submitIDCode(text_input_editor_text_activity_id_code.getText().toString());
    }

    private void submitIDCode(String idCode) {

        register_progress = ProgressDialog.show(this, null, getResources().getString(R.string.operation_progress), true);

        theMovieDbAPI.codeID(idCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    bindIDCodeResponse(response, register_progress, idCode);
                }, e -> {
                    Toast.makeText(this, getString(R.string.annulProcess), Toast.LENGTH_SHORT).show();
                    register_progress.dismiss();
                    Timber.e(e, "Error fetching post id code: %s", e.getMessage());
                });
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void bindIDCodeResponse(CodeTv response, ProgressDialog register_progress, String codeID) {

        if (response != null) {

            if (response.getCode() == 200) {

                for (int i = 0; i < response.getValues().size(); i++) {

                    Log.w("TAG", "bindIDCodeResponse: " + response.getValues().get(i).getName() + "====" + response.getValues().get(i).getValue());
                    //Toast.makeText(this, String.valueOf(response.getValues().get(i).getName()), Toast.LENGTH_SHORT).show();
                    if (response.getValues().get(i).getName().toLowerCase().equalsIgnoreCase("subscribed") &&
                            response.getValues().get(i).getValue().toLowerCase().equalsIgnoreCase("true")) {

                        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        prf.setString("CODE_TV", codeID);
                        new Handler().postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        register_progress.dismiss();
                                        Intent i = new Intent(IDCode.this, MainActivity.class);
                                        startActivity(i);
                                        // close this activity
                                        finish();
                                    }
                                }, 2000
                        );

                    } else if (response.getValues().get(i).getName().toLowerCase().equalsIgnoreCase("subscribed") &&
                            response.getValues().get(i).getValue().toLowerCase().equalsIgnoreCase("false")) {

                        register_progress.dismiss();
                        View view = LayoutInflater.from(IDCode.this).inflate(R.layout.alert_dialog_success, null);
                        TextView title = (TextView) view.findViewById(R.id.title);
                        TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                        ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                        title.setText(getString(R.string.infoEnTeteAlertDialog));
                        imageButton.setImageResource(R.drawable.ic_cancel_64);
                        statutOperation.setText(getString(R.string.stateSubscribed));
                        build_error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                recreate();
                            }
                        });
                        build_error.setCancelable(false);
                        build_error.setView(view);
                        build_error.show();

                    }

                }

            } else {
                register_progress.dismiss();
                View view = LayoutInflater.from(IDCode.this).inflate(R.layout.alert_dialog_success, null);
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView statutOperation = (TextView) view.findViewById(R.id.statutOperation);
                ImageButton imageButton = (ImageButton) view.findViewById(R.id.image);
                title.setText(getString(R.string.infoEnTeteAlertDialog));
                imageButton.setImageResource(R.drawable.ic_cancel_64);
                statutOperation.setText(response.getMessage());
                build_error.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                build_error.setCancelable(false);
                build_error.setView(view);
                build_error.show();
            }

        } else {
            Toast.makeText(this, getString(R.string.annulProcess), Toast.LENGTH_SHORT).show();
        }
        register_progress.dismiss();
    }


    private boolean validatIDCode() {
        if (text_input_editor_text_activity_id_code.getText().toString().trim().isEmpty() || text_input_editor_text_activity_id_code.getText().length() < 3) {
            text_input_editor_text_activity_id_code.setError(getString(R.string.error_short_value));
            requestFocus(text_input_editor_text_activity_id_code);
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * attachBaseContext(Context newBase) methode callback permet de verifier la langue au demarrage
     *
     * @param newBase
     * @since 2021
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

}