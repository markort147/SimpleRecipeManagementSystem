package recipes.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class AppUser {
    @Id
    private String username;
    private String password;
    private String authority;

    @OneToMany
    private List<Recipe> recipes;
}
