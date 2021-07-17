package kg.baiysh.TemplateForTheProject.repository;

import kg.baiysh.TemplateForTheProject.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByName(String name);
}
