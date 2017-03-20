package com.taradevko.aem.seo.service;

import com.taradevko.aem.misc.dao.PageDao;
import com.taradevko.aem.testingutils.AemContextHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;

import java.util.Collections;

public class CanonicalTaggingImplTestBase extends AemContextHelper {

    static final String RESOURCE_PATH_WITH_ANOTHER_PREFIX = "/content/mysite/another-category/page/component";
    static final String RESOURCE_PATH_WITH_CANONICAL = "/content/mysite/categoty4/page-with-canonical/component";
    static final String PAGE_PATH_WITH_CANONICAL = "/content/mysite/specific/page-with-canonical";
    static final String RESOURCE_PATH_WITHOUT_CANONICAL = "/content/mysite/categoty4/page-without-canonical/component";
    static final String RESOURCE_PATH_WITH_NULL_PAGE_PATH = "/content/mysite/categoty4/null-page";

    private static final String CANONICAL_FOR_PAGE_WITH_ANOTHER_PREFIX = "/content/mysite/maincategory/page-without-canonical";

    static final String URL_FOR_PAGE_WITH_CANONICAL = "https://doma.in/content/mysite/maincategory/page-with-canonical";
    static final String URL_FOR_SPECIFIC_PAGE_WITH_CANONICAL = "https://doma.in/content/mysite/canonical-specific/page-with-canonical";

    CanonicalTaggingImpl canonicalTagging;

    @Before
    public void setUp() {
        canonicalTagging = new CanonicalTaggingImpl();

        PageDao pageDao = new PageDao() {
            @Override
            public String getPagePath(final String resourcePath) {
                if (RESOURCE_PATH_WITH_NULL_PAGE_PATH.equals(resourcePath)) {
                    return null;
                }
                return resourcePath.replace("/component", "");
            }

            @Override
            public String externalizePagePath(final String pagePath) {

                if (StringUtils.isNotBlank(pagePath) && !CANONICAL_FOR_PAGE_WITH_ANOTHER_PREFIX.equals(pagePath)) {
                    return "https://doma.in" + pagePath;
                }
                return null;
            }
        };

        aemContext.registerService(PageDao.class, pageDao);
        aemContext.registerInjectActivateService(canonicalTagging, Collections.singletonMap(CanonicalTaggingImpl.PAGE_MAPPING,
                new String[] {
                    "/content/mysite/categoty[0-9]>/content/mysite/maincategory",
                    "/content/mysite/specific/page-with-canonical=>/content/mysite/canonical-specific/page-with-canonical",
        }));
    }
}
