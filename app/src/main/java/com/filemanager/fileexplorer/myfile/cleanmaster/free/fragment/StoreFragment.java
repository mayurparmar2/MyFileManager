package com.filemanager.fileexplorer.myfile.cleanmaster.free.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.chad.library.adapter.base.animation.ScaleInAnimation;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.DocumnetActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.LookActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.ResulfActivity;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Activity.customview.CircularProgressIndicator;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.DTO.File_DTO;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Iinterface.CallbackFav;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.MainActivity;
import com.demo.example.R;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Data_Manager;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Image_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.MusicUltil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.Ultil.Video_Ultil;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Favourite_Adapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.MusicAdapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.adapter.Recent_Adapter;
import com.filemanager.fileexplorer.myfile.cleanmaster.free.database.FavoritSongs;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class StoreFragment extends Fragment implements Recent_Adapter.OnClickItem {
    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double d) {
            long j;
            long j2;
            if (StoreFragment.curent != 0) {
                if (StoreFragment.curent == 1) {
                    j2 = StoreFragment.totalspace;
                } else {
                    j = 0;
                    return String.valueOf(j) + "%";
                }
            } else {
                j2 = StoreFragment.getTotalInternalMemorySize().longValue();
            }
            j = (long) ((d / j2) * 100.0d);
            return String.valueOf(j) + "%";
        }
    };
    private static int curent;
    private static long totalfree;
    private static long totalspace;
    private CircularProgressIndicator circularProgressIndicator;
    private Favourite_Adapter favourite_adapter;
    private ArrayList<File_DTO> file_dtos_recent;
    private LinearLayout l_favourite;
    private LinearLayout l_hide;
    private LinearLayout l_recent;
    private LinearLayout l_recycle;
    private ActivityResultLauncher<IntentSenderRequest> launcher;
    private LinearLayout linearLayout_Document;
    private LinearLayout linearLayout_apk;
    private LinearLayout linearLayout_app;
    private LinearLayout linearLayout_dowload;
    private LinearLayout linearLayout_img;
    private LinearLayout linearLayout_music;
    private LinearLayout linearLayout_newfiles;
    private LinearLayout linearLayout_video;
    private LinearLayout linearLayout_zip;
    private MusicAdapter musicAdapter;
    private Recent_Adapter recent_adapter;
    private RecyclerView recyclerView;
    private Spinner spinner_storage;
    private TextView t_count_document;
    private TextView t_count_dowload;
    private TextView t_count_fav;
    private TextView t_countapk;
    private TextView t_countapp;
    private TextView t_countimg;
    private TextView t_countmusic;
    private TextView t_countrecycle;
    private TextView t_countzip;
    private TextView t_coutvidep;
    private TextView t_newfile;
    private TextView textView_space;
    private TextView textView_used;
    private int tt_zip = 0;
    private TextView txt_viewallfile;
    private Ultil ultil;

    private void setcountfilefolder() {
    }

    @Override
    public void itemclickFr(int i) {
    }

    @Override
    public void menuclickFr(int i) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_store, viewGroup, false);
        this.spinner_storage = (Spinner) inflate.findViewById(R.id.spiner);
        this.textView_space = (TextView) inflate.findViewById(R.id.count_tutolspace);
        this.textView_used = (TextView) inflate.findViewById(R.id.count_used);
        this.linearLayout_music = (LinearLayout) inflate.findViewById(R.id.l_music);
        this.linearLayout_img = (LinearLayout) inflate.findViewById(R.id.l_image);
        this.linearLayout_video = (LinearLayout) inflate.findViewById(R.id.l_video);
        this.linearLayout_Document = (LinearLayout) inflate.findViewById(R.id.l_document);
        this.linearLayout_app = (LinearLayout) inflate.findViewById(R.id.l_apps);
        this.linearLayout_dowload = (LinearLayout) inflate.findViewById(R.id.l_dowloaded);
        this.linearLayout_apk = (LinearLayout) inflate.findViewById(R.id.l_APKs);
        this.linearLayout_zip = (LinearLayout) inflate.findViewById(R.id.l_zip);
        this.linearLayout_newfiles = (LinearLayout) inflate.findViewById(R.id.l_newfile);
        this.l_favourite = (LinearLayout) inflate.findViewById(R.id.l_favaorite);
        this.l_hide = (LinearLayout) inflate.findViewById(R.id.l_hide);
        this.l_recycle = (LinearLayout) inflate.findViewById(R.id.l_recycle);
        CircularProgressIndicator circularProgressIndicator = (CircularProgressIndicator) inflate.findViewById(R.id.circular_progress);
        this.circularProgressIndicator = circularProgressIndicator;
        circularProgressIndicator.setTextSize1((int) dpToPx(50.0f));
        this.circularProgressIndicator.setAnimationEnabled(true);
        this.t_count_dowload = (TextView) inflate.findViewById(R.id.txt_countdowload);
        this.t_countrecycle = (TextView) inflate.findViewById(R.id.txt_countrecycle);
        this.t_countimg = (TextView) inflate.findViewById(R.id.t_countImage);
        this.t_coutvidep = (TextView) inflate.findViewById(R.id.txt_countvideo);
        this.t_countmusic = (TextView) inflate.findViewById(R.id.txt_countmusic);
        this.t_count_document = (TextView) inflate.findViewById(R.id.txt_count_document);
        this.t_countzip = (TextView) inflate.findViewById(R.id.txt_countzip);
        this.t_countapp = (TextView) inflate.findViewById(R.id.txt_countapp);
        this.t_countapk = (TextView) inflate.findViewById(R.id.txt_countapkfile);
        this.t_newfile = (TextView) inflate.findViewById(R.id.txt_countnewfile);
        this.t_count_fav = (TextView) inflate.findViewById(R.id.t_count_fav);
        this.txt_viewallfile = (TextView) inflate.findViewById(R.id.txt_viewall_recycle);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recycleview_recent);
        Ultil ultil = new Ultil(getContext());
        this.ultil = ultil;
        this.file_dtos_recent = ultil.getallfileRecent();
        CreateSpiner();
        setcountfilefolder();
        setmemory(curent);
        this.spinner_storage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                int unused = StoreFragment.curent = i;
                StoreFragment.this.setmemory(i);
            }
        });
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getResultCode() == -1) {
                    Toast.makeText(StoreFragment.this.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.linearLayout_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "music");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.linearLayout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "images");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.linearLayout_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "video");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.linearLayout_Document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreFragment.this.getActivity().startActivity(new Intent(StoreFragment.this.getContext(), DocumnetActivity.class));
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        externalMemoryAvailable(getActivity());
        this.linearLayout_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "app");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.linearLayout_dowload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "dowload");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.linearLayout_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "zip");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.linearLayout_apk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "apk");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.linearLayout_newfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "newfile");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.l_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "fav");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.l_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "recycle");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.l_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreFragment.this.getActivity().startActivity(new Intent(StoreFragment.this.getContext(), LookActivity.class));
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        this.txt_viewallfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreFragment.this.getContext(), ResulfActivity.class);
                intent.putExtra("nameitem", "recent");
                StoreFragment.this.getActivity().startActivity(intent);
                Animatoo.animateSwipeLeft(StoreFragment.this.getContext());
            }
        });
        RecentView();
        CallbackFav.getInstance().setListener(new CallbackFav.OnCustomStateListener() {
            @Override
            public void stateChanged() {
            }
        });
        return inflate;
    }

    public static float dpToPx(float f) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, Resources.getSystem().getDisplayMetrics());
    }

    private void RecentView() {
        new Ultil(getContext()).checkrecent_fileExist();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
        Recent_Adapter recent_Adapter = new Recent_Adapter(getContext(), this);
        this.recent_adapter = recent_Adapter;
        recent_Adapter.setList(this.file_dtos_recent);
        this.recent_adapter.setAdapterAnimation(new ScaleInAnimation());
        this.recyclerView.setAdapter(this.recent_adapter);
    }

    public static boolean externalMemoryAvailable(Activity activity) {
        File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(activity, null);
        if (externalFilesDirs.length > 1 && externalFilesDirs[0] != null && externalFilesDirs[1] != null) {
            totalspace = externalFilesDirs[1].getTotalSpace();
            totalfree = externalFilesDirs[1].getFreeSpace();
            return true;
        }
        Log.d("TAG", "externalMemoryAvailable: fal");
        return false;
    }

    @SuppressLint("ResourceType")
    private void CreateSpiner() {
        String[] strArr;
        if (externalMemoryAvailable(getActivity())) {
            strArr = new String[]{"Internal", "External Storage"};
        } else {
            strArr = new String[]{"Internal"};
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), (int) R.layout.customspiner, strArr);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinner_storage.setAdapter((SpinnerAdapter) arrayAdapter);
        this.spinner_storage.setSelection(0);
    }


    public void setmemory(int i) {
        if (i == 0) {
            this.textView_space.setText(bytesToHuman(getTotalInternalMemorySize().longValue() - getAvailableInternalMemorySize().longValue()));
            this.textView_used.setText(bytesToHuman(getTotalInternalMemorySize().longValue()));
            this.circularProgressIndicator.setProgress(getTotalInternalMemorySize().longValue() - getAvailableInternalMemorySize().longValue(), getTotalInternalMemorySize().longValue());
            this.circularProgressIndicator.setAnimationEnabled(true);
            this.circularProgressIndicator.setProgressTextAdapter(TIME_TEXT_ADAPTER);
        } else if (i == 1) {
            this.textView_space.setText(bytesToHuman(totalspace - totalfree));
            this.textView_used.setText(bytesToHuman(totalspace));
            CircularProgressIndicator circularProgressIndicator = this.circularProgressIndicator;
            long j = totalspace;
            circularProgressIndicator.setProgress(j - totalfree, j);
            this.circularProgressIndicator.setAnimationEnabled(true);
            this.circularProgressIndicator.setProgressTextAdapter(TIME_TEXT_ADAPTER);
        }
    }


    public ArrayList<File_DTO> setRecylerView() {
        ArrayList<File_DTO> arrayList = new ArrayList<>();
        File file = new File(MainActivity.getStore(getContext()) + "/Bin");
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                String absolutePath = listFiles[i].getAbsolutePath();
                listFiles[i].lastModified();
                listFiles[i].getName().substring(listFiles[i].getName().lastIndexOf("%") + 1);
                listFiles[i].length();
                File_DTO file_DTO = new File_DTO();
                file_DTO.setPath(absolutePath);
                arrayList.add(file_DTO);
            }
        }
        return arrayList;
    }

    public static Long getAvailableInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return Long.valueOf(statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
    }

    public static Long getTotalInternalMemorySize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return Long.valueOf(statFs.getBlockCountLong() * statFs.getBlockSizeLong());
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static String bytesToHuman(long j) {
        int i = (j > 1024 ? 1 : (j == 1024 ? 0 : -1));
        if (i < 0) {
            return floatForm(j) + " byte";
        } else if (i >= 0 && j < 1048576) {
            return floatForm(j / ((double) 1024)) + " Kb";
        } else if (j >= 1048576 && j < 1073741824) {
            return floatForm(j / 1048576) + " Mb";
        } else if (j >= 1073741824 && j < 1099511627776L) {
            return floatForm(j / 1073741824) + " Gb";
        } else if (j >= 1099511627776L && j < 1125899906842624L) {
            return floatForm(j / 1099511627776L) + " Tb";
        } else if (j >= 1125899906842624L && j < 1152921504606846976L) {
            return floatForm(j / 1125899906842624L) + " Pb";
        } else if (j >= 1152921504606846976L) {
            return floatForm(j / 1152921504606846976L) + " Eb";
        } else {
            return "???";
        }
    }

    private void getramsize() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(memoryInfo);
        long j = memoryInfo.totalMem;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Updaterecent().execute(new Void[0]);
    }


    public class Updaterecent extends AsyncTask<Void, Integer, String> {
        Data_Manager data_manager;
        int tt_apk_;
        int tt_app;
        int tt_doc;
        int tt_dowload;
        int tt_image;
        int tt_music;
        int tt_re;
        int tt_video;

        public Updaterecent() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.data_manager = new Data_Manager(StoreFragment.this.getContext());
        }


        @Override
        public String doInBackground(Void... voidArr) {
            new Ultil(StoreFragment.this.getContext()).checkrecent_fileExist();
            this.tt_image = new Image_Ultil(StoreFragment.this.getActivity()).allImage().size();
            this.tt_video = new Video_Ultil(StoreFragment.this.getContext()).getallvideo().size();
            this.tt_music = new MusicUltil(StoreFragment.this.getContext()).allsong().size();
            this.data_manager.setDocs();
            this.tt_doc = this.data_manager.getfilepdf().size() + this.data_manager.getfidocx().size() + this.data_manager.getfileppt().size() + this.data_manager.getfiletxt().size() + this.data_manager.getfilexls().size();
            this.tt_app = this.data_manager.readAllAppssss(StoreFragment.this.getContext()).size();
            this.tt_apk_ = this.data_manager.getallapp().size();
            this.tt_dowload = this.data_manager.filesDowload().size();
            this.tt_re = StoreFragment.this.setRecylerView().size();
            StoreFragment.this.tt_zip = this.data_manager.getzipwithMediastore().size();
            if (StoreFragment.this.recent_adapter != null) {
                StoreFragment storeFragment = StoreFragment.this;
                storeFragment.file_dtos_recent = storeFragment.ultil.getallfileRecent();
                return "aa";
            }
            return "aa";
        }


        @Override
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            FavoritSongs favoritSongs = new FavoritSongs(StoreFragment.this.getContext());
            favoritSongs.open();
            int size = favoritSongs.getAllRows().size();
            favoritSongs.close();
            TextView textView = StoreFragment.this.t_countimg;
            textView.setText("" + this.tt_image);
            TextView textView2 = StoreFragment.this.t_coutvidep;
            textView2.setText("" + this.tt_video);
            TextView textView3 = StoreFragment.this.t_countmusic;
            textView3.setText("" + this.tt_music);
            TextView textView4 = StoreFragment.this.t_count_document;
            textView4.setText("" + this.tt_doc);
            TextView textView5 = StoreFragment.this.t_countzip;
            textView5.setText("" + StoreFragment.this.tt_zip);
            TextView textView6 = StoreFragment.this.t_countapp;
            textView6.setText("" + this.tt_app);
            TextView textView7 = StoreFragment.this.t_countapk;
            textView7.setText("" + this.tt_apk_);
            TextView textView8 = StoreFragment.this.t_count_dowload;
            textView8.setText("" + this.tt_dowload);
            TextView textView9 = StoreFragment.this.t_count_fav;
            textView9.setText("" + size);
            TextView textView10 = StoreFragment.this.t_countrecycle;
            textView10.setText("" + this.tt_re);
            TextView textView11 = StoreFragment.this.t_newfile;
            textView11.setText("" + this.data_manager.getallfileindecive().size());
            new Ultil(StoreFragment.this.getContext()).checkrecent_fileExist();
            StoreFragment.this.recent_adapter.setList(StoreFragment.this.file_dtos_recent);
            StoreFragment.this.recent_adapter.notifyDataSetChanged();
        }
    }
}
