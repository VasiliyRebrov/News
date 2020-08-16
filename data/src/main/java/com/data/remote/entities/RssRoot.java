package com.data.remote.entities;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name = "rss", strict = false)
public class RssRoot {
    @ElementList(name = "item", inline = true)
    @Path("channel")
    public List<RssItem> rssItemList;
}
