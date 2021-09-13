/**
 * 
 */
package net.ecology.repository;

import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import net.ecology.auth.GlobalMarshalingHelper;
import net.ecology.base.DataAdapter;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonConstants;
import net.ecology.common.CommonUtility;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.general.AttachmentService;
import net.ecology.dmx.manager.GlobalDmxManager;
import net.ecology.domain.entity.Attachment;
import net.ecology.entity.config.Configuration;
import net.ecology.exceptions.CerberusException;
import net.ecology.factory.DataMarshallingFactory;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobeConstants;
import net.ecology.globe.GlobalMarshallingRepository;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.model.MarshallingProvider;
import net.ecology.model.osx.OSXConstants;

/**
 * @author ducbq
 *
 */
@Component
public class GlobalDataRepository extends ComponentRoot {
	private static final long serialVersionUID = 5369560309637596590L;

	@Inject
	private GlobalEsiRepository globalEsiRepository;

  @Inject
  private GlobalDmxManager globalDmxManager;

  @Inject
  private AttachmentService attachmentService;

  @Inject
  private GlobalMarshallingRepository globalMarshallingRepository;

	@Inject
	private GlobalMarshalingHelper globalMarshalingHelper;

	@Inject
	private ConfigurationService configurationService;

	@Async
  public void dispatch(Context context) throws CerberusException {
		if (CommonUtility.isEmpty(context) || CommonUtility.isEmpty(context.get(GlobeConstants.CONTEXT_DISPATCH_REPO_PATH))) {
  		logger.error("The context or the default context attribute is empty!!!");
  		return;
  	}
		logger.error("Enter GlobalDataRepository:dispatch.");

		this.globalMarshalingHelper.dispatch(context);
		
		//loadInitialConfigurations();

		String path = (String)context.get(GlobeConstants.CONTEXT_DISPATCH_REPO_PATH);
		IOContainer ioContainer = reserve(path);
		System.out.println(ioContainer);

    //IOContainer currentIOContainer = loadRepositoryResources(path);
    //System.out.println(currentIOContainer);
		//this.loadDataRepositories(repositoryPath);

		this.globalEsiRepository.dispatch(path);
  	logger.error("Leave GlobalDataRepository:dispatch.");
	}

  private void configureMasterData() throws CerberusException {
    logger.info("Enter initializeMasterData");
    Context context = null;
    Optional<Attachment> optAttachment = this.attachmentService.getByName(GlobeConstants.APP_DEFAULT_CATALOUE_DATA);
    if (!optAttachment.isPresent()) {
      logger.info("There is no attachment represents the default catalog data!");
      context = Context.builder()
          .build()
          //.put(OSXConstants.INPUT_STREAM, this.globalDmxManager.getResourceInputStream(GlobalSharedConstants.APP_DATA_REPO_DIRECTORY + GlobalSharedConstants.APP_DEFAULT_CATALOUE_DATA))
          .put(OSXConstants.RESOURCE_REPO, GlobeConstants.REPO_DIRECTORY + GlobeConstants.APP_DEFAULT_CATALOUE_DATA)
          .put(OSXConstants.RESOURCE_NAME, GlobeConstants.APP_DEFAULT_CATALOUE_DATA)
          ;
      this.globalDmxManager.archive(context);
    } else {
      logger.info("There is one attachment represents the default catalog data!");
    }

    //context = configureRepositoryData();
    //configureDefaultSchedulers(context);
    this.globalMarshalingHelper.dispatch(context);
    logger.info("Leave initializeMasterData");
  }

