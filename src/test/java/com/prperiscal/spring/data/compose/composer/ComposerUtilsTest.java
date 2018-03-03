package com.prperiscal.spring.data.compose.composer;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.net.URL;

import com.google.common.collect.Maps;
import com.prperiscal.spring.data.compose.model.ComposeData;
import org.junit.Test;

public class ComposerUtilsTest {

    @Test
    public void getEntityClassTest() throws Exception {
        Class<?> composeClass = ComposerUtils.getEntityClass(ComposeData.class.getName());
        assertThat(composeClass).isAssignableFrom(ComposeData.class);
    }

    @Test
    public void getResourceTest() throws Exception {
       URL url =  ComposerUtils.getResource(this.getClass(), "ResourceCompose.json");
       assertThat(url).isNotNull();
       assertThat(url.toString()).endsWith("/com/prperiscal/spring/data/compose/composer/ResourceCompose.json");
    }

    @Test
    public void getResourceFullNameTest() throws Exception {
        URL url =  ComposerUtils.getResource(this.getClass(), "com/prperiscal/spring/data/compose/composer/ResourceCompose.json");
        assertThat(url).isNotNull();
        assertThat(url.toString()).endsWith("/com/prperiscal/spring/data/compose/composer/ResourceCompose.json");
    }

    @Test
    public void getGetterTest() throws Exception {
        ComposeData composeData = new ComposeData();
        composeData.setMetadata(Maps.newHashMap());
        Method getter = ComposerUtils.getGetter(composeData, "metadata");
        Object getObject = getter.invoke(composeData);
        assertThat(getObject).isEqualTo(composeData.getMetadata());
    }

}