package com.la.receta.config

import org.slf4j.{Logger, LoggerFactory}

trait ApplicationLogger {

  lazy val className: String =
    if (this.getClass.getCanonicalName != null)
      this.getClass.getCanonicalName
    else "none"

  val log: Logger = LoggerFactory.getLogger(className)
}
