
package com.datalogics.pdfl.samples;

/*
 *
 * The FlattenForms sample demonstrates how to Flatten XFA into AcroForms.
 *
 *  - Flatten XFA (Dynamic or Static) to regular page content which converts and expands XFA fields to regular PDF content and removes the XFA fields.
 *  - Flatten AcroForms to regular page content which converts AcroForm fields to regular page content and removes the AcroForm fields.
 *
 * Copyright (c) 2024, Datalogics, Inc. All rights reserved.
 */
import com.datalogics.PDFL.*;
import java.util.EnumSet;
public class FlattenForms
{

    public static void main(String[] args) throws Throwable {
        System.out.println("FlattenForms sample:");

        Library lib = new Library(EnumSet.of(LibraryFlags.INIT_FORMS_EXTENSION));

        try
        {
            if (!lib.isFormsExtensionAvailable())
            {
                System.out.println("Forms Plugins were not properly loaded!");
                return;
            }

            //Must be set to true to prevent default legacy behavior of PDFL
            lib.setAllowOpeningXFA(true);

            System.out.println("Initialized the library.");

            //XFA document
            String sInput = Library.getResourceDirectory() + "Sample_Input/DynamicXFA.pdf";
            String sOutput = "../FlattenXFA-out.pdf";

            if (args.length > 0)
            {
                sInput = args[0];
            }

            if (args.length > 1)
            {
                sOutput = args[1];
            }

            Document doc = new Document(sInput);
            long pagesOutput = doc.flattenXFAFormFields();

            System.out.println("XFA document was expanded into " + pagesOutput + " Flattened pages.");

            doc.save(EnumSet.of(SaveFlags.FULL, SaveFlags.LINEARIZED), sOutput);

            doc.delete();

            //AcroForms document
            sInput = Library.getResourceDirectory() + "Sample_Input/AcroForm.pdf";
            sOutput = "../FlattenAcroForms-out.pdf";

            doc = new Document(sInput);

            doc.flattenAcroFormFields();

            System.out.println("AcroForms document was Flattened.");

            doc.save(EnumSet.of(SaveFlags.FULL, SaveFlags.LINEARIZED), sOutput);
        }
        finally {
            lib.delete();
        }
   }
}

