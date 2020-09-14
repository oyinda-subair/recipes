package com.la.receta.config

import org.slf4j.LoggerFactory

trait Logger {

  lazy val className: String =
    if (this.getClass.getCanonicalName != null)
      this.getClass.getCanonicalName
    else "none"

  val log: org.slf4j.Logger = LoggerFactory.getLogger(className)
}
