package org.altemista.cloudfwk.it.security.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * OAuth 2.0 query string-related utilities
 * @author NTT DATA
 */
public final class QueryStringUtil {
	
	/**
	 * Convenience method to parse the query string
	 * received at the redirect uri of an authorization code grant flow
	 * @param url String
	 * @return Map of the parameters
	 */
	public static Map<String, String> parseAuthorizationCodeRedirect(String url) {
		
		return parse(url, "?");
	}
	
	/**
	 * Convenience method to parse the query string
	 * received at the redirect uri of an implicit grant flow (i.e. received as uri fragment)
	 * @param url String
	 * @return Map of the parameters
	 */
	public static Map<String, String> parseImplicitRedirect(String url) {
	
		return parse(url, "#");
	}
	
	/**
	 * Convenience method to parse the query string received in the redirect uri
	 * @param url String
	 * @param separator Usually, "?" for authorization code grant and "#" for implicit grant
	 * @return Map of the parameters
	 */
	public static Map<String, String> parse(String url, String separator) {
		
		try {
			// Extracts the query string (received as uri fragment)
			String queryString = StringUtils.substringAfter(url, separator);
			if (StringUtils.isEmpty(queryString)) {
				return Collections.emptyMap();
			}
			
			// Parses the query string
			Map<String, String> map = new LinkedHashMap<String, String>();
			
			// Split by '&' and ';' 
			for (String urlParam : StringUtils.split(queryString, "&;")) {
				
				// Each pair split by '='
				String key = StringUtils.substringBefore(urlParam, "=");
				String value = StringUtils.substringAfter(urlParam, "=");
				map.put(URLDecoder.decode(key, "UTF-8"),
						StringUtils.isEmpty(value) ? null : URLDecoder.decode(value, "UTF-8"));
			}
			return map;
			
		} catch (UnsupportedEncodingException ignored) {
			// (should never happen)
			return Collections.emptyMap();
		}
	}

	/**
	 * Private default constructor (utility class)
	 */
	private QueryStringUtil() {
		super();
	}

}
