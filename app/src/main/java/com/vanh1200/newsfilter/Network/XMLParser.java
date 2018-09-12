package com.vanh1200.newsfilter.Network;

import android.util.Log;

import com.vanh1200.newsfilter.Model.News;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XMLParser extends DefaultHandler{
    private static final String TAG = "XMLParser";
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private News news;
    private String value;
    private boolean allowed_item = false;// skip first item and allow to add item to list
    private boolean allowed_des = false;
    private boolean allowed_link = false;
    private boolean allowed_pub_date = false;
    private boolean allowed_title = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(qName.equals(XMLTag.TAG_ITEM)){
            news = new News();
        }
        value = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        value += new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(news == null) return;
        switch (qName){
            case XMLTag.TAG_ITEM:
                if(!allowed_item){
                    allowed_item = true;
                    news = null;
                    break;
                }
                newsArrayList.add(news);
                news = null;
                break;
            case XMLTag.TAG_DESCRIPTION:
                if(!allowed_des){
                    allowed_des = true;
                    break;
                }
                // Find link of image
                String temp_image = "img src=\"//";
                int beginIndex = value.indexOf(temp_image) + temp_image.length();
                String link = value.substring(beginIndex);
                String imgSrc = link.substring(0, link.indexOf("\""));
                imgSrc = "https://" + imgSrc;
                Log.d(TAG, "endElement: " + imgSrc);
                news.setImage(imgSrc);

                // Find des
                String temp_description = "</font></b></font><br><font size=\"-1\">";
                String des = link.substring(link.indexOf(temp_description) + temp_description.length());
                String final_des = des.substring(0, des.indexOf("</font><br><font size=\"-1\""));
                final_des = final_des.replace("&nbsp;...","" );
                final_des = final_des.replace("<b>","" );
                final_des = final_des.replace("</b>","" );
                final_des = final_des.replace("&quot;", "\"");
                Log.d(TAG, "endElement: description: " + final_des);
                news.setDescription(final_des);

                break;
            case XMLTag.TAG_LINK:
                if(!allowed_link){
                    allowed_link = true;
                    break;
                }
                String temp_link = "url=";
                news.setLink(value.substring(value.indexOf(temp_link) + temp_link.length(), value.length()));
                break;
            case XMLTag.TAG_PUBDATE:
                if(!allowed_pub_date){
                    allowed_pub_date = true;
                    break;
                }
                news.setPubDate(value.substring(0, 16)); // lấy đủ trừ phần giờ không cần thiết.
                break;
            case XMLTag.TAG_TITLE:
                if(!allowed_title){
                    allowed_title = true;
                    break;
                }
                // set title
                String temp_title = " - ";
                int endIndex = value.indexOf(temp_title);
                String title = value.substring(0, endIndex);
                news.setTitle(title.trim());

                // set publisher
                news.setPublisher(value.substring(
                        value.indexOf(temp_title) + temp_title.length(),
                        value.length()).trim());
                break;
            default:
                break;
        }
    }

    public ArrayList<News> getNewsArrayList() {
        return newsArrayList;
    }
}
