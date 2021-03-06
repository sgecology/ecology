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

package net.ecology.model;

/**
 * 
 * Ödeme planı tanıtımı için, hesaplamanın tutar olarak direkt veya 
 * oran bazında hesaplanacağının seçimi.
 * 
 * @author dumlupinar
 *
 */
public enum PaymentPlanCalcType {
	
	Rate,	//oran üzerinden
	Amount,	//tutar üzerinden
	Remaining //kalan

}
