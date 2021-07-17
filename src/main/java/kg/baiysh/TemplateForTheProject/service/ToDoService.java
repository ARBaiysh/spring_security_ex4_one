package kg.baiysh.TemplateForTheProject.service;

import kg.baiysh.TemplateForTheProject.domain.ToDo;
import kg.baiysh.TemplateForTheProject.repository.ToDoRepository;
import kg.baiysh.TemplateForTheProject.validation.ToDoValidationError;
import kg.baiysh.TemplateForTheProject.validation.ToDoValidationErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    public Optional<ToDo> findById(String id) {
        return toDoRepository.findById(id);
    }

    public void delete(ToDo toDo) {
        toDoRepository.delete(toDo);
    }

    public boolean setCompleted(String id) {
        if(!toDoRepository.existsById(id)) {
            return false;
        }
        ToDo toDo = findById(id).get();
        toDo.setCompleted(true);
        save(toDo);
        return true;

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

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        return new ToDoValidationError(exception.getMessage());
    }
}
