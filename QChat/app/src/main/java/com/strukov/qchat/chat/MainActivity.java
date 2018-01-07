package com.strukov.qchat.chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.strukov.qchat.chat.fragments.messages.MessagesFragment;
import com.strukov.qchat.chat.fragments.search.SearchFragment;
import com.strukov.qchat.chat.fragments.settings.SettingsFragment;
import com.strukov.qchat.login.LoginActivity;
import com.strukov.qchat.R;
import com.strukov.qchat.di.QChatApp;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements ChatView, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    ChatPresenterImpl presenter;

    @Inject
    SettingsFragment settingsFragment;

    @Inject
    MessagesFragment messagesFragment;

    @Inject
    SearchFragment searchFragment;

    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QChatApp.getApp(this).getChatComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        presenter.onCreate(this);
    }

    @Override
    protected void onStart() {
        presenter.onStart();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        if (isFinishing()) {
            presenter.onCloseRealm();
            QChatApp.getApp(this).releaseChatComponent();
            QChatApp.getApp(this).releaseSettingsComponent();
            QChatApp.getApp(this).releaseSearchComponent();
            QChatApp.getApp(this).releaseMessagesComponent();
        }
        super.onDestroy();
    }

    @Override
    public void onNetworkUnavailable() {
        Snackbar.make(findViewById(R.id.main_layout), R.string.network_unavailable, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStartLogIn() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void setUser(String image, String name) {

        View view = mNavigationView.getHeaderView(0);
        TextView textUserName = view.findViewById(R.id.text_user_name);
        CircleImageView imageUser = view.findViewById(R.id.image_user);

        if (image != null) {
            Picasso.with(this).load(image).into(imageUser);
        }
        textUserName.setText(name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.nav_search:
                manager.beginTransaction()
                        .replace(R.id.container, searchFragment)
                        .commit();
                break;
            case R.id.nav_setting:
                manager.beginTransaction()
                        .replace(R.id.container, settingsFragment)
                        .commit();
                break;

            case R.id.nav_messages:
                manager.beginTransaction()
                        .replace(R.id.container, messagesFragment)
                        .commit();
            case R.id.nav_share:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void deleteFragment() {
       getSupportFragmentManager().beginTransaction()
               .remove(getSupportFragmentManager().findFragmentById(R.id.container))
               .commit();
       QChatApp.getApp(this).releaseSettingsComponent();
    }
}