package com.mafia.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

public class HttpManager implements HttpResponseListener {

    HttpRequest request;
    private String result;

    public HttpManager(String stroka, String action)
    {
        request = new HttpRequest();
        request.setMethod(Net.HttpMethods.POST); //or POST
        //request.setContent(""); //you can put here some PUT/GET content
        request.setUrl("https://pamminvestment.org/mafia/" + stroka);
        Gdx.net.sendHttpRequest(request, this);
    }

    @Override
    public void handleHttpResponse(HttpResponse httpResponse) {
        result = httpResponse.getResultAsString();
        //System.out.println(result);
    }

    @Override
    public void failed(Throwable t) {

    }

    @Override
    public void cancelled() {

    }

    public String getResults(){
        return result;
    }
}
