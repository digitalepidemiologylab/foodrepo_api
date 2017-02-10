package com.salathelab.openfoodapidemo;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.salathelab.openfoodapidemo.async.ProductsDownloader;

public class MainActivity extends AppCompatActivity {
    private EditText txtKey;
    private Button btnToggleDownload;
    private TextView txtResult;
    private TextView lblStatus;
    private RelativeLayout lytMain;
    private ScrollView scrlTxtResult;
    private CheckBox chkScrollContent;

    boolean debugOutput = true;
    boolean downloaderActive;
    ProductsDownloader productsDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lytMain = (RelativeLayout)findViewById(R.id.activity_main);
        txtKey = (EditText)findViewById(R.id.txt_key);
        btnToggleDownload = (Button)findViewById(R.id.btn_toggle_download);
        scrlTxtResult = (ScrollView)findViewById(R.id.scroller_for_txt_result);
        chkScrollContent = (CheckBox)findViewById(R.id.chk_scroll_content);
        txtResult = (TextView)findViewById(R.id.txt_result);
        txtResult = (TextView)findViewById(R.id.txt_result);
        lblStatus = (TextView)findViewById(R.id.lbl_status);

        btnToggleDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocus();
                if (downloaderActive) {
                    stopDownload();
                } else {
                    startDownload();
                }
                toggleDownloaderActive();
            }
        });

        chkScrollContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    scrollResultsToBottom(false);
                }
            }
        });
    }

    private void startDownload() {
        txtResult.setText("");
        productsDownloader = new ProductsDownloader();
        ProductsDownloader.DownloaderParamsStart params = new ProductsDownloader.DownloaderParamsStart(
                txtKey.getText().toString(),
                new ProductsDownloader.ProgressCallback() {
                    @Override
                    public void onStatusChanged(CharSequence status) {
                        lblStatus.setText(status);
                        if (debugOutput) {
                            SpannableString s = new SpannableString("    " + status);
                            s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            s.setSpan(new RelativeSizeSpan(0.7f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            s.setSpan(new ForegroundColorSpan(0xFFFF0000), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            s.setSpan(new BackgroundColorSpan(0x20FFA500), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            appendResultLine(s);
                        }
                    }
                    @Override
                    public void onDataAdded(CharSequence data) {
                        appendResultLine(data);
                    }
                },
                new ProductsDownloader.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        lblStatus.setText("Idle");
                        toggleDownloaderActive();
                    }
                },
                new ProductsDownloader.CancelCallback() {
                    @Override
                    public void onCancel() {
                        lblStatus.setText("Idle");
                    }
                }
        );
        productsDownloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    private void stopDownload() {
        if (productsDownloader != null) {
            productsDownloader.cancel(true);
            productsDownloader = null;
        }
    }

    private void toggleDownloaderActive() {
        downloaderActive = !downloaderActive;
        btnToggleDownload.setText(downloaderActive ? "Stop" : "Run");
    }

    private void clearFocus() {
        lytMain.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lytMain.getWindowToken(), 0);
    }

    private void appendResultLine(CharSequence s) {
        txtResult.append(s);
        txtResult.append("\n");

        if (chkScrollContent.isChecked()) {
            scrollResultsToBottom(true);
        }
    }

    private void scrollResultsToBottom(boolean afterPost) {
        if (afterPost) {
            // this will wait one frame before scrolling.
            // Useful if we know we want to scroll on the same frame that we are adding content.
            scrlTxtResult.post(new Runnable() {
                @Override
                public void run() {
                    scrlTxtResult.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        } else {
            scrlTxtResult.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
