package com.mirea.belaya_da.employeedb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SuperHero.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SuperHeroDao superHeroDao();
}