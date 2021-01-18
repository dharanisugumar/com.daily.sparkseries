package com.akamai.nse.siphocore.connectors.jdbc

import java.sql.SQLException

import scala.collection.immutable.ListMap

trait Table extends Serializable {

  @throws(classOf[SQLException])
  def truncate: ListMap[String,String]

  @throws(classOf[SQLException])
  def select:   ListMap[String,String]

  @throws(classOf[SQLException])
  def filter(setValue:String):   ListMap[String,String]

  @throws(classOf[SQLException])
  def insert(setValue:String):   ListMap[String,String]

  @throws(classOf[SQLException])
  def update(setValue:String):   ListMap[String,String]
}
