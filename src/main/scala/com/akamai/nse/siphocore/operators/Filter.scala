package com.akamai.nse.siphocore.operators

import org.apache.flink.api.common.functions.RichFilterFunction

class Filter extends RichFilterFunction{
  override def filter(t: Nothing): Boolean = ???
}
