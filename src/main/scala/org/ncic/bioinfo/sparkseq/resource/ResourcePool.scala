package org.ncic.bioinfo.sparkseq.resource

import org.ncic.bioinfo.sparkseq.exceptions.ResourceException

import scala.collection.mutable

/**
  * Author: wbc
  */
object ResourcePool {
  def apply(): ResourcePool = new ResourcePool()
}

class ResourcePool {

  // 维护两套hash，以hashMap为主，通过resource自带的key索引，hashSet用于快速值索引
  private val resourceMap: mutable.HashMap[String, Resource] = mutable.HashMap()
  private val resourceSet: mutable.HashSet[Resource] = mutable.HashSet()

  /**
    * 将资源添加进资源池
    * 如果有重复的key，则抛出异常
    *
    * @param resource 待添加的资源
    * @throws ResourceException
    */
  def addResource(resource: Resource): Unit = {
    if (resourceMap.contains(resource.key)) {
      throw new ResourceException("Same resource key: " + resource.key)
    }
    resourceMap.put(resource.key, resource)
    resourceSet.add(resource)
  }

  /**
    * 将资源添加进资源池，如果已经有重复的key，则替换原有的resource
    *
    * @param resource
    */
  def replaceResource(resource: Resource): Unit = {
    if (resourceMap.contains(resource.key)) {
      val resourceOld = resourceMap.get(resource.key).get
      //删除原有的resource，即使他们可能只是key相同
      resourceSet.remove(resourceOld)
    }
    resourceMap.put(resource.key, resource)
    resourceSet.add(resource)
  }

  def containsResourceKey(key: String): Boolean = {
    resourceMap.contains(key)
  }

  def containsResource(resource: Resource): Boolean = {
    resourceSet.contains(resource)
  }

  def getResourceByKey(key: String): Option[Resource] = {
    resourceMap.get(key)
  }

  def copy(): ResourcePool = {
    val newPool = ResourcePool()
    resourceSet.foreach(resource => newPool.addResource(resource))
    newPool
  }

}
