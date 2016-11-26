package com.lchli.angithub.common.netApi;

import com.lchli.angithub.common.netApi.callbacks.OkCallback;
import com.lchli.angithub.common.utils.UniversalLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by lchli on 2016/4/25.
 */
public class FileClient {


    private static final int CONNECT_TIMEOUT = 10_000;
    private static final int READ_TIMEOUT = 30_000;
    private static final int DOWNLOAD_BUF_SIZE = 2048;
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);
    public static final boolean DEBUG = true;
    private static final String tag = FileClient.class.getSimpleName();
    private static OkHttpClient okHttpClient = OKClientProvider.getHttpClientBuilder().build();
    private static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");


    public interface DownloadFileCallback {

        void onProgress(final int totalBytes, final int finishedBytes);

        void onFail(final Exception e);

        void onSuccess();
    }

    public abstract class FileUploadCallback<T> extends OkCallback<T> {

        abstract void onRequestProgress(long bytesWritten, long contentLength);

    }

    public static class CancelableRunnable implements Runnable {

        private final AtomicBoolean isUserCancel = new AtomicBoolean(false);
        private DownloadFileCallback callback;
        private String urlPath;
        private String saveFilePath;

        public CancelableRunnable(DownloadFileCallback callback, String urlPath, String saveFilePath) {
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
            UniversalLog.get().e(msg);
        }
    }


    public static CancelableRunnable downloadFile(String urlPath, String saveFilePath,
                                                  DownloadFileCallback callback) {

        log(String.format("urlPath:%s\nsaveFilePath:%s", urlPath, saveFilePath));

        CancelableRunnable cancelableRunnable = new CancelableRunnable(callback, urlPath, saveFilePath);
        executor.execute(cancelableRunnable);
        return cancelableRunnable;
    }


    private static Call uploadFileImpl(final String url, Map<String, String> textParams,
                                       Map<String, File> fileParams, final FileUploadCallback callback, String filesParamName) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (!isNullOrEmpty(textParams)) {
            Set<Map.Entry<String, String>> set = textParams.entrySet();
            for (Map.Entry<String, String> entry : set) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }

        }

        if (!isNullOrEmpty(fileParams)) {
            Set<Map.Entry<String, File>> set = fileParams.entrySet();
            for (Map.Entry<String, File> entry : set) {
                builder.addFormDataPart(filesParamName != null ? filesParamName : entry.getKey(), entry.getValue().getName(),
                        RequestBody.create(MEDIA_TYPE_STREAM, entry.getValue()));
            }

        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
                    @Override
                    public void onRequestProgress(final long bytesWritten, final long contentLength) {
                        if (callback.isCallbackInUiThread()) {

                            callback.runInUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onRequestProgress(bytesWritten, contentLength);
                                }
                            });

                        } else {
                            callback.onRequestProgress(bytesWritten, contentLength);
                        }

                    }
                }))
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);

        return call;

    }

    private static Call uploadFile(final String url, Map<String, String> textParams,
                                   Map<String, File> fileParams, final FileUploadCallback callback) {

        return uploadFileImpl(url, textParams, fileParams, callback, null);

    }

    public static Call uploadFile(final String url, Map<String, String> textParams,
                                  List<File> files, String filesParamName, final FileUploadCallback callback) {
        Map<String, File> fileParams = new HashMap<>();
        if (!isEmptyList(files)) {
            for (int i = 0; i < files.size(); i++) {
                fileParams.put(i + "", files.get(i));
            }
        }

        return uploadFileImpl(url, textParams, fileParams, callback, filesParamName);
    }


    private static boolean isNullOrEmpty(Map map) {
        return map == null || map.isEmpty();
    }


    private static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * Decorates an OkHttp request body to count the number of bytes written when writing it. Can
     * decorate any request body, but is most useful for tracking the upload progress of large
     * multipart requests.
     *
     * @author Leo Nikkilä
     */
    private static class CountingRequestBody extends RequestBody {

        protected RequestBody delegate;
        protected Listener listener;

        protected CountingSink countingSink;

        public CountingRequestBody(RequestBody delegate, Listener listener) {
            this.delegate = delegate;
            this.listener = listener;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            try {
                return delegate.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {

            countingSink = new CountingSink(sink);
            BufferedSink bufferedSink = Okio.buffer(countingSink);

            delegate.writeTo(bufferedSink);

            bufferedSink.flush();
        }

        private final class CountingSink extends ForwardingSink {

            private long bytesWritten = 0;

            public CountingSink(Sink delegate) {
                super(delegate);
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                bytesWritten += byteCount;
                listener.onRequestProgress(bytesWritten, contentLength());
            }

        }

        private interface Listener {
            void onRequestProgress(long bytesWritten, long contentLength);
        }

    }


}
