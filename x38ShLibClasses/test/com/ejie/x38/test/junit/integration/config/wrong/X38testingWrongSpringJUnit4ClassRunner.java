package com.ejie.x38.test.junit.integration.config.wrong;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Eurohelp S.L.
 */
public class X38testingWrongSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {

	/**
	 * @param clazz
	 * @throws InitializationError
	 */
	public X38testingWrongSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	protected Object createTest() throws Exception {
		try {
			super.createTest();
		} catch (Exception e) {
			if (e.getCause().getMessage().indexOf(
					"Falta definir el bean 'requestMappingHandlerAdapter' de tipo 'RequestMappingHandlerAdapter'") >= 0) {
				throw new Exception("No se puede inicializar UDA en el contexto de SPRING");
			} else if (e.getCause().getMessage().indexOf(
					"Falta definir el bean 'messageSource' de tipo 'ReloadableResourceBundleMessageSource'") >= 0) {
				throw new Exception("No se puede inicializar UDA en el contexto de SPRING");
			} else if (e.getCause().getMessage().indexOf(
					"Falta definir el bean 'validationManager' de tipo 'com.ejie.x38.validation.ValidationManager'") >= 0) {
				throw new Exception("No se puede inicializar UDA en el contexto de SPRING");
			} else {
				throw new X38UdaConfigException(
						"Hubo excepción al inicializar el contexto de spring con la configuración incorrecta de UDA, pero no se detectaron las trazas adecuadas del validador");
			}
		}
		throw new X38UdaConfigException(
				"No hubo excepción al inicializar el contexto de spring con la configuración incorrecta de UDA");
	}

	/**
	 * {@code springMakeNotifier()} is an exact copy of
	 * {@link BlockJUnit4ClassRunner BlockJUnit4ClassRunner's}
	 * {@code makeNotifier()} method, but we have decided to prefix it with "spring"
	 * and keep it {@code private} in order to avoid the compatibility clashes that
	 * were introduced in JUnit between versions 4.5, 4.6, and 4.7.
	 */
	private EachTestNotifier springMakeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

	/**
	 * Performs the same logic as
	 * {@link BlockJUnit4ClassRunner#runChild(FrameworkMethod, RunNotifier)}, except
	 * that tests are determined to be <em>ignored</em> by
	 * {@link #isTestMethodIgnored(FrameworkMethod)}.
	 */
	@Override
	protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
		EachTestNotifier eachNotifier = springMakeNotifier(frameworkMethod, notifier);
		if (isTestMethodIgnored(frameworkMethod)) {
			eachNotifier.fireTestIgnored();
			return;
		}

		eachNotifier.fireTestStarted();
		try {
			methodBlock(frameworkMethod).evaluate();
		} catch (Throwable e) {
			if (e instanceof X38UdaConfigException) {
				eachNotifier.addFailure(e);
			}
		} finally {
			eachNotifier.fireTestFinished();
		}
	}
}
