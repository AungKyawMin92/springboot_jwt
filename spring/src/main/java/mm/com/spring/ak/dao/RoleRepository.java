package mm.com.spring.ak.dao;

import org.springframework.data.repository.CrudRepository;

import mm.com.spring.ak.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
