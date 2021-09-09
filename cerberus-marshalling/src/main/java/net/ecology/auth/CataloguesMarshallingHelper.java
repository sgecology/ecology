/**
 * 
 */
package net.ecology.auth;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.base.DataAdapter;
import net.ecology.common.CommonConstants;
import net.ecology.common.CommonUtility;
import net.ecology.entity.general.Catalogue;
import net.ecology.exceptions.CerberusException;
import net.ecology.factory.DataMarshallingFactory;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.global.GlobeConstants;
import net.ecology.globe.MarshallingConstants;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.model.MarshallingProvider;
import net.ecology.osx.model.DmxWorksheet;
import net.ecology.service.general.CatalogueService;

/**
 * @author ducbq
 *
 */
@Component
public class CataloguesMarshallingHelper extends ComponentRoot {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5096394289415355310L;

	@Inject
	private CatalogueService catalogueService;

	public void dispatch(Context context) throws CerberusException {
		logger.info("Enter GlobalMarshalingHelper.dispatch()");
		marshallingCatalogues(context);
		configureMasterSchedulers(context);
		logger.info("Leave GlobalMarshalingHelper.dispatch()");
	}

	private void configureMasterSchedulers(Context context) throws CerberusException {
    logger.info("Enter initSchedulers");
    IOContainer ioContainer = (IOContainer)context.get(Context.DEFAULT);

		DataAdapter dataMarshaller = DataMarshallingFactory.builder().build().getAdapter(MarshallingProvider.DATA_PROVIDER_CSV);

		context.put(CommonConstants.IO_DATA_CONTAINER, ioContainer);
		if (context.containsKey(CommonConstants.SEPARATOR)){
			context.put(CommonConstants.SEPARATOR, CommonConstants.PIPELINE);
		}

		if (context.containsKey(CommonConstants.SKIP_LINES)){
			context.put(CommonConstants.SKIP_LINES, 1);
		}

		Context fetchedDataContext = dataMarshaller.marshal(context);
		if (CommonUtility.isEmpty(fetchedDataContext) || fetchedDataContext.isEmpty())
			return;

		List<String[]> jobSchedulerElements = (List<String[]>)fetchedDataContext.get(Context.DEFAULT);
		if (CommonUtility.isEmpty(jobSchedulerElements))
			return;

    //Context context = prepareSchedulesData();
	  //globalSchedulerService.initSchedulers(context);
    //globalSchedulerService.start(context);
    logger.info("Leave initSchedulers");
	}

	private void marshallingCatalogues(Context context) {
		DmxWorksheet dataWorksheet = (DmxWorksheet) context.get(MarshallingConstants.CATALOGUE_DATA_FILE);
		List<?> catalogueParts = null;
		Catalogue catalogue = null;
		for (Object key : dataWorksheet.keys()) {
			catalogueParts = dataWorksheet.get(key);
			if (!this.catalogueService.exists(GlobeConstants.PROP_CODE, catalogueParts.get(1))) {
				catalogue = marshallCatalogue(catalogueParts);
				this.catalogueService.save(catalogue);
			}
		}
	}

	private Catalogue marshallCatalogue(List<?> parts) {
		Catalogue marshalledObj = Catalogue.builder().subtype((String) parts.get(0)).code((String) parts.get(1)).name((String) parts.get(2)).build();

		return marshalledObj;
	}
}
