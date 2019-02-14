package com.leam.omplepecs;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;


public class OmplePECsController implements Initializable {

	@FXML
    TextField dir;

    @FXML
    private void getFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Seleccionar arxiu PEC");
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF", "*.PDF"));
    	File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            dir.setText(file.getAbsolutePath());
        } else {
            dir.setText("");
        }
    }

    @FXML
    private void pbGenerar(ActionEvent event) {
    	
    	if (!dir.getText().isEmpty()) {
    		// original PEC file
    		File pdfFile = new File(dir.getText());
		
			// generated PDF file
			File destFile = new File(pdfFile.getParent(), pdfFile.getName().replaceAll(".pdf", "_camps.pdf"));		
			if (destFile.exists()) destFile.delete();		// if the file exists, delete to replace
		
			try {
				// original PDF form
	            PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
	            AcroFields form = reader.getAcroFields();
	            // destination PDF form - NEW
	            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
	            AcroFields dest = stamper.getAcroFields();
	            
	            List<String> names = new ArrayList<>();
	
	            if (form.getFields().size()>0) { 
	                for (String key : form.getFields().keySet()) {
	                	// ignore APE1, APE2, NOMBRE, DNI, HONOR, COMENT fields
	                	if (!key.equalsIgnoreCase("APE1") & !key.equalsIgnoreCase("APE2") & !key.equalsIgnoreCase("NOMBRE") &
	                		!key.equalsIgnoreCase("DNI") & !key.equalsIgnoreCase("HONOR") & !key.equalsIgnoreCase("COMENT")) {
	        	            
	                		// get form fields names	                        
	                        if (key.substring(0,1).equalsIgnoreCase("P")) names.add(key);
		                	
	                		String c = "";
	                		if (form.getFieldType(key) == AcroFields.FIELD_TYPE_COMBO) {
		                		// combobox: get highest option
		                		String[] opts = form.getListOptionDisplay(key);       		
		                		c = opts[opts.length-1];
		                		dest.setField(key, opts[opts.length-1]);
		                	} 
	                		if (form.getFieldType(key) == AcroFields.FIELD_TYPE_TEXT) {
	                			// text field: 1234567890 text according to field length
		                		PdfNumber maxLength = form.getFieldItem(key).getMerged(0).getAsNumber(PdfName.MAXLEN);
								if (maxLength != null) {
									int len = maxLength.intValue();
									if (len <= 10) {
										// for length <= 10, build 123... text
										for(int i=1; i<=len; i++){
											c = c + Integer.toString(i);
										}
									} else {
										c = String.join("", Collections.nCopies(len, "a"));
									}
		                		}
								
		                	}
		                	
	                		// set field content, if necessary
	                		if (c.length()>0) dest.setField(key, c);
	                	}
	                }
	            }
	            // close destination PDF form
	            stamper.close();
	            
	            // sort field names alphabetically
                Collections.sort(names);
                // save txt with field names
                File txtFile = new File(destFile.getAbsolutePath().replaceAll(".pdf", ".txt"));
                Files.write(Paths.get(txtFile.getAbsolutePath()), names, Charset.forName("UTF-8"));
	            
	            Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Omple PEC");
	            alert.setHeaderText("Procés finalitzat");
	            alert.setContentText("S'han creat els arxius:\n" + destFile.getAbsolutePath() + 
	            		"\n" + txtFile.getAbsolutePath());
	            alert.showAndWait();
			} catch (Exception e) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("Error");
	            alert.setContentText(e.getMessage());
	            alert.showAndWait();
			}
    	} else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("ATENCIÃ“");
            alert.setHeaderText("Falta l'arxiu amb la PEC");
            alert.setContentText(null);
            alert.showAndWait();		
    	}
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

}
