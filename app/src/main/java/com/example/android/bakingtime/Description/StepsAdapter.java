package com.example.android.bakingtime.Description;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder>{

    private static int mTimes;
    private static int mIndex;

    final private ListItemClickListener mOnClickListener;


    private int mNumberItems;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public StepsAdapter(int index,int times, ListItemClickListener listener) {
        mIndex=index;
        mTimes=times;
        mOnClickListener=listener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.steps_item_design;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepViewHolder viewHolder=new StepViewHolder(view);
        view.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTimes;
    }

    class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        @BindView(R.id.tv_step_name) TextView stepsToBeDone;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex) {
            stepsToBeDone.setText(MainActivity.shortDescription[mIndex][listIndex]);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
