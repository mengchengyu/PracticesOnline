package net.lzzy.practicesonline.activities.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.fragments.AnalysisFragment;
import net.lzzy.practicesonline.activities.fragments.ChartFragment;
import net.lzzy.practicesonline.activities.fragments.GridFragment;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;

import java.util.List;

/**
 * Created by lzzy_gxy on 2019/5/13.
 * Description:
 */
public class ResultActivity extends BaseActivity implements GridFragment.onResultSwitchListener ,
        ChartFragment.OnResultSwitchListener {

    public static final String EXTRA_POSITION = "extraPosition";
    private List<QuestionResult> results;
    public static final String PRACTICE_ID = "practiceId";
    private String practiceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        practiceId = getIntent().getStringExtra(QuestionActivity.EXTRA_PRACTICE_ID);
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_result_container;
    }

    @Override
    protected Fragment createFragment() {
        results =getIntent().getParcelableArrayListExtra(QuestionActivity.EXTRA_RESULT);

        return GridFragment.newInstance(results);
        //GridFragment
    }

    @Override
    public void goBack(int position) {
        Intent intent=new Intent();
        intent.putExtra(EXTRA_POSITION, position);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void gotoChart() {
        getManager().beginTransaction()
                .replace(getContainerId(), ChartFragment.newInstance(results)).commit();
    }


    @Override
    public void gotoGrid() {
        getManager().beginTransaction()
                .replace(getContainerId(), GridFragment.newInstance(results)).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        new AlertDialog.Builder(this)
                .setMessage("返回到哪里？")
                .setNeutralButton("返回题目", (dialog, which) -> {
                    finish();

                })
                .setNegativeButton("章节列表", (dialog, which) -> {
                    intent.setClass(ResultActivity.this,PracticesActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setPositiveButton("返回收藏", (dialog, which) -> {
                    intent.putExtra(PRACTICE_ID,practiceId);
                    setResult(QuestionActivity.CONTEXT_INCLUDE_CODE,intent);
                    finish();
                })
                .show();
    }


    //
}
