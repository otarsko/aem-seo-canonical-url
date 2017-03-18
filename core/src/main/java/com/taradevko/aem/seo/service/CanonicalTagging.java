package com.taradevko.aem.seo.service;

/**
 * <p>Service for looking for the canonical page.</p>
 *
 * OSGi Implementation is to be configured. Rule has next syntax:
 * <ul>
 *     <li>for **strict** rules, where page is mapped to another page: <br/>
 *      `_PATH PAGE 1_=>_CANONICAL PAGE PATH_`</li>
 *     <li>for rules which map pages by regex and provide lookup path: <br/>
 *      `_PATTERN FOR PAGE WHICH CHILD WILL BE MAPPED_>_LOOKUP PATH_`</li>
 * </ul>
 *
 * <p>Example:</p>
 * <p>
 *      for rule <code>/content/mysite/categoty[0-9]>/content/mysite/maincategory</code> <br/>
 *      configured publish domain <code>https://doma.in</code> <br/>
 *      and resource path <code>/content/mysite/categoty4/page-with-canonical/component</code> <br/>
 *      canonical path will be  <code>https://doma.in/content/mysite/maincategory/page-with-canonical</code>
 * </p>
 * <p>
 *     for strict rule <code>/content/mysite/specific/page-with-canonical=>/content/mysite/canonical-specific/page-with-canonical</code>
 *      and resource path <code>/content/mysite/specific/page-with-canonical</code> <br/>
 *      configured publish domain <code>https://doma.in</code> <br/>
 *      canonical path will be  <code>https://doma.in/content/mysite/canonical-specific/page-with-canonical</code>
 * </p>
 */
public interface CanonicalTagging {

    /**
     * <p>Method, which returns canonical link for the given resource path.</p>
     * <p>Internally, page will be extracted for the provided resource path.
     *      Using rules canonical path will be tried to found.</p>
     * <p>Using Externalizer, path will be converted to the link.</p>
     *
     * @param resourcePath which will be used to get page it is part of
     * @return canonical link or null
     */
    String getCanonicalLink(String resourcePath);
}
