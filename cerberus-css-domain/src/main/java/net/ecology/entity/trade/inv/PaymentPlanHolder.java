/*
 * Copyleft 2007-2011 Ozgur Yazilim A.S.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * www.tekir.com.tr
 * www.ozguryazilim.com.tr
 *
 */

package net.ecology.entity.trade.inv;

import java.util.Date;
import java.util.List;

import net.ecology.entity.general.MoneySet;
import net.ecology.entity.trade.InvoicePaymentPlanItem;
import net.ecology.entity.trade.PaymentPlan;

/**
 * Ödeme planı işlemleri için ortak arabirim...
 * 
 * @author sinan.yumak
 *
 */
public interface PaymentPlanHolder {

	PaymentPlan getPaymentPlan();

	//TODO: eğer sipariş için de ödeme planı kullanılacaksa, 
	//InvoicePaymentPlanItem modelini duruma uygun biçimde düzenlemek gerekecek.
	List<InvoicePaymentPlanItem> getPaymentPlanItems();

	/**
	 * Belge toplam tutarını döndürür.
	 * @return grand total
	 */
	MoneySet getGrandTotal();

	/**
	 * Belge toplam vergi tutarını döndürür.
	 * @return tax
	 */
	MoneySet getTotalTax();
	
	/**
	 * Belge toplam masraf tutarını döndürür.
	 * @return expense
	 */
	MoneySet getTotalExpense();
	
	/**
	 * Belge toplam harç tutarını döndürür.
	 * @return fee
	 */
	MoneySet getTotalFee();
	
	/**
	 * Belge vergi matrahını döndürür.
	 * @return tax excluded total
	 */
	MoneySet getTaxExcludedTotal();

	/**
	 * Belge tarihini döndürür.
	 * @return document date
	 */
	Date getDate();
	
}
