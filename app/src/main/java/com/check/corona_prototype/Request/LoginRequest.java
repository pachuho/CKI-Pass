package com.check.corona_prototype.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    // 서버 URL
    final static private String URL = "http://52.79.235.161/login.php";
    private Map<String, String> parameters;

    //생성자
    public LoginRequest(String id, String pwd, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("pwd", pwd);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
