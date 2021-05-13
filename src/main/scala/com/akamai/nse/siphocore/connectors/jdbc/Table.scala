package com.akamai.nse.siphocore.connectors.jdbc

import java.sql.SQLException
import com.akamai.nse.siphocore.common.service.AbstractServiceTrait
import scala.collection.immutable.ListMap

/** Package for Table
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

trait Table extends AbstractServiceTrait with Serializable {

  @throws(classOf[SQLException])
  def truncate: ListMap[String,String]

  @throws(classOf[SQLException])
  def select:   ListMap[String,String]

  @throws(classOf[SQLException])
  def filter(setValue:Long):   ListMap[String,String]

  @throws(classOf[SQLException])
  def insert(setValue:Long):   ListMap[String,String]

  @throws(classOf[SQLException])
  def update(setValue:Long):   ListMap[String,String]

  @throws(classOf[SQLException])
  def execute(setVal1:Long, setVal2:Int, setVal3:Int):   ListMap[String,String]

  @throws(classOf[SQLException])
  def execute(setVal:Long):   ListMap[String,String]
}
