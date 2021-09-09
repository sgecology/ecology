/**
 * 
 */
package net.ecology.model;

/**
 * @author ducbq
 *
 */
public enum MarshallingProvider {
	DATA_PROVIDER_CSV("providerCSV"),
	DATA_PROVIDER_EXCEL("providerExcel")
	;

	private String provider;

	private MarshallingProvider(String provider) {
		this.setProvider(provider);
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
}
