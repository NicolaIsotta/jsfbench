package jsf2jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


@Entity
@Table(name = "Customer")
public class User implements Serializable {

    private String username;
    private String password;
    private String name;

    public User(String name, String password, String username) {
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public User() {
    }

    @NotNull
    @Size(max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Size(min = 5, max = 15)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Id
    @Size(min = 5, max = 15)
    @Pattern(regexp = "^\\w*$", message = "not a valid username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User(" + username + ")";
    }
}
