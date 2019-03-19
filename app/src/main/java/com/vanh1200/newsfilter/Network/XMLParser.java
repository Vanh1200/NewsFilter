package com.vanh1200.newsfilter.Network;

import com.vanh1200.newsfilter.Model.News;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XMLParser extends DefaultHandler {
    private static final String TAG = "XMLParser";
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private News news;
    private String value;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals(XMLTag.TAG_ITEM)) {
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
        if (news == null) return;
        switch (qName) {
            case XMLTag.TAG_ITEM:
                newsArrayList.add(news);
                news = null;
                break;
            case XMLTag.TAG_DESCRIPTION:
                news.setDescription(value);
                break;
            case XMLTag.TAG_LINK:
                news.setLink(value);
                break;
            case XMLTag.TAG_PUBDATE:
                news.setPubDate(value.substring(0, 16)); // lấy đủ trừ phần giờ không cần thiết.
                break;
            case XMLTag.TAG_TITLE:
                news.setTitle(value);
                break;
            default:
                break;
        }
    }

    public ArrayList<News> getNewsArrayList() {
        return newsArrayList;
    }
}
