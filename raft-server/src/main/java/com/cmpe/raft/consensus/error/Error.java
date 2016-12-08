package com.cmpe.raft.consensus.error;

/**
 * Created by Sushant on 26-11-2016.
 */
public final class Error {

    public static final ErrorResponse INVALID_REQUEST_MISSING_QUERY_PARAM =
            new ErrorResponse("Invalid request, missing query parameters", "REQ_001");

}
