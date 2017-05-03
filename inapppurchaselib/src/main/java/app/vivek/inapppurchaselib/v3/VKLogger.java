package app.vivek.inapppurchaselib.v3;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This is used for maintain the log values.
 *
 * @author Vivek Kumar
 */
public class VKLogger {

	private static String TAG = "";
	public static boolean logstatus = true;

	private VKLogger() {
	}

	public static void setLogStatus(boolean isShow){
		logstatus=isShow;
	}
	public static void setTag(String tagValue){
		TAG=tagValue;
	}

	public static void w(final String TAG,final String s) {
		if (logstatus) {
			Log.w(TAG, ":->" + s);
		}
	}
	public static void w(final String s) {
		if (logstatus) {
			Log.w(TAG, ":->" + s);
		}
	}
	public static void warn(final String s) {
		if (logstatus) {
			Log.w(TAG, ":->" + s);
		}
	}


	public static void i(final String TAG,final String s) {
		if (logstatus) {
			Log.i(TAG, ":->" + s);
		}
	}
	public static void i(final String s) {
		if (logstatus) {
			Log.i(TAG, ":->" + s);
		}
	}

	public static void info(final String s) {
		if (logstatus) {
			Log.i(TAG, ":->" + s);
		}
	}

	public static void info(final String s, final Throwable throwable) {
		if (logstatus) {
			Log.i(TAG, s, throwable);
		}
	}

	public static void e(final String TAG,final String s) {
		if (logstatus) {
			Log.e(TAG, ":->" + s);
		}
	}

	public static void e(final String s) {
		if (logstatus) {
			Log.e(TAG, ":->" + s);
		}
	}

	public static void error(final String s) {
		if (logstatus) {
			Log.e(TAG, ":->" + s);
		}
	}

	public static void error(final String tag, final String s) {
		if (logstatus) {
			Log.e(tag, ":->" + s);
		}
	}


	public static void error(final Throwable throwable) {
		if (logstatus) {
			Log.e(TAG, null, throwable);
		}
	}

	public static void error(final String s, final Throwable throwable) {
		if (logstatus) {
			Log.e(TAG, s, throwable);
		}
	}


	public static void d(final String TAG,final String s) {
		if (logstatus) {
			Log.w(TAG, ":->" + s);
		}
	}
	public static void d(final String s) {
		if (logstatus) {
			Log.w(TAG, ":->" + s);
		}
	}
	public static void debug(final String s) {
		if (logstatus) {
			Log.w(TAG, ":->" + s);
		}
	}


    public static void writeLogInSdcard(final boolean isAppend,final String text){
        new Thread(){
            public void run() {
                try {
                    File myFile = new File(Environment.getExternalStorageDirectory()+"/inappPurchase.txt");
                    //BufferedWriter for performance, true to set append to file flag
                    BufferedWriter buf = new BufferedWriter(new FileWriter(myFile, true));
                    buf.append(text);
                    buf.newLine();
                    buf.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();

    }
}
