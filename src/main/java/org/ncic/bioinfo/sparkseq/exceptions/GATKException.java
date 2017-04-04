package org.ncic.bioinfo.sparkseq.exceptions;

/**
 * Author: wbc
 */
public class GATKException extends RuntimeException {
    public GATKException(String msg) {
        super(msg);
    }

    public GATKException(String message, Throwable throwable) {
        super(message, throwable);
    }
}