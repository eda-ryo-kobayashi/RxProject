package kobayashi.com.rxproject;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by ryo on 2017/03/18.
 *
 * プロジェクト固有のアプリケーションクラス
 */

public class AppRxProject extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
