package kg.baiysh.TemplateForTheProject.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_table", uniqueConstraints = {@UniqueConstraint(columnNames = "login")})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String login;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;
}