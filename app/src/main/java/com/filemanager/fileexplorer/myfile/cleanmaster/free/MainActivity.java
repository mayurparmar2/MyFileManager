package com.filemanager.fileexplorer.myfile.cleanmaster.free;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.net.MailTo;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.demo.example.BuildConfig;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.LaucherActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.ResulfActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.tablayout.TabView;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Sharepre_Ulti;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.viewpagerAdapter.FragmentAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.viewpagerAdapter.ZoomOutPageTransformer;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.FilesFragment;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment.StoreFragment;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final String PRIMARY_VOLUME_NAME = "primary";
    private static final String TAG = "AAAA";
    public static Uri uri;
    private int countingsStars;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    FilesFragment filesFragment;
    private FragmentAdapter fragmentAdapter;
    private ImageView imageView_laucher;
    private ImageView img_opendrawer;
    private boolean isopen;
    private RelativeLayout r_feedback;
    private RelativeLayout r_lan;
    private RelativeLayout r_moreapp;
    private RelativeLayout r_rate;
    private RelativeLayout r_recycle;
    private RelativeLayout r_share;
    StoreFragment storeFragment;
    private TabView tabView;
    private Toolbar toolbar;
    private TextView txt_Files;
    private TextView txt_storage;
    ViewPager2 viewPager2;
    private String rateStatus = "DIALOG";
    private TabView[] tabViews = new TabView[2];
    boolean flag = false;

    private void bindView() {
        this.tabViews[0] = (TabView) findViewById(R.id.tabv_tab0);
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        this.toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        this.imageView_laucher = (ImageView) findViewById(R.id.lauchericon);
        this.img_opendrawer = (ImageView) findViewById(R.id.opend_drawer);
        this.r_recycle = (RelativeLayout) findViewById(R.id.r_recyclerbin);
        this.r_lan = (RelativeLayout) findViewById(R.id.r_lan);
        this.r_share = (RelativeLayout) findViewById(R.id.r_share);
        this.r_rate = (RelativeLayout) findViewById(R.id.r_rate);
        this.r_moreapp = (RelativeLayout) findViewById(R.id.r_moreapp);
        this.r_feedback = (RelativeLayout) findViewById(R.id.r_feed_back);
        this.drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawerToggle = actionBarDrawerToggle;
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_setting)).getBitmap();
        this.drawerToggle.setHomeAsUpIndicator(R.drawable.drawer_icon);
        this.drawerToggle.setDrawerIndicatorEnabled(false);
        this.drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                MainActivity.this.m67xd16d57cc(view);
            }
        });
        this.img_opendrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.isopen = !mainActivity.isopen;
                if (MainActivity.this.isopen) {
                    MainActivity.this.drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    MainActivity.this.drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        this.drawerLayout.addDrawerListener(this.drawerToggle);
        this.drawerToggle.syncState();
        this.txt_Files = (TextView) findViewById(R.id.txt_file);
        this.txt_storage = (TextView) findViewById(R.id.txt_storages);
        this.viewPager2 = (ViewPager2) findViewById(R.id.viewpager_activity);
        ArrayList arrayList = new ArrayList();
        arrayList.add("Storage");
        arrayList.add("File");
        this.fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        this.storeFragment = new StoreFragment();
        this.filesFragment = new FilesFragment();
        this.fragmentAdapter.addFrag(this.storeFragment);
        this.fragmentAdapter.addFrag(this.filesFragment);
        this.viewPager2.setAdapter(this.fragmentAdapter);
        this.viewPager2.setPageTransformer(new ZoomOutPageTransformer());
        bindView();
        this.tabViews[0].setOnTabSelectedListener(new TabView.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int i) {
                try {
                    MainActivity.this.viewPager2.setCurrentItem(i);
                } catch (Exception unused) {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.txt_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.viewPager2.setCurrentItem(0);
            }
        });
        this.txt_Files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.viewPager2.setCurrentItem(1);
            }
        });
        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                if (i == 0) {
                    MainActivity.this.tabViews[0].select(0, true);
                } else {
                    MainActivity.this.tabViews[0].select(i, true);
                }
            }

            @Override
            public void onPageScrolled(int i, float f, int i2) {
                super.onPageScrolled(i, f, i2);
            }
        });
        this.imageView_laucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, LaucherActivity.class));
                    }
                }.obtainMessage(111, "parameter").sendToTarget();
                Animatoo.animateSwipeLeft(MainActivity.this);
            }
        });
        createhide();
        createBin();
        createVideoFolder();
        this.r_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ResulfActivity.class);
                intent.putExtra("nameitem", "recycle");
                MainActivity.this.startActivity(intent);
                Animatoo.animateSwipeLeft(MainActivity.this);
            }
        });
        this.r_lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("https://filemanagerfileexplorer.blogspot.com/2022/01/privacy-policy-for-file-explorer-my.html"));
                MainActivity.this.startActivity(intent);
            }
        });
        this.r_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", "Remi Filemanager");
                    intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n");
                    MainActivity.this.startActivity(Intent.createChooser(intent, "choose one"));
                } catch (Exception unused) {
                }
            }
        });
        this.r_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.rateApp();
            }
        });
        this.r_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.composeEmail(new String[]{"tatcachilathuthach92@gmail.com"}, "File manager feedback");
            }
        });
        this.r_moreapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.moreApps();
            }
        });
    }


    public void m67xd16d57cc(View view) {
        if (this.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            this.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void rateApp() {
        try {
            startActivity(rateIntentForUrl("market://details"));
        } catch (ActivityNotFoundException unused) {
            startActivity(rateIntentForUrl("https://play.google.com/store/apps/details"));
        }
    }

    @SuppressLint("WrongConstant")
    private Intent rateIntentForUrl(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("%s?id=%s", str, getPackageName())));
        int i = Build.VERSION.SDK_INT;
        intent.addFlags(1208483840);
        return intent;
    }

    public void moreApps() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:REMI Studio")));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/search?q=pub:REMI Studio")));
        }
    }


    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.drawerToggle.onConfigurationChanged(configuration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.drawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }
        int itemId = menuItem.getItemId();
