package net.lzzy.practicesonline.activities.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ChartFragment extends BaseFragment {

    private static final String ARG_RESULTS = "argResults";
    private static final String COLOR_GREEN = "#629755";
    private static final String COLOR_RED = "#D81B60";
    private static final String COLOR_PRIMARY = "#008577";
    private static final String COLOR_BROWN = "#00574B";
    private List<QuestionResult> results;
    private OnResultSwitchListener listener;
    private PieChart pChart;
    private LineChart lChart;
    private BarChart bChart;
    private Chart[] charts;
    private String[] titles={"正确错误比例(单位%)","题目阅读量统计","题目错误类型统计"};
    private int rightCount=0;


    public static ChartFragment newInstance(List<QuestionResult> results) {
        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_RESULTS, (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            results = getArguments().getParcelableArrayList(ARG_RESULTS);
        }
        for (QuestionResult result:results){
            if (result.isRight()){
                rightCount++;
            }
        }
    }

    @Override
    protected void populate() {
        find(R.id.fragment_chart_tv).setOnClickListener(v -> {
            if (listener != null) {
                listener.gotoGrid();
            }
        });
        initCharts();
        configPieChart();
        displayPieChart();
        pChart.setVisibility(View.VISIBLE);
    }

    private void displayPieChart() {
        List<PieEntry> entries=new ArrayList<>();
        entries.add(new PieEntry(rightCount,"正确"));
        entries.add(new PieEntry(results.size()-rightCount,"错误"));
        PieDataSet dataSet=new PieDataSet(entries,"");
        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0,40));
        dataSet.setSelectionShift(5f);
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.parseColor(COLOR_GREEN));
        colors.add(Color.parseColor(COLOR_RED));
        dataSet.setColors(colors);
        PieData data =new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pChart.setData(data);
        pChart.invalidate();


    }

    private void configPieChart() {
        pChart.setUsePercentValues(true);
        pChart.setDrawHoleEnabled(false);
        pChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);


    }

    private void initCharts() {
        pChart = find(R.id.fragment_chart_pie);
        lChart = find(R.id.fragment_chart_line);
        bChart = find(R.id.fragment_chart_bar);
        charts = new Chart[]{pChart,lChart,bChart};
        int i=0;
        for (Chart chart:charts){
            chart.setTouchEnabled(false);
            chart.setVisibility(View.GONE);
            Description desc=new Description();
            desc.setText(titles[i++]);
            chart.setDescription(desc);
            chart.setNoDataText("数据获取中....");
            chart.setExtraOffsets(5,10,5,25);
        }
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
        if (context instanceof OnResultSwitchListener) {
            listener = (OnResultSwitchListener) context;
        } else {
            throw new ClassCastException(context.toString() + "");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnResultSwitchListener {
        /**
         *
         */
        void gotoGrid();
    }
}
