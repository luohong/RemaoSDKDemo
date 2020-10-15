## 热猫直播SDK集成文档

### 1.添加依赖
Gradle编译环境（AndroidStudio）
- 拷贝RemaoSDK.aar到App的lib目录下
- 在App级别的build.gradle引入RemaoSDK.aar和其他包（易于控制版本和去重）

> compile(name: 'RemaoSDK', ext: 'aar')
> or
> implementation fileTree(dir: 'libs', include: ['*.jar','*.aar'])

``` 
api 'androidx.legacy:legacy-support-v4:1.0.0'
api 'androidx.appcompat:appcompat:1.2.0'
api 'androidx.recyclerview:recyclerview:1.1.0'
api 'com.r0adkll:slidableactivity:2.0.6'
api 'com.android.volley:volley:1.1.1'
api 'com.facebook.fresco:fresco:2.3.0'
api 'com.facebook.fresco:animated-base:2.3.0'
api 'com.facebook.fresco:animated-gif:2.3.0'
api 'com.facebook.fresco:animated-webp:2.3.0'
api 'com.google.code.gson:gson:2.8.6'
api 'cz.msebera.android:httpclient:4.4.1.2'
api 'org.msgpack:jackson-dataformat-msgpack:0.8.20'
api 'com.qiniu:qiniu-android-sdk:7.3.15'
api 'com.squareup.okhttp3:okhttp:3.12.11' // 警告：更新版本以后导致Android4.x手机出现SSL ExceptionInInitializerError（启动闪退）
api 'com.facebook.flipper:flipper-network-plugin:0.51.0'
api 'com.kaopiz:kprogresshud:1.2.0'
api 'com.liulishuo.okdownload:okdownload:1.0.7'
api 'com.liulishuo.okdownload:okhttp:1.0.7'
api 'com.airbnb.android:lottie:3.4.1'
api 'androidx.constraintlayout:constraintlayout:1.1.3'
api 'com.google.android.material:material:1.2.0'
api 'androidx.gridlayout:gridlayout:1.0.0'
api 'com.qiniu:happy-dns:0.2.11'
api 'com.plattysoft.leonids:LeonidsLib:1.3.2'
api 'com.github.yyued:SVGAPlayer-Android:2.5.3'
api 'org.adw.library:discrete-seekbar:1.0.1'
api 'com.contrarywind:Android-PickerView:4.1.9'
api 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:5.5.3'

api 'com.alibaba:arouter-api:1.5.0'
annotationProcessor 'com.alibaba:arouter-compiler:1.2.2'
```

### 2.初始化SDK
> RemaoSDK.init(Context context);

### 3.登录
- RemaoSDK.login(String token, String openId, Callback callback)

```java
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
```

### 4.接入微信支付
- 将 WXPayEntryActivity extends qsbk.app.pay.wxapi.WXPayEntryActivity 
- 发起微信支付前需要设置APP_ID. PayConstants.WECHAT_APP_ID = "wechat_app_id";
- 在 WXPayEntryActivity 的 handleResp 方法处理自己的微信支付响应
- 删除支付宝和微信的jar包（alipaySdk-20180601.jar，libammsdk.jar）


### 5.代码混淆
如果你启用了混淆，请在你的proguard-rules.pro中加入如下代码：

```
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontoptimize
-ignorewarnings

-keep class qsbk.app.** {
    *;
}
-dontwarn qsbk.app.**


-keepattributes SourceFile,LineNumberTable

# filedownloader uses okhttp3-lib, so need add below proguard rules.
-dontwarn okhttp3.*
-dontwarn okio.**
-dontwarn javax.annotation.**

-dontwarn com.liulishuo.okdownload.**
-keep class com.liulishuo.okdownload.**{*;}

#################
-keep class com.squareup.** { *; }
-dontwarn com.squareup.**
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn **.R$*
-dontwarn **.R
-dontwarn com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.android.volley.** { *; }
-keep class cz.msebera.android.** { *; }
-keep class org.adw.library.** { *; }
-keep class org.greenrobot.eventbus.** { *; }

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

## agora
-keep class io.agora.** {*;}

## msgpack
-keep class com.fasterxml.jackson.** {*;}
-keep class org.msgpack.** {*;}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }

##---------------End: proguard configuration for Gson  ----------

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * implements java.io.Serializable {*;}

## Facebook fresco
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class android.support.**{*;}
-keep class com.android.support.**{*;}


-keep public class * extends android.view.View
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}

# MTA
-keep class com.tencent.stat.** {*;}
-keep class com.tencent.mid.** {*;}
-keep class org.json.** {*;}
```


