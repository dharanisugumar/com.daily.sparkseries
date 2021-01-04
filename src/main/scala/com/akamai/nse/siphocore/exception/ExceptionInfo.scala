package com.akamai.nse.siphocore.exception

import java.io.FileNotFoundException

object Exception  {


//  class FileNotFoundException extends FileNotFoundException
//  class IOException extends IOException


  private var exception = null
  private var name = null
  private var className = null
  private var methodName = null
  private var fileName = null
  private var lineNumber = 0
  private var arguments = null


  override def toString: String = "ExceptionInfo [exception=" + exception + ", name=" + name + ", className=" + className + ", methodName=" + methodName + ", fileName=" + fileName + ", lineNumber=" + lineNumber + ", arguments=" + arguments + "]"
}

