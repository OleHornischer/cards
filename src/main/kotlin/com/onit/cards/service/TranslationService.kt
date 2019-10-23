package com.onit.cards.service

import com.onit.cards.data.TranslationRepository
import com.onit.cards.exception.ObjectNotFoundException
import com.onit.cards.model.Translation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface TranslationService {
    fun findTranslation(translationId: String): Translation?
    fun findAllTranslationsForGame(gameId: String): List<Translation>
    fun saveTranslation(translation: Translation): Translation
    fun deleteTranslation(translation: Translation)
    fun startSession(translationId: String)
}

@Service("translationService")
class TranslationServiceImpl : TranslationService {

    val log: Logger = LoggerFactory.getLogger("TranslationService")
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    lateinit var translationRepository: TranslationRepository

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun findTranslation(translationId: String): Translation? {
        val translation = translationRepository.findByIdOrNull(translationId)
        log.debug("Looked up translation for id " + translation)
        return translation
    }

    override fun findAllTranslationsForGame(gameId: String): List<Translation> {
        val translations = translationRepository.findAllByGameId(gameId)
        log.debug("Looked up " + translations.size + " translations for gameid " + gameId)
        return translations
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun saveTranslation(translation: Translation): Translation {
        val translation = translationRepository.save(translation)
        log.info("Saved translation with id " + translation.id)
        return translation
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun deleteTranslation(translation: Translation) {
        translationRepository.delete(translation)
        log.debug("Deleted translation with id " + translation)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun startSession(translationId: String) {
        val translation = translationRepository.findByIdOrNull(translationId)
        translation?.let { t -> translationRepository.save(t.copy(sessionCount = t.sessionCount + 1)) }
                ?: throw ObjectNotFoundException()
        log.info("Started session for translation id " + translationId)
    }
}