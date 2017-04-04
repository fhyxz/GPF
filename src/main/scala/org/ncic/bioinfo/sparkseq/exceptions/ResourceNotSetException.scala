package org.ncic.bioinfo.sparkseq.exceptions

/**
  * Author: wbc
  */
class ResourceNotSetException(resourceKey: String)
  extends ResourceException("Input resource not defined: " + resourceKey) {

}
