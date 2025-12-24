package com.mirea.belaya_da.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "superheroes")  // Создаем таблицу "superheroes"
public class SuperHero {

    @PrimaryKey(autoGenerate = true)  // Автоматическая генерация ID
    public long id;

    public String name;        // Имя героя
    public String power;       // Суперспособность
    public int strength;       // Сила (1-100)
    public String universe;    // Вселенная (Marvel/DC/другая)
    public String realName;    // Настоящее имя
    public String firstAppearance; // Первое появление

    // Конструкторы
    public SuperHero() {}

    public SuperHero(String name, String power, int strength,
                     String universe, String realName, String firstAppearance) {
        this.name = name;
        this.power = power;
        this.strength = strength;
        this.universe = universe;
        this.realName = realName;
        this.firstAppearance = firstAppearance;
    }
}