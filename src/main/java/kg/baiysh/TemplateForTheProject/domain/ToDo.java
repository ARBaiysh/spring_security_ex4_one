package kg.baiysh.TemplateForTheProject.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ToDo {
    @NotNull
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @NotNull
    @NotBlank
    private String description;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime created;
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private LocalDateTime modified;
    private boolean completed;

    public ToDo(String description) {
        this.description = description;
        completed = true;
    }

    @PrePersist
    void onCreate() {
        this.setCreated(LocalDateTime.now());
        this.setModified(LocalDateTime.now());
    }

    @PreUpdate
    void onUpdate() {
        this.setModified(LocalDateTime.now());
    }
}
