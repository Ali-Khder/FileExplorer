package com.FileExplorer.interfaces;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public interface selectedOnlyForReports {

    String getId();

    String getBarrier();

    @JsonFormat(pattern = "dd-MM-yyyy")
    Date getCreatedAt();

    @JsonFormat(pattern = "dd-MM-yyyy")
    Date getUpdatedAt();

    @JsonFormat(pattern = "dd-MM-yyyy")
    Date getBookedAt();

    @JsonFormat(pattern = "dd-MM-yyyy")
    Date getUnbookedAt();
}
