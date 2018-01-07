package com.strukov.qchat.messaging;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.strukov.qchat.R;
import com.strukov.qchat.di.QChatApp;
import com.strukov.qchat.messaging.adapter.MessagingAdapter;
import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.User;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class MessagingActivity extends AppCompatActivity implements MessagingView, TextView.OnEditorActionListener {

    @Inject
    MessagingPresenterImpl presenter;

    @BindView(R.id.recycler_messaging)
    RecyclerView recyclerMessaging;

    @BindView(R.id.edit_message)
    EditText editMessage;

    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        QChatApp.getApp(this).getMessagingComponent().inject(this);
        ButterKnife.bind(this);

        mToolbar = findViewById(R.id.toolbar_messaging);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setTitle(null);

        editMessage.setOnEditorActionListener(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        int userId = bundle.getInt("user_id");

        presenter.onCreate(this, userId);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        if (isFinishing()) {
            presenter.onCloseRealm();
            QChatApp.getApp(this).releaseMessagingComponent();
        }
        super.onDestroy();
    }

    @Override
    public void setUserInfo(Friend friend) {
        TextView  textName = mToolbar.findViewById(R.id.text_toolbar_user);
        CircleImageView imageUser = mToolbar.findViewById(R.id.image_toolbar);

        textName.setText(friend.getName());
        Picasso.with(this).load(friend.getImage()).into(imageUser);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        boolean handled = false;
        if (i == EditorInfo.IME_ACTION_SEND) {
            presenter.sendMessage(editMessage.getText().toString());
            handled = true;
        }
        editMessage.setText("");
        recyclerMessaging.smoothScrollToPosition(0);
        return handled;
    }

    @Override
    public void setRecyclerView(RealmResults<Message> messages) {
        MessagingAdapter adapter = new MessagingAdapter(messages);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerMessaging.setLayoutManager(manager);
        recyclerMessaging.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
