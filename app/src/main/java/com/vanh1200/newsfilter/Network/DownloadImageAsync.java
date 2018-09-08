package com.vanh1200.newsfilter.Network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadImageAsync extends AsyncTask<String, Void, Bitmap>{
  private OnResultDownloaded onResultDownloaded;

    public DownloadImageAsync(OnResultDownloaded onResultDownloaded) {
        this.onResultDownloaded = onResultDownloaded;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String link = strings[0];
        try {
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            InputStream stream = connection.getInputStream();
            byte[] arrBytes = new byte[1024];
            ArrayList<Byte> arrayListDatas = new ArrayList<>();
            double count;
            while((count = stream.read(arrBytes)) != -1){
                for(int i = 0; i < arrBytes.length; i++){
                    arrayListDatas.add(arrBytes[i]);
                }
            }
            stream.close();

            byte[] arrDecode = new byte[arrayListDatas.size()];
            for (int i = 0; i < arrayListDatas.size(); i++) {
                arrDecode[i] = arrayListDatas.get(i);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(arrDecode, 0, arrayListDatas.size());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        onResultDownloaded.onDownloadedImage(bitmap);
    }

    public interface OnResultDownloaded{
        void onDownloadedImage(Bitmap bitmap);
    }
}
