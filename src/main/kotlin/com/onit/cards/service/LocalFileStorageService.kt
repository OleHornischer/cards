package com.onit.cards.service

import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths

@Service("localFileStorageService")
class LocalFileStorageService : FileStorageService {

    val cardImageDirectory: String = "Cards_Images"

    override fun storeFile(id: String, fileData: String) {
        val file = getCardImageFilePath(id)
        Files.writeString(file, fileData)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun loadFile(id: String): String {
        val file = getCardImageFilePath(id)
        return Files.readString(file)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun deleteFile(id: String) {
        val file = getCardImageFilePath(id)
        Files.deleteIfExists(file)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun getCardImageFilePath(id: String) =
            Paths.get(System.getProperty("java.io.tmpdir"), cardImageDirectory, "$id.cards")
}