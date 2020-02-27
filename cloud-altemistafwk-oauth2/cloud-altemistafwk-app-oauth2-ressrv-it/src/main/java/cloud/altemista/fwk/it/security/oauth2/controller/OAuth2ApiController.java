package cloud.altemista.fwk.it.security.oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAuth 2.0 resources
 * @author NTT DATA
 */
@RestController
@RequestMapping(path = OAuth2ApiController.MAPPING)
public class OAuth2ApiController {

	/** MAPPING String */
	public static final String MAPPING = "/api";

	/** The ALIVE_MESSAGE String */
	public static final String HELLO_MESSAGE = "Hello, %s!";

	/** The AUTHORIZATION_CODE_ONLY_MESSAGE */
	public static final String AUTHORIZATION_CODE_ONLY_MESSAGE = "For authorization code grant only";

	/** The IMPLICIT_ONLY_MESSAGE */
	public static final String IMPLICIT_ONLY_MESSAGE = "For implicit grant only";

	/** The CLIENT_CREDENETIALS_ONLY_MESSAGE */
	public static final String CLIENT_CREDENETIALS_ONLY_MESSAGE = "For client credenetials grant only";

	/** The PASSWORD_ONLY_MESSAGE */
	public static final String PASSWORD_ONLY_MESSAGE = "For resource owner password grant only";
	
	/**
	 * Simple REST service that represents a resource 
	 * @return String
	 */
	@RequestMapping(path = "/hello", method = RequestMethod.GET)
	public String alive() {
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		String name = authentication.getName();
		
		return String.format(HELLO_MESSAGE, name);
	}
	
	@RequestMapping(path = "/helloAuthorizationCode", method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('AUTHSCOPE')")
	public String onlyForAuthorizationCode() {
		
		return AUTHORIZATION_CODE_ONLY_MESSAGE;
	}
	
	@RequestMapping(path = "/helloImplicit", method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('IMPSCOPE')")
	public String onlyForImplicit() {
		
		return IMPLICIT_ONLY_MESSAGE;
	}
	
	@RequestMapping(path = "/helloClientCredentials", method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('CLISCOPE')")
	public String onlyForClientCredentials() {
		
		return CLIENT_CREDENETIALS_ONLY_MESSAGE;
	}
	
	@RequestMapping(path = "/helloPassword", method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('PWDSCOPE')")
	public String onlyForPassword() {
		
		return PASSWORD_ONLY_MESSAGE;
	}
}
