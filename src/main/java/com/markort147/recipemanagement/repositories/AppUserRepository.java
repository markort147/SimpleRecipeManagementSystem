package com.markort147.recipemanagement.repositories;

import com.markort147.recipemanagement.models.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, String> {
}
