package com.strukov.qchat.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.SignInButton;
import com.strukov.qchat.R;
import com.strukov.qchat.di.QChatApp;
import com.strukov.qchat.services.UpdateMessagesIntentService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @Inject
    LoginPresenterImpl presenter;

    @BindView(R.id.sign_in_button)
    SignInButton signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        QChatApp.getApp(this).getLoginComponent().inject(this);

        signIn.setOnClickListener(this::onClickGoogle);

        presenter.onCreate(this);
    }

    public void onClickGoogle(View view) {
        presenter.onClick(view);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        if (isFinishing()) {
            presenter.onCloseRealm();
            QChatApp.getApp(this).releaseLoginComponent();
        }
        super.onDestroy();
    }

    @Override
    public void startSignInActivity(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void finishSignInActivity() {
        finish();
    }

    @Override
    public void onNetworkUnavailable() {
        Snackbar.make(findViewById(R.id.layout_login), R.string.network_unavailable, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateMessages(int userId) {
        Intent intent = new Intent(this, UpdateMessagesIntentService.class);
        intent.putExtra("userId", userId);
        startService(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
