package com.rfranco.zookeeperrestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ruben.martinez on 21/11/2017.
 */
@ResponseStatus(value=HttpStatus.CONFLICT)
public class NodeAlreadyExistsException extends RuntimeException {
    public NodeAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
