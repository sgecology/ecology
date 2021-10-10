/**
 * 
 */
package net.ecology.domain;

/**
 * @author ducbq
 *
 */
public enum MarshallingProvider {
	PROVIDER_CSV(".csv"),
	PROVIDER_EXCEL(".xlsx")
	;

	private String extension;

	private MarshallingProvider(String extension) {
		this.setExtension(extension);
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
