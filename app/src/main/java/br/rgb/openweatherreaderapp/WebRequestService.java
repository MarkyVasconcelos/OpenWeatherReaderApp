package br.rgb.openweatherreaderapp;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class WebRequestService extends IntentService {
    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_ERROR = 1125;

    public static final String RESULT_KEY = "result_key";

    private static final String URL_KEY = "url_key";
    private static final String RECEIVER_KEY = "receiver_key";


    public WebRequestService() {
        super("WebRequestService");
    }

    public static void request(Context context, String url, ResultReceiver receiver) {
        Intent intent = new Intent(context, WebRequestService.class);
        intent.putExtra(URL_KEY, url);
        intent.putExtra(RECEIVER_KEY, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra(URL_KEY);
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER_KEY);

        Bundle result = new Bundle();
        try {
            String resultGet = getUrl(url);
            result.putString(RESULT_KEY, resultGet);
            receiver.send(RESULT_OK, result);
        } catch (IOException e) {
            e.printStackTrace();
            result.putString(RESULT_KEY, e.getMessage());
            receiver.send(RESULT_ERROR, result);
        }
    }

    private String getUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection openConnection = url.openConnection();
        openConnection.connect();
        InputStream inputStream = openConnection.getInputStream();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int n = inputStream.read(buffer); n >= 0; n = inputStream.read(buffer))
            out.write(buffer, 0, n);

        return out.toString();
    }
}