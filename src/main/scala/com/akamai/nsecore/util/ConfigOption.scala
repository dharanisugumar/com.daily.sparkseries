package com.akamai.nsecore.util

abstract class ConfigOption[T] {
  def apply(key: String, value: T): ConfigOption[T] = ???


  var key: String
  var defaultValue:T
  var description: String

  def this(key: String, defaultValue: T,description:String) {
    this()
    this.key = checkNotNull(key)
    this.description
    this.defaultValue = defaultValue

  }

  def checkNotNull[T](reference: T): T = if (reference == null) throw new NullPointerException
  else reference

  def withDescription(description: String): String = {
    description
  }

  def key(key: String): OptionBuilder = {
    checkNotNull(key)
    new OptionBuilder(key)
  }

  final class OptionBuilder private[Configuration](val key: String) {
    def defaultValue[T](value: T): ConfigOption[T] = {
      checkNotNull(value)
      ConfigOption.this(this.key, value)
    }

    //def noDefaultValue = new ConfigOption[T](this.key, null.asInstanceOf[Any])
  }

  override def toString: String = String.format("Key: '%s' , default: %s ", this.key, this.defaultValue)
}


