package kg.baiysh.TemplateForTheProject.domain;

import java.util.UUID;

public class ToDoBuilder {
    private static ToDoBuilder instance = new ToDoBuilder();
    private UUID id = null;
    private String description = "";

    private ToDoBuilder() {
    }

    public static ToDoBuilder create() {
        return instance;
    }

    public ToDoBuilder withDescription(String description) {
        this.description = description;
        return instance;
    }

    public ToDoBuilder withId(String id) {
        this.id = UUID.fromString(id);
        return instance;
    }

    public ToDo build() {
        ToDo result = new ToDo(this.description);
        if (id != null) result.setId(id);
        return result;
    }
}
