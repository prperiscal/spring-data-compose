package com.prperiscal.spring.data.compose;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.prperiscal.spring.data.compose.composer.DatabaseComposer;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * <p>Execution Listener which allows the logic execution for preparing the database.
 * <p>Spring will rollback the transaction, see: {@link Transactional}. So no clean up is need on after Test method.
 *
 * @author <a href="mailto:prperiscal@gmail.com">Pablo Rey Periscal</a>
 * @since 1.0.0
 */
public class DataComposeExecutionListener extends AbstractTestExecutionListener {

    @Autowired
    private DatabaseComposer databaseComposer;

    /**
     * <p>Pre-processes a test class <em>before</em> execution of all tests within the class.
     * <p>This method should be called immediately before framework-specific <em>before class</em> lifecycle callbacks.
     * <p>Implements an autowired to load the {@link DatabaseComposer}.
     *
     * @param testContext the test context for the test; never {@code null}
     *
     * @throws Exception allows any exception to propagate
     * @since 1.0.0
     */
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        Validate.notNull(testContext, "The validated '%s' is null", "testContext");
        GenericWebApplicationContext appContext = (GenericWebApplicationContext) testContext.getApplicationContext();
        appContext.registerBean(DatabaseComposer.class);

        testContext.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
    }

    /**
     * <p>Pre-processes a test <em>before</em> execution of <em>before</em> lifecycle callbacks of the underlying test
     * framework &mdash; for example, by setting up test fixtures.
     * <p>This method <strong>must</strong> be called immediately prior to framework-specific <em>before</em> lifecycle
     * callbacks. For historical reasons, this method is named {@code beforeTestMethod}. Since the introduction of
     * {@link #beforeTestExecution}, a more suitable name for this method might be something like {@code
     * beforeTestSetUp} or {@code beforeEach}; however, it is unfortunately impossible to rename this method due to
     * backward compatibility concerns.
     * <p>Calls the {@link DatabaseComposer} to execute the composing logic.
     *
     * @param testContext the test context in which the test method will be executed; never {@code null}
     *
     * @throws Exception allows any exception to propagate
     * @see #afterTestMethod
     * @see #beforeTestExecution
     * @see #afterTestExecution
     * @since 1.0.0
     */
    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        Validate.notNull(testContext, "The validated '%s' is null", "testContext");
        DataComposeResource dataComposeResourceAnnotation = testContext.getTestMethod().getDeclaredAnnotation(DataComposeResource.class);
        if(dataComposeResourceAnnotation != null) {
            if (isEmpty(dataComposeResourceAnnotation.value())) {
                databaseComposer.compose(testContext.getTestClass(), testContext.getTestMethod().getName());
            } else {
                databaseComposer.compose(testContext.getTestClass(), dataComposeResourceAnnotation.value());
            }
        }
    }

}
