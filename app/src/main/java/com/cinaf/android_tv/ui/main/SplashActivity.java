package com.cinaf.android_tv.ui.main;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cinaf.android_tv.App;
import com.cinaf.android_tv.R;
import com.cinaf.android_tv.data.Api.TheMovieDbAPI;
import com.cinaf.android_tv.data.models.CodeTv;
import com.cinaf.android_tv.provider.PrefManager;
import com.cinaf.android_tv.translate.LocaleHelper;
import com.cinaf.android_tv.ui.check_subscription.IDCode;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Inject
    TheMovieDbAPI theMovieDbAPI;
    private PrefManager prf;
    private ProgressDialog register_progress;
    private AlertDialog.Builder build_error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        App.instance().appComponent().inject(this);
        prf = new PrefManager(getApplicationContext());
        build_error = new AlertDialog.Builder(this);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if (prf.getString("CODE_TV").isEmpty()) {
                    Intent i = new Intent(SplashActivity.this, IDCode.class);
                    startActivity(i);
                    finish();
                } else {
                    submitIDCode(prf.getString("CODE_TV"));
                }
            }
        }, SPLASH_TIME_OUT);
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


    private void bindIDCodeResponse(CodeTv response, ProgressDialog register_progress, String codeID) {

        if (response != null) {

            if (response.getCode() == 200) {

                for (int i = 0; i < response.getValues().size(); i++) {
                    Log.w("TAG", "bindIDCodeResponse: " + response.getValues().get(i).getName() + "====" + response.getValues().get(i).getValue());
                    //Toast.makeText(this, String.valueOf(response.getValues().get(i).getName()), Toast.LENGTH_SHORT).show();
                    if (response.getValues().get(i).getName().toLowerCase().equalsIgnoreCase("subscribed") &&
                            response.getValues().get(i).getValue().toLowerCase().equalsIgnoreCase("true")) {

                        register_progress.dismiss();
                        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                        prf.setString("CODE_TV", codeID);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (response.getValues().get(i).getName().toLowerCase().equalsIgnoreCase("subscribed") &&
                            response.getValues().get(i).getValue().toLowerCase().equalsIgnoreCase("false")) {

                        register_progress.dismiss();
                        View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.alert_dialog_success, null);
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
                                Intent i = new Intent(SplashActivity.this, IDCode.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        build_error.setCancelable(false);
                        build_error.setView(view);
                        build_error.show();
                    }

                }


            } else {
                register_progress.dismiss();
                View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.alert_dialog_success, null);
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
                        Intent i = new Intent(SplashActivity.this, IDCode.class);
                        startActivity(i);
                        finish();
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