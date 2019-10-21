package com.onit.cards.service

import com.onit.cards.data.TranslationRepository
import com.onit.cards.model.Translation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface TranslationService {
    fun findTranslation(translationId: String): Translation?
    fun findAllTranslationsForGame(gameId: String): List<Translation>
    fun saveTranslation(translation: Translation): Translation
    fun deleteTranslation(translation: Translation)
}

@Service("translationService")
class TranslationServiceImpl : TranslationService {

    @Autowired
    lateinit var translationRepository: TranslationRepository

    override fun findTranslation(translationId: String): Translation? = translationRepository.findByIdOrNull(translationId)

    override fun findAllTranslationsForGame(gameId: String): List<Translation> = translationRepository.findAllByGameId(gameId)

    override fun saveTranslation(translation: Translation): Translation = translationRepository.save(translation)

    override fun deleteTranslation(translation: Translation) = translationRepository.delete(translation)
}