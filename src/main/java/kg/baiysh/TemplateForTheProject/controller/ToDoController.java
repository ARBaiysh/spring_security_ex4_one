package kg.baiysh.TemplateForTheProject.controller;


import kg.baiysh.TemplateForTheProject.domain.ToDo;
import kg.baiysh.TemplateForTheProject.domain.ToDoBuilder;
import kg.baiysh.TemplateForTheProject.service.ToDoService;
import kg.baiysh.TemplateForTheProject.validation.ToDoValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Slf4j
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping("/todo")
    @PreAuthorize(value = "hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Iterable<ToDo>> getToDos() {
        return ResponseEntity.ok(toDoService.findAllToDo());
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo, Errors errors, Principal principal) {
        return toDoService.createToDo(toDo, errors, principal);
    }

    @GetMapping("/todo/{id}")
    @PreAuthorize(value = "hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ToDo> getToDoById(@PathVariable UUID id) {
        Optional<ToDo> toDo = toDoService.findById(id);
        return toDo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/todo/completed/{completed}")
    @PreAuthorize(value = "hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Iterable<ToDo>> getToDosByCompleted(@PathVariable Boolean completed) {
        return ResponseEntity.ok(toDoService.findByCompleted(completed));
    }

    @PostMapping("/todo/{id}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity setCompleted(@PathVariable UUID id) {
        return toDoService.setCompleted(id);
    }

    @DeleteMapping("/todo/{id}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable String id) {
        toDoService.delete(ToDoBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<ToDo> deleteToDo(@RequestBody ToDo toDo) {
        toDoService.delete(toDo);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        return new ToDoValidationError(exception.getMessage());
    }

}
