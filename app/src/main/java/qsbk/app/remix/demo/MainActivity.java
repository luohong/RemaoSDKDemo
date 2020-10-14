package qsbk.app.remix.demo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import qsbk.app.core.net.Callback;
import qsbk.app.core.net.response.BaseResponse;
import qsbk.app.core.utils.ToastUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RemaoSDK.changeToTest();

        RemaoSDK.login("test", "123", new Callback(){
            @Override
            public void onSuccess(BaseResponse data) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new QkkLiveListFragment()).commitNow();
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtil.Short(msg);
            }
        });

    }
}