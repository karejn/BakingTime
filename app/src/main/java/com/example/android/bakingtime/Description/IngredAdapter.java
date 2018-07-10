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



public class IngredAdapter extends RecyclerView.Adapter<IngredAdapter.IngredViewHolder>{

    private static int mTimes;
    private static int mIndex;

    public IngredAdapter(int index,int times) {
        mIndex=index;
        mTimes=times;
    }

    @Override
    public IngredViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredient_item_design;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        IngredViewHolder viewHolder=new IngredViewHolder(view);
        view.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTimes;
    }

    class IngredViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_ingred_name) TextView ingredTextView;
        @BindView(R.id.tv_quantity) TextView quantityTextView;
        @BindView(R.id.tv_measure) TextView measureTextView;

        public IngredViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        void bind(int listIndex) {
            ingredTextView.setText(MainActivity.ingredient[mIndex][listIndex]);
            quantityTextView.setText(MainActivity.quantity[mIndex][listIndex]);
            measureTextView.setText(MainActivity.measure[mIndex][listIndex]);
        }
    }

}
