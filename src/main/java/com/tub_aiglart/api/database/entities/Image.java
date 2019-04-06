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
import org.jetbrains.annotations.NotNull;

@Table(keyspace = "tub", name = "images")
public class Image extends CacheableDatabaseEntity<Image> {

    private String title;
    private String description;
    private String size;
    private boolean displayed;

    public Image(Long id, String name, String description, String size, boolean displayed) {
        super(id);
        this.title = name;
        this.description = description;
        this.size = size;
        this.displayed = displayed;
    }

    @CacheConstructor
    public Image(Long id) {
        super(id);
    }

    public Image() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    @com.datastax.driver.mapping.annotations.Accessor
    public static interface Accessor extends CacheAccessor<Image> {
        @NotNull
        @Query("SELECT * FROM Images WHERE id = :id")
        @Override
        Result<Image> get(@Param("id") long id);

        @NotNull
        @Query("SELECT * FROM Images WHERE title = :title ALLOW FILTERING")
        Result<Image> get(@Param("title") String title);
    }
}
