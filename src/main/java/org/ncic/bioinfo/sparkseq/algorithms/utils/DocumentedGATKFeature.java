package org.ncic.bioinfo.sparkseq.algorithms.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: wbc
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DocumentedGATKFeature {
    /**
     * Should we actually document this feature, even though it's annotated?
     */
    public boolean enable() default true;

    /**
     * The overall group name (walkers, readfilters) this feature is associated with
     */
    public String groupName();

    /**
     * A human readable summary of the purpose of this group of features
     */
    public String summary() default "";

    /**
     * Are there links to other docs that we should include?  CommandLineGATK.class for walkers, for example?
     */
    public Class[] extraDocs() default {};

    /**
     * Who is the go-to developer for operation/documentation issues?
     */
    public String gotoDev() default "NA";
}
