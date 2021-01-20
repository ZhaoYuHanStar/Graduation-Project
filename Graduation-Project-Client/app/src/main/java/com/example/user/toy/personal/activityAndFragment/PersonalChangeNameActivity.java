package com.example.user.toy.personal.activityAndFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.toy.R;
import com.example.user.toy.personal.entity.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalChangeNameActivity extends AppCompatActivity {
    private Intent intent;
    private EditText etName;
    private Button button;
    private String name;
    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        sharedPreferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        user = new Gson().fromJson(sharedPreferences.getString("user",null),User.class);
        intent = getIntent();
        name = intent.getStringExtra("name");
        findViews();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("changename",etName.getText().toString());
                setResult(101,intent);
                okHttpClient = new OkHttpClient();
                user.setNickname(etName.getText().toString());
                requestData(user);
            }
        });
    }

    private void requestData(final User user) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"),new Gson().toJson(user));
        final Request request = new Request.Builder().url("").post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.equals("true")){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user",new Gson().toJson(user));
                    editor.commit();
                    Log.e("222","成功");
                    finish();
                }else {
                    Log.e("111","失败");
                }
            }
        });
    }

    private void findViews() {
        etName = findViewById(R.id.et_change_name);
        button = findViewById(R.id.btn_change_name);
        etName.setText(user.getNickname());
    }

}
