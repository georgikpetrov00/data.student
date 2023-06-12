package com.grandp.data.command;

import com.grandp.data.exception.CommandCannotBeExecutedException;

import javax.swing.undo.CannotUndoException;

public interface Command {

    void execute() throws CommandCannotBeExecutedException;

    void revert() throws CannotUndoException;

}
