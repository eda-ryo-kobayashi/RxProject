package kobayashi.com.rxproject;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kobayashi.com.rxproject.databinding.ActivityLoginBinding;

/**
 * Created by ryo on 2017/03/19.
 *
 * ログイン画面
 */

public class LoginActivity extends AppCompatActivity {

    private CompositeDisposable _disposables;
    private ActivityLoginBinding _binding;

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupObservables();
    }

    @Override
    protected void onPause() {
        releaseObservables();
        super.onPause();
    }

    private void setupObservables() {
        _disposables = new CompositeDisposable();

        // ログイン
        Disposable login = RxView.clicks(_binding.login)
            .doOnNext(unused -> {
                if(validateInputs()) {
                    MainActivity.start(this);
                    finish();
                } else {
                    releaseObservables();
                    setupObservables();
                }
            })
            .subscribe();

        // ユーザー名とパスワードの入力チェック
        Observable<Boolean> inputUserName = RxTextView.textChanges(_binding.userName)
            .map(input -> input.length() > 0);
        Observable<Boolean> inputPassword = RxTextView.textChanges(_binding.password)
            .map(input -> input.length() > 0);
        // 正常な入力ならLoginボタン有効化
        Disposable validateLogin = Observable
            .combineLatest(inputUserName, inputPassword,
                (userName, password) -> userName && password)
            .subscribe(RxView.enabled(_binding.login));

        _disposables.addAll(login, validateLogin);
    }

    private void releaseObservables() {
        if(_disposables == null || _disposables.isDisposed()) return;
        _disposables.dispose();
    }

    private boolean validateInputs() {
        return true;
    }
}