//        if (itemId == R.id.about) {
//            Toast.makeText(this, "About button selected", Toast.LENGTH_SHORT).show();
//            return true;
//        } else if (itemId == R.id.help) {
//            Toast.makeText(this, "Help button selected", Toast.LENGTH_SHORT).show();
//            return true;
//        } else if (itemId == R.id.search) {
//            Toast.makeText(this, "Search button selected", Toast.LENGTH_SHORT).show();
//            return true;
//        } else {
//            return super.onOptionsItemSelected(menuItem);
//        }
        return false;
    }

    private void createBin() {
        String store = getStore(this);
        File file = new File(store);
        File file2 = new File(store + "/Bin");
        if (!file.isDirectory()) {
            file.mkdir();
        }
        if (file2.isDirectory()) {
            return;
        }
        file2.mkdir();
    }

    private void createVideoFolder() {
        String store = getStore(this);
        File file = new File(store);
        File file2 = new File(store + "/Video");
        if (!file.isDirectory()) {
            file.mkdir();
        }
        if (file2.isDirectory()) {
            return;
        }
        file2.mkdir();
    }

    private void createhide() {
        String store = getStore(this);
        File file = new File(store);
        File file2 = new File(store + "/remiImagehide");
        if (!file.isDirectory()) {
            file.mkdir();
        }
        if (file2.isDirectory()) {
            return;
        }
        file2.mkdir();
    }

    public static String getStore(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            File externalFilesDir = context.getExternalFilesDir(null);
            if (externalFilesDir != null) {
                return externalFilesDir.getAbsolutePath();
            }
            return "/storage/emulated/0/Android/data/" + context.getPackageName();
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + context.getPackageName();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return;
            }
            outputStream.write(bArr, 0, read);
        }
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @Override
    public void onBackPressed() {
        dialogExit();
    }

    private void DialogLanguage() {
        new Sharepre_Ulti(this);
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.dialog_language);
        ((RelativeLayout) dialog.findViewById(R.id.r_vietnam)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ((RelativeLayout) dialog.findViewById(R.id.r_usuk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ((RelativeLayout) dialog.findViewById(R.id.r_korea)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ((RelativeLayout) dialog.findViewById(R.id.r_france)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ((TextView) dialog.findViewById(R.id.txt_dismiss)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void composeEmail(String[] strArr, String str) {
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
        intent.putExtra("android.intent.extra.EMAIL", strArr);
        intent.putExtra("android.intent.extra.SUBJECT", str);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void dialogExit() {
        final Sharepre_Ulti sharepre_Ulti = new Sharepre_Ulti(this);
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setContentView(R.layout.diaolog_exit);
        TextView textView = (TextView) dialog.findViewById(R.id.cancel);
        TextView textView2 = (TextView) dialog.findViewById(R.id.exit);
        TextView textView3 = (TextView) dialog.findViewById(R.id.rate);
        int readSharedPrefsInt = sharepre_Ulti.readSharedPrefsInt("checkrate", 0);
        if (readSharedPrefsInt != 0) {
            textView3.setVisibility(View.GONE);
        } else {
            textView3.setVisibility(View.VISIBLE);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = MainActivity.this;
                mainActivity.ratePkg(mainActivity, mainActivity.getPackageName());
                sharepre_Ulti.writeSharedPrefs("checkrate", 1);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void ratePkg(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + str));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
