package com.taradevko.aem.testingutils;

import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.resourceresolver.MockResourceResolverFactory;
import org.junit.Rule;

import java.util.Map;

public class AemContextHelper {

    @Rule
    public AemContext aemContext = new AemContext();

    protected ResourceResolverFactory getFactoryExceptionOnGet() {
        return new MockResourceResolverFactory() {
            @Override
            public ResourceResolver getServiceResourceResolver(final Map<String, Object> authenticationInfo) throws LoginException {
                throw new LoginException();
            }
        };
    }
}
