/*
* Copyright 2011 E.J.I.E., S.A.
*
* Licencia con arreglo a la EUPL, VersiÃ³n 1.1 exclusivamente (la Â«LicenciaÂ»);
* Solo podrÃ¡ usarse esta obra si se respeta la Licencia.
* Puede obtenerse una copia de la Licencia en
*
* http://ec.europa.eu/idabc/eupl.html
*
* Salvo cuando lo exija la legislaciÃ³n aplicable o se acuerde por escrito,
* el programa distribuido con arreglo a la Licencia se distribuye Â«TAL CUALÂ»,
* SIN GARANTÃ�AS NI CONDICIONES DE NINGÃšN TIPO, ni expresas ni implÃ­citas.
* VÃ©ase la Licencia en el idioma concreto que rige los permisos y limitaciones
* que establece la Licencia.
*/
package com.ejie.x38.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author UDA
 *
 */
//Getion asociada al acelerador de codigo de produccion (https => http) en EJIE
public class ManagementUrl {
	
	private static final Logger logger = LoggerFactory.getLogger(ManagementUrl.class);

	//MÃ©todo que determina si la llamada esta o no acelerada 
	public static boolean isAcceleratedUrl(HttpServletRequest request){
		if (request.getHeader ("N38_URL") != null){
			logger.info("The aplication is being accelerated");
			return true;
		} else {
			logger.info("The aplication isn't being accelerated");
			return false;
		}
	}
	
	//MÃ©todo que devuelve la url real asociada a la aceleracion  
	public static String getUrl(HttpServletRequest request){
		String url = request.getHeader("N38_URL");
		if (url != null){
			logger.info("N38_URL header: " + url);

		} else {
			url = request.getRequestURL().toString();
			logger.info("Request header: " + url);
			
		}
		if(request.getQueryString() != null){
			try {
				url = url+"?"+URLEncoder.encode(request.getQueryString(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("getUrl(): Error Url encoder.", e);
			}
		}
		return url;
	}
}