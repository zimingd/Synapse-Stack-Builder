package org.sagebionetworks.template.repo;


import static org.sagebionetworks.template.Constants.IS_DEVELOPER_TEST_STACK;
import static org.sagebionetworks.template.Constants.PROPERTY_KEY_IS_DEVELOPER_TEST_STACK;

import com.google.inject.Inject;
import org.apache.velocity.VelocityContext;
import org.sagebionetworks.template.config.RepoConfiguration;

/**
 * Used to provide context about whether or not an instance is a developer test stack
 *
 * Sometimes we allocate less powerful instances for developer stacks to save on costs
 * Note: certain important security features, such as encryption-at-rest,
 * only work on more powerful instance types (e.g. m3.medium/c.5large), not the baseline instance types that AWS offers (t2.micro/t2.small)
 */
public class DeveloperTestStackVelocityContextProvider implements VelocityContextProvider{
	RepoConfiguration config;

	@Inject
	public DeveloperTestStackVelocityContextProvider(RepoConfiguration config) {
		this.config = config;
	}

	@Override
	public void addToContext(VelocityContext context) {
		Boolean isDeveloperStack = config.getBooleanProperty(PROPERTY_KEY_IS_DEVELOPER_TEST_STACK);
		context.put(IS_DEVELOPER_TEST_STACK, isDeveloperStack);
	}
}

