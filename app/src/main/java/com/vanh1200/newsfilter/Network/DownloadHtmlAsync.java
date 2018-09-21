package com.vanh1200.newsfilter.Network;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

public class DownloadHtmlAsync extends AsyncTask<String, Integer, String>{
    private static final String FOLDER_NAME = "Saved news";
    private DownloadHtmlCallBack callBack;
    private String fileName;
    private String suffix;
    private int position;

    public DownloadHtmlAsync(DownloadHtmlCallBack callBack) {
        this.callBack = callBack;
    }

    private static final String TAG = "DownloadHtmlAsync";

    @Override
    protected String doInBackground(String... strings) {
        fileName = strings[1];
        position = Integer.parseInt(strings[2]);
        return getWebPage(strings[0]);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String url = saveHtmlFile(s, fileName, suffix);
        callBack.onSavedHtml(url, position);
    }

    private String saveHtmlFile(String html, String name, String suffix) {
        File folder = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
        if(!folder.exists()){
            folder.mkdir();
        }
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + FOLDER_NAME + "/";
        String fileName = name + ".html";
        File file = new File(path, fileName);

        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] data = html.getBytes();
            out.write(data);
            out.close();
            return "file:///" + file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getWebPage(String address) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet();

        InputStream inputStream = null;

        String response = null;

        try {

            URI uri = new URI(address);
            httpGet.setURI(uri);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statutCode = httpResponse.getStatusLine().getStatusCode();
            int length = (int) httpResponse.getEntity().getContentLength();

            Log.v(TAG, "HTTP GET: " + address);
            Log.v(TAG, "HTTP StatutCode: " + statutCode);
            Log.v(TAG, "HTTP Lenght: " + length + " bytes");

            inputStream = httpResponse.getEntity().getContent();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            int inChar;
            StringBuffer stringBuffer = new StringBuffer();

            while ((inChar = reader.read()) != -1) {
                stringBuffer.append((char) inChar);
            }

            response = stringBuffer.toString();

        } catch (ClientProtocolException e) {
            Log.e(TAG, "HttpActivity.getPage() ClientProtocolException error", e);
        } catch (IOException e) {
            Log.e(TAG, "HttpActivity.getPage() IOException error", e);
        } catch (URISyntaxException e) {
            Log.e(TAG, "HttpActivity.getPage() URISyntaxException error", e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();

            } catch (IOException e) {
                Log.e(TAG, "HttpActivity.getPage() IOException error lors de la fermeture des flux", e);
            }
        }

        return response;
    }

    public interface DownloadHtmlCallBack{
        void onSavedHtml(String url, int position);
    }
}
