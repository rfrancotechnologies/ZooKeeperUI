package com.rfranco.zookeeperrestapi.config.users;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ruben.martinez on 01/12/2017.
 */
@Component
@ConfigurationProperties(prefix="users")
public class ConfiguredUsers {
    private List<ConfiguredUserDetails> usersDetails;

    public List<ConfiguredUserDetails> getUsersDetails() {
        return usersDetails;
    }

    public void setUsersDetails(List<ConfiguredUserDetails> usersDetails) {
        this.usersDetails = usersDetails;
    }
}
