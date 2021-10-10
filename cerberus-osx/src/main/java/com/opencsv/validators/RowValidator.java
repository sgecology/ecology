package com.opencsv.validators;

import com.opencsv.exceptions.CsvValidationException;

/**
 * This is the interface for validators for an array of {@link String}s read by
 * the {@link com.opencsv.CSVReader} after it is processed.
 * This should only be used if you have a very good understanding and full
 * control of the data being processed. Good examples of a RowValidator would
 * be to ensure the number of columns in the row or, if you know what data are
 * in which column, check the format and type of data.
 *
 * @author Scott Conway
 * @since 5.0
 */
public interface RowValidator {
    /**
     * Performs the validation check on the string and returns the result.
     * While not called directly in opencsv it is in the interface to provide
     * an easy way to test if the validator is functioning properly.
     *
     * @param row Array of strings to be validated
     * @return {@code true} if the row is valid, {@code false} otherwise
     */
    boolean isValid(String[] row);

    /**
     * Performs the validation check on the row (an array of {@link String}s)
     * and throws an exception if invalid.
     *
     * @param row Array of {@link String}s to be validated.
     * @throws CsvValidationException Thrown if invalid. Should contain a
     * message describing the error.
     */
    void validate(String[] row) throws CsvValidationException;
}
