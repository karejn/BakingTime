package com.example.android.bakingtime.Description;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class IngredFragmentContent extends Fragment {
    @BindView(R.id.rv_ingred)
    RecyclerView IngredList;
    private Unbinder unbinder;

    private IngredAdapter mAdapter;

    public IngredFragmentContent() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredient_item, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        IngredList.setLayoutManager(layoutManager);

        IngredList.setHasFixedSize(true);

        int flag = 0;
        for (int i = 0; i < MainActivity.ingredient[DetailedList.id].length; i++) {
            if (MainActivity.ingredient[DetailedList.id][i] == null) {
                break;
            } else {
                flag += 1;
            }
        }

        mAdapter = new IngredAdapter(DetailedList.id, flag);
        IngredList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
