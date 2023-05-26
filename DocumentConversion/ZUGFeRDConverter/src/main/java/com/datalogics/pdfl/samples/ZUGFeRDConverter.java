package com.datalogics.pdfl.samples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.EnumSet;

import com.datalogics.PDFL.SaveFlags;
import com.datalogics.PDFL.Document;
import com.datalogics.PDFL.FileAttachment;
import com.datalogics.PDFL.Library;
import com.datalogics.PDFL.PDFAConvertParams;
import com.datalogics.PDFL.PDFAConvertType;
import com.datalogics.PDFL.PDFAConvertResult;

/*
 *
 * This sample demonstrates converting the input PDF with the input Invoice ZUGFeRD XML to a ZUGFeRD compliant PDF.
 *
 * Copyright (c) 2007-2023, Datalogics, Inc. All rights reserved.
 *
 */
public class ZUGFeRDConverter
{
    public static void main(String[] args) throws Exception {
        System.out.println("ZUGFeRDConverter Sample:");

        Library lib = new Library();
        try {
            System.out.println("Initialzed the library.");

            if (args.length < 2)
            {
                System.out.println("You must specify an input PDF and an input ZUGFeRD Invoice XML file, e.g.:");
                System.out.println();
                System.out.println("ConvertToZUGFeRD input-file.pdf input-ZUGFeRD-invoice.xml");
                return;
            }

            String sInputPDF = args[0];
            String sInputInoviceXML = args[1];
            String sOutput = "ZUGFeRDConverter-out.pdf";

            System.out.println("Converting " + sInputPDF + " with " + sInputInoviceXML + ", output file is " + sOutput);

            // Step 1) Open the input PDF
            Document doc = new Document(sInputPDF);

            // Step 2) Open the input Invoice XML and attach it to the PDF
            FileAttachment attachment = new FileAttachment(doc, sInputInoviceXML);

            // Make a conversion parameters object
            PDFAConvertParams pdfaParams = new PDFAConvertParams();
            pdfaParams.setIgnoreFontErrors(false);
            pdfaParams.setNoValidationErrors(false);
            pdfaParams.setValidateImplementationLimitsOfDocument(true);

            // Step 3) Convert the input PDF to be a PDF/A-3 document.
            PDFAConvertResult pdfaResult = doc.cloneAsPDFADocument(PDFAConvertType.RGB_3B, pdfaParams);

            // The conversion may have failed: we must check if the result has a valid Document
            if (pdfaResult.getPDFADocument() == null)
            {
                System.out.println("ERROR: Could not convert " + sInputPDF + " to PDF/A.");
            }
            else
            {
                System.out.println("Successfully converted " + sInputPDF + " to PDF/A.");

                Document pdfaDoc = pdfaResult.getPDFADocument();

                //Step 4) Add the required XMP metadata entries
                AddMetadataAndExtensionSchema(pdfaDoc, sInputInoviceXML);

                //Step 5) Save the document
                pdfaDoc.save(pdfaResult.getPDFASaveFlags(), sOutput);
            }

            attachment.delete();
            doc.delete();
        }
        finally
        {
            lib.delete();
        }
    }

