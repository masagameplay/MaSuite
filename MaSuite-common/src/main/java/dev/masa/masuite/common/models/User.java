package dev.masa.masuite.common.models;

import dev.masa.masuite.api.user.IUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Accessors(fluent = true)
@Data()
public class User implements IUser {
    private UUID uniqueId;
    private String username;
    private Date firstLogin;
    private Date lastLogin;
}
