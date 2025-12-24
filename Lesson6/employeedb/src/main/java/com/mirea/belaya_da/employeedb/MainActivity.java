package com.mirea.belaya_da.employeedb;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPower;
    private Button buttonAdd;
    private Button buttonShowAll;
    private Button buttonMarvel;
    private Button buttonDC;
    private Button buttonStrongest;
    private TextView textViewResult;
    private TextView textViewStatus;

    private AppDatabase database;
    private SuperHeroDao heroDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        // Инициализация базы данных
        database = App.getInstance().getDatabase();
        heroDao = database.superHeroDao();

        setupClickListeners();

        // Показываем количество героев при запуске
        updateStatus();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextPower = findViewById(R.id.editTextPower);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonShowAll = findViewById(R.id.buttonShowAll);
        buttonMarvel = findViewById(R.id.buttonMarvel);
        buttonDC = findViewById(R.id.buttonDC);
        buttonStrongest = findViewById(R.id.buttonStrongest);
        textViewResult = findViewById(R.id.textViewResult);
        textViewStatus = findViewById(R.id.textViewStatus);
    }

    private void setupClickListeners() {
        // Добавление нового героя
        buttonAdd.setOnClickListener(v -> addNewHero());

        // Показать всех героев
        buttonShowAll.setOnClickListener(v -> showAllHeroes());

        // Фильтр по вселенной Marvel
        buttonMarvel.setOnClickListener(v -> showHeroesByUniverse("Marvel"));

        // Фильтр по вселенной DC
        buttonDC.setOnClickListener(v -> showHeroesByUniverse("DC"));

        // Показать топ-3 самых сильных
        buttonStrongest.setOnClickListener(v -> showStrongestHeroes(3));
    }

    private void addNewHero() {
        String name = editTextName.getText().toString().trim();
        String power = editTextPower.getText().toString().trim();

        if (name.isEmpty() || power.isEmpty()) {
            Toast.makeText(this, "Заполните имя и способность", Toast.LENGTH_SHORT).show();
            return;
        }

        // Создаем нового героя с базовыми значениями
        SuperHero newHero = new SuperHero(
                name,
                power,
                70,  // сила по умолчанию
                "Не указана",  // вселенная
                "Неизвестно",  // настоящее имя
                "Не указано"   // первое появление
        );

        // Добавляем в базу данных
        heroDao.insert(newHero);

        // Очищаем поля
        editTextName.setText("");
        editTextPower.setText("");

        Toast.makeText(this, "Герой добавлен: " + name, Toast.LENGTH_SHORT).show();
        updateStatus();
    }

    private void showAllHeroes() {
        List<SuperHero> heroes = heroDao.getAll();
        displayHeroes(heroes, "Все супер-герои:");
    }

    private void showHeroesByUniverse(String universe) {
        List<SuperHero> heroes = heroDao.getByUniverse(universe);
        displayHeroes(heroes, "Герои вселенной " + universe + ":");
    }

    private void showStrongestHeroes(int limit) {
        List<SuperHero> heroes = heroDao.getStrongest(limit);
        displayHeroes(heroes, "Топ-" + limit + " самых сильных героев:");
    }

    private void displayHeroes(List<SuperHero> heroes, String title) {
        if (heroes.isEmpty()) {
            textViewResult.setText(title + "\n\nСписок пуст");
            return;
        }

        StringBuilder result = new StringBuilder();
        result.append(title).append("\n\n");
        result.append("Всего: ").append(heroes.size()).append("\n\n");

        for (SuperHero hero : heroes) {
            result.append("🦸 ").append(hero.name).append("\n");
            result.append("   Сила: ").append(hero.strength).append("/100\n");
            result.append("   Способность: ").append(hero.power).append("\n");
            result.append("   Вселенная: ").append(hero.universe).append("\n");
            result.append("   Настоящее имя: ").append(hero.realName).append("\n");
            result.append("   Первое появление: ").append(hero.firstAppearance).append("\n");
            result.append("   ID: ").append(hero.id).append("\n");
            result.append("────────────────────\n");
        }

        textViewResult.setText(result.toString());
    }

    private void updateStatus() {
        int count = heroDao.getAll().size();
        textViewStatus.setText("В базе: " + count + " героев | Готово к работе");
    }
}