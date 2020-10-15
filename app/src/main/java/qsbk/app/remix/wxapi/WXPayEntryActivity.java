package qsbk.app.remix.wxapi;

import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by chandler on 16/4/12.
 */
public class WXPayEntryActivity extends qsbk.app.pay.wxapi.WXPayEntryActivity implements IWXAPIEventHandler {

    private static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 发起支付请求前需要更新微信支付APP_ID
        // PayConstants.WECHAT_APP_ID = "wechat_app_id";
    }

    @Override
    public void onResp(BaseResp resp) {
        // 处理自己的微信支付响应
    }

}