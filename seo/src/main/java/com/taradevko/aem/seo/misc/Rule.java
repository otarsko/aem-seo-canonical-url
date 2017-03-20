package com.taradevko.aem.seo.misc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Class which represents rule which will be used during searching for the canonical page</p>
 */
public final class Rule {

    private final String prefixPatternString;
    private final String lookupPath;
    private final boolean isStrict;
    private final Pattern prefixPattern;

    /**
     * <p>Constructor for Rule.</p>
     *
     * @param prefixPatternString pattern for matching page path OR page path (if rule is strict)
     * @param lookupPath path, where the canonical page will be searched for
     * @param isStrict marks this rule as a strict, so lookupPath will be treated as a path of the canonical page
     */
    public Rule(final String prefixPatternString, final String lookupPath, final boolean isStrict) {
        this.prefixPatternString = prefixPatternString;
        this.lookupPath = lookupPath;
        this.isStrict = isStrict;
        this.prefixPattern = Pattern.compile(prefixPatternString);
    }

    /**
     *
     * @return the lookup path
     */
    public String getLookupPath() {
        return lookupPath;
    }

    /**
     * Tests if input matches the prefix pattern.
     *
     * @param input value to test
     * @return true if input matches the prefix pattern. False - otherwise
     */
    public Matcher match(final String input) {
        return prefixPattern.matcher(input);
    }

    /**
     *
     * @return true if the rule is strict
     */
    public boolean isStrict() {
        return isStrict;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Rule rule = (Rule) o;

        return new EqualsBuilder()
                .append(isStrict, rule.isStrict)
                .append(prefixPatternString, rule.prefixPatternString)
                .append(lookupPath, rule.lookupPath)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(prefixPatternString)
                .append(lookupPath)
                .append(isStrict)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("prefixPatternString", prefixPatternString)
                .append("lookupPath", lookupPath)
                .append("isStrict", isStrict)
                .append("prefixPattern", prefixPattern)
                .toString();
    }
}
