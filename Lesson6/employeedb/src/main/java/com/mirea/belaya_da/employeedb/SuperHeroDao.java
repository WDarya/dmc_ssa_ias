package com.mirea.belaya_da.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SuperHeroDao {

    // Получить всех героев
    @Query("SELECT * FROM superheroes")
    List<SuperHero> getAll();

    // Получить героя по ID
    @Query("SELECT * FROM superheroes WHERE id = :id")
    SuperHero getById(long id);

    // Поиск по имени
    @Query("SELECT * FROM superheroes WHERE name LIKE '%' || :name || '%'")
    List<SuperHero> findByName(String name);

    // Получить героев по вселенной
    @Query("SELECT * FROM superheroes WHERE universe = :universe")
    List<SuperHero> getByUniverse(String universe);

    // Получить самых сильных героев
    @Query("SELECT * FROM superheroes ORDER BY strength DESC LIMIT :limit")
    List<SuperHero> getStrongest(int limit);

    // Вставить героя
    @Insert
    void insert(SuperHero hero);

    // Обновить героя
    @Update
    void update(SuperHero hero);

    // Удалить героя
    @Delete
    void delete(SuperHero hero);

    // Удалить всех героев
    @Query("DELETE FROM superheroes")
    void deleteAll();
}