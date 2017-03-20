package com.taradevko.aem.seo.service;


import com.taradevko.aem.misc.dao.PageDao;
import com.taradevko.aem.seo.misc.Rule;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Service
@Component(
        label = "Canonical Tagging Support",
        description = "Configuration for the Canonical Tagging Support service",
        metatype = true)
public class CanonicalTaggingImpl implements CanonicalTagging {

    private static final Logger LOG = LoggerFactory.getLogger(CanonicalTaggingImpl.class);

    @Property(
            cardinality = Integer.MIN_VALUE,
            label = "Page path mapping",
            description = "Define page mapping")
    static final String PAGE_MAPPING = "canonical.mapping.rules";
    private static final String STRICT_MAPPING_SEPARATOR = "=>";
    private static final String REGEX_MAPPING_SEPARATOR = ">";

    @Reference
    private PageDao pageDao;

    private List<Rule> canonicalRules;

    @Override
    public String getCanonicalLink(final String resourcePath) {
        for (Rule rule : canonicalRules) {
            if (rule.match(resourcePath).find()) {
                final String externalizedCanonicalPage = findCanonicalPage(resourcePath, rule);
                if (StringUtils.isNotBlank(externalizedCanonicalPage)) {
                    return externalizedCanonicalPage;
                }
            }
        }

        return null;
    }

    private String findCanonicalPage(final String resourcePath, final Rule rule) {
        final String pagePath = pageDao.getPagePath(resourcePath);
        if (StringUtils.isBlank(pagePath)) {
            LOG.warn("Can not get page path for the resource '{}'. Have you configured system user for this bundle?");
            return null;
        }

        if (rule.isStrict()) {
            return pageDao.externalizePagePath(rule.getLookupPath());
        } else {

            final Matcher pagePathMatcher = rule.match(pagePath);

            pagePathMatcher.find();

            final String pageRelativePath = pagePathMatcher.group(1);
            final String canonicalPagePath = rule.getLookupPath() + pageRelativePath;

            return pageDao.externalizePagePath(canonicalPagePath);
        }
    }

    @Activate
    @Modified
    public void activate(final Map<String, Object> properties) {
        canonicalRules = new ArrayList<>();

        final String[] canonicalRulesArray = PropertiesUtil.toStringArray(properties.get(PAGE_MAPPING));
        if (canonicalRulesArray.length < 1) {
            return;
        }

        for (final String ruleString : canonicalRulesArray) {
            final Rule rule = parseRule(ruleString);
            if (rule != null) {
                canonicalRules.add(rule);
            }
        }
    }
    private Rule parseRule(final String ruleString) {
        String separator = REGEX_MAPPING_SEPARATOR;
        boolean isStrict = false;
        if (ruleString.contains(STRICT_MAPPING_SEPARATOR)) {
            separator = STRICT_MAPPING_SEPARATOR;
            isStrict = true;
        }
        final String[] ruleSplit = ruleString.split(separator);

        // check if both parts of the rule are not blank
        if (validateRule(ruleSplit)) {
            if (isStrict) {
                return new Rule(ruleSplit[0], ruleSplit[1], isStrict);
            } else {
                return new Rule(formatPrefixPattern(ruleSplit[0]), formatLookupPath(ruleSplit[1]), isStrict);
            }
        } else {
            LOG.warn("Incorrect mapping rule '{}'", ruleString);
        }

        return null;
    }

    private boolean validateRule(final String[] ruleSplit) {
        return ruleSplit.length == 2 && StringUtils.isNoneBlank(ruleSplit[0], ruleSplit[1]);
    }

    private String formatPrefixPattern(final String prefixPattern) {
        return addPatternGroupEnding(ensureSlashEnding(prefixPattern));
    }

    private String ensureSlashEnding(final String path) {
        if (!path.endsWith("/")) {
            return path + "/";
        }
        return path;
    }

    private String addPatternGroupEnding(final String pattern) {
        return pattern + "(.+)";
    }

    private String formatLookupPath(final String lookupPath) {
        return ensureSlashEnding(lookupPath);
    }

    public void bindPageDao(final PageDao pageDao) {
        this.pageDao = pageDao;
    }

    @VisibleForTesting
    public List<Rule> getCanonicalRules() {
        return canonicalRules;
    }
}

