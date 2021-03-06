package org.sagebionetworks.template.repo.beanstalk;

public enum EnvironmentType {

	REPOSITORY_SERVICES("services-repository", "repo", "SynapesRepoWorkersInstanceProfile"),
	REPOSITORY_WORKERS("services-workers", "workers","SynapesRepoWorkersInstanceProfile"),
	PORTAL("portal", "portal", "SynapesPortalInstanceProfile");

	String pathName;
	String cnamePrefix;
	String instanceProfileSuffix;

	EnvironmentType(String path, String cnamePrefix, String instanceProfileSuffix) {
		this.pathName = path;
		this.cnamePrefix = cnamePrefix;
		this.instanceProfileSuffix = instanceProfileSuffix;
	}

	/**
	 * Create the URL used to download the given war file version from artifactory.
	 * 
	 * @param version
	 * @return
	 */
	public String createArtifactoryUrl(String version) {
		StringBuilder builder = new StringBuilder(
				"http://sagebionetworks.artifactoryonline.com/sagebionetworks/libs-releases-local/org/sagebionetworks");
		builder.append("/");
		builder.append(pathName);
		builder.append("/");
		builder.append(version);
		builder.append("/");
		builder.append(pathName);
		builder.append("-");
		builder.append(version);
		builder.append(".war");
		return builder.toString();
	}

	/**
	 * Create the S3 key to be used for the given ware file version.
	 * 
	 * @param version
	 * @return
	 */
	public String createS3Key(String version) {
		StringBuilder builder = new StringBuilder("versions");
		builder.append("/");
		builder.append(pathName);
		builder.append("/");
		builder.append(pathName);
		builder.append("-");
		builder.append(version);
		builder.append(".war");
		return builder.toString();
	}

	/**
	 * Get the short name for this type.
	 * 
	 * @return
	 */
	public String getShortName() {
		return this.cnamePrefix;
	}

	/**
	 * The suffix of Instance Profile that references the IAM service role for this
	 * type.
	 * 
	 * @return
	 */
	public String getInstanceProfileSuffix() {
		return this.instanceProfileSuffix;
	}
	
	/**
	 * Get the EnvironmentType matching the passed prefix.
	 * @param prefix
	 * @return
	 */
	public static EnvironmentType valueOfPrefix(String prefix) {
		for(EnvironmentType type: values()) {
			if(type.cnamePrefix.equals(prefix)){
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown prefix: "+prefix);
	}
}
