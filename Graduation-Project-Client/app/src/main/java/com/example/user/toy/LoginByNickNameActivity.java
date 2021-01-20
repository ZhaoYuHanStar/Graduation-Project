package com.example.user.toy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginByNickNameActivity extends AppCompatActivity {

    //用户信息
    private EditText nickname;
    private EditText password;
    //记住密码和自动登录
    private SharedPreferences sharedPreferences = null;
    private CheckBox automatic = null;
    private CheckBox remember = null;
    private TextView copyright = null;
    private ImageView symbol = null;
    private Gson gson;
    private Intent intentFrom;
    private Intent toIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_login_nickname);
        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);
        init();
        setCopyRight();
        isAutoLogin();
    }

    //初始化控件
    public void init() {
        intentFrom = this.getIntent();
        gson = new Gson();
        symbol = findViewById(R.id.symbol);
        nickname = findViewById(R.id.et_loginNickname);
        password = findViewById(R.id.et_loginPassword);
        automatic = findViewById(R.id.automatic);
        remember = findViewById(R.id.remember);
        copyright = findViewById(R.id.copyright);
    }

    //判断来源activity
    public void checkActivity(){
        String activityFrom = intentFrom.getStringExtra("activityFrom");
        toIntent = new Intent(LoginByNickNameActivity.this,activityFrom.getClass());
    }
    //点击事件
    public void onClickMain(View v) {
        switch (v.getId()) {
            //登录
            case R.id.tv_login:
                if (nickname.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(LoginByNickNameActivity.this, "请输入完整的账户信息", Toast.LENGTH_SHORT).show();
                } else {
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute(" "+"loginbn/"+nickname.getText().toString()+"/"+password.getText().toString());
                }
                break;
            //手机号登录
            case R.id.btn_login_phone:
                Intent toPhone = new Intent(LoginByNickNameActivity.this, LoginByPhoneActivity.class);
                startActivity(toPhone);
                break;
            //注册
            case R.id.tv_register:
                Intent toRegister = new Intent(LoginByNickNameActivity.this, RegisterActivity.class);
                startActivity(toRegister);
                break;
            //忘记密码
            case R.id.tv_find_psw:
                Intent toFind = new Intent(LoginByNickNameActivity.this, FindPasswordActivity.class);
                startActivity(toFind);
                break;


        }

    }

    //异步任务
    //2.账号密码登录
    public class LoginTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //用post方式传递数据
                httpURLConnection.setRequestMethod("GET");
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
            if (result == "1"){
                Toast.makeText(LoginByNickNameActivity.this, "密码错误!", Toast.LENGTH_SHORT).show();
            }else if (result == "0"){
                Toast.makeText(LoginByNickNameActivity.this, "账号不存在，请先注册！", Toast.LENGTH_SHORT);
                toIntent = new Intent(LoginByNickNameActivity.this,RegisterActivity.class);
                startActivity(toIntent);
            }else {
                Log.e("----------", "账号密码登录");
                /**
                 * 这需要判断是哪个页面触发的intent，再跳到那个页面
                 */
                startActivity(toIntent);
                Toast.makeText(LoginByNickNameActivity.this, "登录成功！", Toast.LENGTH_SHORT);
                //在需要写入数据的地方创建Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", result);
                if (automatic.isChecked()) {
                    //保存自动登录信息
                    editor.putBoolean("isAuto", true);//自动登录框
                    editor.putString("nickname", nickname.getText().toString());
                    editor.putString("password", password.getText().toString());
                } else {
                    editor.putBoolean("isAuto", false);
                    editor.remove("password");
                }
                if (remember.isChecked()) {
                    //保存记住密码
                    editor.putString("password", password.getText().toString());
                    editor.putString("nickname", nickname.getText().toString());
                    editor.putBoolean("isRemember", true);//记住密码
                } else {
                    editor.putBoolean("isRemember", false);
                    editor.remove("password");
                }
                //put并没有真正生效，必须commit
                editor.commit();
            }
            super.onPostExecute(result);
        }
    }

    //自动登录&&记住密码
    //读取文件（保存的一系列数据），实现记住密码自动登录
    private void isAutoLogin() {
        boolean isAuto = sharedPreferences.getBoolean("isAuto", false);
        boolean isRemember = sharedPreferences.getBoolean("isRemember", false);
        if (isAuto) {
            String nicknameAuto = sharedPreferences.getString("nickname", "");
            String passwordAuto = sharedPreferences.getString("password", "");
            nickname.setText(nicknameAuto);
            password.setText(passwordAuto);
            automatic.setChecked(true);

            //模拟请求服务器端登录成功
            Intent intent = new Intent(LoginByNickNameActivity.this, TabHostActivity.class);
            startActivity(intent);
            finish();
        }

        if (isRemember) {
            String nicknameAuto = sharedPreferences.getString("nickname", "");
            String passwordAuto = sharedPreferences.getString("password", "");
            nickname.setText(nicknameAuto);
            password.setText(passwordAuto);
            remember.setChecked(true);
        }
    }

    //设置版本号
    public void setCopyRight() {
        String str = "简餐@2020 All Service Reserved";
        copyright.setTextSize(15);
        copyright.setText(Html.fromHtml(str));
    }

}
