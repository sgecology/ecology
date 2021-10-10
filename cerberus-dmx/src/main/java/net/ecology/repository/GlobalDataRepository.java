/**
 * 
 */
package net.ecology.repository;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.exceptions.CerberusException;
import net.ecology.framework.component.BasisComp;

/**
 * @author ducbq
 *
 */
@Component
public class GlobalDataRepository extends BasisComp {
	private static final long serialVersionUID = 5369560309637596590L;

	@Inject
	private DataInterchangeRepository internalDataRepository;

	//@Async
  public void dispatch(String dispatchRepoPath) throws CerberusException {
		logger.error("Enter GlobalDataRepository:dispatch.");
		try {
			this.internalDataRepository.dispatch(dispatchRepoPath);
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		logger.error("Leave GlobalDataRepository:dispatch.");
	}

  /*private Context loadRepositoryResources(String path) throws CerberusException, IOException {
  	Context ioContainer = null;
  	
  	//Attachment attachment = null;
  	Configuration configuration = null;
  	Collection<String> archivedNames = null;
  	if (this.configurationService.exists(GlobeConstants.PROP_NAME, GlobeConstants.CACHE_DISPATCH_REPOSITORY)) {
    	logger.info("There is data archived in database, loading from database. ");
    	configuration = this.configurationService.getByName(GlobeConstants.CACHE_DISPATCH_REPOSITORY).orElse(null);;
    	if (null != configuration) {
    		archivedNames = CollectionsUtility.newCollection();
    		for (Configuration config :configuration.getSubordinates()) {
    			archivedNames.add(config.getValueExtended());
    			attachment = this.attachmentService.getByName(config.getValueExtended()).orElse(null);
        	if (CommonUtility.isNotEmpty(attachment)){
        		System.out.println(attachment.getId());
        	}
    		}
    		ioContainer = this.globalDmxManager.unarchive(archivedNames);
    	}
    	attachment = this.attachmentService.getByName(configuration.getName()).orElse(null);
    	if (CommonUtility.isNotEmpty(attachment))
    		return null;
  	}

    ioContainer = this.globalMarshallingRepository.resourcesAsIOContainer(path);
    if (null==ioContainer) {
    	logger.info("There is no data in path: " + path);
    	return null;
    }

  	Collection<String> archivedAttachmentNames = this.globalDmxManager.archives(ioContainer);
  	this.configurationService.saveAndFlush(Configuration.builder()
    	.group(GlobeConstants.CACHE_REPOSITORY)
    	.name(GlobeConstants.CACHE_DISPATCH_REPOSITORY)
    	.value(GlobeConstants.RAISED)
    	.valueExtended(String.join(GlobeConstants.PIPELINE, archivedAttachmentNames))
    	.info("Cache data for repository!")
    	.build()
    );
    return ioContainer;
  }*/

  /*private Configuration loadInitialConfigurations() {
  	Configuration dataRepoConfig = null;
  	logger.info("Enter loadInitialConfigurations(). ");
  	//Load initial configuration of dispatch repository
  	if (!this.configurationService.exists(GlobeConstants.PROP_NAME, GlobeConstants.CACHE_DISPATCH_REPOSITORY)) {
  		dataRepoConfig = Configuration.builder()
        	.group(GlobeConstants.CACHE_REPOSITORY)
        	.name(GlobeConstants.CACHE_DISPATCH_REPOSITORY)
        	.value(GlobeConstants.RAISED)
        	.info("Cache data for repository!")
        	.build();
  		this.configurationService.saveAndFlush(dataRepoConfig);
  	} else {
  		dataRepoConfig = this.configurationService.getByName(GlobeConstants.CACHE_MESSAGE_LABEL).orElse(null);
  	}

  	Configuration configuration = null;
  	//Load initial message label configuration
  	if (!this.configurationService.exists(GlobeConstants.PROP_NAME, GlobeConstants.CACHE_MESSAGE_LABEL)) {
  		configuration = Configuration.builder()
  				.parent(dataRepoConfig)
        	.group(GlobeConstants.CACHE_REPOSITORY)
        	.name(GlobeConstants.CACHE_MESSAGE_LABEL)
        	.value(GlobeConstants.RAISED)
        	.valueExtended(GlobeConstants.REPO_DIRECTORY + "messages.csv")
        	.info("Cache data for message labels!")
        	.build();
  		this.configurationService.saveAndFlush(configuration);
  	} else {
  		configuration = this.configurationService.getByName(GlobeConstants.CACHE_MESSAGE_LABEL).orElse(null);
  	}

  	//Load initial access policy configurations
  	if (!this.configurationService.exists(GlobeConstants.PROP_NAME, GlobeConstants.CACHE_ACCESS_POLICY)) {
  		configuration = Configuration.builder()
  				.parent(dataRepoConfig)
        	.group(GlobeConstants.CACHE_REPOSITORY)
        	.name(GlobeConstants.CACHE_ACCESS_POLICY)
        	.value(GlobeConstants.RAISED)
        	.valueExtended(GlobeConstants.REPO_DIRECTORY + "policy/decisions.csv")
        	.info("Cache data for access policy!")
        	.build();
  		this.configurationService.saveAndFlush(configuration);
  	} else {
  		configuration = this.configurationService.getByName(GlobeConstants.CACHE_ACCESS_POLICY).orElse(null);
  	}

  	logger.info("Leave loadInitialConfigurations(). ");
  	return dataRepoConfig;
  }

  private Context reserve(Context context) throws CerberusException, IOException{
  	Map<Object, Object> resourceMap = null;
  	Context ioContainer = null;
		String path = (String)context.get(GlobeConstants.CONTEXT_DISPATCH_REPO_PATH);

  	//Attachment attachment = null;
  	Configuration configuration = null;
  	Collection<String> archivedNames = null;
  	if (this.configurationService.exists(GlobeConstants.PROP_NAME, GlobeConstants.CACHE_DISPATCH_REPOSITORY)) {
    	logger.info("There is data archived in database, loading from database. ");
    	configuration = this.configurationService.getByName(GlobeConstants.CACHE_DISPATCH_REPOSITORY).orElse(null);;
    	if (null != configuration) {
    		archivedNames = CollectionsUtility.newCollection();
    		for (Configuration config :configuration.getSubordinates()) {
    			archivedNames.add(config.getValueExtended());
    		}
    		ioContainer = this.globalDmxManager.unarchive(archivedNames);
    	}
  	} else {
  		resourceMap = this.globalMarshallingRepository.resourcesAsIOContainer(path);
      if (null==ioContainer) {
      	logger.info("There is no data in path: " + path);
      	return null;
      }

    	Collection<String> archivedAttachmentNames = this.globalDmxManager.archives(ioContainer);
  		configuration = Configuration.builder()
        	.group(GlobeConstants.CACHE_REPOSITORY)
        	.name(GlobeConstants.CACHE_DISPATCH_REPOSITORY)
        	.value(GlobeConstants.RAISED)
        	.valueExtended(String.join(GlobeConstants.PIPELINE, archivedAttachmentNames))
        	.info("Cache data for access policy!")
        	.build();
  		this.configurationService.saveAndFlush(configuration);

  		for (String archivedAttachmentName :archivedAttachmentNames){
    		this.configurationService.saveAndFlush(Configuration.builder()
    				.parent(configuration)
          	.group(GlobeConstants.CACHE_REPOSITORY)
          	.name(archivedAttachmentName)
          	.value(GlobeConstants.RAISED)
          	.valueExtended(archivedAttachmentName)
          	.info("Configured data of: " + String.join(GlobeConstants.PIPELINE, archivedAttachmentNames))
          	.build()
          );
  		}
  	}
    return ioContainer;
  }*/
}