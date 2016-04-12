/*
 * BruceHurrican
 * Copyright (c) 2016.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *    This document is Bruce's individual learning the android demo, wherein the use of the code from the Internet, only to use as a learning exchanges.
 *   And where any person can download and use, but not for commercial purposes.
 *   Author does not assume the resulting corresponding disputes.
 *   If you have good suggestions for the code, you can contact BurrceHurrican@foxmail.com
 *   本文件为Bruce's个人学习android的demo, 其中所用到的代码来源于互联网，仅作为学习交流使用。
 *   任和何人可以下载并使用, 但是不能用于商业用途。
 *   作者不承担由此带来的相应纠纷。
 *   如果对本代码有好的建议，可以联系BurrceHurrican@foxmail.com
 */

package app.bruce.com.jsonapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bruceutils.base.BaseActivity;
import com.bruceutils.net.volley.TempStringRequest;
import com.bruceutils.net.volley.VolleyRequestListener;
import com.bruceutils.net.volley.VolleySingleton;
import com.bruceutils.utils.LogUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 比较JSON解析效率
 */
public class JsonActivity extends BaseActivity {

    private final int PARSE_TYPE_JSON = 0;
    private final int PARSE_TYPE_GSON = 1;
    private final int PARSE_TYPE_FAST_JSON = 2;
    @Bind(R.id.btn_json_normal)
    Button btn_json_normal;
    @Bind(R.id.btn_json_gson)
    Button btn_json_gson;
    @Bind(R.id.btn_json_fast_json)
    Button btn_json_fast_json;
    @Bind(R.id.btn_json_result)
    TextView tv_json_result;
    private String jsonTag = "jsonTag";
    private long startParseTime, endParseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_json_normal, R.id.btn_json_gson, R.id.btn_json_fast_json})
    public void onClick(View v) {
        if (TextUtils.isEmpty(jsonTag)) {
            VolleySingleton.getInstance(JsonActivity.this.getApplicationContext()).getRequestQueue().cancelAll(jsonTag);
        }
        String url = "http://192.168.31.127:8080/jsondata/girl.json";
        TempStringRequest stringRequest;
        switch (v.getId()) {
            case R.id.btn_json_normal:
                LogUtils.i("============json===============");
                stringRequest = new TempStringRequest(Request.Method.GET, url, null, new GetJsonDataListener(JsonActivity.this, PARSE_TYPE_JSON));
                stringRequest.setTag(jsonTag);
                VolleySingleton.getInstance(JsonActivity.this.getApplicationContext()).addToRequestQueue(stringRequest);
                break;
            case R.id.btn_json_gson:
                LogUtils.i("============gson===============");
                stringRequest = new TempStringRequest(Request.Method.GET, url, null, new GetJsonDataListener(JsonActivity.this, PARSE_TYPE_GSON));
                stringRequest.setTag(jsonTag);
                VolleySingleton.getInstance(JsonActivity.this.getApplicationContext()).addToRequestQueue(stringRequest);
                break;
            case R.id.btn_json_fast_json:
                LogUtils.i("============fast json===============");
                stringRequest = new TempStringRequest(Request.Method.GET, url, null, new GetJsonDataListener(JsonActivity.this, PARSE_TYPE_FAST_JSON));
                stringRequest.setTag(jsonTag);
                VolleySingleton.getInstance(JsonActivity.this.getApplicationContext()).addToRequestQueue(stringRequest);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (TextUtils.isEmpty(jsonTag)) {
            VolleySingleton.getInstance(JsonActivity.this.getApplicationContext()).getRequestQueue().cancelAll(jsonTag);
        }
    }

    /**
     * 普通json解析json
     *
     * @param result
     */
    private void getJsonData(String result) {
        startParseTime = System.currentTimeMillis();
        try {
            JSONObject jsonObject = new JSONObject(result);
            final String id = jsonObject.getString("id");
            final String name = jsonObject.getString("name");
            final String age = jsonObject.getString("age");
            final String hooby = jsonObject.getString("hooby");
            endParseTime = System.currentTimeMillis();
            LogUtils.i("普通方式json解析耗时->" + (endParseTime - startParseTime) + "ms");
            showToastShort("普通方式json解析耗时->" + (endParseTime - startParseTime) + "ms");
            LogUtils.i("id->" + id + "\nname->" + name + "\nage->" + age + "\nhooby->" + hooby);
            tv_json_result.post(new Runnable() {
                @Override
                public void run() {
                    tv_json_result.setText("id->" + id + "\nname->" + name + "\nage->" + age + "\nhooby->" + hooby);
                }
            });
        } catch (JSONException e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * gson方式解析
     *
     * @param result
     */
    private void getJsonDataInGson(String result) {
        startParseTime = System.currentTimeMillis();
        Gson gson = new Gson();
        final Girl girl = gson.fromJson(result, Girl.class);
        endParseTime = System.currentTimeMillis();
        LogUtils.i("GSON解析耗时->" + (endParseTime - startParseTime) + "ms");
        showToastShort("GSON解析耗时->" + (endParseTime - startParseTime) + "ms");
        tv_json_result.post(new Runnable() {
            @Override
            public void run() {
                tv_json_result.setText("id->" + girl.id + "\nname->" + girl.name + "\nage->" + girl.age + "\nhooby->" + girl.hooby);
            }
        });
        LogUtils.i("id->" + girl.id + "\nname->" + girl.name + "\nage->" + girl.age + "\nhooby->" + girl.hooby);
    }

    private void getJsonDataInFastJson(String result) {
        startParseTime = System.currentTimeMillis();
        final Girl girl = JSON.parseObject(result, Girl.class);
        endParseTime = System.currentTimeMillis();
        LogUtils.i("fastjson解析耗时->" + (endParseTime - startParseTime) + "ms");
        showToastShort("fastjson解析耗时->" + (endParseTime - startParseTime) + "ms");
        tv_json_result.post(new Runnable() {
            @Override
            public void run() {
                tv_json_result.setText("id->" + girl.id + "\nname->" + girl.name + "\nage->" + girl.age + "\nhooby->" + girl.hooby);
            }
        });
        LogUtils.i("id->" + girl.id + "\nname->" + girl.name + "\nage->" + girl.age + "\nhooby->" + girl.hooby);
    }

    public static class GetJsonDataListener implements VolleyRequestListener {
        WeakReference<JsonActivity> weakReference;
        int parseType;

        GetJsonDataListener(JsonActivity activity, int parseType) {
            weakReference = new WeakReference<JsonActivity>(activity);
            this.parseType = parseType;
        }

        @Override
        public void onSuccessListener(String result) {
            JsonActivity context = weakReference.get();
            switch (parseType) {
                case 0:
                    context.getJsonData(result);
                    break;
                case 1:
                    context.getJsonDataInGson(result);
                    break;
                case 2:
                    context.getJsonDataInFastJson(result);
                    break;
            }
        }

        @Override
        public void onErrorListener(VolleyError error) {
            LogUtils.e(error.toString());
        }
    }
}
