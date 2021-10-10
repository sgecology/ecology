/**
 * 
 */
package net.ecology.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import net.ecology.common.CommonUtility;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.component.BasisComp;

/**
 * @author ducbui
 *
 */
@Component
public class ResourcesServicesHelper extends BasisComp {
	private static final String CLASSPATH = "classpath:/";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4783730564446715179L;

	@Inject
	private ResourceLoader resourceLoader;
	
	private Resource loadClasspathResource(String resourcePath) {
		Resource resource = this.resourceLoader.getResource(CLASSPATH + resourcePath);
		logger.info("Resource is loaded::" + resource);
		return resource;
	}

	public byte[] loadClasspathResourceBytes(final String resourcePath) throws CerberusException {
		Resource resource = null;
		InputStream resourceDataStream = null;
		byte[] bdata = null;
		try {
			resource = loadClasspathResource(resourcePath);
			if (null==resource)
				throw new CerberusException("Unable to get resource from path: " + resourcePath);

			resourceDataStream = resource.getInputStream();
			logger.info("Found resource by given path: " + resourcePath);
			bdata = CommonUtility.getByteArray(resourceDataStream);
			/*
			byte[] bdata = FileCopyUtils.copyToByteArray(resourceDataStream);
			String data = new String(bdata, StandardCharsets.UTF_8);
			System.out.println(bdata);
			*/
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return bdata;
	}

	public InputStream loadClasspathResourceStream(final String resourcePath) throws CerberusException {
		Resource resource = null;
		InputStream resourceDataStream = null;
		try {
			resource = loadClasspathResource(resourcePath);
			if (null==resource)
				throw new CerberusException("Unable to get resource from path: " + resourcePath);

			resourceDataStream = resource.getInputStream();
			logger.info("Found resource by given path: " + resourcePath);
			/*
			byte[] bdata = FileCopyUtils.copyToByteArray(resourceDataStream);
			String data = new String(bdata, StandardCharsets.UTF_8);
			System.out.println(bdata);
			*/
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return resourceDataStream;
	}

	public File loadClasspathResourceFile(final String resourcePath) throws CerberusException {
		Resource resource = null;
		File resourceFile = null;
		try {
			resource = this.loadClasspathResource(resourcePath);
			if (null==resource)
				throw new CerberusException("Unable to get resource from path: " + resourcePath);

			InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
			logger.info("Real path: " + resourceStream);

			String baseName = CommonUtility.getFileBaseName(resourcePath);
			String extension = CommonUtility.getFileExtension(resourcePath);
			resourceFile = File.createTempFile(baseName, "." + extension);
			resourceFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(resourceFile);
			IOUtils.copy(resourceStream, out);
			
			
			//log.info("Is exist: " + resource.exists() + ". Is file: " + resource.isFile() + ". " + ". |" + resource.getDescription());
			//resourceFile = resource.getFile();
			logger.info("Found resource file by given path: " + resourcePath);
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return resourceFile;
	}
}
