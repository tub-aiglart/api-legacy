/*
 * API - A basic REST API made for tub-aiglart.com
 *
 * Copyright (C) 2019  Oskar Lang
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package com.tub_aiglart.api.database.entities;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.Table;
import com.tub_aiglart.api.database.CacheAccessor;
import com.tub_aiglart.api.database.CacheConstructor;
import com.tub_aiglart.api.utils.Role;
import org.jetbrains.annotations.NotNull;

@Table(keyspace = "tub", name = "users")
public class User extends CacheableDatabaseEntity<User> {

    private String name;
    private String email;
    private String password;
    private Role role;

    public User(Long id, String name, String email, String password, Role role) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @CacheConstructor
    public User(Long id) {
        super(id);
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @com.datastax.driver.mapping.annotations.Accessor
    public static interface Accessor extends CacheAccessor<User> {
        @NotNull
        @Query("SELECT * FROM users WHERE id = :id")
        @Override
        Result<User> get(@Param("id") long id);

        @NotNull
        @Query("SELECT * FROM users WHERE name = :name ALLOW FILTERING")
        Result<User> get(@Param("name") String name);
    }
}
