package com.taradevko.aem.misc.dao;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class PageDaoImplTest {

    public static final class GetPagePath extends PageDaoImplTestBase {

        @Test
        public void shouldReturnPagePath() {
            assertThat(pageDao.getPagePath(RESOURCE_PATH)).isEqualTo(PAGE_PATH);
        }

        @Test
        public void shouldReturnNullOnExceptionWhileGettingRR() {

            pageDao.bindResourceResolverFactory(getFactoryExceptionOnGet());
            assertThat(pageDao.getPagePath(RESOURCE_PATH)).isNull();
        }
    }

    public static final class ExternalizePagePath extends PageDaoImplTestBase {

        @Test
        public void shouldExternalizePagePath() {
            assertThat(pageDao.externalizePagePath(PAGE_PATH)).isEqualTo("domain" + PAGE_PATH);
        }

        @Test
        public void shouldReturnNullOnExceptionWhileGettingRR() {

            pageDao.bindResourceResolverFactory(getFactoryExceptionOnGet());
            assertThat(pageDao.externalizePagePath(PAGE_PATH)).isNull();
        }
    }
}