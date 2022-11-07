package com.akamai.nse.siphocore.exception

import java.io.FileNotFoundException
import java.sql.SQLException
import com.akamai.nse.siphocore.common.JsonUtil

/** Package for SiphoCustomException
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

case class ExceptionMessage(message: String,className:String,methodName:String)
case class ExceptionFileMessage(message: String,className:String,methodName:String,fileName:String)
case class ExceptionRunTimeMessage(message: String,className:String,methodName:String)
case class ExceptionNullPointerMessage(message: String,className:String,methodName:String)

sealed trait SiphoCustomException {
  self: Throwable => def getMessage: String

}

case class SiphoException(message: String,className:String,methodName:String) extends Exception with SiphoCustomException {

  throw new Exception() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName))
  }
}

case class SiphoFileException(message: String,className:String,methodName:String,fileName:String) extends Exception with SiphoCustomException {

  throw new FileNotFoundException with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionFileMessage(message, className, methodName, fileName))
  }

}


case class SiphoRunTimeException(message: String,className:String,methodName:String,fileName:String) extends Exception with SiphoCustomException {

  throw new RuntimeException() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionRunTimeMessage(message, className, methodName))
  }

}

case class SiphoSqlException(message: String,className:String,methodName:String) extends Exception with SiphoCustomException {

  throw new SQLException() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName))
  }
}

case class SiphoNullPointerException(message: String,className:String,methodName:String) extends Exception with SiphoCustomException {

  throw new NullPointerException() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName))
  }
}


case class SiphoKeyNotFoundException(message: String,className:String,methodName:String) extends Exception with SiphoCustomException {

  throw new Exception() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionMessage(message, className, methodName))
  }
}

case class SiphoClassNotFoundException(message: String,className:String,methodName:String,fileName:String) extends Exception with SiphoCustomException {

  throw new ClassNotFoundException() with SiphoCustomException {

    override val getMessage = JsonUtil.toJson(ExceptionFileMessage(message, className, methodName, fileName))
  }

}