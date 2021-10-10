/**
 * 
 */
package net.ecology.helper;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.common.CommonConstants;
import net.ecology.common.CommonUtility;
import net.ecology.domain.Context;
import net.ecology.domain.MarshallingProvider;
import net.ecology.entity.general.Catalogue;
import net.ecology.exceptions.CerberusException;
import net.ecology.factory.DataMarshallingFactory;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobeConstants;
import net.ecology.instruments.base.IAdapter;
import net.ecology.marshalling.MarshallingConstants;
import net.ecology.model.XWorksheet;
import net.ecology.service.general.CatalogueService;

/**
 * @author ducbq
 *
 */
@Component
public class CataloguesMarshallingHelper extends BasisComp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5096394289415355310L;

	@Inject
	private CatalogueService catalogueService;

	/*public void dispatch(Context context) throws CerberusException {
		logger.info("Enter GlobalMarshalingHelper.dispatch()");
		marshallingCatalogues(context);
		configureMasterSchedulers(context);
		logger.info("Leave GlobalMarshalingHelper.dispatch()");
	}*/

	/*
	private void configureMasterSchedulers(Context context) throws CerberusException {
    logger.info("Enter initSchedulers");
    Context ioContainer = (Context)context.get(Context.DEFAULT);

		IAdapter dataMarshaller = DataMarshallingFactory.builder().build().getAdapter(MarshallingProvider.PROVIDER_CSV);

		context.put(CommonConstants.IO_DATA_CONTAINER, ioContainer);
		if (context.containsKey(CommonConstants.SEPARATOR)){
			context.put(CommonConstants.SEPARATOR, CommonConstants.PIPELINE);
		}

		if (context.containsKey(CommonConstants.SKIP_LINES)){
			context.put(CommonConstants.SKIP_LINES, 1);
		}

		Context fetchedDataContext = dataMarshaller.marshall(context, (String)context.get(CommonConstants.SEPARATOR), (int)context.get(CommonConstants.SKIP_LINES));
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
	*/

	private void marshallingCatalogues(Context context) {
		String repoDir = (String)context.get(GlobeConstants.PROP_REPO_DIR);
		
		
		XWorksheet dataWorksheet = (XWorksheet) context.get(MarshallingConstants.REPO_CATALOGUES);
		if (null==dataWorksheet)
			return;

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
