package com.lchli.angithub;


import com.lchli.angithub.common.utils.UniversalLog;

/**
 * Created by lchli on 2016/8/14.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    // private.private static final String EXCEPTION_DIR = String.format("%s/%s", LocalConst.STUDY_APP_ROOT_DIR, "Exception");
    //private.private static final String RECENT_EXCEPTION_FILE = String.format("%s/%s", EXCEPTION_DIR, "RecentException.txt");

    static {
        // FileUtils.mkdirs(new File(EXCEPTION_DIR));
        // ExtFileUtils.makeFile(RECENT_EXCEPTION_FILE);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        logException(ex);
    }

    public static void logException(Throwable ex) {
        UniversalLog.get().e(ex);

//        try {
//            PrintWriter printWriterFile = new PrintWriter(new File(RECENT_EXCEPTION_FILE));
//            ex.printStackTrace(printWriterFile);
//            printWriterFile.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            LogUtils.e("app exception log save fail.");
//        }
    }
}
