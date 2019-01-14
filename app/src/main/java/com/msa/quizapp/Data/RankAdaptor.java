package com.msa.quizapp.Data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.msa.quizapp.Model.Quiz;
import com.msa.quizapp.Model.Rank;
import com.msa.quizapp.R;

import java.util.List;

public class RankAdaptor extends ArrayAdapter<Rank> {
    private Activity context;
    private List<Rank> rankList;

    public RankAdaptor(Activity context, List<Rank> rankList) {
        super(context, R.layout.quiz_row, rankList);
        this.context = context;
        this.rankList = rankList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View itemListView = inflater.inflate(R.layout.rank_row,null,true);
        TextView rankpostion,user_name,user_points;
        CircularImageView user_image;
        rankpostion = (TextView) itemListView.findViewById(R.id.rank_position);
        user_name = (TextView) itemListView.findViewById(R.id.user_name);
        user_image = (CircularImageView) itemListView.findViewById(R.id.user_image);
        user_points = (TextView) itemListView.findViewById(R.id.user_points);

        Rank rank = getItem(position);
        rankpostion.setText((position+1)+"");
        user_name.setText(rank.getUname());
        user_points.setText(rank.getTotalpt()+"");
        Glide.with(context)
                .load(rank.getUimage())
                .into(user_image);

        return  itemListView;
    }
}
