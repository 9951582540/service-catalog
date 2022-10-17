package com.wissen.servicecatalog.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utility {
	static Logger logger = LoggerFactory.getLogger(Utility.class);

	public static String getSiteURL(HttpServletRequest request) {
		logger.info("Getting site URL from Utility");
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
}
