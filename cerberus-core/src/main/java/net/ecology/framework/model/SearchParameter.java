/**
 * 
 */
package net.ecology.framework.model;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.ecology.common.CollectionsUtility;

/**
 * @author bqduc
 *
 */
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class SearchParameter {
	private String platform;
	private String location;
	private String origin;
	private String objectCode;
	private String objectName;
	private LocalDate from;
	private LocalDate to;
	private Pageable pageable;
	private Model model;
	private String fromDate;
	private String toDate;
	private String keyword;

	@Builder.Default
	private Map<String, Object> parameterMap = CollectionsUtility.newHashedMap();

	public String getPlatform() {
		return platform;
	}
	public SearchParameter setPlatform(String platform) {
		this.platform = platform;
		return this;
	}
	public String getLocation() {
		return location;
	}
	public SearchParameter setLocation(String location) {
		this.location = location;
		return this;
	}
	public String getOrigin() {
		return origin;
	}
	public SearchParameter setOrigin(String origin) {
		this.origin = origin;
		return this;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public SearchParameter setObjectCode(String objectCode) {
		this.objectCode = objectCode;
		return this;
	}
	public String getObjectName() {
		return objectName;
	}
	public SearchParameter setObjectName(String objectName) {
		this.objectName = objectName;
		return this;
	}
	public LocalDate getFrom() {
		return from;
	}
	public SearchParameter setFrom(LocalDate from) {
		this.from = from;
		return this;
	}
	public LocalDate getTo() {
		return to;
	}
	public SearchParameter setTo(LocalDate to) {
		this.to = to;
		return this;
	}
	public Pageable getPageable() {
		return pageable;
	}
	public SearchParameter setPageable(Pageable pageable) {
		this.pageable = pageable;
		return this;
	}
	public Model getModel() {
		return model;
	}
	public SearchParameter setModel(Model model) {
		this.model = model;
		return this;
	}
	public String getFromDate() {
		return fromDate;
	}
	public SearchParameter setFromDate(String fromDate) {
		this.fromDate = fromDate;
		return this;
	}
	public String getToDate() {
		return toDate;
	}
	public SearchParameter setToDate(String toDate) {
		this.toDate = toDate;
		return this;
	}
	public static SearchParameter getInstance(){
		return new SearchParameter();
	}
	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}
	public SearchParameter setParameterMap(Map<String, Object> parameterMap) {
		this.parameterMap = parameterMap;
		return this;
	}

	public SearchParameter put(String key, Object value){
		this.parameterMap.put(key, value);
		return this;
	}

	public Object get(String key){
		return this.parameterMap.get(key);
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
