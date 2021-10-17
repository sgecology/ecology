/*
* Copyright 2017, Bui Quy Duc
* by the @authors tag. See the LICENCE in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package net.ecology.entity.crx;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.entity.general.Currency;
import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * A user.
 * 
 * @author bqduc
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "activity")
@EqualsAndHashCode(callSuper = true)
public class Activity extends RepoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6871420675191370015L;

	@NotNull
	private String subject;

	@NotNull
	private String target;

	@NotNull
	private String source;

	@NotNull
	private String purpose;
	
	@Size(max = 500)
	@Column(name=GlobeConstants.PROP_NAME)
	private String name;
	
	@Size(max = 150)
	@Column(name="regarding")
	private String regarding;

	@Lob
	@Column(name = "info", columnDefinition = "TEXT")
	@Type(type = "org.hibernate.type.TextType")
	private String info;

	@Column(name = "issue_date")
	private Date dueDate;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Activity parent;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private GeneralCatalogue type;

	@ManyToOne
	@JoinColumn(name = "priority_id")
	private GeneralCatalogue priority;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private GeneralCatalogue category;

	@ManyToOne
	@JoinColumn(name = "sub_category_id")
	private GeneralCatalogue subCategory;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private UserAccountProfile owner;

	@Column(name = "cost")
	private BigDecimal cost;

	@ManyToOne
	@JoinColumn(name = "currency_id")
	private Currency currency;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "attachment1", columnDefinition="TEXT")
	private String attachment1;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "attachment2", columnDefinition="TEXT")
	private String attachment2;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "attachment3", columnDefinition="TEXT")
	private String attachment3;

	public String getName() {
		return name;
	}

	public Activity getParent() {
		return parent;
	}
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRegarding() {
		return regarding;
	}

	public void setRegarding(String regarding) {
		this.regarding = regarding;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public GeneralCatalogue getType() {
		return type;
	}

	public void setType(GeneralCatalogue type) {
		this.type = type;
	}

	public GeneralCatalogue getPriority() {
		return priority;
	}

	public void setPriority(GeneralCatalogue priority) {
		this.priority = priority;
	}

	public GeneralCatalogue getCategory() {
		return category;
	}

	public void setCategory(GeneralCatalogue category) {
		this.category = category;
	}

	public GeneralCatalogue getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(GeneralCatalogue subCategory) {
		this.subCategory = subCategory;
	}

	public UserAccountProfile getOwner() {
		return owner;
	}

	public void setOwner(UserAccountProfile owner) {
		this.owner = owner;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getAttachment1() {
		return attachment1;
	}

	public void setAttachment1(String attachment1) {
		this.attachment1 = attachment1;
	}

	public String getAttachment2() {
		return attachment2;
	}

	public void setAttachment2(String attachment2) {
		this.attachment2 = attachment2;
	}

	public String getAttachment3() {
		return attachment3;
	}

	public void setAttachment3(String attachment3) {
		this.attachment3 = attachment3;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
}