  private Context loadDataRepositories(String path) {
    logger.info("Enter initializeMasterData");
    IOContainer currentIOContainer = loadRepositoryResources(path);
    
    Collection<String> resourceNames = this.globalMarshallingRepository.enumerateResourceNames(path);
    Attachment attachment = null;
    for (String resourceName: resourceNames) {
    	attachment = this.attachmentService.getByName(resourceName).orElse(null);
    	if (null==attachment) {
    		currentIOContainer = this.globalMarshallingRepository.resourceAsIOContainer(resourceName);
    		if (null != currentIOContainer) {
    			this.globalDmxManager.archive(resourceName, currentIOContainer, null);
    		}
    	}
    }

    IOContainer ioContainer = this.globalMarshallingRepository.resourcesAsIOContainer(path);

		
		Context context = null;
    Optional<Attachment> optAttachment = this.attachmentService.getByName(GlobeConstants.APP_DEFAULT_CATALOUE_DATA);
    if (!optAttachment.isPresent()) {
      logger.info("There is no attachment represents the default catalog data!");
      context = Context.builder()
          .build()
          //.put(OSXConstants.INPUT_STREAM, this.globalDmxManager.getResourceInputStream(GlobalSharedConstants.APP_DATA_REPO_DIRECTORY + GlobalSharedConstants.APP_DEFAULT_CATALOUE_DATA))
          .put(OSXConstants.RESOURCE_REPO, GlobeConstants.REPO_DIRECTORY + GlobeConstants.APP_DEFAULT_CATALOUE_DATA)
          .put(OSXConstants.RESOURCE_NAME, GlobeConstants.APP_DEFAULT_CATALOUE_DATA)
          ;
      this.globalDmxManager.archive(context);
    } else {
      logger.info("There is one attachment represents the default catalog data!");
    }

    ///context = loadConfiguredRepositoryData(GlobeConstants.APP_DATA_REPO_DIRECTORY + "**/*" + GlobalEsiConsts.configDataRepoCSV);
    //configureDefaultSchedulers(context);
    this.globalMarshalingHelper.dispatch(context);
    logger.info("Leave initializeMasterData");

    /*
  	Configuration schedulesConfig = configurationService.getByName(SchedulingConstants.CONFIG_SCHEDULES).orElse(null);
		if (null!= schedulesConfig){
			logger.info("The application schedules is configured already!!");
			return null;
		}

		Configuration systemConfig = configurationService.getByName(SchedulingConstants.CONFIG_SYSTEM_CONFIG).orElse(null);
		if (null==systemConfig){
			systemConfig = Configuration.builder()
					.name(SchedulingConstants.CONFIG_SYSTEM_CONFIG)
					.value(SchedulingConstants.CONFIG_SYSTEM_CONFIG_VALUE)
					.build();

			configurationService.saveAndFlush(systemConfig);
		}
		*/
		Context dataContext = Context.builder().build()
				.put(CommonConstants.SEPARATOR, CommonConstants.PIPELINE)
				.put(CommonConstants.SKIP_LINES, 0)
				.put(CommonConstants.IO_DATA_CONTAINER, ioContainer);
		DataAdapter csvDataAdapter = DataMarshallingFactory.getAdapter(MarshallingProvider.DATA_PROVIDER_CSV);
		return csvDataAdapter.marshal(dataContext);
  }

  private IOContainer loadRepositoryResources(String path) {
  	IOContainer ioContainer = null;

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
    			/*attachment = this.attachmentService.getByName(config.getValueExtended()).orElse(null);
        	if (CommonUtility.isNotEmpty(attachment)){
        		System.out.println(attachment.getId());
        	}*/
    		}
    		ioContainer = this.globalDmxManager.unarchive(archivedNames);
    	}
    	/*attachment = this.attachmentService.getByName(configuration.getName()).orElse(null);
    	if (CommonUtility.isNotEmpty(attachment))
    		return null;*/
  	}

    ioContainer = this.globalMarshallingRepository.resourcesAsIOContainer(path);
    if (null==ioContainer) {
    	logger.info("There is no data in path: " + path);
    	return null;
    }

  	Collection<String> archivedAttachmentNames = this.globalDmxManager.archive(ioContainer);
  	this.configurationService.saveAndFlush(Configuration.builder()
    	.group(GlobeConstants.CACHE_REPOSITORY)
    	.name(GlobeConstants.CACHE_DISPATCH_REPOSITORY)
    	.value(GlobeConstants.RAISED)
    	.valueExtended(String.join(GlobeConstants.PIPELINE, archivedAttachmentNames))
    	.info("Cache data for repository!")
    	.build()
    );
    return ioContainer;
  }

  private Configuration loadInitialConfigurations() {
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

  private IOContainer reserve(String path){
  	IOContainer ioContainer = null;

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
      ioContainer = this.globalMarshallingRepository.resourcesAsIOContainer(path);
      if (null==ioContainer) {
      	logger.info("There is no data in path: " + path);
      	return null;
      }

    	Collection<String> archivedAttachmentNames = this.globalDmxManager.archive(ioContainer);
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
  }
}