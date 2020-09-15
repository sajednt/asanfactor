package com.sajednt.html2pdf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.sajednt.html2pdf.function.Functions;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class ReportViewer extends AppCompatActivity {
    Toolbar mToolbar;
    Functions func;
    PDFView pdfView;
    Bitmap bit;
    Button btnSave, btnShare;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_viewer);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        func = new Functions();

        final File file = (File) getIntent().getExtras().get("fileName");


        btnSave = findViewById(R.id.btnsave);
        pdfView = findViewById(R.id.pdfview);
        btnShare = findViewById(R.id.btnshare);
        pdfView.fromFile(file).onLoad(new OnLoadCompleteListener() {
            @Override
            public void loadComplete(int nbPages) {

            }
        }).load();


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File copied = new File(Environment.getExternalStorageDirectory(), "/factor/reports/"+file.getName());
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(copied));
                intent.setType("application/pdf");
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                InputStream inStream = null;
                try {
//                    inStream = new FileInputStream(file);
//                    bit = renderToBitmap(getApplicationContext(), inStream);

                    int x = 1500;
                    int y = 2020;
                    Rect rect = new Rect(0, 0, x, y);

                    bit = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
                    bit.setHasAlpha(true);
                    Canvas canvas = new Canvas(bit);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(bit, 0, 0, null);

                    PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
                    renderer.openPage(0).render(bit, rect, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);


                } catch (Exception ex) {

                } finally {

                }
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //Creates app specific folder
                File outputFile = new File( path , file.getName().replace(".pdf","")+".jpg");

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(outputFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bit.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                Toast.makeText(ReportViewer.this, "در گالری شما ذخیره شد", Toast.LENGTH_LONG).show();

            }
        });



    }

    public void createFolder(){
        File factorDir = new File (Environment.getExternalStorageDirectory() , "factor");
        if(!factorDir.exists()){
            factorDir.mkdirs();
        }
        File reportDir = new File(factorDir, "reports");
        if(!reportDir.exists()){
            reportDir.mkdirs();
        }
    }

    private void writeTextFileToSdcard(File fileObj, String fileName) {

        fileObj.mkdirs();
        File file = new File(fileObj, fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(out);
            myOutWriter.close();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sharePDF(String path) {

        createFolder();

        File pdf = new File(path);
        if (pdf.exists()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdf));
            intent.setType("application/pdf");
            startActivity(intent);
        } else {
            Log.i("DEBUG", "File doesn't exist");
        }
    }

}
