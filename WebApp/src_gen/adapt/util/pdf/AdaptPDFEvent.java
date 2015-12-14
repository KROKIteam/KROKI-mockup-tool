package adapt.util.pdf;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import adapt.util.staticnames.Settings;

public class AdaptPDFEvent extends PdfPageEventHelper {

	public void onStartPage(PdfWriter writer, Document document) {
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(Settings.FULL_DATE_FORMAT);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(formatter.format(now)), 50, 800, 0);
    }

	// FOOTER: onEndPage
}