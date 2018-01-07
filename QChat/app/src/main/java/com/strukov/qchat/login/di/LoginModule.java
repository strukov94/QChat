package com.strukov.qchat.login.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.strukov.qchat.R;
import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.chat.di.ChatScope;
import com.strukov.qchat.login.LoginModelImpl;
import com.strukov.qchat.login.LoginPresenterImpl;
import com.strukov.qchat.utils.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by Matthew on 15.12.2017.
 */

@Module
public class LoginModule {
    @LoginScope
    @Provides
    Realm providesRealm() {
        return Realm.getDefaultInstance();
    }

    @LoginScope
    @Provides
    GoogleSignInOptions providesGoogleSignInOptions(Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();
    }

    @LoginScope
    @Provides
    GoogleSignInClient providesGoogleSignInClient(GoogleSignInOptions gso, Context context) {
        return GoogleSignIn.getClient(context, gso);
    }

    @LoginScope
    @Provides
    CompositeDisposable CompositeDisposable() {
        return new CompositeDisposable();
    }

    @LoginScope
    @Provides
    LoginModelImpl providesModel(Context context, Realm realm, QChatApi api, FirebaseInstanceId instanceId, SharedPreferences sharedPreferences) {
        return new LoginModelImpl(context, realm, api, instanceId, sharedPreferences);
    }

    @LoginScope
    @Provides
    LoginPresenterImpl providesPresenter(LoginModelImpl model, GoogleSignInClient googleSignInClient, RxSchedulers schedulers
            , CompositeDisposable disposable, FirebaseAuth auth) {
        return new LoginPresenterImpl(model, googleSignInClient, schedulers, disposable, auth);
    }
}
