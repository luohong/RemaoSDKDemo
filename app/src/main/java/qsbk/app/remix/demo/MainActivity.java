package qsbk.app.remix.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import qsbk.app.core.net.Callback;
import qsbk.app.core.net.response.BaseResponse;
import qsbk.app.core.utils.ToastUtil;
import qsbk.app.remao.sdk.LiveFragment;
import qsbk.app.remao.sdk.RemaoSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RemaoSDK.changeToTest();

        RemaoSDK.login("test", "123", new Callback() {
            @Override
            public void onSuccess(BaseResponse data) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new LiveFragment()).commitNow();
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtil.Short(msg);
            }
        });

//        RemaoSDK.getLiveList(new Callback() {
//            @Override
//            public void onSuccess(BaseResponse data) {
//                List<CommonVideo> list = data.getListResponse("lives", new TypeToken<List<CommonVideo>>() {
//                });
//                if (list != null) {
//                    // 业务处理
//                }
//            }
//
//            @Override
//            public void onFailed(int code, String msg) {
//
//            }
//        });

    }
}