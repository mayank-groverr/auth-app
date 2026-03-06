package practice.mayank.auth.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import practice.mayank.auth.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {
    Optional<RefreshToken> findByToken(String token);
}
