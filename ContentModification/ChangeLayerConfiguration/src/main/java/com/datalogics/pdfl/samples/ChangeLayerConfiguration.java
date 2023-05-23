/*
 * This sample changes the On/Off configuration for a set of Optional Content Groups,
 * or layers, within a PDF document. By changing the On or Off state in the default
 * configuration, the sample makes the layers visible or invisible when opened in a 
 * PDF viewer.
 * 
 * The sample changes the states of the layers in the document called Layers.pdf and
 * saves the result to a new PDF document.
 *
 * Copyright (c) 2007-2023, Datalogics, Inc. All rights reserved.
 *
 */
package com.datalogics.pdfl.samples;

import com.datalogics.PDFL.*;
import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;

public class ChangeLayerConfiguration {

	public static void main(String[] args)
	{
		System.out.println("ChangeLayerConfiguration Sample:");

		Library lib = new Library();

		try {
			System.out.println("Initialized the library.");

			/*
			 * The OptionalContentConfig hold three properties that affect initial state:
			 * the BaseState, the ON array, and the OFF array.  These properties can
			 * be used in three different ways to get the same combination of visible
			 * and invisible layers when the document is first displayed.
			 *
			 * The Layers.pdf file has four layers: 'Layer 1', 'Layer 2', 'Layer 3',
			 * and 'Guides and Grids'.  By default, the first 3 layers are on,
			 * and 'Guides and Grids' is off.  We want to create a new document with the
			 * following state:
			 *  o   Layer 1 = OFF
			 *  o   Layer 2 = OFF
			 *  o   Layer 3 = ON
			 *  o   Guides and Grids = ON
			 *
			 * There are three ways to achieve this:
			 *
			 * METHOD ONE:
			 *  We set the BaseState to OFF, and add 'Layer 3' and 'Guides and Grids' to
			 *  the ON array.  The BaseState is a default state for ALL layers in a PDF --
			 *  all layers will take on this state unless they are included in the ON array.
			 *  Note that the OFF should be empty in this case.
			 *
			 * METHOD TWO:
			 *  We set the BaseState to ON, and add 'Layer 1' and 'Layer 2' to the OFF
			 *  array.  This method is the opposite of method one: all layers are
			 *  automatically made visible, then any layers in the OFF array are toggled.
			 *  The ON array should be empty with this method.  This is the most common way
			 *  of specifying default layer visibility.
			 *
			 * METHOD THREE:
			 *  We set the BaseState to Unchanged, add 'Layer 1' and 'Layer 2' to the OFF
			 *  array, and 'Layer 3' and 'Guides and Grids' to the ON array.  The Unchanged
			 *  BaseState means that any layers that are not in the ON or OFF array will not
			 *  be affected by this OptionalContentConfig.
			 *
			 * Note that if the Unchanged BaseState is used in the default OptionalContentConfig
			 * and there are layers that are not included in either the ON or OFF array, the
			 * visibility state of those layers is undefined.  For this reason, it is strongly
			 * recommended to use methods one or two in the default OptionalContentConfig.
			 * Also note that with method three, placing the same layer in both the ON and OFF
			 * arrays will lead to unpredictable behavior; however, the library will not
			 * prevent this situation.  Use care when using method three.
			 *
			 * For our demonstration, we will use method one.
			 */

                        String sInput = Library.getResourceDirectory() + "Sample_Input/Layers.pdf";
                        String sOutput = "ChangeLayerConfiguration-out.pdf";

                        if (args.length > 0)
                            sInput = args[0];
                        if (args.length > 1 )
                            sOutput = args[1];
                        System.out.println("Input file: " + sInput + ", writing to " + sOutput);

			Document doc = new Document(sInput);

			// Get the default OptionalContentConfig and OptionalContentGroups
			OptionalContentConfig config = doc.getDefaultOptionalContentConfig();
			List<OptionalContentGroup> ocgs = doc.getOptionalContentGroups();

			// Create a 'working' array
			ArrayList<OptionalContentGroup> worklist = new ArrayList<OptionalContentGroup>();

			// Set the BaseState to Off
			config.setBaseState(OptionalContentBaseState.BASE_STATE_OFF);

			// Add 'Layer 3' and 'Guides and Grids' to the ON array
			for (OptionalContentGroup layer : ocgs)
				if (layer.getName().contentEquals("Layer 3") || layer.getName().contentEquals("Guides and Grids"))
					worklist.add(layer);

			// Now set the ON array to our new set of layers, and the OFF array
			// to empty.
			// NOTE: clearing the OFF array is IMPORTANT!  When we opened the
			// Layers.pdf, it had the 'Guides and Grids' layer in the OFF
			// array; if we leave it in there, it will appear in BOTH
			// the ON and OFF arrays and cause unpredictable behavior.
			config.setOnArray(worklist);
			config.setOffArray(new ArrayList<OptionalContentGroup>());


			// Save the new document
			doc.save(EnumSet.of(SaveFlags.FULL), sOutput);
			System.out.println("Saving ChangeLayerConfiguration-out.pdf");
		}
		finally {
			lib.delete();
		}
	}
}

