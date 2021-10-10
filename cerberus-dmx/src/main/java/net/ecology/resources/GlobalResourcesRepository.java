/**
 * 
 *//*
package net.ecology.resources;

import java.io.FileNotFoundException;
import java.util.Collection;

import javax.inject.Inject;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.domain.DataContext;
import net.ecology.exceptions.CerberusException;

*//**
 * @author ducbq
 *
 *//*
@Component
public class GlobalResourcesRepository {
  @Inject
  private ResourceLoader resourceLoader;

	public DataContext resourceAsIOContainer(String path) throws CerberusException {
		DataContext ioDataContainer = null;
	  try {
	    Resource resource = this.resourceLoader.getResource(path);
	    if (null==resource){
	      throw new CerberusException("Unable to get resource from path: " + path);
	    }

	    ioDataContainer = (DataContext) DataContext.builder()
	    		.build()
	    		.put(DataContext.DEFAULT_INPUT_DATA_STREAM, resource.getInputStream());
    } catch (FileNotFoundException fnfe){
    	//logger.error(fnfe);
    } catch (Exception e) {
      throw new CerberusException(e);
    }
	  return ioDataContainer;
	}

	public String getRelativeResourcePath(String classpath, Resource resource) throws CerberusException {
		String relativeResourcePath = "";
		try {
			if (CommonUtility.isEmpty(classpath)){
				classpath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX).toURI().getPath();
			}
			relativeResourcePath = ResourceUtils.CLASSPATH_URL_PREFIX + "/" + resource.getURL().getPath().substring(classpath.length());
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return relativeResourcePath;
	}
	
	public Collection<String> enumerateResourceNames(String path) throws CerberusException {
		Collection<String> resourceNames = CollectionsUtility.newList();
  	Resource[] resources;
		try {
			String classpath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX).toURI().getPath();
			resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(path);
	  	for (Resource resource :resources) {
	  		resourceNames.add(getRelativeResourcePath(classpath, resource));
	  		//resourceNames.add(ResourceUtils.CLASSPATH_URL_PREFIX + "/" + resource.getURL().getPath().substring(classpath.length()));
	  		//ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResource(ResourceUtils.CLASSPATH_URL_PREFIX + "/" + resource.getURL().getPath().substring(classpath.length()));
	  	}
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return resourceNames;
	}

	public DataContext resources(String path) throws CerberusException {
		DataContext ioContainer = DataContext.builder().build();
  	String resourceName = null;
  	Resource[] resources = null;
  	try {
	  	resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(path);
	  	for (Resource resource :resources) {
	  		resourceName = getRelativeResourcePath(null, resource);
	  		//if (!resourceName.endsWith(GlobeConstants.REPO_MASTER)){
				ioContainer.put(resourceName, resource.getInputStream());
	  		//}
	  	}
    } catch (Exception e) {
      throw new CerberusException(e);
    }
	  return ioContainer;
	}
}*/