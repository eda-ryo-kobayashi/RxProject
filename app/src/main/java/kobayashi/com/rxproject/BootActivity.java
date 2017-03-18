package kobayashi.com.rxproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ryo on 2017/03/18.
 *
 * 起動アクティビティ
 */

public class BootActivity extends AppCompatActivity {

    private Disposable _disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 時間経過で遷移する
        // Single = onSuccess | onError のどちらかが一回だけ呼ばれる
//        _disposable = Single.timer(2, TimeUnit.SECONDS, Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(aLong -> {
//                Timber.d("Thread : %s", Thread.currentThread().getName());
//                LoginActivity.start(this);
//                finish();
//            });
        // Completable = onComplete | onError 値は流されない
        _disposable = Completable.timer(2, TimeUnit.SECONDS, Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> {
                Timber.d("Thread : %s", Thread.currentThread().getName());
                LoginActivity.start(this);
                finish();
            });
    }

    @Override
    protected void onPause() {
        if(!_disposable.isDisposed()) {
            _disposable.dispose();
        }
        super.onPause();
    }
}
