package as.bwei.com.reglogin.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import as.bwei.com.reglogin.bean.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HP on 2018/10/13.
 */

public class AccountModel {
    private OkHttpClient client;

    //创建方法供p层调用
    public AccountModel(){
        client = new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .build();
    }

    public void reg(String name,String password,final AccountCallback callback){
        FormBody formBody = new FormBody
                .Builder()
                .add("mobile",name)
                .add("password",password)
                .build();
        Request builder = new Request
                .Builder()
                .url("https://www.zhaoapi.cn/user/reg")
                .post(formBody)
                .build();
        client.newCall(builder).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onSuccess(response.body().string());
            }
        });
    }
    public void login(String name,String password,final AccountCallback callback) {
        FormBody formBody = new FormBody
                .Builder()
                .add("mobile",name)
                .add("password",password)
                .build();
        Request builder = new Request
                .Builder()
                .url("https://www.zhaoapi.cn/user/login")
                .post(formBody)
                .build();
        client.newCall(builder).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code()==200){
                    String result = response.body().string();
                    Gson gson = new Gson();
                    User user = gson.fromJson(result, User.class);
                    String code = user.getCode();
                    callback.onSuccess(code);
                }
            }
        });
    }

    public interface AccountCallback {
        void onSuccess(String msg);
        void onError(String errorMsg);
    }
}
