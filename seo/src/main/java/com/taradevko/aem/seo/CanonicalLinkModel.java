package com.taradevko.aem.seo;

import com.taradevko.aem.misc.ResourceModel;
import com.taradevko.aem.seo.service.CanonicalTagging;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

/**
 * Sling model to be used for providing canonical tags. For HTL (sightly) it can look as follows:
 * <pre>
 *     &lt;link
 *        data-sly-use.canonicalTagging="com.taradevko.aem.seo.CanonicalLinkModel"
 *        data-sly-test="${canonicalTagging.hasCanonicalPage}"
 *        rel="canonical"
 *        href="${canonicalTagging.canonicalUrl}.html" /&gt;
 * </pre>
 */
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

    /**
     * Method for checking ic current page has canonical.
     *
     * @return true if current page has canonical page. Otherwise - false.
     */
    public boolean isHasCanonicalPage() {
        return StringUtils.isNotBlank(canonicalUrl);
    }

    /**
     * Method for getting canonical page.
     *
     * @return canonical url as a string or null.
     */
    public String getCanonicalUrl() {
        return canonicalUrl;
    }
}
