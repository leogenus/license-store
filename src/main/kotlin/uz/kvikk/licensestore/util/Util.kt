package uz.kvikk.licensestore.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class Log {
    val log: Logger = LoggerFactory.getLogger(this::class.java.declaringClass)
}