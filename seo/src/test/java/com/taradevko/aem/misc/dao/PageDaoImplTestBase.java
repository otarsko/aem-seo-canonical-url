package com.taradevko.aem.misc.dao;

import com.day.cq.commons.Externalizer;
import com.taradevko.aem.testingutils.AemContextHelper;
import io.wcm.testing.mock.aem.builder.ContentBuilder;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class PageDaoImplTestBase extends AemContextHelper {

    static final String RESOURCE_PATH = "/content/mysite/page1/jcr:content/someResource";
    static final String PAGE_PATH = "/content/mysite/page1";

    PageDaoImpl pageDao;

    @Rule
    public AemContext aemContext = new AemContext(ResourceResolverType.JCR_OAK);

    private Externalizer externalizer;

    @Before
    public void setUp() throws PersistenceException {
        externalizer = mock(Externalizer.class);

        final ContentBuilder contentBuilder = aemContext.create();
        contentBuilder.page(PAGE_PATH);
        final Resource resource = contentBuilder.resource(RESOURCE_PATH);
        resource.getResourceResolver().commit();


        doReturn("domain" + PAGE_PATH).when(externalizer).publishLink(any(ResourceResolver.class), eq(PAGE_PATH));
        aemContext.registerService(Externalizer.class, externalizer);

        pageDao = new PageDaoImpl();
        aemContext.registerInjectActivateService(pageDao);
    }
}
