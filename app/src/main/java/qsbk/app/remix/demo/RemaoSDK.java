package qsbk.app.remix.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.reflect.TypeToken;
import com.qiushibaike.statsdk.StatSDK;
import com.tencent.stat.StatConfig;

import java.util.HashMap;
import java.util.Map;

import qsbk.app.core.model.CommonVideo;
import qsbk.app.core.model.User;
import qsbk.app.core.net.Callback;
import qsbk.app.core.net.NetRequest;
import qsbk.app.core.net.UrlConstants;
import qsbk.app.core.net.response.BaseResponse;
import qsbk.app.core.provider.ImageProvider;
import qsbk.app.core.provider.UserInfoProvider;
import qsbk.app.core.utils.AppUtils;
import qsbk.app.core.utils.Constants;
import qsbk.app.core.utils.DeviceUtils;
import qsbk.app.core.utils.ToastUtil;
import qsbk.app.live.utils.DomainUtils;

public class RemaoSDK {

    public static void init(Context context) {
//        UrlConstants.init();

        // Fresco初始化
        Fresco.initialize(context);

        String channel = "qkk";

        AppUtils.init(context, Constants.SOURCE_REMIX, Constants.SOURCE_REMIX, "go!live!", channel);
        AppUtils.getInstance().setUserInfoProvider(new RemaoUserInfoProvider());
        AppUtils.getInstance().setImageProvider(new ImageProvider());

//        NetRequest.setSalt("boomclap!");

        // 初始化Qsbk统计SDK
        String deviceId = DeviceUtils.getDeviceId(true);
        StatSDK.init("remix001", context);
        StatSDK.setAppInfo(DeviceUtils.getAppVersion(), channel, deviceId);

        // 初始化腾讯移动统计服务
        StatConfig.setEnableStatService(true);
        StatConfig.setNativeCrashDebugEnable(false);
        StatConfig.setDebugEnable(BuildConfig.DEBUG);
        StatConfig.setInstallChannel(channel);
        StatConfig.setAppKey(context, "ADH564IPLT5H");
        com.tencent.stat.StatService.registerActivityLifecycleCallbacks((Application) context.getApplicationContext());

//        // 登陆时调用
//        if (provider != null) {
//            StatMultiAccount account = new StatMultiAccount(StatMultiAccount.AccountType.CUSTOM, String.valueOf(provider.getUserId()));
//            long time = System.currentTimeMillis() / 1000;
//            // 登陆时间，单秒为秒
//            account.setLastTimeSec(time);
//            com.tencent.stat.StatService.reportMultiAccount(context, account);
//        }

        // 啵啵不设置默认域名地址
//        DomainUtils.setDefaultUrl(BoboUrlConstants.DOMAIN_CONF);
        DomainUtils.refreshDomain();
    }

    public static void doChangeEnv() {
        String remixHost = UrlConstants.getApiDomain();
        if (remixHost.equals(UrlConstants.API_DOMAIN)) {
            changeToTest();
        } else {
            changeToProduction();
        }
    }

    public static void changeToProduction() {
        UrlConstants.setLiveDomain(UrlConstants.LIVE_DOMAIN);
        UrlConstants.setApiDomain(UrlConstants.API_DOMAIN);
        UrlConstants.setPayDomain(UrlConstants.PAY_DOMAIN);

        Map<String, Pair<String, String>> extensionMap = UrlConstants.getExtensionDomain();
        for (String domainKey : extensionMap.keySet()) {
            String domainUrl = extensionMap.get(domainKey).first;
            UrlConstants.setExtensionDomainCache(domainKey, domainUrl);
        }

        ToastUtil.Short("切到正式环境");
    }

    public static void changeToTest() {
        UrlConstants.setApiDomain(UrlConstants.TEST_DOMAIN);
        UrlConstants.setLiveDomain(UrlConstants.LIVE_TEST_DOMAIN);
        UrlConstants.setPayDomain(UrlConstants.PAY_TEST_DOMAIN);

        Map<String, Pair<String, String>> extensionMap = UrlConstants.getExtensionDomain();
        for (String domainKey : extensionMap.keySet()) {
            String domainUrl = extensionMap.get(domainKey).second;
            UrlConstants.setExtensionDomainCache(domainKey, domainUrl);
        }

        ToastUtil.Short("切到测试环境");
    }

    public static void login(final String token, final String openId, final Callback callback) {
        NetRequest.getInstance().post(UrlConstants.SIGNIN, new Callback() {
            @Override
            public void onPreExecute() {
                if (callback != null) {
                    callback.onPreExecute();
                }
            }

            @Override
            public void onSuccess(BaseResponse data) {
                User user = data.getResponse("user", new TypeToken<User>() {
                });
                String token = data.getSimpleDataStr("token");
                AppUtils.getInstance().getUserInfoProvider().setUser(user);
                AppUtils.getInstance().getUserInfoProvider().setToken(token);

                if (callback != null) {
                    callback.onSuccess(data);
                }
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = null;
                if (callback != null) {
                    params = callback.getParams();
                }
                if (params == null) {
                    params = new HashMap<>();
                }
                params.put("sns", "qkk");
                params.put("token", token);
                if (!TextUtils.isEmpty(openId)) {
                    params.put("openId", openId);
                }
                return params;
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                if (callback != null) {
                    callback.onFailed(code, msg);
                }
            }
        });
    }

    private static class RemaoUserInfoProvider extends UserInfoProvider {

        private User user;
        private String token;

        @Override
        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public User getUser() {
            return user;
        }

        @Override
        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String getToken() {
            return token;
        }

        @Override
        public void setUserSig(String userSig) {

        }

        @Override
        public String getUserSig() {
            return null;
        }

        @Override
        public void setCertificate(long certificate) {

        }

        @Override
        public long getCertificate() {
            return 0;
        }

        @Override
        public long getPackageCoin() {
            return 0;
        }

        @Override
        public long getBalance() {
            return 0;
        }

        @Override
        public void setBalance(long balance, long package_coin) {

        }

        @Override
        public boolean isLogin() {
            return !TextUtils.isEmpty(token) && user != null && user.getOriginId() > 0;
        }

        @Override
        public void toLogin(Activity activity, int requestCode) {
            // 登录操作
        }

        @Override
        public void onLogout(String message) {
            // 退出登录操作
        }

        @Override
        public void toUserPage(Activity activity, User user) {

        }

        @Override
        public void toShare(Activity activity, CommonVideo video, String from, int assignSns) {

        }

        @Override
        public void toMain(Activity activity, String tab) {

        }
    }

}
