/**
 * 
 */
package net.ecology.osx.model;

import net.ecology.osx.model.OfficeDocumentType;

/**
 * @author ducbq
 *
 */
public enum OfficeDocumentType {
	HSSF_WORKBOOK, 
	XSSF_WORKBOOK, 
	INVALID;

	public static boolean isExcelDocument(OfficeDocumentType officeDocumentType) {
		return (HSSF_WORKBOOK.equals(officeDocumentType) || XSSF_WORKBOOK.equals(officeDocumentType));
	}
}
