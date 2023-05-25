/*
 * Copyright (C) 2011-2023, Datalogics, Inc. All rights reserved.
 * 
 */

package com.datalogics.pdfl.JavaViewer.Views.Actions;

import java.awt.event.ActionEvent;

import com.datalogics.pdfl.JavaViewer.Document.JavaDocument;
import com.datalogics.pdfl.JavaViewer.Document.Command.CommandType;
import com.datalogics.pdfl.JavaViewer.Document.Command.DocumentCommand;
import com.datalogics.pdfl.JavaViewer.Presentation.ApplicationController;

public class UndoAction extends SimpleAction {
    public void actionPerformed(ActionEvent e) {
        ((ApplicationController) getApplication()).undo();
    }

    @Override
    protected Class<? extends DocumentCommand> getMainCommand() {
        return null;
    }

    @Override
    public boolean isPermitted() {
        return JavaDocument.isCommandPermitted(getApplication().getActiveDocument(), CommandType.VIEW);
    }
}
