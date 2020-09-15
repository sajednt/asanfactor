package com.sajednt.html2pdf.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

public class Functions {

    public void createFolders(Context context){

        File rootDataDir = context.getFilesDir();
        
        File dir = new File(rootDataDir, "image");
        if(!dir.exists()) {
            dir.mkdirs();
        }

        File dir1 = new File(rootDataDir, "report");
        if(!dir1.exists()) {
            dir1.mkdirs();
        }
    }

    public int getLastNumber(Context context){
        int number = 1001;
        File rootDataDir = new File(context.getFilesDir(), "report");

        File[] files = rootDataDir.listFiles();

        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                if(a.lastModified() > b.lastModified() )
                    return 1;
                if(a.lastModified() < b.lastModified() )
                    return -1;
                return 0;
            }});

        if(files.length < 1){

            return  number;

        }
        else{

            String sep = files[files.length-1].getName().replace(".pdf","");
            try {
                int x = Integer.parseInt(sep);
                return x+1;
            }
            catch (Exception e){

                return number;
            }
        }
    }

    public void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath );
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    public boolean storeImage(Bitmap imageData, String filename) {
        // get path to external storage (SD card)

        File sdIconStorageDir = null;

        sdIconStorageDir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Pictures/");
        // create storage directories, if they don't exist
        if (!sdIconStorageDir.exists()) {
            sdIconStorageDir.mkdirs();
        }
        try {
            String filePath = sdIconStorageDir.toString() + File.separator + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            //Toast.makeText(m_cont, "Image Saved at----" + filePath, Toast.LENGTH_LONG).show();
            // choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }
        return true;
    }

}
