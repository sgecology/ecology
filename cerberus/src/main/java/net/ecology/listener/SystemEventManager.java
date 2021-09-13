/**
 * 
 */
package net.ecology.listener;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.opencsv.exceptions.CsvException;

import net.ecology.auth.GlobalMarshalingHelper;
import net.ecology.common.CommonConstants;
import net.ecology.common.DateTimeUtility;
import net.ecology.css.service.general.AttachmentService;
import net.ecology.dmx.manager.GlobalDmxManager;
import net.ecology.domain.entity.Attachment;
import net.ecology.entity.scheduler.SchedulePlan;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobeConstants;
import net.ecology.global.SchedulingConstants;
import net.ecology.globe.GlobalMarshallingRepository;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.model.osx.OSXConstants;
import net.ecology.repository.GlobalDataRepository;
import net.ecology.utility.CSVUtilityHelper;

/**
 * @author ducbq
 *
 */
@Component
public class SystemEventManager extends ComponentRoot {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7564662407839189753L;

	@Inject
	private GlobalMarshalingHelper globalMarshalingHelper;

  @Inject
  private GlobalDmxManager globalDmxManager;

  @Inject
  private AttachmentService attachmentService;

  @Inject
  private GlobalMarshallingRepository globalMarshallingRepository;
  
  @Inject
  private GlobalDataRepository globalDataRepository;

  @EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
    logger.info("Enter onApplicationReady");
		try {
			this.globalDataRepository.dispatch(Context.builder().build().put(GlobeConstants.CONTEXT_DISPATCH_REPO_PATH, GlobeConstants.REPO_DIRECTORY + GlobeConstants.ALL_CSV_FILES));
			//this.globalDataRepository.dispatch(Context.builder().build().put(GlobeConstants.REPO_DIRECTORY + GlobeConstants.ALL_CSV_FILES));
			//globalEsiRepository.dispatch(GlobeConstants.REPO_DIRECTORY);
		  //configureMasterData();
	    //this.globalMarshalingHelper.dispatch(Context.builder().build());
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("Leave onApplicationReady");
	}

  protected void configureMasterData() throws CerberusException {
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

	private void configureDefaultSchedulers(Context context) throws CerberusException {
    logger.info("Enter initSchedulers");
    IOContainer ioContainer = (IOContainer)context.get(Context.DEFAULT);
    List<String[]> csvContextData;
    
		try {
			csvContextData = CSVUtilityHelper.builder().build()
					.fetchCsvData(ioContainer.get(), CommonConstants.PIPELINE, 0);
		} catch (IOException | CsvException e) {
			throw new CerberusException (e);
		}
/*
    globalSchedulerService.initSchedulers(Context.builder().build()
    		.put(SchedulingConstants.CTX_SCHEDULE_PLANS, SchedulePlan.builder()
    				.code("20210215-SP-01")
            .name("Default schedule plan")
            .startTime(DateTimeUtility.getSystemDateTime())
            .jobType("System Job")
            .type("Root Schedule Plan")
            .build())
        .put(SchedulingConstants.CTX_JOB_SCHEDULE_ELEMENTS, csvContextData));

    */
    //Context context = prepareSchedulesData();
	  //globalSchedulerService.initSchedulers(context);
    //globalSchedulerService.start(context);
    logger.info("Leave initSchedulers");
	}

	private Context prepareSchedulesData() throws CerberusException {
		Context context = null;
		IOContainer ioDataContainer = null;
		CSVUtilityHelper csvUtility = null;
		List<String[]> csvContextData = null;
		try {
			ioDataContainer = this.globalMarshallingRepository.resourceAsIOContainer(GlobeConstants.REPO_DIRECTORY +"schedulers.osx");
	    csvUtility = CSVUtilityHelper.builder().build();
	    csvContextData = csvUtility.fetchCsvData(ioDataContainer.get(), CommonConstants.PIPELINE, 0);
	    context = new Context()
	    		.put(SchedulingConstants.CTX_SCHEDULE_PLANS, SchedulePlan.builder()
	    				.code("20210215-SP-01")
	            .name("Default schedule plan")
	            .startTime(DateTimeUtility.getSystemDateTime())
	            .jobType("System Job")
	            .type("Root Schedule Plan")
	            .build())
	        .put(SchedulingConstants.CTX_JOB_SCHEDULE_ELEMENTS, csvContextData);
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return context;
	}

	private Context configureRepositoryData() throws CerberusException {
		IOContainer ioContainer = this.globalMarshallingRepository.resourcesAsIOContainer(GlobeConstants.REPO_DIRECTORY +"**/*.osx");
		return Context.builder().build().put(ioContainer);//this.globalDmxManager.getRepositoryResourceContext(GlobeConstants.APP_DATA_REPO_DIRECTORY +"**/*.osx");
	}
}
