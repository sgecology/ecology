/**
 * 
 */
package net.ecology.framework.controller;

import java.io.Serializable;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.ecology.domain.RestErrorInfo;
import net.ecology.domain.model.SearchCondition;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.service.IService;

/**
 * @author bqduc
 *
 */
public abstract class RestCoreController<T, PK extends Serializable> extends UIControllerRoot {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4267881118060120236L;
	protected static final String DEFAULT_PAGE_SIZE = "100";
	protected static final String DEFAULT_PAGE_NUM = "0";
	
	protected abstract IService<?, PK> getBusinessService();

	@RequestMapping(
			value = "/create", 
			method = RequestMethod.POST, 
			consumes = { "application/json", "application/xml" }, 
			produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(
			value = "Create a aquafeed resource.", 
			notes = "Returns the URL of the new resource in the Location header.")
	public @ResponseBody T createBusinessObject(
			@RequestBody T clientObject, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		return this.doCreateBusinessObject(clientObject);
	}

	@RequestMapping(value = "/fetch", method = RequestMethod.GET, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(
			value = "Get a paginated list of all aquafeeds.", 
			notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)")
	public @ResponseBody Page<T> fetchBusinessObjects(
			@ApiParam(value = "The page number (zero-based)", required = true) 
			@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
			@ApiParam(value = "Tha page size", required = true) 
			@RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
			HttpServletRequest request, HttpServletResponse response) {
		return this.doFetchBusinessObjects(page, size);
	}

	@RequestMapping(value = "/fetch/{id}", method = RequestMethod.GET, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get a single aquafeed.", notes = "You have to provide a valid aquafeed ID.")
	public @ResponseBody T fetchBusinessObject(@ApiParam(value = "The ID of the aquafeed.", required = true) @PathVariable("id") PK id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.doFetchBusinessObject(id);
	}

	@RequestMapping(
			value = "/search", 
			method = RequestMethod.POST, 
			consumes = { "application/json", "application/xml" }, 
			produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Get a list of business objects.", notes = "You have to provide a valid search conditions.")
	public @ResponseBody List<T> searchBusinessObjects(@RequestBody SearchCondition clientSearchConditions,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		return this.searchBusinessObjects(clientSearchConditions);
	}

	@RequestMapping(
			value = "/update/{id}", 
			method = RequestMethod.PUT, 
			consumes = { "application/json", "application/xml" }, 
			produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Request to update a business object.", notes = "You have to provide a valid business pobject ID in the URL and in the payload. The business object ID attribute can not be updated.")
	public @ResponseBody T updateBusinessObject(@ApiParam(value = "The ID of the existing aquafeed resource.", required = true) @PathVariable("id") Long id,
			@RequestBody T updatedClientObject, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		return this.doUpdateBusinessObject(updatedClientObject);
	}

	// TODO: @ApiImplicitParams, @ApiResponses
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = { "application/json", "application/xml" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete a aquafeed resource.", notes = "You have to provide a valid aquafeed ID in the URL. Once deleted the resource can not be recovered.")
	public void deleteBusinessObject(@ApiParam(value = "The ID of the existing aquafeed resource.", required = true) @PathVariable("id") Long id,
			HttpServletRequest request, HttpServletResponse response) {
		this.doDeleteBusinessObject(id);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DataFormatException.class)
	public @ResponseBody RestErrorInfo handleDataStoreException(DataFormatException ex, WebRequest request,
			HttpServletResponse response) {
		logger.info("Converting Data Store exception to RestResponse : " + ex.getMessage());

		return new RestErrorInfo(ex, "You messed up.");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CerberusException.class)
	public @ResponseBody RestErrorInfo handleResourceNotFoundException(CerberusException ex, WebRequest request,
			HttpServletResponse response) {
		logger.info("CerberusException handler:" + ex.getMessage());

		return new RestErrorInfo(ex, "Sorry I couldn't find it.");
	}

	// TODO: replace with exception mapping
	public static <T> T checkResourceFound(final T resource) {
		if (resource == null) {
			throw new CerberusException("resource not found");
		}
		return resource;
	}

	protected T doUpdateBusinessObject(T updatedClientObject) {
		return null;
	}

	protected Page<T> doFetchBusinessObjects(Integer page, Integer size) {
		return null;
	}

	protected T doFetchBusinessObject(PK id) {
		IService<?, PK> service = this.getBusinessService();
		if (null==service)
			return null;

		@SuppressWarnings("unchecked")
		T fetchedBizObject = (T) service.getObject(id);
		return fetchedBizObject;
	}

	protected List<T> searchBusinessObjects(SearchCondition searchCondition) {
		return null;
	}

	protected void doDeleteBusinessObject(Long id) {
	}

	protected T doCreateBusinessObject(T businessObject) {
		return null;
	}

	protected T createBusinessProxyObject(boolean isDummy) {
		return null;
	}
}
