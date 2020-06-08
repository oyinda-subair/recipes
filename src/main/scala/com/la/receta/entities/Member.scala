package com.la.receta.entities

import org.joda.time.DateTime

case class Member(userId: String,
                  username: String,
                  email: String,
                  password: String,
                  timestampCreated: DateTime,
                  timestampUpdated: Option[DateTime]
                 )
