package com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.viewpagerAdapter.FragmentAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.viewpagerAdapter.ZoomOutPageTransformer;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.slide.Slide2Fragment;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.slide.Slide3Fragment;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.slide.SlideFragment;
import com.zhpan.indicator.IndicatorView;


public class SlideActivity extends AppCompatActivity {
    private FragmentAdapter fragmentAdapter;
    private TextView img_skip;
    private TextView txt_bold;
    private TextView txt_next;
    private TextView txt_read_more;
    private TextView txt_thin;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_slide);
        if (checkPermission()) {
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }
        this.txt_next = (TextView) findViewById(R.id.img_next);
        this.img_skip = (TextView) findViewById(R.id.img_skip);
        this.txt_bold = (TextView) findViewById(R.id.txt1);
        this.txt_thin = (TextView) findViewById(R.id.txt2);
        this.txt_read_more = (TextView) findViewById(R.id.tvReadMore);
        IndicatorView indicatorView = (IndicatorView) findViewById(R.id.indicator);
        final ViewPager2 viewPager2 = (ViewPager2) findViewById(R.id.viewPager1);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        this.fragmentAdapter = fragmentAdapter;
        fragmentAdapter.addFrag(new SlideFragment());
        this.fragmentAdapter.addFrag(new Slide2Fragment());
        this.fragmentAdapter.addFrag(new Slide3Fragment());
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
        viewPager2.setAdapter(this.fragmentAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int i, float f, int i2) {
                super.onPageScrolled(i, f, i2);
                if (i == 0) {
                    SlideActivity.this.img_skip.setBackgroundResource(R.drawable.bgrskip);
                    SlideActivity.this.txt_bold.setText(R.string.txtslide1);
                    SlideActivity.this.txt_thin.setText(R.string.txtthinslide1);
                    SlideActivity.this.txt_next.setBackgroundResource(R.drawable.imgnextred);
                    SlideActivity.this.img_skip.setVisibility(View.VISIBLE);
                    SlideActivity.this.txt_read_more.setVisibility(View.GONE);
                    SlideActivity.this.txt_next.setText("");
                } else if (i == 1) {
                    SlideActivity.this.img_skip.setBackgroundResource(R.drawable.bgrskip);
                    SlideActivity.this.img_skip.setVisibility(View.VISIBLE);
                    SlideActivity.this.txt_bold.setText(R.string.txtslide2);
                    SlideActivity.this.txt_thin.setText(R.string.txtthinslide2);
                    SlideActivity.this.txt_next.setBackgroundResource(R.drawable.imgnextred);
                    SlideActivity.this.txt_read_more.setVisibility(View.GONE);
                    SlideActivity.this.txt_next.setText("");
                } else {
                    SlideActivity.this.txt_read_more.setVisibility(View.VISIBLE);
                    SlideActivity.this.img_skip.setBackgroundColor(0);
                    SlideActivity.this.txt_bold.setText(R.string.txtslide3);
                    SlideActivity.this.txt_thin.setText(R.string.txtthinslide3);
                    SlideActivity.this.txt_next.setText("Allow");
                    SlideActivity.this.txt_next.setBackgroundResource(R.drawable.slide_icon_bgr);
                }
            }
        });
        this.txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager2.getCurrentItem() == 0) {
                    viewPager2.setCurrentItem(1);
                } else if (viewPager2.getCurrentItem() == 1) {
                    viewPager2.setCurrentItem(2);
                } else {
                    SlideActivity.this.startActivity(new Intent(SlideActivity.this, SplashActivity.class));
                    SlideActivity.this.finish();
                }
            }
        });
        this.img_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager2.setCurrentItem(2);
            }
        });
        indicatorView.setSliderColor(getResources().getColor(R.color.white), getResources().getColor(R.color.indiacator));
        indicatorView.setSliderHeight(getResources().getDimension(R.dimen.dp_2));
        indicatorView.setSliderWidth(getResources().getDimension(R.dimen.dp_8));
        indicatorView.setSlideMode(0);
        indicatorView.setIndicatorStyle(0);
        indicatorView.setupWithViewPager(viewPager2);
        SpannableString spannableString = new SpannableString(getString(R.string.read_more));
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        this.txt_read_more.setText(spannableString);
        this.txt_read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://filemanagerfileexplorer.blogspot.com/2022/01/privacy-policy-for-file-explorer-my.html"));
                SlideActivity.this.startActivity(intent);
            }
        });
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            return Environment.isExternalStorageManager();
        }
        return ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
}
