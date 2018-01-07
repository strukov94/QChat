package com.strukov.qchat.chat.fragments.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.iid.FirebaseInstanceId;
import com.strukov.qchat.R;
import com.strukov.qchat.chat.fragments.messages.adapter.MessagesAdapter;
import com.strukov.qchat.di.QChatApp;
import com.strukov.qchat.messaging.MessagingActivity;
import com.strukov.qchat.models.Message;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Matthew on 21.12.2017.
 */

public class MessagesFragment extends Fragment implements MessagesView, MessagesAdapter.OnClickUserMessage {

    @BindView(R.id.recycler_messages)
    RecyclerView recyclerMessages;

    @Inject
    MessagesPresenterImpl presenter;

    public MessagesFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        QChatApp.getApp(context).getMessagesComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, rootView);

        presenter.onCreate(this);
        return rootView;
    }

    @Override
    public void setRecyclerView(RealmResults<Message> messages, Realm realm, int userId) {
        MessagesAdapter adapter = new MessagesAdapter(messages, getContext(), realm, this, userId);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerMessages.setLayoutManager(manager);
        recyclerMessages.setAdapter(adapter);
    }

    @Override
    public void onClickItem(int userId) {
        Intent intent = new Intent(getActivity(), MessagingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", userId);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}

