package com.akamai.nse.siphocore.connectors.jdbc

import scala.collection.immutable.ListMap

trait Table extends Serializable {

  def truncate: ListMap[String,String]
  def select:   ListMap[String,String]
  def filter(setValue:String):   ListMap[String,String]
  def insert(setValue:String):   ListMap[String,String]
  def update(setValue:String):   ListMap[String,String]
}
