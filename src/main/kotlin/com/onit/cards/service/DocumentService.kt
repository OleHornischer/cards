package com.onit.cards.service

import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.onit.cards.model.Card
import net.glxn.qrgen.core.image.ImageType
import net.glxn.qrgen.javase.QRCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URISyntaxException
import java.nio.file.Paths
import java.util.stream.Stream


interface DocumentService {
    fun getQrCodesForGame(gameId: String): ByteArray
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Service("documentService")
class DocumentServiceImpl : DocumentService {

    val log: Logger = LoggerFactory.getLogger("DocumentService")

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    lateinit var cardService: CardService


    @Value("\${qr.code.size.px}")
    private val qrCodeSize: String? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun getQrCodesForGame(gameId: String): ByteArray {
        val cards = cardService.findAllCardsForGame(gameId)
        val document = Document()

        val byteArrayOutputStream = ByteArrayOutputStream()

        PdfWriter.getInstance(document, byteArrayOutputStream)

        document.open()
        val table = PdfPTable(2)
        addTableHeader(table)
        addRows(cards, table)

        document.add(table)
        document.close()
        return byteArrayOutputStream.toByteArray()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun addTableHeader(table: PdfPTable) {
        Stream.of("Card title", "QR code")
                .forEach { columnTitle ->
                    val header = PdfPCell()
                    header.backgroundColor = BaseColor.LIGHT_GRAY
                    header.borderWidth = 2f
                    header.phrase = Phrase(columnTitle)
                    table.addCell(header)
                }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun addRows(cards: List<Card>, table: PdfPTable) {
        cards.forEach {
            table.addCell(it.title)
            val img = Image.getInstance(QRCode.from(it.id)
                    .withSize(qrCodeSize!!.toInt(), qrCodeSize.toInt())
                    .to(ImageType.PNG).file().absolutePath)

            val imageCell = PdfPCell(img)
            table.addCell(imageCell)
        }
    }
}