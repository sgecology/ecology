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

package net.ecology.entity.options;

/**
 * Option key bilgilerini tutar.
 * @author sinan.yumak
 *
 */
public interface OptionKey {
	/**
	 * Optionın değerini döndürür.
	 * @return option value
	 */
	String getValue();
	
	/**
	 * Option için varsayılan değeri döndürür.
	 * @return default option value
	 */
	String getDefaultValue();

}
