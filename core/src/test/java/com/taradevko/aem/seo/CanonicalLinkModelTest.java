package com.taradevko.aem.seo;

import com.taradevko.aem.misc.ResourceModel;
import com.taradevko.aem.seo.service.CanonicalTagging;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CanonicalLinkModelTest {

    public static final String RESOURCE_PATH = "/some/path";
    public static final String NO_CANONICAL_RESOURCE_PATH = "/some/path/noCanonical";
    @Rule
    public AemContext aemContext = new AemContext();

    @Before
    public void setUp() {
        aemContext.addModelsForPackage("com.taradevko.aem");
        aemContext.addModelsForPackage("com.taradevko.aem.misc");

        aemContext.registerService(CanonicalTagging.class, new CanonicalTagging() {
            @Override
            public String getCanonicalLink(final String resourcePath) {
                if (resourcePath.contains("noCanonical")) {
                    return null;
                }
                return "/canonical" + resourcePath;
            }
        });
    }

    @Test
    public void shouldAdaptFromRequest() {
        assertThat(getRequestForPath(RESOURCE_PATH).adaptTo(CanonicalLinkModel.class)).isNotNull();
    }

    @Test
    public void shouldReturnTrueIfHasCanonicalPage() {
        final CanonicalLinkModel canonicalLinkModel = getRequestForPath(RESOURCE_PATH)
                .adaptTo(CanonicalLinkModel.class);
        assertThat(canonicalLinkModel.isHasCanonicalPage()).isTrue();
    }

    @Test
    public void shouldReturnFalseIfHasNoCanonicalPage() {
        final CanonicalLinkModel canonicalLinkModel = getRequestForPath(NO_CANONICAL_RESOURCE_PATH).
                adaptTo(CanonicalLinkModel.class);
        assertThat(canonicalLinkModel.isHasCanonicalPage()).isFalse();
    }

    @Test
    public void shouldReturnCanonicalUrl() {
        final CanonicalLinkModel canonicalLinkModel = getRequestForPath(RESOURCE_PATH)
                .adaptTo(CanonicalLinkModel.class);
        assertThat(canonicalLinkModel.getCanonicalUrl()).isEqualTo("/canonical" + RESOURCE_PATH);
    }

    @Test
    public void shouldReturnNullIfNoCanonicalPage() {
        final CanonicalLinkModel canonicalLinkModel = getRequestForPath(NO_CANONICAL_RESOURCE_PATH)
                .adaptTo(CanonicalLinkModel.class);
        assertThat(canonicalLinkModel.getCanonicalUrl()).isNull();
    }

    private SlingHttpServletRequest getRequestForPath(final String path) {
        final Resource resource = aemContext.create().resource(path);
        final MockSlingHttpServletRequest request = aemContext.request();
        request.setResource(resource);
        return request;
    }
}
