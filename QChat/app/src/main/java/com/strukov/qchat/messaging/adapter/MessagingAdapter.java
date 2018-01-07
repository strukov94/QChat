package com.strukov.qchat.messaging.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strukov.qchat.R;
import com.strukov.qchat.models.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static com.strukov.qchat.utils.ConvertDate.getDate;

/**
 * Created by Matthew on 27.12.2017.
 */

public class MessagingAdapter extends RealmRecyclerViewAdapter<Message, MessagingAdapter.MessagingViewHolder> {

    public MessagingAdapter(OrderedRealmCollection<Message> messages) {
        super(messages, true);
    }

    @Override
    public MessagingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessagingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagingViewHolder holder, int position) {
        Message obj = getItem(position);
        if (obj != null) {
            LinearLayout.LayoutParams timeParams = (LinearLayout.LayoutParams) holder.textTime.getLayoutParams();
            LinearLayout.LayoutParams messageParams = (LinearLayout.LayoutParams) holder.textMessage.getLayoutParams();
            if (!obj.isOut()) {
                timeParams.gravity = Gravity.END;
                messageParams.gravity = Gravity.END;

            } else {
                timeParams.gravity = Gravity.START;
                messageParams.gravity = Gravity.START;
            }

            holder.textTime.setLayoutParams(timeParams);
            holder.textMessage.setLayoutParams(messageParams);

            holder.textTime.setText(getDate(obj.getDate()));
            holder.textMessage.setText(obj.getMessage());
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getMessage_id();
    }

    class MessagingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_message_time)
        TextView textTime;

        @BindView(R.id.text_message)
        TextView textMessage;

        public MessagingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
