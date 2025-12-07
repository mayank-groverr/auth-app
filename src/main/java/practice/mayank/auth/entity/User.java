package practice.mayank.auth.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("user")
public class User {

    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String mobileNumber;
    private String password;
}
