/*
* Copyright 2011 E.J.I.E., S.A.
*
* Licencia con arreglo a la EUPL, Versión 1.1 exclusivamente (la «Licencia»);
* Solo podrá usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en
*
* http://ec.europa.eu/idabc/eupl.html
*
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito,
* el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL»,
* SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas.
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones
* que establece la Licencia.
*/
package com.ejie.x38.util;

import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author UDA
 *
 */
public class WebContextParameterManager implements ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger("com.ejie.x38.util.WebContextParameterManager");

	@Autowired
	private WebApplicationContext webApplicationContext;

	@PostConstruct
	public void init() {
		Properties appProperties = new Properties();
		logger.info("Loads the application context parameters");

		StaticsContainer.setWebAppName(webApplicationContext.getServletContext().getInitParameter("webAppName"));
		logger.info("The applications name is: " + StaticsContainer.getWebAppName());

		StaticsContainer.setWebId(webApplicationContext.getId());
		logger.info("The applications Id is: " + StaticsContainer.getWebId());

		try {
			logger.debug("Loading properties from: " + StaticsContainer.getWebAppName() + "/"
					+ StaticsContainer.getWebAppName() + ".properties");
			InputStream propertiesStream = this.getClass().getClassLoader().getResourceAsStream(
					StaticsContainer.getWebAppName() + "/" + StaticsContainer.getWebAppName() + ".properties");
			appProperties.load(propertiesStream);
			propertiesStream.close();
		} catch (Exception e) {
			logger.error(StackTraceManager.getStackTrace(e));
		}

		StaticsContainer.setStaticsUrl(appProperties.getProperty("statics.path"));
		logger.info("WARs specific Static Content URL is: " + StaticsContainer.getStaticsUrl());

		StaticsContainer.setModelPackageName("com.ejie." + StaticsContainer.getWebAppName() + ".model.");
		logger.info("Applications Model Package is: " + StaticsContainer.getModelPackageName() + ".model.");

		StaticsContainer.setLoginUrl(appProperties.getProperty("xlnets.path"));
		logger.info("The URL to access the security provider of the application (\"XLNets\") is: "
				+ StaticsContainer.getLoginUrl());

		if (appProperties.getProperty("xlnets.inPortal") != null
				&& ((appProperties.getProperty("xlnets.inPortal")).toLowerCase()).equals("true")) {
			StaticsContainer.setAplicInPortal(true);
			logger.info(
					"The application " + StaticsContainer.getWebAppName() + " is integrated in the portals of lote3");
		} else {
			StaticsContainer.setAplicInPortal(false);
			logger.info("The application " + StaticsContainer.getWebAppName()
					+ " isn't integrated in the portals of lote3");
		}

		if (StaticsContainer.getLoginUrl() == null) {
			logger.error("Login URL is not Set!");
		}

		StaticsContainer.setWeblogicInstance(System.getProperty("weblogic.Name"));
		logger.info("The WebLogic Instance Name is: " + StaticsContainer.getWeblogicInstance());

		if (appProperties.getProperty("cookie.rootPath") != null
				&& ((appProperties.getProperty("cookie.rootPath")).toLowerCase()).equals("true")) {
			StaticsContainer.setCookiePathRoot(true);
			logger.info("The cookie path is " + StaticsContainer.isCookiePathRoot());
		}

		if (appProperties.getProperty("cookie.secure") != null
				&& ((appProperties.getProperty("cookie.secure")).toLowerCase()).equals("true")) {
			StaticsContainer.setCookieSecure(true);
			logger.info("The cookie secure value is " + StaticsContainer.isCookieSecure());
		}
	}

	// Getters & Setters
	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		webApplicationContext = (WebApplicationContext) context;
	}
}