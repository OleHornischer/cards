package com.onit.cards.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service("fileStorageServiceFactory")
class FileStorageServiceFactory {


    val log: Logger = LoggerFactory.getLogger("FileStorageServiceFactory")

    @Autowired
    lateinit var localFileStorageService: LocalFileStorageService
    @Autowired
    lateinit var awsFileStorageService: AWSFileStorageService

    @Value("\${environment}")
    private val environment: String? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getFileStorageService(): FileStorageService {
        log.info("Found environment " + environment)
        when (environment?.toLowerCase()) {
            "local" -> return localFileStorageService
            "aws" -> return awsFileStorageService
            null -> {
                log.warn("Missing configuration environment. Using local als default")
                return localFileStorageService
            }
            else -> throw IllegalArgumentException("Found unknown environment $environment")
        }
    }
}