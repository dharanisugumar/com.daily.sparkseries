package org.apache.flink.util

import org.apache.flink.annotation.Public


/** Base class of all Flink-specific unchecked exceptions. */
@Public
@SerialVersionUID(193141189399279147L)
class FlinkRuntimeException extends RuntimeException {
  /**
   * Creates a new Exception with the given message and null as the cause.
   * @param message The exception message
   */
  def this(message: String) {
    this()
  //  super(message)
  }

  /**
   * Creates a new exception with a null message and the given cause.
   * @param cause The exception that caused this exception
   */
  def this(cause: Throwable) {
    this()
  //  super (cause)
  }

  /**
   * Creates a new exception with the given message and cause.
   * @param message The exception message
   * @param cause   The exception that caused this exception
   */
  def this(message: String, cause: Throwable) {
    this()
  //  super (message, cause)
  }
}