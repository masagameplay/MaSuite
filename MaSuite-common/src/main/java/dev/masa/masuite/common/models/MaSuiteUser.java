package dev.masa.masuite.common.models;

import dev.masa.masuite.api.user.IMaSuiteUser;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Accessors(fluent = true)
@Data()
public class MaSuiteUser implements IMaSuiteUser {
    private UUID uniqueId;
    private String username;
    private Date firstLogin;
    private Date lastLogin;
}
