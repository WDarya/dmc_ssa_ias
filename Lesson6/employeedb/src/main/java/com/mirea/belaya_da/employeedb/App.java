package com.mirea.belaya_da.employeedb;

import android.app.Application;
import androidx.room.Room;

public class App extends Application {

    private static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Создание базы данных
        database = Room.databaseBuilder(this, AppDatabase.class, "superheroes-db")
                .allowMainThreadQueries()  // Разрешаем запросы в главном потоке (для простоты)
                .build();

        // Добавляем тестовые данные
        addTestData();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    private void addTestData() {
        SuperHeroDao dao = database.superHeroDao();

        // Проверяем, есть ли уже данные
        if (dao.getAll().isEmpty()) {
            // Добавляем тестовых супер-героев
            dao.insert(new SuperHero(
                    "Супермен",
                    "Суперсила, полет, теплое зрение, суперскорость",
                    95,
                    "DC",
                    "Кларк Кент",
                    "Action Comics #1 (1938)"
            ));

            dao.insert(new SuperHero(
                    "Бэтмен",
                    "Интеллект, технологии, боевые навыки, богатство",
                    85,
                    "DC",
                    "Брюс Уэйн",
                    "Detective Comics #27 (1939)"
            ));

            dao.insert(new SuperHero(
                    "Человек-паук",
                    "Паучье чутье, сила, ловкость, стенолазание",
                    80,
                    "Marvel",
                    "Питер Паркер",
                    "Amazing Fantasy #15 (1962)"
            ));

            dao.insert(new SuperHero(
                    "Железный человек",
                    "Технологии, броня, интеллект, богатство",
                    90,
                    "Marvel",
                    "Тони Старк",
                    "Tales of Suspense #39 (1963)"
            ));

            dao.insert(new SuperHero(
                    "Чудо-женщина",
                    "Суперсила, полет, лассо истины, браслеты",
                    92,
                    "DC",
                    "Диана Принс",
                    "All Star Comics #8 (1941)"
            ));
        }
    }
}