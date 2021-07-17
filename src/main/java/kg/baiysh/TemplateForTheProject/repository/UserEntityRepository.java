package kg.baiysh.TemplateForTheProject.repository;

import kg.baiysh.TemplateForTheProject.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByLogin(String login);

    Boolean existsByLogin(String login);
}