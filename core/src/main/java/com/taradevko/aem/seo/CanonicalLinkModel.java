package com.taradevko.aem.seo;

import com.taradevko.aem.misc.ResourceModel;
import com.taradevko.aem.seo.service.CanonicalTagging;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class CanonicalLinkModel {

    @Self
    private ResourceModel resourceModel;

    @OSGiService
    private CanonicalTagging canonicalTagging;

    private String canonicalUrl;

    @PostConstruct
    private void init() {
        canonicalUrl = canonicalTagging.getCanonicalLink(resourceModel.getPath());
    }

    public boolean isHasCanonicalPage() {
        return StringUtils.isNotBlank(canonicalUrl);
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }
}
