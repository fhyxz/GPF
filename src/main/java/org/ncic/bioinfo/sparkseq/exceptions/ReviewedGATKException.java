package org.ncic.bioinfo.sparkseq.exceptions;

/**
 * Author: wbc
 */
public class ReviewedGATKException extends GATKException {

    public ReviewedGATKException(String msg) {
        super(msg);
    }

    public ReviewedGATKException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
