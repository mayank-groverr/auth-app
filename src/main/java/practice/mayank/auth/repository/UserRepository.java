package practice.mayank.auth.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import practice.mayank.auth.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);
}
