package com.onit.cards.service

import org.springframework.stereotype.Service

@Service("awsFileStorageService")
class AWSFileStorageService : FileStorageService {

    override fun storeFile(id: String, fileData: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun loadFile(id: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun deleteFile(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}