/*
 * Created on 11/15/17.
 * Written by Islam Salah with assistance from members of Blink22.com
 */

package android.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

/**
 * Converts HTML to PDF.
 * <p>
 * Can convert only one task at a time, any requests to do more conversions before
 * ending the current task are ignored.
 */
public class PdfConverter implements Runnable {

    private static final String TAG = "PdfConverter";
    private static PdfConverter sInstance;
    private MyCustomObjectListener listener;
    private Context mContext;
    private String mHtmlString;
    private File mPdfFile;
    private PrintAttributes mPdfPrintAttrs;
    private boolean mIsCurrentlyConverting;
    private WebView mWebView;

    private PdfConverter() {
        this.listener = null;
    }

    public static synchronized PdfConverter getInstance() {
        if (sInstance == null)
            sInstance = new PdfConverter();

        return sInstance;
    }



    public interface MyCustomObjectListener {

        public void onObjectReady(String bitmap);

    }

    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        mWebView = new WebView(mContext);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
                    throw new RuntimeException("call requires API level 19");
                else {
                    PrintDocumentAdapter documentAdapter = mWebView.createPrintDocumentAdapter();
                    documentAdapter.onLayout(null, getPdfPrintAttrs(), null, new PrintDocumentAdapter.LayoutResultCallback() {}, null);
                    documentAdapter.onWrite(new PageRange[]{PageRange.ALL_PAGES}, getOutputFileDescriptor(), null, new PrintDocumentAdapter.WriteResultCallback() {
                        @Override
                        public void onWriteFinished(PageRange[] pages) {

                            listener.onObjectReady("tamam");

                            destroy();
                        }
                    });


                }
            }

        });

        mWebView.loadDataWithBaseURL("file:///android_asset/" ,mHtmlString, "text/HTML", "UTF-8" , null);
    }



    public PrintAttributes getPdfPrintAttrs() {
        return mPdfPrintAttrs != null ? mPdfPrintAttrs : getDefaultPrintAttrs();
    }

    public void setPdfPrintAttrs(PrintAttributes printAttrs) {
        this.mPdfPrintAttrs = printAttrs;
    }

    public void convert(Context context, String htmlString, File file) {
        if (context == null)
            throw new IllegalArgumentException("context can't be null");
        if (htmlString == null)
            throw new IllegalArgumentException("htmlString can't be null");
        if (file == null)
            throw new IllegalArgumentException("file can't be null");

        if (mIsCurrentlyConverting)
            return;

        mContext = context;
        mHtmlString = htmlString;
        mPdfFile = file;
        mIsCurrentlyConverting = true;
        runOnUiThread(this);
    }

    private ParcelFileDescriptor getOutputFileDescriptor() {
        try {
            mPdfFile.createNewFile();
            return ParcelFileDescriptor.open(mPdfFile, ParcelFileDescriptor.MODE_TRUNCATE | ParcelFileDescriptor.MODE_READ_WRITE);
        } catch (Exception e) {
            Log.d(TAG, "Failed to open ParcelFileDescriptor", e);
        }
        return null;
    }

    private PrintAttributes getDefaultPrintAttrs() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null;

        return new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setResolution(new PrintAttributes.Resolution("RESOLUTION_ID", "RESOLUTION_ID", 300, 300))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

    }

    private void runOnUiThread(Runnable runnable) {
        Handler handler = new Handler(mContext.getMainLooper());
        handler.post(runnable);
        
    }

    private void destroy() {
        mContext = null;
        mHtmlString = null;
        mPdfFile = null;
        mPdfPrintAttrs = null;
        mIsCurrentlyConverting = false;
        mWebView = null;
    }


}