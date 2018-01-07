package com.strukov.qchat.chat.fragments.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.strukov.qchat.R;
import com.strukov.qchat.chat.MainActivity;
import com.strukov.qchat.di.QChatApp;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matthew on 20.12.2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener, SettingsView {
    @Inject
    SettingsPresenterImpl presenter;

    @BindView(R.id.button_settings_exit)
    Button butExit;

    private MainActivity mMainActivity;

    public SettingsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        QChatApp.getApp(context).getSettingsComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity  = (MainActivity) getActivity();
        presenter.onCreate(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);

        butExit.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        presenter.onClick(view);
    }

    @Override
    public void startLoginActivity() {
        mMainActivity.deleteFragment();
        mMainActivity.onStartLogIn();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
