package com.bolsadeideas.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LocaleResolver localeResolver;
	

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {


		Factura factura =(Factura)model.get("factura");
		//Locale locale = localeResolver.resolveLocale(request);
		
		PdfPTable tabla = new PdfPTable(1);
		
		PdfPCell cell = null;
		cell =new PdfPCell(new Phrase("Datos del Cliente"));
		//cell =new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datos.cliente",null, locale)));	
		cell.setBackgroundColor(new Color(184,218,255));
		
		tabla.addCell(cell);
		tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla.addCell(factura.getCliente().getEmail());
		
		PdfPTable tabla1=new PdfPTable(1);
		cell =new PdfPCell(new Phrase("Datos de la factura"));		
		cell.setBackgroundColor(new Color(195,230,203));
		tabla1.addCell(cell);
		tabla1.addCell("Folio: " + factura.getId());
		tabla1.addCell("Descripcion: " + factura.getDescripcion());
		tabla1.addCell("Fecha: " + factura.getCreateAt());
		
		document.add(tabla);
		document.add(tabla1);
		
		PdfPTable tabla3=new PdfPTable(4);
		tabla3.setWidths(new float[] {2.5f,1,1,1});
		tabla3.addCell("Producto");
		tabla3.addCell("Precio");
		tabla3.addCell("Cantidad");
		tabla3.addCell("Total");
		
		for(ItemFactura item:factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(item.getProducto().getPrecio().toString());
			cell =new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tabla3.addCell(cell);
			tabla3.addCell(item.calcularImporte().toString());
		}
		
		cell =new PdfPCell(new Phrase("Total: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(factura.getTotal().toString());
		
		
		document.add(tabla3);
		
	}
	

}
