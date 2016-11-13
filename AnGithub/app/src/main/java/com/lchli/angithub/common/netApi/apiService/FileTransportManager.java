package com.lchli.angithub.common.netApi.apiService;

import com.lchli.angithub.common.netApi.RestClient;
import com.lchli.angithub.common.utils.Preconditions;
import com.lchli.angithub.common.utils.UniversalLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lchli on 2016/11/13.
 */

public class FileTransportManager {



  public interface FileTransportService {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

  }

  public interface FileDownloadCallback {
    void onProgress(long downloaded, long total);

    void onError(Throwable e);

    void onSuccess();
  }



  public static Subscription download(final String fileUrl, final File fileSaveLocation,
      final FileDownloadCallback callback) {
    Preconditions.checkNotNull(fileUrl, "fileUrl cannot be null.");
    Preconditions.checkNotNull(fileSaveLocation, "fileSaveLocation cannot be null.");
    File dir = new File(fileSaveLocation.getParent());
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        if (callback != null) {
          callback.onError(new Exception("fileSaveLocation create fail."));
        }
        return null;
      }
    }
    if (!fileSaveLocation.exists()) {
      try {
        fileSaveLocation.createNewFile();
      } catch (IOException e) {
        UniversalLog.get().e(e);

        if (callback != null) {
          callback.onError(e);
        }
        return null;
      }
    }

    FileTransportService fileTransportService =
        RestClient.instance().createService(FileTransportService.class);

    return fileTransportService.downloadFile(fileUrl)
        .map(new Func1<ResponseBody, Throwable>() {
          @Override
          public Throwable call(ResponseBody responseBody) {
            if (responseBody != null) {
              return writeResponseBodyToDisk(responseBody, fileSaveLocation, callback);
            }
            return new Exception("responseBody is null.");
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Throwable>() {
          @Override
          public void onCompleted() {

      }

          @Override
          public void onError(Throwable e) {
            UniversalLog.get().e(e);
            if (callback != null) {
              callback.onError(e);
            }
          }

          @Override
          public void onNext(Throwable e) {
            if (callback != null) {
              if (e == null) {
                UniversalLog.get().e("file download success.");
                callback.onSuccess();
              } else {
                callback.onError(e);
              }
            }
          }
        });
  }

  private static Throwable writeResponseBodyToDisk(ResponseBody body, File fileSaveLocation,
      FileDownloadCallback callback) {
    try {
      InputStream inputStream = null;
      OutputStream outputStream = null;
      try {
        byte[] fileReader = new byte[4096];

        long fileSize = body.contentLength();
        long fileSizeDownloaded = 0;

        inputStream = body.byteStream();
        outputStream = new FileOutputStream(fileSaveLocation);

        while (true) {
          int read = inputStream.read(fileReader);

          if (read == -1) {
            break;
          }

          outputStream.write(fileReader, 0, read);

          fileSizeDownloaded += read;
          if (callback != null) {
            callback.onProgress(fileSizeDownloaded, fileSize);
          }
          UniversalLog.get().e("file download: " + fileSizeDownloaded + " of " + fileSize);
        }

        outputStream.flush();

        return null;
      } catch (IOException e) {
        UniversalLog.get().e(e);
        return e;
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }

        if (outputStream != null) {
          outputStream.close();
        }
      }
    } catch (IOException e) {
      UniversalLog.get().e(e);
      return e;
    }
  }



}
