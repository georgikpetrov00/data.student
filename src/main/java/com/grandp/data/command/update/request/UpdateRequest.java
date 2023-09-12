package com.grandp.data.command.update.request;

import com.grandp.data.exception.UpdateRequestCannotBeExecutedException;

public interface UpdateRequest {

    void execute() throws UpdateRequestCannotBeExecutedException;
    void revert();
}
