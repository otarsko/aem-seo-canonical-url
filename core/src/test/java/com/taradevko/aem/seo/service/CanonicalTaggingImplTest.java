package com.taradevko.aem.seo.service;

import com.taradevko.aem.seo.misc.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class CanonicalTaggingImplTest {

    @RunWith(Parameterized.class)
    public static final class GetCanonicalLink extends CanonicalTaggingImplTestBase {

        private String message;
        private String resourcePath;
        private String expected;

        public GetCanonicalLink(final String message, final String resourcePath, final String expected) {
            this.message = message;
            this.resourcePath = resourcePath;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"Should return externalized page url",
                            RESOURCE_PATH_WITH_CANONICAL, URL_FOR_PAGE_WITH_CANONICAL},
                    {"Should return externalized page url for page with specific rule",
                            PAGE_PATH_WITH_CANONICAL, URL_FOR_SPECIFIC_PAGE_WITH_CANONICAL},
                    {"Should return null for path, which is not mapped by the rules",
                            RESOURCE_PATH_WITH_ANOTHER_PREFIX, null},
                    {"Should return null for path, which does not have canonical page",
                            RESOURCE_PATH_WITHOUT_CANONICAL, null},
                    {"Should return null if page path not resolved",
                            RESOURCE_PATH_WITH_NULL_PAGE_PATH, null}
            });
        }

        @Test
        public void test() {
            assertThat(canonicalTagging.getCanonicalLink(resourcePath)).as(message).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static final class Activate extends CanonicalTaggingImplTestBase {

        private String message;
        private Object rulesArray;
        private List<Rule> expected;

        public Activate(final String message, final String[] rulesArray, final List<Rule> expected) {
            this.message = message;
            this.rulesArray = rulesArray;
            this.expected = expected;
        }

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"Should init canonicalRules with empty list if no rules specified",
                            new String[] {},
                            Collections.emptyList()},
                    {"Should correctly init rule",
                            new String[] {"/content/mysite/a[1-3]>/content/mysite"},
                            Collections.singletonList(new Rule("/content/mysite/a[1-3]/(.+)", "/content/mysite/", false))},
                    {"Should not add slashes to the paths if they are already there",
                            new String[] {"/content/mysite/a[1-3]/>/content/mysite/"},
                            Collections.singletonList(new Rule("/content/mysite/a[1-3]/(.+)", "/content/mysite/", false))},
                    {"Should parse strict rule",
                            new String[] {"/content/mysite/a[1-3]/=>/content/mysite/"},
                            Collections.singletonList(new Rule("/content/mysite/a[1-3]/", "/content/mysite/", true))},
                    {"Should skip rule if it has wrong format",
                            new String[] {"/content/mysite/a[1-3]_+_/content/mysite"},
                            Collections.emptyList()}
            });
        }

        @Test
        public void test() {
            canonicalTagging.activate(Collections.singletonMap(CanonicalTaggingImpl.PAGE_MAPPING, rulesArray));
            assertThat(canonicalTagging.getCanonicalRules()).as(message).containsExactlyElementsOf(expected);
        }
    }
}