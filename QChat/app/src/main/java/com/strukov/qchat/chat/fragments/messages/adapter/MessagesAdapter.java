package com.strukov.qchat.chat.fragments.messages.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.strukov.qchat.R;
import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

import static com.strukov.qchat.utils.ConvertDate.getDate;

/**
 * Created by Matthew on 27.12.2017.
 */

public class MessagesAdapter extends RealmRecyclerViewAdapter<Message, MessagesAdapter.MessagesViewHolder> {
    private Context mContext;
    private Realm mRealm;
    private OnClickUserMessage mOnClick;
    private int mUserId;

    public interface OnClickUserMessage {
        void onClickItem(int userId);
    }

    public MessagesAdapter(OrderedRealmCollection<Message> messages, Context context, Realm realm, OnClickUserMessage onClick, int userId) {
        super(messages,true);
        mContext = context;
        mRealm = realm;
        mOnClick = onClick;
        mUserId = userId;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_messages, parent, false);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        Message message = getItem(position);

        if (message != null) {
            int friendId = message.isOut() ? message.getWriter_id() : message.getReceiver_id();
            User user = mRealm.where(User.class).equalTo("id", mUserId).findFirst();
            Friend friend = user.getFriends().where().equalTo("friend_id", friendId).findFirst();

            if (friend != null) {
                holder.friend = friend;
                Picasso.with(mContext).load(friend.getImage()).into(holder.imageUser);
                holder.textName.setText(friend.getName());
                holder.textDate.setText(getDate(message.getDate()));
                holder.textMessage.setText(message.getMessage());
            }
        }
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_messages_name)
        TextView textName;

        @BindView(R.id.text_messages_date)
        TextView textDate;

        @BindView(R.id.text_messages)
        TextView textMessage;

        @BindView(R.id.image_messages)
        CircleImageView imageUser;

        @BindView(R.id.messages_view)
        LinearLayout view;

        public Friend friend;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClick.onClickItem(friend.getFriend_id());
        }
    }
}
