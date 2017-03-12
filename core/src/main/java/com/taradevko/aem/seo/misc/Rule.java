package com.taradevko.aem.seo.misc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Rule {

    private final String prefixPatternString;
    private final String lookupPath;
    private final boolean isStrict;
    private final Pattern prefixPattern;

    public Rule(final String prefixPatternString, final String lookupPath, final boolean isStrict) {
        this.prefixPatternString = prefixPatternString;
        this.lookupPath = lookupPath;
        this.isStrict = isStrict;
        this.prefixPattern = Pattern.compile(prefixPatternString);
    }

    public String getLookupPath() {
        return lookupPath;
    }

    public Matcher match(final String input) {
        return prefixPattern.matcher(input);
    }

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
