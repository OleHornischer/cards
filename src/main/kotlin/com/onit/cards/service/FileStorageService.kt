package com.onit.cards.service

interface FileStorageService {
    fun storeFile(id: String, fileData: String): Unit
    fun loadFile(id: String): String
    fun deleteFile(id: String)
}