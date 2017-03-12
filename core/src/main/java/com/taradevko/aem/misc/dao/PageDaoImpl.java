package com.taradevko.aem.misc.dao;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component
public class PageDaoImpl implements PageDao {

    private static final Logger LOG = LoggerFactory.getLogger(PageDaoImpl.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private Externalizer externalizer;

    @Override
    public String getPagePath(final String resourcePath) {
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(null);
            final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            final Page containingPage = pageManager.getContainingPage(resourcePath);
            if (containingPage != null) {
                return containingPage.getPath();
            }
        } catch (LoginException e) {
            LOG.error("Can not obtain resource resolver: '{}'", e.getMessage());
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
        return null;
    }

    @Override
    public String externalizePagePath(final String pagePath) {
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(null);
            return externalizer.publishLink(resourceResolver, pagePath);
        } catch (LoginException e) {
            LOG.error("Can not obtain resource resolver: '{}'", e.getMessage());
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
        return null;
    }

    public void bindResourceResolverFactory(final ResourceResolverFactory rrf) {
        this.resourceResolverFactory = rrf;
    }

    public void bindExternalizer(final Externalizer externalizer) {
        this.externalizer = externalizer;
    }
}
