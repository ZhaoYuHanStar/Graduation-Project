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
import android.text.InputFilter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.toy.personal.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity {
    private EditText phoneNum;
    private EditText name;
    private EditText password;
    private EditText myCode;
    private TextView service;
    private RadioButton sex1;
    private RadioButton sex2;
    private RadioButton age1;
    private RadioButton age2;
    private RadioButton age3;
    private RadioButton age4;
    private String result = null;
    private SharedPreferences sharedPreferences = null;
    private Gson gson;
    private User user;
    private String userStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        //初始化控件
        init();
        //监听密码框，确认密码输入位数
        password.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);

    }

    public void init() {
        name = findViewById(R.id.et_regname);
        password = findViewById(R.id.et_regpsw);
        phoneNum = findViewById(R.id.et_telephone);
        service = findViewById(R.id.tv_registerService);
        myCode = findViewById(R.id.et_code);
        gson = new GsonBuilder().serializeNulls() .create();

        sex1 = findViewById(R.id.rb_id1);
        sex2 = findViewById(R.id.rb_id2);
        age1 = findViewById(R.id.rb_age1);
        age2 = findViewById(R.id.rb_age2);
        age3 = findViewById(R.id.rb_age3);
        age4 = findViewById(R.id.rb_age4);

    }

    public void onClickRegister(View v) {
        switch (v.getId()) {
            //发送验证码
            case R.id.tv_registerSendCode:
                SMSSDK.getVerificationCode("86", phoneNum.getText().toString());
                break;

            // 提交验证码，其中的code表示验证码，如“1357”
            case R.id.bt_register:
                SMSSDK.submitVerificationCode("86", phoneNum.getText().toString(), myCode.getText().toString());
                break;

            //服务条款
            case R.id.tv_registerService:
                Intent toService = new Intent(RegisterActivity.this, ServiceWord.class);
                startActivity(toService);
                break;

        }
    }

    //必须同时包含大小写字母及数字
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
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
                                Toast.makeText(RegisterActivity.this, "验证码已发送!", Toast.LENGTH_SHORT).show();
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(RegisterActivity.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理验证码验证通过的结果
                                if (name.getText().toString().equals("") || password.getText().toString().equals("")) {
                                    Toast.makeText(RegisterActivity.this, "请输入完整的信息!", Toast.LENGTH_SHORT).show();

                                } else if (! (password.length() >5 && password.length()<13)) {
                                    Toast.makeText(RegisterActivity.this, "密码须为6-12位", Toast.LENGTH_SHORT);
                                } else if (!isContainAll(password.getText().toString())) {
                                    Toast.makeText(RegisterActivity.this, "密码必须同时包含大小写字母及数字", Toast.LENGTH_SHORT).show();
                                } else {
                                    user = new User();
                                    user.setName(name.getText().toString());
                                    user.setPassword(password.getText().toString());
                                    user.setPhone(phoneNum.getText().toString());
                                    if (sex1.isChecked()){
                                        user.setSex("男");
                                    }
                                    if (sex2.isChecked()){
                                        user.setSex("女");
                                    }
                                    if (age1.isChecked()){
                                        user.setAge(1);
                                    }
                                    if (age2.isChecked()){
                                        user.setAge(2);
                                    }
                                    if (age3.isChecked()){
                                        user.setAge(3);
                                    }
                                    if (age4.isChecked()){
                                        user.setAge(4);
                                    }
                                    userStr = gson.toJson(user);

                                    RegisterTask registerTask = new RegisterTask();
                                    registerTask.execute(""+"register/"+userStr);
                                }
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(RegisterActivity.this, "验证码不正确！", Toast.LENGTH_SHORT).show();

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

    //验证码验证成功后，使用异步任务将用户信息发送给服务器端
    public class RegisterTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //用post方式传递数据
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream is = httpURLConnection.getInputStream();
                byte[] btr = new byte[1024];
                int len;

                while ((len = is.read(btr)) != -1) {
                    result = new String(btr, 0, len);
                }
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            switch (result) {
                //注册成功
                case "1":
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", userStr);
                    Toast.makeText(RegisterActivity.this, "注册成功，返回登录！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginByNickNameActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                //注册失败
                case "0":
                    Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                    break;


            }
            super.onPostExecute(result);
        }
    }

    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}




