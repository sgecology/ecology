/**
 * 
 */
package net.ecology.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import net.ecology.auth.GlobalMarshalingHelper;
import net.ecology.base.DataAdapter;
import net.ecology.base.Marshaller;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonConstants;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.general.AttachmentService;
import net.ecology.dmx.manager.GlobalDmxManager;
import net.ecology.domain.entity.Attachment;
import net.ecology.entity.config.Configuration;
import net.ecology.entity.i18n.I18nLocale;
import net.ecology.exceptions.CerberusException;
import net.ecology.factory.DataMarshallingFactory;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;
import net.ecology.global.SchedulingConstants;
import net.ecology.globe.GlobalMarshallingRepository;
import net.ecology.lingual.service.LocaleService;
import net.ecology.marshal.ScheduleJobMarshaller;
import net.ecology.marshal.SchedulePlanMarshaller;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.model.MarshallingProvider;
import net.ecology.model.osx.OSXConstants;

/**
 * @author ducbq
 *
 */
@Component
public class GlobalEsiRepository extends ComponentRoot {
  /**
	 * 
	 */
	private static final long serialVersionUID = -559448591301536785L;
	@Inject
	protected ConfigurationService configurationService;

	@Inject
  private AttachmentService attachmentService;

  @Inject
  private GlobalMarshallingRepository globalMarshallingRepository;

  @Inject
	private GlobalMarshalingHelper globalMarshalingHelper;

  @Inject
  private GlobalDmxManager globalDmxManager;

  @Inject
  private LocaleService localeService;

  @Async
  public void dispatch(String path) throws CerberusException {
  	logger.info("Enter GlobalEsiRepository:dispatch");
  	Collection<I18nLocale> i18nLocales = dispatchInitialI18n();
  	dispatchInitialMessages(i18nLocales);
  	
  	Context dataContext = loadDataRepository(path);
  	System.out.println(dataContext);

  	loadSchedules(null);
  	loadSystemMasterData();
  	logger.info("Leave GlobalEsiRepository:dispatch");
	}

  private Context loadDataRepository(String path) {
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
		IOContainer ioContainer = this.globalMarshallingRepository.resourcesAsIOContainer(path);
		Context dataContext = Context.builder().build()
				.put(CommonConstants.SEPARATOR, CommonConstants.PIPELINE)
				.put(CommonConstants.SKIP_LINES, 0)
				.put(CommonConstants.IO_DATA_CONTAINER, ioContainer);
		DataAdapter csvDataAdapter = DataMarshallingFactory.getAdapter(MarshallingProvider.DATA_PROVIDER_CSV);
		return csvDataAdapter.marshal(dataContext);
  }

  protected void loadSystemMasterData() throws CerberusException {
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

    ///context = loadConfiguredRepositoryData(GlobeConstants.APP_DATA_REPO_DIRECTORY + "**/*" + GlobalEsiConsts.configDataRepoCSV);
    //configureDefaultSchedulers(context);
    this.globalMarshalingHelper.dispatch(context);
    logger.info("Leave initializeMasterData");
  }

	private void loadSchedules(List<String[]> dataRepoParts) throws CerberusException {
		Configuration schedulesConfig = configurationService.getByName(SchedulingConstants.CONFIG_SCHEDULES).orElse(null);
		if (null!= schedulesConfig){
			logger.info("The application schedules is configured already!!");
			return;
		}

		Configuration systemConfig = configurationService.getByName(SchedulingConstants.CONFIG_SYSTEM_CONFIG).orElse(null);
		if (null==systemConfig){
			systemConfig = Configuration.builder()
					.name(SchedulingConstants.CONFIG_SYSTEM_CONFIG)
					.value(SchedulingConstants.CONFIG_SYSTEM_CONFIG_VALUE)
					.build();

			configurationService.saveAndFlush(systemConfig);
		}

		Marshaller<?, ?> marshaller = DataMarshallingFactory.requestMarshaller(ScheduleJobMarshaller.class);
		System.out.println(marshaller);
		System.out.println(DataMarshallingFactory.requestMarshaller(SchedulePlanMarshaller.class));
		/*IOContainer ioContainer = this.globalMarshallingRepository.resourcesAsIOContainer(dataRepoPath);
		Context dataContext = Context.builder().build().put(ioContainer);
		DataAdapter csvDataAdapter = DataMarshallingFactory.getAdapter(MarshallingProvider.DATA_PROVIDER_CSV);
		csvDataAdapter.marshal(dataContext);

		schedulesConfig = Configuration.builder()
				.name(SchedulingConstants.CONFIG_SCHEDULES)
				.value(SchedulingConstants.CONFIG_SCHEDULES_VALUE)
				.parent(systemConfig)
				.build();

		configurationService.saveAndFlush(schedulesConfig);*/

	}

	//-----------------------------------------------------------------I18n-Locale-----------------------------------------------------------------
	private Collection<I18nLocale> dispatchInitialI18n() {
		final String languageProp = "language";
		Collection<I18nLocale> i18nLocales = CollectionsUtility.newCollection();
		logger.info("Enter dispatch initial I18n");
		I18nLocale i18nLocale = null;
		if (!this.localeService.exists(languageProp, GlobalConstants.USA.getLanguage())) {
			i18nLocale = I18nLocale.builder()
					.displayLanguage(GlobalConstants.USA.getDisplayLanguage())
					.language(GlobalConstants.USA.getLanguage())
					.build();
			this.localeService.saveAndFlush(i18nLocale);
			i18nLocales.add(i18nLocale);
		} else {
			i18nLocales.add(this.localeService.getLocale(GlobalConstants.USA.getLanguage()));
		}
		
		if (!this.localeService.exists(languageProp, GlobalConstants.VIETNAM.getLanguage())) {
			i18nLocale = I18nLocale.builder()
					.displayLanguage(GlobalConstants.VIETNAM.getDisplayLanguage())
					.language(GlobalConstants.VIETNAM.getLanguage())
					.build();
			this.localeService.saveAndFlush(i18nLocale);
			i18nLocales.add(i18nLocale);
		} else {
			i18nLocales.add(this.localeService.getLocale(GlobalConstants.VIETNAM.getLanguage()));
		}
		logger.info("Leave dispatch initial I18n");
		return i18nLocales;
	}

	private void dispatchInitialMessages(Collection<I18nLocale> i18nLocales) {
  	logger.info("Enter GlobalEsiRepository:dispatchInitialMessages");
  	logger.info("Enter GlobalEsiRepository:dispatchInitialMessages");
	}
	//-----------------------------------------------------------------End of I18n-Locale----------------------------------------------------------
}