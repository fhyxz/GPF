package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.Cigar;
import htsjdk.samtools.CigarElement;
import htsjdk.samtools.CigarOperator;
import htsjdk.samtools.SAMRecord;

import java.util.Iterator;

/**
 * Author: wbc
 */
public class BadCigarFilter extends Filter {

    public boolean filterOut(final SAMRecord rec) {
        final Cigar c = rec.getCigar();

        // if there is no Cigar then it can't be bad
        if( c.isEmpty() ) {
            return false;
        }

        Iterator<CigarElement> elementIterator = c.getCigarElements().iterator();

        CigarOperator firstOp = CigarOperator.H;
        while (elementIterator.hasNext() && (firstOp == CigarOperator.H || firstOp == CigarOperator.S)) {
            CigarOperator op = elementIterator.next().getOperator();

            // No reads with Hard/Soft clips in the middle of the cigar
            if (firstOp != CigarOperator.H && op == CigarOperator.H) {
                return true;
            }
            firstOp = op;
        }

        // No reads starting with deletions (with or without preceding clips)
        if (firstOp == CigarOperator.D) {
            return true;
        }

        boolean hasMeaningfulElements = (firstOp != CigarOperator.H && firstOp != CigarOperator.S);
        boolean previousElementWasIndel = firstOp == CigarOperator.I;
        CigarOperator lastOp = firstOp;
        CigarOperator previousOp = firstOp;

        while (elementIterator.hasNext()) {
            CigarOperator op = elementIterator.next().getOperator();

            if (op != CigarOperator.S && op != CigarOperator.H) {

                // No reads with Hard/Soft clips in the middle of the cigar
                if (previousOp == CigarOperator.S || previousOp == CigarOperator.H)
                    return true;

                lastOp = op;

                if (!hasMeaningfulElements && op.consumesReadBases()) {
                    hasMeaningfulElements = true;
                }

                if (op == CigarOperator.I || op == CigarOperator.D) {

                    // No reads that have consecutive indels in the cigar (II, DD, ID or DI)
                    if (previousElementWasIndel) {
                        return true;
                    }
                    previousElementWasIndel = true;
                }
                else {
                    previousElementWasIndel = false;
                }
            }
            // No reads with Hard/Soft clips in the middle of the cigar
            else if (op == CigarOperator.S && previousOp == CigarOperator.H) {
                return true;
            }

            previousOp = op;
        }

        // No reads ending in deletions (with or without follow-up clips)
        // No reads that are fully hard or soft clipped
        return lastOp == CigarOperator.D || !hasMeaningfulElements;
    }

}
