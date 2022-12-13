package com.FileExplorer.interfaces;

import com.FileExplorer.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public interface selectedOnlyForReports {

    String getId();

    String getBarrier();

    User getUser();

    @JsonFormat(pattern = "dd-MM-yyyy")
    String getCreatedAt();

    @JsonFormat(pattern = "dd-MM-yyyy")
    String getUpdatedAt();

    @JsonFormat(pattern = "dd-MM-yyyy")
    String getBookedAt();

    @JsonFormat(pattern = "dd-MM-yyyy")
    String getUnbookedAt();
}
