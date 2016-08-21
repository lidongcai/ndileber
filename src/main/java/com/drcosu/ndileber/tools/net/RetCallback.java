package com.drcosu.ndileber.tools.net;

import com.drcosu.ndileber.app.SApplication;
import com.drcosu.ndileber.tools.HJson;
import com.drcosu.ndileber.tools.UDialog;
import com.orhanobut.logger.Logger;

import org.apache.http.HttpException;

import java.net.ConnectException;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.internal.framed.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shidawei on 16/8/5.
 */
public abstract class RetCallback<T> implements Callback<T>{

    //出错提示
    public static String networkMsg = "网络请求失败,请检查";
    public static String parseMsg;
    public static String unknownMsg;

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    protected abstract void onSuccess(Call<T> call, Response<T> response);

    protected abstract void failure(Call<T> call, Throwable throwable);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        RetLog.log(call);
        onSuccess(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        RetLog.log(call);
        Throwable t = throwable;
        while (t != null) {
            if (t instanceof ConnectException) {
                UDialog.alert(UDialog.DIALOG_ERROR,networkMsg).show();
            }
            t = t.getCause();
        }
        failure(call,throwable);
    }

    public void log(Call<T> call){

        if(SApplication.netLog){

            StringBuilder sb = new StringBuilder("");
            Request request = call.request();
//            Headers headers = request.headers();
//            Set<String> name = headers.names();
//            for(String n : name){
//                sb.append("\t{").append(n).append(" = ").append(headers.get(n)).append("}");
//            }
            sb.append("请求URL:"+request.url()+" 请求METHOD:"+request.method()+" 请求是否是https:"+request.isHttps());
            Logger.d(sb.toString());
        }
    }



}
