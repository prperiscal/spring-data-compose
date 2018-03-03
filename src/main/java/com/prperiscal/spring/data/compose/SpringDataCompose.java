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
 * <p>Annotates the Test class to use Spring Data Compose and make available the use of {@link DataComposeResource}.
 * <p>Registers the needed listeners which allows the logic execution for preparing the database.
 * <p>{@link Transactional} annotation will be inherited with the use of this annotation.
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