    public static void AddMetadataAndExtensionSchema(Document document, String sInputInoviceXML) {
        String namespaceURI = "urn:ferd:pdfa:CrossIndustryDocument:invoice:2p0#";
        String namespacePrefix = "zf";

        String pathDocumentType = "DocumentType";
        String pathDocumentTypeValue = "INVOICE";

        String pathDocumentFileName = "DocumentFileName";
        String pathDocumentFileNameValue = sInputInoviceXML;

        String pathConformanceLevel = "ConformanceLevel";
        String pathConformanceLevelValue = "BASIC";

        String pathVersion = "Version";
        String pathVersionValue = "2p0";

        //Set the XMP ZUGFeRD properties
        document.setXMPMetadataProperty(namespaceURI, namespacePrefix, pathDocumentType, pathDocumentTypeValue);
        document.setXMPMetadataProperty(namespaceURI, namespacePrefix, pathDocumentFileName, pathDocumentFileNameValue);
        document.setXMPMetadataProperty(namespaceURI, namespacePrefix, pathConformanceLevel, pathConformanceLevelValue);
        document.setXMPMetadataProperty(namespaceURI, namespacePrefix, pathVersion, pathVersionValue);

        //Create the PDF/A Extension Schema for ZUGFeRD since it's not part of the PDF/A standard.
        String extensionSchema;
        extensionSchema = "<rdf:Description rdf:about=\"\"" + System.lineSeparator()
        + "xmlns:pdfaExtension=\"http://www.aiim.org/pdfa/ns/extension/\"" + System.lineSeparator()
        + "xmlns:pdfaSchema=\"http://www.aiim.org/pdfa/ns/schema#\"" + System.lineSeparator()
        + "xmlns:pdfaProperty=\"http://www.aiim.org/pdfa/ns/property#\">" + System.lineSeparator()
        + "<pdfaExtension:schemas>" + System.lineSeparator()
        + "<rdf:Bag>" + System.lineSeparator()
        + "<rdf:li rdf:parseType=\"Resource\">" + System.lineSeparator()
        + "<pdfaSchema:schema>ZUGFeRD PDFA Extension Schema</pdfaSchema:schema>" + System.lineSeparator()
        + "<pdfaSchema:namespaceURI>urn:ferd:pdfa:CrossIndustryDocument:invoice:2p0#</pdfaSchema:namespaceURI>" + System.lineSeparator()
        + "<pdfaSchema:prefix>zf</pdfaSchema:prefix>" + System.lineSeparator()
        + "<pdfaSchema:property>" + System.lineSeparator()
        + "<rdf:Seq>" + System.lineSeparator()
        + "<rdf:li rdf:parseType=\"Resource\">" + System.lineSeparator()
        + "<pdfaProperty:name>DocumentFileName</pdfaProperty:name>" + System.lineSeparator()
        + "<pdfaProperty:valueType>Text</pdfaProperty:valueType>" + System.lineSeparator()
        + "<pdfaProperty:category>external</pdfaProperty:category>" + System.lineSeparator()
        + "<pdfaProperty:description>name of the embedded XML invoice file</pdfaProperty:description>" + System.lineSeparator()
        + "</rdf:li>" + System.lineSeparator()
        + "<rdf:li rdf:parseType=\"Resource\">" + System.lineSeparator()
        + "<pdfaProperty:name>DocumentType</pdfaProperty:name>" + System.lineSeparator()
        + "<pdfaProperty:valueType>Text</pdfaProperty:valueType>" + System.lineSeparator()
        + "<pdfaProperty:category>external</pdfaProperty:category>" + System.lineSeparator()
        + "<pdfaProperty:description>INVOICE</pdfaProperty:description>" + System.lineSeparator()
        + "</rdf:li>" + System.lineSeparator()
        + "<rdf:li rdf:parseType=\"Resource\">" + System.lineSeparator()
        + "<pdfaProperty:name>Version</pdfaProperty:name>" + System.lineSeparator()
        + "<pdfaProperty:valueType>Text</pdfaProperty:valueType>" + System.lineSeparator()
        + "<pdfaProperty:category>external</pdfaProperty:category>" + System.lineSeparator()
        + "<pdfaProperty:description>The actual version of the ZUGFeRD XML schema</pdfaProperty:description>" + System.lineSeparator()
        + "</rdf:li>" + System.lineSeparator()
        + "<rdf:li rdf:parseType=\"Resource\">" + System.lineSeparator()
        + "<pdfaProperty:name>ConformanceLevel</pdfaProperty:name>" + System.lineSeparator()
        + "<pdfaProperty:valueType>Text</pdfaProperty:valueType>" + System.lineSeparator()
        + "<pdfaProperty:category>external</pdfaProperty:category>" + System.lineSeparator()
        + "<pdfaProperty:description>The conformance level of the embedded ZUGFeRD data</pdfaProperty:description>" + System.lineSeparator()
        + "</rdf:li>" + System.lineSeparator()
        + "</rdf:Seq>" + System.lineSeparator()
        + "</pdfaSchema:property>" + System.lineSeparator()
        + "</rdf:li>" + System.lineSeparator()
        + "</rdf:Bag>" + System.lineSeparator()
        + "</pdfaExtension:schemas>" + System.lineSeparator()
        + "</rdf:Description>" + System.lineSeparator()
        + "</rdf:RDF>" + System.lineSeparator();

        String xmpMetadata = document.getXMPMetadata();

        //We're going to look for the ending of our typical XMP Metadata to replace it with our Extension Schema
        String newXMPMetadata = xmpMetadata.replace("</rdf:RDF>", extensionSchema);

        //Update with our new XMP Metadata
        document.setXMPMetadata(newXMPMetadata);
    }
}
