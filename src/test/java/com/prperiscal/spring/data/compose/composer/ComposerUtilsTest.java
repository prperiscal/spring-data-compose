package com.prperiscal.spring.data.compose.composer;

import java.util.ArrayList;

import org.junit.Test;

/**
 * @author <a href="mailto:prperiscal@gmail.com">Pablo Rey Periscal</a>
 */
public class ComposerUtilsTest {
    @Test
    public void getEntityClass() throws Exception {
    }

    @Test
    public void getResource() throws Exception {
    }

    @Test
    public void getValidPackagePath() throws Exception {
    }

    @Test
    public void getGetter() throws Exception {
        ArrayList<Integer> li = new ArrayList<Integer>();
        ArrayList<Float> lf = new ArrayList<Float>();
        if (li.getClass() == lf.getClass()) // evaluates to true
            System.out.println("Equal");
    }

}