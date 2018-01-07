package com.strukov.qchat.chat.fragments.settings.di;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.strukov.qchat.R;
import com.strukov.qchat.chat.fragments.settings.SettingsModelImpl;
import com.strukov.qchat.chat.fragments.settings.SettingsPresenterImpl;
import com.strukov.qchat.utils.rx.RxSchedulers;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by Matthew on 21.12.2017.
 */

@Module
public class SettingsModule {
    @SettingsScope
    @Provides
    GoogleSignInOptions providesGoogleSignInOptions(Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();
    }

    @SettingsScope
    @Provides
    GoogleSignInClient providesGoogleSignInClient(GoogleSignInOptions gso, Context context) {
        return GoogleSignIn.getClient(context, gso);
    }

    @Named("settings")
    @SettingsScope
    @Provides
    CompositeDisposable CompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    @SettingsScope
    SettingsModelImpl providesSettingsModelImpl(Context context, Realm realm, FirebaseAuth auth, GoogleSignInClient googleSignInClient) {
        return new SettingsModelImpl(context, realm, auth, googleSignInClient);
    }

    @Provides
    @SettingsScope
    SettingsPresenterImpl providesSettingsPresenterImpl(SettingsModelImpl model, RxSchedulers rxSchedulers, CompositeDisposable disposable) {
        return new SettingsPresenterImpl(model, rxSchedulers, disposable);
    }
}
