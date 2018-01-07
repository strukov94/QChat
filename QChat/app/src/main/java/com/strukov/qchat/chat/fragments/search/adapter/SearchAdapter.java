package com.strukov.qchat.chat.fragments.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.strukov.qchat.R;
import com.strukov.qchat.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matthew on 24.12.2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<User> mUserList = new ArrayList<>();
    private Context mContext;
    private UserCardOnClick mOnClick;

    public interface UserCardOnClick {
        void onClickItem(int userId, String name);
    }

    public SearchAdapter(Context context, UserCardOnClick onClick) {
        mContext = context;
        mOnClick = onClick;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        if (mUserList.get(position).getImage() != null)
            Picasso.with(mContext).load(mUserList.get(position).getImage()).into(holder.imageUser);
        holder.textName.setText(mUserList.get(position).getName());
        holder.textName.setTag(mUserList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        if (mUserList == null) return 0;
        return mUserList.size();
    }

    public void swapAdapter(List<User> users) {
        mUserList = users;
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image_card_user)
        CircleImageView imageUser;

        @BindView(R.id.text_card_user)
        TextView textName;

        @BindView(R.id.card_user)
        RelativeLayout view;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClick.onClickItem(mUserList.get(getAdapterPosition()).getId(), mUserList.get(getAdapterPosition()).getName());
        }
    }
}
