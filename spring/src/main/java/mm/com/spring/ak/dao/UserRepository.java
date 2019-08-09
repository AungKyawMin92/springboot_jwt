package mm.com.spring.ak.dao;

import org.springframework.data.repository.CrudRepository;

import mm.com.spring.ak.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
