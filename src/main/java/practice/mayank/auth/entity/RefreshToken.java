package practice.mayank.auth.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "tokens")
@Getter
@Setter
public class RefreshToken {

    @Id
    private ObjectId id;
    @DBRef
    private User user;
    private String token;
    private Instant expiryDate;
}
