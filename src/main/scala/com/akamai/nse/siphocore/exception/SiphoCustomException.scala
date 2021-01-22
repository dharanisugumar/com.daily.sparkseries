package com.akamai.nse.siphocore.exception

import java.io.FileNotFoundException

import com.akamai.nse.siphocore.common.JsonUtil


case class ExceptionMessage(message: String,className:String,methodName:String,lineNumber:Int)
case class ExceptionFileMessage(message: String,className:String,methodName:String,fileName:String,lineNumber:Int)
case class ExceptionRunTimeMessage(message: String,className:String,methodName:String,lineNumber:Int)
case class ExceptionNullPointerMessage(message: String,className:String,methodName:String,lineNumber:Int)

sealed trait SiphoCustomException {
  self: Throwable => def getMessage: String

}

case class SiphoException(message: String,className:String,methodName:String,lineNumber:Int) extends Exception with SiphoCustomException {

  throw new Exception() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName, lineNumber))
  }
}

case class SiphoFileException(message: String,className:String,methodName:String,fileName:String,lineNumber:Int) extends Exception with SiphoCustomException {

  throw new FileNotFoundException with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionFileMessage(message, className, methodName, fileName, lineNumber))
  }

}


case class SiphoRunTimeException(message: String,className:String,methodName:String,fileName:String,lineNumber:Int) extends Exception with SiphoCustomException {

  throw new RuntimeException() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionRunTimeMessage(message, className, methodName, lineNumber))
  }

}

case class SiphoSqlException(message: String,className:String,methodName:String,lineNumber:Int) extends Exception with SiphoCustomException {

  throw new Exception() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName, lineNumber))
  }
}

case class SiphoNullPointerException(message: String,className:String,methodName:String,lineNumber:Int) extends Exception with SiphoCustomException {

  throw new Exception() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName, lineNumber))
  }
}


case class SiphoKeyNotFoundException(message: String,className:String,methodName:String,lineNumber:Int) extends Exception with SiphoCustomException {

  throw new Exception() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName, lineNumber))
  }
}