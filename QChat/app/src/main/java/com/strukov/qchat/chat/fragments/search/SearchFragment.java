package com.strukov.qchat.chat.fragments.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strukov.qchat.R;
import com.strukov.qchat.api.QChatApi;
import com.strukov.qchat.chat.MainActivity;
import com.strukov.qchat.chat.fragments.search.adapter.SearchAdapter;
import com.strukov.qchat.di.QChatApp;
import com.strukov.qchat.messaging.MessagingActivity;
import com.strukov.qchat.models.Users;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Matthew on 24.12.2017.
 */

public class SearchFragment extends Fragment implements SearchView, SearchAdapter.UserCardOnClick {
    @BindView(R.id.recycler_search)
    RecyclerView recyclerSearch;

    @Inject
    SearchPresenterImpl presenter;

    @Inject
    SearchAdapter adapter;

    public SearchFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        QChatApp.getApp(context).getSearchComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerSearch.setHasFixedSize(true);
        recyclerSearch.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate(this);
    }

    @Override
    public void onNetworkUnavailable() {
        ((MainActivity)getActivity()).onNetworkUnavailable();
    }

    @Override
    public void swapAdapter(Users users) {
        adapter.swapAdapter(users.getUsers());
    }

    @Override
    public void onClickItem(int userId, String name) {
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
