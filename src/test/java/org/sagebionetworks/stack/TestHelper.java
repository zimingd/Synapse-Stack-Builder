package org.sagebionetworks.stack;

import java.io.IOException;
import java.util.Properties;

import org.sagebionetworks.stack.config.InputConfiguration;

import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.elasticbeanstalk.model.ApplicationVersionDescription;
import com.amazonaws.services.identitymanagement.model.ServerCertificateMetadata;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.Endpoint;
import com.amazonaws.services.cloudsearch.model.DomainStatus;
import com.amazonaws.services.cloudsearch.model.ServiceEndpoint;
import com.amazonaws.services.route53.model.Change;
import com.amazonaws.services.route53.model.ListResourceRecordSetsRequest;
import com.amazonaws.services.route53.model.ListResourceRecordSetsResult;
import com.amazonaws.services.route53.model.RRType;
import com.amazonaws.services.route53.model.ResourceRecord;
import com.amazonaws.services.route53.model.ResourceRecordSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.sagebionetworks.stack.Constants.*;

/**
 * Helper used by tests for setup.
 * 
 * @author John
 *
 */
public class TestHelper {


	/**
	 * Create a test Config.
	 * @return
	 * @throws IOException
	 */
	public static InputConfiguration createTestConfig(String stack) throws IOException{
		Properties inputProperties = createInputProperties(stack);
		InputConfiguration config = new InputConfiguration(inputProperties);
		Properties defaults = createDefaultProperties();
		config.addPropertiesWithPlaintext(defaults);
		// Add the SSL ARN
		config.setSSLCertificateARN("ssl:arn:123:456");
		return config;
	}

