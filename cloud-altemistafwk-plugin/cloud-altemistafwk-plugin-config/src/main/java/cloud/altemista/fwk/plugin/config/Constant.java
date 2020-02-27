package cloud.altemista.fwk.plugin.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Symbolic constants for the plug-in (and should NOT be changed)
 * @author NTT DATA
 */
public class Constant {
	
	/*
	 * Default values: values arbitrarily chosen; can be changed without major implications
	 */
	
	/** Default value for the groupId of a new application */
	public static final String DEFAULT_GROUP_ID = "com.mycompany";
	
	/** Default value for the applicationName, or rootArtifactId of a new application */
	public static final String DEFAULT_ARTIFACT_ID = "new-application";
	
	/** Default value for the version of a new application */
	public static final String DEFAULT_VERSION = "1.0.0-SNAPSHOT";
	
	/** Default value for the appShortId of a new application */
	public static final String DEFAULT_APP_SHORT_ID = "application";
	
	/** Default value for the businessShortId of a new business module */
	public static final String DEFAULT_BUSINESS_SHORT_ID = "module";

	/*
	 * Internal symbolic constants: values that CAN be carefully changed to modify the behavior of the application
	 */
	
	/** Regex that matches any invalid character of a Maven groupId (or a more restricted Java package) */
	public static final String GROUP_ID_INVALID_CHARS_REGEX = "[^\\w\\.]";
	
	/** Regex that matches any valid final value of a Maven groupId (or a more restricted Java package) */
	public static final String GROUP_ID_REGEX = "^[A-Za-z_]\\w*(\\.[A-Za-z_]\\w*)*$";
	
	/** The maximum length of a Maven groupId (or a more restricted Java package) */
	public static final int GROUP_ID_MAX_LENGTH = 128;
	
	/** Regex that matches any invalid character of a Maven artifactId */
	public static final String ARTIFACT_ID_INVALID_CHARS_REGEX = "[^\\w\\-]";
	
	/** Regex that matches any valid final value of a Maven artifactId */
	public static final String ARTIFACT_ID_REGEX = "^[A-Za-z_]([\\w\\-]*\\w)?$";
	
	/** The maximum length of a Maven artifactId */
	public static final int ARTIFACT_ID_MAX_LENGTH = 64;
	
	/** Regex that matches any invalid character of a Maven version */
	public static final String VERSION_INVALID_CHARS_REGEX = "[^\\w\\.\\-]";
	
	/** Regex that matches any valid final value of a Maven version */
	public static final String VERSION_REGEX = "^\\w+([\\.\\-]\\w+)*$";
	
	/** The maximum length of a Maven version */
	public static final int VERSION_MAX_LENGTH = 64;
	
	/** Regex that matches any invalid character of an application short ID (or a more restricted Java identifier) */
	public static final String APPSHORTID_INVALID_CHARS_REGEX = "[^\\w]";
	
	/** Regex that matches any valid final value of an application short ID (or a more restricted Java identifier) */
	public static final String APPSHORTID_REGEX = "^[A-Za-z_]\\w*$";
	
	/** The maximum length of an application short ID (or a more restricted Java identifier) */
	public static final int APPSHORTID_MAX_LENGTH = 24;
	
	/** The ToStringStyle used by the DTOs (mainly for testing purposes) */
	public static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;
	
	/*
	 * External symbolic constants: represent values taken from outside the plug-in and, thus, SHOULD NOT be changed
	 * (e.g.: Maven coordinates that should exist on the repository)
	 */
	
	/** The main groupId of the TSF+ artifacts */
	public static final String TSFP_MAIN_GROUP_ID = "org.terasoluna.plus";
	/** The main groupId of the Altemista artifacts */
	public static final String ALTEMISTA_MAIN_GROUP_ID = "cloud.altemista.fwk";
	
	/** Common prefix for all the TSF+ artifacts groupId */
	public static final String TSFP_GROUP_ID_PREFIX = TSFP_MAIN_GROUP_ID + ".";
	/** Common prefix for all the Altemista artifacts groupId */
	public static final String ALTEMISTA_GROUP_ID_PREFIX = ALTEMISTA_MAIN_GROUP_ID + ".";
	
