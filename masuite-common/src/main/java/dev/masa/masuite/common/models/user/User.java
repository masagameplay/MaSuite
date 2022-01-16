package dev.masa.masuite.common.models.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import dev.masa.masuite.api.models.user.IUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Accessors(fluent = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "masuite_users")
public class User implements IUser {
    @DatabaseField(dataType =  DataType.UUID, id = true, columnName = "uuid")
    private UUID uniqueId;
    @DatabaseField
    private String username;
    @DatabaseField(dataType = DataType.DATE)
    private Date firstLogin;
    @DatabaseField(dataType = DataType.DATE)
    private Date lastLogin;
}
