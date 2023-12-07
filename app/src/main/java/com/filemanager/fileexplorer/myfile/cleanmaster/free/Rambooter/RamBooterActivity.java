package com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.tablayout.TabView;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Rambooter.taskdialog.TaskFragment;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.viewpagerAdapter.ZoomOutPageTransformer;


public class RamBooterActivity extends AppCompatActivity {
    private ImageView img_back;
    private ViewPager2 pager = null;
    private MemoryBoosterAdapter pagerAdapter = null;
    private TabView tabView;
    private String[] title;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ram_booter);
        this.title = getResources().getStringArray(R.array.my_tab_array);
        this.tabView = (TabView) findViewById(R.id.tabv_tab0);
        this.pager = (ViewPager2) findViewById(R.id.pager);
        this.img_back = (ImageView) findViewById(R.id.img_back);
        MemoryBoosterAdapter memoryBoosterAdapter = new MemoryBoosterAdapter(getSupportFragmentManager(), getLifecycle());
        this.pagerAdapter = memoryBoosterAdapter;
        memoryBoosterAdapter.addfr(new BoostFragment());
        this.pagerAdapter.addfr(new TaskFragment());
        this.pagerAdapter.addfr(new MoreFragments());
        this.pager.setAdapter(this.pagerAdapter);
        this.pager.setPageTransformer(new ZoomOutPageTransformer());
        this.tabView.setOnTabSelectedListener(new TabView.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int i) {
                RamBooterActivity.this.pager.setCurrentItem(i);
            }
        });
        this.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                RamBooterActivity.this.tabView.select(i, true);
            }
        });
        this.img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RamBooterActivity.this.finish();
                Animatoo.animateSlideRight(RamBooterActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
