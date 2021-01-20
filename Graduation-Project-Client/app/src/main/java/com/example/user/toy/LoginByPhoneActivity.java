package com.example.user.toy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginByPhoneActivity extends AppCompatActivity {

    private EditText phonenum;
    private EditText myCode;
    private String result;
    private ImageView symbol;
    private TextView toRegister;
    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        init();
        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        showTip();
        SMSSDK.registerEventHandler(eventHandler);
    }

    //控件初始化
    public void init() {
        phonenum = findViewById(R.id.et_user_phone);
        myCode = findViewById(R.id.et_phonecode);
        symbol = findViewById(R.id.symbol);
        toRegister = findViewById(R.id.tv_toRegister);
    }


    public void onClickPhone(View v) {
        switch (v.getId()) {
            //发送验证码
            case R.id.et_sendPhoneCode:
                if (phonenum.getText().toString().equals("")) {
                    Toast.makeText(LoginByPhoneActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();

                } else {
                    SMSSDK.getVerificationCode("86", phonenum.getText().toString());
                }

                break;

            // 提交验证码，其中的code表示验证码，如“1357”
            case R.id.btn_loginByPhone:
                if (phonenum.getText().toString().equals("") || myCode.getText().toString().equals("")) {
                    Toast.makeText(LoginByPhoneActivity.this, "请输入必要信息", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("86", phonenum.getText().toString(), myCode.getText().toString());
                }

                break;

            //到注册页面
            case R.id.tv_toRegister:
                Intent intent = new Intent(LoginByPhoneActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }

    //验证码事件处理线程
    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;

            new android.os.Handler(Looper.getMainLooper(), new android.os.Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    try {
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理成功得到验证码的结果
                                // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                                Toast.makeText(LoginByPhoneActivity.this, "验证码已发送!", Toast.LENGTH_SHORT).show();
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(LoginByPhoneActivity.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证码验证通过的结果
                                //发送json格式的字符串
                                LoginByPhoneTask loginByPhoneTask = new LoginByPhoneTask();
                                loginByPhoneTask.execute( ""+"loginbp/"+phonenum);
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(LoginByPhoneActivity.this, "验证码不正确！", Toast.LENGTH_SHORT).show();

                            }
                        }
                    } catch (Exception e) {
                        //解决在子线程中调用Toast的异常情况处理
                        Looper.prepare();
                        Looper.loop();
                    }
                    return false;
                }
            }).sendMessage(msg);
        }
    };

    //异步任务
    //手机号登录
    public class LoginByPhoneTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //用post方式传递数据
                httpURLConnection.setRequestMethod("POST");
                //发送数据到服务器端
                OutputStream os = httpURLConnection.getOutputStream();
                os.write("手机号登录".getBytes());

                //接收服务器端发回的数据
                InputStream is = httpURLConnection.getInputStream();
                byte[] btr = new byte[1024];
                int len;

                while ((len = is.read(btr)) != -1) {
                    result = new String(btr, 0, len);
                }
                is.close();
                os.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == "0"){
                Toast.makeText(LoginByPhoneActivity.this, "账号不存在，请先注册！", Toast.LENGTH_SHORT);
            }else{
                Log.e("--------", "手机号登录");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", result);
                Intent intent = new Intent(LoginByPhoneActivity.this, TabHostActivity.class);
                startActivity(intent);
            }
            super.onPostExecute(result);
        }
    }

    //显示温馨提示
    public void showTip(){

        String str = "温馨提示：如您未注册，请先<font color='#01b091'>注册</font>";
        toRegister.setTextSize(15);
        toRegister.setText(Html.fromHtml(str));
    }
}

