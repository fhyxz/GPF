package org.ncic.bioinfo.sparkseq.exceptions

/**
  * Author: wbc
  */
class ResourceSetException(resourceKey: String)
  extends ResourceException("Output resource is already set:" + resourceKey){

}