	/** The groupId of the TSF+ bill of materials (BOM) artifact */
	public static final String TSFP_BOM_GROUP_ID = "org.terasoluna.plus.framework";
	/** The groupId of the Altemista bill of materials (BOM) artifact */
	public static final String ALTEMISTA_BOM_GROUP_ID = "cloud.altemista.fwk.framework";
	
	/** The artifactId of the TSF+ bill of materials (BOM) artifact */
	public static final String TSFP_BOM_ARTIFACT_ID = "terasoluna-plus-bom";
	/** The artifactId of the Altemista bill of materials (BOM) artifact */
	public static final String ALTEMISTA_BOM_ARTIFACT_ID = "cloud-altemistafwk-bom";
	
	/** The "appShortId" property in the application pom.xml */
	public static final String MAVEN_PROPERTY_APP_SHORT_ID = "appShortId";
	
	/** The archetype parameter  "applicationName" (or rootArtifactId) */
	public static final String ARCHETYPE_PARAMETER_APP_NAME = "applicationName";
	
	/** The archetype parameter "appShortId" */
	public static final String ARCHETYPE_PARAMETER_APP_SHORT_ID = "appShortId";
	
	/** The archetype parameter "terasolunaPlusVersion" */
	public static final String ARCHETYPE_PARAMETER_TSFP_VERSION = "terasolunaPlusVersion";
	
	/** The archetype parameter "businessShortId" */
	public static final String ARCHETYPE_PARAMETER_BUSINESS_SHORT_ID = "businessShortId";
	
	/**
	 * The artifacId suffix-based priorities to consider a module project a candidate to have the shared configurations
	 * when any of its module project types specifies a priority (or the module project has no or unknown type).
	 * Lower values (starting with 0) represents higher priority. 
	 * @see cloud.altemista.fwk.plugin.core.impl.PluginReaderServiceImpl#computeSharedImplementationPriority(ModuleProjectDto)
	 * @see cloud.altemista.fwk.plugin.config.model.ModuleProjectTypeDto#sharedImplementationPriority
	 */
	public static final Map<String, Integer> SHARED_IMPLEMENTATION_PRIORITY_BY_SUFFIX;
	static {
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("-env", 10);
		SHARED_IMPLEMENTATION_PRIORITY_BY_SUFFIX = Collections.unmodifiableMap(map);
	}
	
	/** The folder that may contain shared configuration after an implementation archetype has been applied */
	public static final String SHARED_CONFIGURATION_FOLDER = "src/main/resources";
	
	/** The wildcards to match the shared configuration files */
	public static final String[] SHARED_CONFIGURATION_WILDCARDS = new String[] {
			// The archetypes use this suffix to identify shared configuration files
			"*-env.*",
			// terasoluna-plus-core-cache-ehcache.conf expects this filename (without the -env suffix)
			"ehcache.xml",
			// terasoluna-plus-core-mybatis-conf expects this filename (without the -env suffix) 
			"mybatis-config.xml"
		};
	
	/** The path and filename of the web application deployment descriptor (the web.xml file) */
	public static final String WEB_APP_DEPLOYMENT_DESCRIPTOR_FILENAME = "src/main/webapp/WEB-INF/web.xml";
	
	/** The path and filename of a web fragment (the web-fragment.xml file) */
	public static final String WEB_FRAGMENT_FILENAME = "src/main/resources/META-INF/web-fragment.xml";
	
	/** The path and filename of a web fragment (the web-fragment.xml file) */
	public static final String[] TSF_VERSIONS = {"1.0.0.RELEASE","1.1.0-RELEASE","1.2.0.RELEASE","1.3.0.RELEASE","1.4.0.RELEASE","1.5.0.RELEASE","1.6.0.RELEASE","2.0.0.RELEASE"};
	
	/**
	 * Private default constructor (constants class)
	 */
	private Constant() {
		super();
	}

}
