package com.rfranco.zookeeperrestapi.services;

import org.springframework.stereotype.Service;

/**
 * Created by ruben.martinez on 12/12/2017.
 */
@Service
public class DateTimeProvider {
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
