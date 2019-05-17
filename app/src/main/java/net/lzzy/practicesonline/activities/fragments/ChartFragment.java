package net.lzzy.practicesonline.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ChartFragment extends BaseFragment {

    private static final String ARG_RESULTS ="argResults";
    private List<QuestionResult> results;
    private OnResultSwitchListener listener;


    public static ChartFragment newInstance(List<QuestionResult>results){
        ChartFragment fragment =new ChartFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList(ARG_RESULTS,(ArrayList<?extends Parcelable>)results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            results =getArguments().getParcelableArrayList(ARG_RESULTS);
        }
    }

    @Override
    protected void populate() {
       find(R.id.fragment_chart_tv).setOnClickListener(v -> {
           if (listener!=null){
               listener.gotoGrid();
           }
       });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chart;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResultSwitchListener){
            listener=(OnResultSwitchListener)context;
        }else {
            throw new ClassCastException(context .toString()+"");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
    public interface OnResultSwitchListener{
        /**
         *
         */
        void gotoGrid();
    }
}
