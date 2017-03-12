package com.taradevko.aem.seo.misc;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class RuleTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Rule.class)
                .withNonnullFields("prefixPatternString", "lookupPath").withIgnoredFields("prefixPattern")
                .verify();
    }
}