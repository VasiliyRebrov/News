package com.data.remote.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class RssItem {
    @Element(name = "category", required = false)
    public String category;

    @Element(name = "pubDate")
    public String pubDate;

    @Element(name = "title")
    public String title;

    @Namespace(reference = "http://news.yandex.ru", prefix = "yandex")
    @Element(name = "full-text", required = false)
    public String fullText;

    @Path("enclosure")
    @Attribute(name = "url", required = false)
    public String enclosureUrl;

    @Element(name = "amplink", required = false)
    public String amplink;

    @Element(name = "description", required = false)
    public String description;
}