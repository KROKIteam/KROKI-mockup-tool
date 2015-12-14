package adapt.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.OutputRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Header;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import adapt.core.AppCache;
import adapt.enumerations.OpenedAs;
import adapt.enumerations.PanelType;
import adapt.model.ejb.AbstractAttribute;
import adapt.model.ejb.EntityBean;
import adapt.model.panel.AdaptStandardPanel;
import adapt.util.pdf.AdaptPDFEvent;
import adapt.util.repository_utils.RepositoryPathsUtil;
import adapt.util.xml_readers.PanelReader;

public class PrintResource extends BaseResource {

	String pdfLocation = RepositoryPathsUtil.getRepositoryRootPath() + File.separator + "static_files";
	String resourceName;
	
	public PrintResource(Context context, Request request, Response response) {
		super(context, request, response);
//		getVariants().add(new Variant(MediaType.APPLICATION_PDF));
	}

	@Override
	public void handleGet() {
		String columnNames = getQuery().getValues("names");
		String resourceId = getQuery().getValues("resource");
		
		AppCache.displayTextOnMainFrame("[PRINT RESOURCE] resource: " + resourceId, 0);
		AppCache.displayTextOnMainFrame("[PRINT RESOURCE] columns: "  + columnNames, 0);
		if(columnNames != null && resourceId != null) {
			resourceName = resourceId;
			buildReport(resourceId, columnNames);
		}
		addToDataModel("css", "messageOk");
		addToDataModel("message", "Cao cao.");
		super.handleGet();
	}

	private Document buildReport(String resourceName, String columns) {
		try {
			Font tileFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Font textFont = new Font(Font.FontFamily.HELVETICA, 14);
			
			Document pdfDocument = new Document();
			PdfWriter writer = PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdfLocation + File.separator + resourceName + ".pdf"));
			AdaptPDFEvent event = new AdaptPDFEvent();
			writer.setPageEvent(event);
			pdfDocument.open();

			AdaptStandardPanel panel = (AdaptStandardPanel) PanelReader.loadPanel(resourceName, PanelType.STANDARDPANEL, null, OpenedAs.DEFAULT);
			
			Paragraph preface = new Paragraph();
			addEmptyLine(preface, 3);
			Paragraph title = new Paragraph(panel.getLabel(), tileFont);
			title.setAlignment(Element.ALIGN_CENTER);
			preface.add(title);
			
			Paragraph dataParagraph = new Paragraph();
			addEmptyLine(dataParagraph, 3);
			buildPDFTable(panel, columns.split(";"), dataParagraph);
			preface.add(dataParagraph);

			pdfDocument.add(preface);
			pdfDocument.close();
			return pdfDocument;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void buildPDFTable(AdaptStandardPanel panel, String[] selectedColumns, Paragraph tableParagraph) {
		PdfPTable table = new PdfPTable(selectedColumns.length);
		Font headerFont = new Font(Font.FontFamily.HELVETICA, 12);
		Font cellFont = new Font(Font.FontFamily.HELVETICA, 12);
		headerFont.setColor(BaseColor.WHITE);
		
		EntityBean bean = panel.getEntityBean();
		for (AbstractAttribute attribute : bean.getAttributes()) {
			if(isInArray(attribute.getFieldName(), selectedColumns)) {
				PdfPCell c1 = new PdfPCell(new Phrase(attribute.getLabel(), headerFont));
			    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			    c1.setBackgroundColor(new BaseColor(52, 103, 137));
			    table.addCell(c1);
			}
		}
		
		for (String columnName : selectedColumns) {
			PdfPCell c1 = new PdfPCell(new Phrase("TEST"));
		    c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(c1);
		}
		table.setHeaderRows(1);
		tableParagraph.add(table);
	}
	
	@Override
	public Representation represent(Variant variant) throws ResourceException {
		addToDataModel("message", "OK");
		addToDataModel("css", "messageOk");
		return getHTMLTemplateRepresentation("popupTemplate.html", dataModel);
	}
	
	private boolean isInArray(String column, String[] columns) {
		for (String name : columns) {
			if(column.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	private static void addEmptyLine(Paragraph paragraph, int number) {
	    for (int i = 0; i < number; i++) {
	      paragraph.add(new Paragraph(" "));
	    }
	  }
}