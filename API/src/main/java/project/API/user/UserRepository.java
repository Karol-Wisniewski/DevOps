package project.API.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Defult methods for our driver (like in monmgoose - findById)
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
