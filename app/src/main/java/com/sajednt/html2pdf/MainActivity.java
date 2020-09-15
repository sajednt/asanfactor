package com.sajednt.html2pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sajednt.html2pdf.adapter.MyPagerAdapter;
import com.sajednt.html2pdf.function.Functions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    MyPagerAdapter pagerAdapter;
    ViewPager viewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar =  findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        isStoragePermissionGranted();

        Functions func = new Functions();

        func.createFolders(getApplicationContext());

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(2);



        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelected(true);
        navigation.setSelectedItemId(R.id.navigation_home);


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewpager.setCurrentItem(2,true);
                        break;
                    case R.id.navigation_list:
                        viewpager.setCurrentItem(1,true);
                        break;
                    case R.id.navigation_setting:
                        viewpager.setCurrentItem(0,true);
                        break;
                }
                return true;

            }
        });

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigation.getMenu().findItem(R.id.navigation_setting).setChecked(true);
                        break;
                    case 1:
                        navigation.getMenu().findItem(R.id.navigation_list).setChecked(true);
                        break;
                    case 2:
                        navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/faktor/");
////
////                HTML2PDF hp = new HTML2PDF(getApplicationContext());
////
////                hp.setMargins(new PrintAttributes.Margins(0,0,0,0));
////                hp.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
////                hp.fromUrl(getApplicationContext(), "http://sajednobakht.ir/factor/index.php", Uri.fromFile(folder), "s.pdf", new HTML2PDF.HTML2PDFListener() {
////                    @Override
////                    public void onConversionFinished(String s, boolean b) {
////                        Toast.makeText(getApplicationContext(),"ok", Toast.LENGTH_LONG);
////                    }
////
////                    @Override
////                    public void onConversionFailed(@Nullable String s) {
////                        Toast.makeText(getApplicationContext(),"no", Toast.LENGTH_LONG);
////
////                    }
////                });
//
////                PdfConverter converter = PdfConverter.Companion.getInstance();
////                File file = new File(Environment.getExternalStorageDirectory().toString(), "file.pdf");
////                String htmlString = LoadData("Report.html");
////                try {
////                    converter.convert(getApplicationContext(), htmlString, file, new PdfConverter.Companion.OnComplete() {
////                        @Override
////                        public void onWriteComplete() {
////
////                        }
////
////                        @Override
////                        public void onWriteFailed() {
////
////                        }
////                    });
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//
//
//            }
//        });
    }




    public String LoadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.commit();
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("tag", "Permission is granted");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Tag", "Permission is granted");
            return true;
        }
    }
}