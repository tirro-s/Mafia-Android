package com.mafia.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;


public class Zapros {

    public void send(String stroka, final String action) {
        HttpRequest request = new HttpRequest(Net.HttpMethods.POST);
        request.setUrl("https://pamminvestment.org/mafia/" + stroka);
        //request.setContent(HttpParametersUtils.convertHttpParameters(params));
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {

            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                if (action.equals("getPlayers")) {
                    PlayState.resp = httpResponse.getResultAsString();
                    PlayState.needUpdate = true;
                }
                if (action.equals("getPeriod")) {
                    PlayState.respPeriod = httpResponse.getResultAsString();
                    PlayState.needUpdatePeriod = true;
                }
              }

            @Override
            public void failed(Throwable t) {
                //Gdx.app.error("HttpRequestExample", "something went wrong", t);
            }

            @Override
            public void cancelled() {
                //Gdx.app.log("HttpRequestExample", "cancelled");
            }
        });
    }
}
