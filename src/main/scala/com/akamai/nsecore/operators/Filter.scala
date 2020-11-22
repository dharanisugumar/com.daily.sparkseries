package com.akamai.nsecore.Operators

import org.apache.flink.api.common.functions.RichFilterFunction

class Filter extends RichFilterFunction{
  override def filter(t: Nothing): Boolean = ???
}
