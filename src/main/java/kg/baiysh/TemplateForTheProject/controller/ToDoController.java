package kg.baiysh.TemplateForTheProject.controller;


import kg.baiysh.TemplateForTheProject.domain.ToDo;
import kg.baiysh.TemplateForTheProject.domain.ToDoBuilder;
import kg.baiysh.TemplateForTheProject.repository.ToDoRepository;
import kg.baiysh.TemplateForTheProject.validation.ToDoValidationError;
import kg.baiysh.TemplateForTheProject.validation.ToDoValidationErrorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ToDoController {
    private Logger LOG = LoggerFactory.getLogger(ToDoController.class);

    private ToDoRepository toDoRepository;

    public ToDoController(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    @GetMapping("/todo")
    @PreAuthorize(value = "hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        return ResponseEntity.ok(toDoRepository.findAll());
    }

    @GetMapping("/todo/completed/{completed}")
    @PreAuthorize(value = "hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Iterable<ToDo>> getToDosByCompleted(@PathVariable Boolean completed) {
        return ResponseEntity.ok(toDoRepository.findByCompleted(completed));
    }

    @GetMapping("/todo/{id}")
    @PreAuthorize(value = "hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ToDo> getToDoById(@PathVariable UUID id) {
        Optional<ToDo> toDo = toDoRepository.findById(id);
        return toDo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/todo/{id}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ToDo> setCompleted(@PathVariable String id) {
        Optional<ToDo> toDo = toDoRepository.findById(UUID.fromString(id));
        if (toDo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ToDo result = toDo.get();
        result.setCompleted(true);
        toDoRepository.save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location", location.toString()).build();
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }
        ToDo result = toDoRepository.save(toDo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/todo/{id}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable String id) {
        toDoRepository.delete(ToDoBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ToDo> deleteToDo(@RequestBody ToDo toDo) {
        toDoRepository.delete(toDo);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        return new ToDoValidationError(exception.getMessage());
    }

}
