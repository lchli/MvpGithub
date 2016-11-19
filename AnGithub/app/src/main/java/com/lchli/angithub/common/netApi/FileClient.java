package com.lchli.angithub.common.netApi;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.request.RequestCall;
import com.zhy.http.okhttp.utils.Exceptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.http.PUT;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by lchli on 2016/4/25.
 */
public class FileClient {

    static {
        OkHttpUtils.initClient(OKClientProvider.getHttpClientBuilder().build());
    }

    private static final int CONNECT_TIMEOUT = 10_000;
    private static final int READ_TIMEOUT = 30_000;
    private static final int DOWNLOAD_BUF_SIZE = 2048;
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);
    private static final boolean DEBUG = true;
    private static final String tag = FileClient.class.getSimpleName();


    public interface TransportFileCallback {

        void onProgress(final int totalBytes, final int finishedBytes);

        void onFail(final Exception e);

        void onSuccess();
    }

    public static class CancelableRunnable implements Runnable {

        private final AtomicBoolean isUserCancel = new AtomicBoolean(false);
        private TransportFileCallback callback;
        private String urlPath;
        private String saveFilePath;

        public CancelableRunnable(TransportFileCallback callback, String urlPath, String saveFilePath) {
            this.callback = callback;
            this.urlPath = urlPath;
            this.saveFilePath = saveFilePath;
        }

        public void cancel() {
            isUserCancel.set(true);
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            FileOutputStream fos = null;
            Exception exception = null;
            try {

                File saveFile = new File(saveFilePath);

                File dir = saveFile.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }

                fos = new FileOutputStream(saveFile);

                URL url = new URL(urlPath);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setDoInput(true);

                final int totalLength = conn.getContentLength();

                inputStream = conn.getInputStream();
                final byte[] buffer = new byte[DOWNLOAD_BUF_SIZE];
                int len;
                int finished = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    if (isUserCancel.get()) {
                        break;
                    }
                    fos.write(buffer, 0, len);
                    finished += len;
                    if (callback != null) {
                        callback.onProgress(totalLength, finished);
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                exception = e;

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (callback != null) {
                    if (exception != null) {
                        callback.onFail(exception);
                        log("download fail.");
                    } else if (!isUserCancel.get()) {
                        callback.onSuccess();
                        log("download success.");
                    } else {
                        log("download cancel.");
                    }
                }
            }
        }
    }

    private static void log(String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }


    public static CancelableRunnable downloadFile(String urlPath, String saveFilePath, TransportFileCallback callback) {

        log(String.format("urlPath:%s\nsaveFilePath:%s", urlPath, saveFilePath));

        CancelableRunnable cancelableRunnable = new CancelableRunnable(callback, urlPath, saveFilePath);
        executor.execute(cancelableRunnable);
        return cancelableRunnable;
    }


    public static RequestCall uploadFile(final String url, Map<String, String> textParams,
                                         Map<String, File> fileParams, Callback callback) {

        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (!isNullOrEmpty(fileParams)) {
            Set<Map.Entry<String, File>> entrySet = fileParams.entrySet();
            for (Map.Entry<String, File> entry : entrySet) {
                builder.addFile(entry.getKey(), entry.getValue().getName(), entry.getValue());
            }
        }

        RequestCall call = builder.params(textParams).build();
        call.execute(callback);
        return call;
    }

    public static RequestCall uploadFile(final String url, Map<String, String> textParams,
                                         List<File> fileParams, String filesParamName, Callback callback) {

        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (!isEmptyList(fileParams)) {
            for (File entry : fileParams) {
                builder.addFile(filesParamName, entry.getName(), entry);
            }
        }
        RequestCall call = builder.params(textParams).build();
        call.execute(callback);
        return call;
    }


    private static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }


    private static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }


}
