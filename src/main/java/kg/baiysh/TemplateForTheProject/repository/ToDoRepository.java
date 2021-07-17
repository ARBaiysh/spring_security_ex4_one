package kg.baiysh.TemplateForTheProject.repository;


import kg.baiysh.TemplateForTheProject.domain.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ToDoRepository extends JpaRepository<ToDo, String> {

//    @Query(value = "CALL GET_TODOS_BY_COMPLETED(:completed);", nativeQuery = true)
//    List<ToDo> findByCompleted(@Param("completed") Integer completed);

    List<ToDo> findByCompleted(Boolean completed);
    Optional<ToDo> findById(String id);
    boolean existsById(String id);

}
