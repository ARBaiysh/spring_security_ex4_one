package kg.baiysh.TemplateForTheProject.service;

import kg.baiysh.TemplateForTheProject.domain.ToDo;
import kg.baiysh.TemplateForTheProject.repository.ToDoRepository;
import kg.baiysh.TemplateForTheProject.validation.ToDoValidationErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ToDoService {
    private final ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public List<ToDo> findAllToDo() {
        return toDoRepository.findAll();
    }


    public List<ToDo> findByCompleted(Boolean completed) {
        return toDoRepository.findByCompleted(completed);
    }


    public ToDo save(ToDo toDo) {
        return toDoRepository.save(toDo);
    }

    public Optional<ToDo> findById(UUID id) {
        return toDoRepository.findById(id);
    }

    public void delete(ToDo build) {
        toDoRepository.delete(build);
    }
    public ResponseEntity setCompleted(UUID id){
        Optional<ToDo> toDo = findById(id);
        if (toDo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ToDo result = toDo.get();
        result.setCompleted(true);
        save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location", location.toString()).build();
    }

    public ResponseEntity<?> createToDo(ToDo toDo, Errors errors, Principal principal) {
        if (errors.hasErrors()) {
            log.error(errors.getObjectName() + "User: " + principal.getName());
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        ToDo result = save(toDo);
        log.info(">>> Save ToDo :" + result + ", created user: " + principal.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}
