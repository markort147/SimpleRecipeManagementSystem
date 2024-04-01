package recipes.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.models.AppUser;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, String> {
}
