package com.leam.omplepecs;


import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;

public class OmplePECs {

	public static void main(String[] args) {
		String pdfFile = "C:/Users/tempo/Desktop/OmplePEC/PEC1_DE0_DNI.pdf";
		
		try {
            PdfReader reader = new PdfReader(pdfFile);
            AcroFields form = reader.getAcroFields();

            if (form.getFields().size()>0) { 
                for (String key : form.getFields().keySet()) {
                	String t = "";
                	if (form.getFieldType(key) == AcroFields.FIELD_TYPE_COMBO) {
                		t = "ComboBox";
                		String[] opts = form.getListOptionDisplay(key);                		
                		t = t + " - " + opts[opts.length-1];
                	} else if (form.getFieldType(key) == AcroFields.FIELD_TYPE_TEXT) {
                		t = "Text";
                		PdfDictionary mergedFieldDictionary = form.getFieldItem(key).getMerged( 0 );
                		PdfNumber maxLengthNumber = mergedFieldDictionary.getAsNumber(PdfName.MAXLEN);
						if (maxLengthNumber != null) {
							int MaxFieldLength = maxLengthNumber.intValue();
							t = t + " - " + Integer.toString(MaxFieldLength);
                		}
                	} else {
                		t = "?";
                	}
                	
                	System.out.println(key + "; " + t);
                }
            }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

}
