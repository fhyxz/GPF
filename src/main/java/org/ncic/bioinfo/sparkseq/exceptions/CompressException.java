package org.ncic.bioinfo.sparkseq.exceptions;

/**
 * Author: wbc
 */
public class CompressException extends GATKException {
    public CompressException() {
        super("Error when compress");
    }

    public CompressException(String info) {
        super(info);
    }
}
