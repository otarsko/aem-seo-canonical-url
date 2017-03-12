package com.taradevko.aem.misc;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class)
public class ResourceModel {

    @SlingObject
    private Resource resource;

    public String getPath() {
        return resource.getPath();
    }
}
