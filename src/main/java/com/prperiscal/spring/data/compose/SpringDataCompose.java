package com.prperiscal.spring.data.compose;

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author <a href="mailto:prperiscal@gmail.com">Pablo Rey Periscal</a>
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional
@TestExecutionListeners(listeners = {DataComposeExecutionListener.class}, mergeMode = MERGE_WITH_DEFAULTS)
public @interface SpringDataCompose {

}
