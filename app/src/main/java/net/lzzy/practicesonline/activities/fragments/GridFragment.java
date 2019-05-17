package net.lzzy.practicesonline.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class GridFragment extends BaseFragment {
    public static final String PARAM = "param";
    private List<QuestionResult> results;
    private GridView gvView;
    private TextView tvView;
    private BaseAdapter adapter;
    private onResultSwitchListener listener;


    @Override
    protected void populate() {
        gvView = find(R.id.fragment_grid_gv);
        tvView = find(R.id.fragment_grid_tv);
        tvView.setOnClickListener(v -> listener.gotoChart());

        if (getArguments() != null) {
            results = getArguments().getParcelableArrayList(PARAM);
        }
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return results.size();
            }

            @Override
            public Object getItem(int position) {
                return results.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_layout, null);
                }
                TextView textView = convertView.findViewById(R.id.grid_layout_tv);
                QuestionResult result = results.get(position);
                if (result.isRight()) {
                    textView.setBackgroundResource(R.drawable.grid_green);
                } else {
                    textView.setBackgroundResource(R.drawable.grid_accent);
                }
                textView.setText(position + 1 + "");
                return convertView;

            }
        };
        gvView.setAdapter(adapter);
        gvView.setOnItemClickListener((parent, view, position, id) -> listener.goBack(position));


    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_grid;
    }

    @Override
    public void search(String kw) {

    }

    public static GridFragment newInstance(List<QuestionResult> results) {
        GridFragment gridFragment = new GridFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(PARAM, (ArrayList<? extends Parcelable>) results);
        gridFragment.setArguments(args);
        return gridFragment;

    }

    public interface onResultSwitchListener {

        void goBack(int position);


        void gotoChart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onResultSwitchListener) {
            listener = (onResultSwitchListener) context;
        } else {
            throw new RuntimeException(context.toString() + "必须实现bbb");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
