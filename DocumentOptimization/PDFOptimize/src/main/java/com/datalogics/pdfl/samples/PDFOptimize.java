package com.datalogics.pdfl.samples;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.datalogics.PDFL.*;

/*
 *
 * This sample program demonstrates the use of PDFOptimizer. This compresses a PDF document
 * to make it smaller so it's easier to process and download.
 *
 * NOTE: Some documents can't be compressed because they're already well-compressed or contain
 * content that can't be assumed is safe to be removed.  However you can fine tune the optimization
 * to suit your applications needs and drop such content to achieve better compression if you already
 * know it's unnecessary.
 * 
 * Copyright (c) 2007-2023, Datalogics, Inc. All rights reserved.
 *
 */

public class PDFOptimize {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("PDFOptimizer:");

        Library lib = new Library();
        try {
            String sInput = Library.getResourceDirectory() + "Sample_Input/sample.pdf";
            String sOutput = "PDFOptimizer-out.pdf";

            if (args.length > 0)
                sInput = args[0];
            if (args.length > 1 )
                sOutput = args[1];

            System.out.println ( "Will optimize " + sInput + " and save as " + sOutput );
            Document doc = new Document(sInput);
            PDFOptimizer optimizer = new PDFOptimizer();
            try {
                File oldfile = new File(sInput);
                long beforeLength = oldfile.length();

                optimizer.optimize(doc, sOutput);

                File newfile = new File(sOutput);
                long afterLength = newfile.length();

                System.out.println("Optimized file ");
                System.out.println(afterLength * 100.0 / beforeLength);
                System.out.println("% the size of the original.");
            }
            finally
            {
                optimizer.delete();
            }
        } 
        finally
        {
            lib.delete();
        }
    }
}