	/**
	 * @return
	 */
	public static Properties createDefaultProperties() {
		Properties defaults = new Properties();
		defaults.put(Constants.KEY_DEFAULT_ID_GEN_PASSWORD_PLAIN_TEXT, "id gen password");
		defaults.put(Constants.KEY_DEFAULT_STACK_INSTANCES_DB_PASSWORD_PLAIN_TEXT, "stack db password");
		defaults.put(Constants.KEY_CIDR_FOR_SSH, "255.255.255/1");
		defaults.put(Constants.KEY_RDS_ALAERT_SUBSCRIPTION_ENDPONT, "dev@sagebaser.org");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_CROWD_APPLICATION_KEY_PLAINTEXT, "crowd-app-key");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_MAIL_PW_PLAINTEXT, "mail password");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_BCC_GOOGLEAPPS_OAUTH_CONSUMER_SECRET_PLAINTEX, "google consumer oath key");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_BCC_GOOGLEAPPS_OAUTH_ACCESS_TOKEN_PLAINTEXT, "google access token");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_BCC_GOOGLEAPPS_OAUTH_ACCESS_TOKEN_SECRET_PLAINTEXT, "google access token secret");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_PORTAL_API_LINKEDIN_KEY, "linkedin key");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_PORTAL_API_LINKEDIN_SECRET_PLAINTEXT, "linkedin secret");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_PORTAL_API_GETSATISFACTION_KEY, "getsatisfaction key");
		defaults.put(KEY_ORG_SAGEBIONETWORKS_PORTAL_API_GETSATISFACTION_SECRET_PLAINTEXT, "getsatisfaction secret");
		
		return defaults;
	}

	/**
	 * Create test input properties.
	 * @param stack
	 * @return
	 */
	public static Properties createInputProperties(String stack) {
		Properties inputProperties = new Properties();
		inputProperties.put(Constants.AWS_ACCESS_KEY, "AWS access key value");
		inputProperties.put(Constants.AWS_SECRET_KEY, "AWS secrite key value");
		inputProperties.put(Constants.STACK_ENCRYPTION_KEY, "Encryption key that is long enough");
		inputProperties.put(Constants.STACK, stack);
		inputProperties.put(Constants.INSTANCE, "A");
		inputProperties.put(Constants.PORTAL_VERSION, "2.4.8");
		inputProperties.put(Constants.AUTHENTICATION_VERSION, "1.2.3");
		inputProperties.put(Constants.REPOSITORY_VERSION, "7.8.9");
		inputProperties.put(Constants.SEARCH_VERSION, "10.11.12");
		return inputProperties;
	}
	
	/**
	 * Create GeneratedResources that can be used for a test.
	 * 
	 * @param config
	 * @return
	 */
	public static GeneratedResources createTestResources(InputConfiguration config){
		GeneratedResources resources  = new GeneratedResources();
		resources.setIdGeneratorDatabase(new DBInstance().withDBInstanceIdentifier(config.getIdGeneratorDatabaseIdentifier()).withEndpoint(new Endpoint().withAddress("id-gen-db.someplace.com")));
		resources.setStackInstancesDatabase(new DBInstance().withDBInstanceIdentifier(config.getStackInstanceDatabaseIdentifier()).withEndpoint(new Endpoint().withAddress("stack-instance-db.someplace.com")));
		resources.setSearchDomain(new DomainStatus().withSearchService(new ServiceEndpoint().withEndpoint("search-service.someplace.com")));
		resources.getSearchDomain().setDocService(new ServiceEndpoint().withEndpoint("doc-service.someplace.com"));
		resources.setSslCertificate(new ServerCertificateMetadata().withArn("ssl:arn:123"));
		resources.setAuthApplicationVersion(new ApplicationVersionDescription().withVersionLabel(config.getAuthVersionLabel()));
		resources.setPortalApplicationVersion(new ApplicationVersionDescription().withVersionLabel(config.getPortalVersionLabel()));
		resources.setRepoApplicationVersion(new ApplicationVersionDescription().withVersionLabel(config.getRepoVersionLabel()));
		resources.setSearchApplicationVersion(new ApplicationVersionDescription().withVersionLabel(config.getSearchVersionLabel()));
		resources.setStackKeyPair(new KeyPairInfo().withKeyName(config.getStackKeyPairName()));
		return resources;
	}
	
	public static InputConfiguration createRoute53TestConfig(String stack) throws IOException {
		Properties inputProperties = createInputProperties(stack);
		InputConfiguration config = new InputConfiguration(inputProperties);
		Properties defaultProperties = createDefaultProperties();
		Map<String, String> cnameProps = getSvcCNAMEs();
		defaultProperties.putAll(cnameProps);
		defaultProperties.put("r53.subdomain", "r53.sagebase.org");
		config.addPropertiesWithPlaintext(defaultProperties);
		return config;
	}
	
	public static Map<ListResourceRecordSetsRequest, ListResourceRecordSetsResult> createListExpectedListResourceRecordSetsRequestAllFound() {
		Map<ListResourceRecordSetsRequest, ListResourceRecordSetsResult> m = new HashMap<ListResourceRecordSetsRequest, ListResourceRecordSetsResult>();
		Map<String, String> map = getSvcCNAMEs();
		for (String k: map.keySet()) {
			ListResourceRecordSetsRequest req = new ListResourceRecordSetsRequest().withStartRecordType(RRType.CNAME).withStartRecordName(k);
			ResourceRecord rr = new ResourceRecord().withValue(map.get(k));
			ListResourceRecordSetsResult res = new ListResourceRecordSetsResult().withResourceRecordSets(new ResourceRecordSet().withName(k).withTTL(300L).withType(RRType.CNAME).withResourceRecords(rr));
			m.put(req, res);
		}
		return m;
	}
	
	private static Map<String, String> getSvcCNAMEs() {
		Map<String, String> cnameProps = new HashMap<String, String>();
		cnameProps.put("authentication.service.generic.subdomain.cname", "auth.r53.sagebase.org");
		cnameProps.put("authentication.service.subdomain.cname", "auth.dev.x.r53.sagebase.org");
		cnameProps.put("portal.environment.generic.subdomain.cname", "synapse.r53.sagebase.org");
		cnameProps.put("portal.environment.subdomain.cname", "synapse.dev.x.r53.sagebase.org");
		cnameProps.put("repo.service.generic.subdomain.cname", "repo.r53.sagebase.org");
		cnameProps.put("repo.service.subdomain.cname", "repo.dev.x.r53.sagebase.org");
		cnameProps.put("search.service.generic.subdomain.cname", "search.r53.sagebase.org");
		cnameProps.put("search.service.subdomain.cname", "search.dev.x.r53.sagebase.org");
		return cnameProps;
	}

}
