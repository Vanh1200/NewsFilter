package com.vanh1200.newsfilter.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.vanh1200.newsfilter.Model.News;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLAsync extends AsyncTask<String, Void, ArrayList<News>>{
    private static final String TAG = "XMLAsync";
    private onResultListenerCallBack listenerCallBack;

    public XMLAsync(onResultListenerCallBack listenerCallBack) {
        this.listenerCallBack = listenerCallBack;
    }

    @Override
    protected ArrayList<News> doInBackground(String... strings) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLParser xmlParser = new XMLParser();
            String url = strings[0];
            parser.parse(url, xmlParser);
            return xmlParser.getNewsArrayList();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<News> news) {
        super.onPostExecute(news);
        listenerCallBack.onParsedResultCallback(news);
    }

    public interface onResultListenerCallBack{
        void onParsedResultCallback(ArrayList<News> arrNews);
    }
}
