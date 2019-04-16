package net.lzzy.practicesonline.activities.activities;

import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.lzzy.practicesonline.activities.utils.AppUtils;

/**
 * Created by lzzy_gxy on 2019/4/11.
 * Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutRes());
        AppUtils.AddActivity(this);//
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(getContainerId());
        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction().add(getContainerId(), fragment).commit();

        }


    }
  //  @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setMessage("要退出吗？")
//                .setPositiveButton("确定",(dialog,which)->AppUtils.exit())
//                .show();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppUtils.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.setRunning(getLocalClassName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppUtils.setStopped(getLocalClassName());
    }

    /**
     * Activity 布局ID
     *
     * @return
     */
    protected abstract int getLayoutRes();

    /**
     * @return
     */
    protected abstract int getContainerId();

    /**
     * 托管对象
     *
     * @return
     */
    protected abstract Fragment createFragment();

}
