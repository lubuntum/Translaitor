package com.example.webtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webtest.database.Database;
import com.example.webtest.database.LangPair;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    //UI элементы для работы
    Button translateBtn;
    Button saveBtn;
    FloatingActionButton openStorageBtn;
    TextInputEditText textInput;
    TextView resultOutput;
    ProgressBar progress;
    public LangPair currentLangPair;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Задаем элементы
        translateBtn = findViewById(R.id.translate_text_btn);
        textInput = findViewById(R.id.text);
        progress = findViewById(R.id.progress);
        resultOutput = findViewById(R.id.translated_text);
        openStorageBtn = findViewById(R.id.storage_btn);
        saveBtn = findViewById(R.id.save_btn);
        //Метод для инициалиазции функционала кнопки
        translateButtonInit();
        openStorageBtnInit();
        DatabaseInit();
        saveBtnInit();
    }
    public void translateButtonInit(){
        translateBtn.setOnClickListener((view)->{
            //Берем текст для перевода из нашего элемента
            String text = textInput.getText().toString();
            //Проверка если пользователь не ввел текст то выводим предупреждение и заканчиваем
            if(text.matches(" *")){
                Toast.makeText(this, "Пожалуйста введите текст для перевода", Toast.LENGTH_SHORT).show();
                return;
            }
            //Начинам загрузку перевод и отображаем статус загрузки
            progress.setVisibility(View.VISIBLE);
            Runnable translateText = () -> {
                //В отдельном потоке загружаем перевод и выводим то что получилось на экран, также убираем статус загрузки
                String result = TranslateAPI.translateText(text,"ru", "en");
                currentLangPair = new LangPair(text, result);
                runOnUiThread(()->{
                    //Вывод текста и исчезновение статуса загрузки
                    resultOutput.setText(result);
                    progress.setVisibility(View.INVISIBLE);
                });
            };
            //Начало работы потока
            new Thread(translateText).start();
        });
    }
    //Функция для отображения сохраненных переводов в формате ru - en
    //Открывается новая Activity - StorageActivity
    public void openStorageBtnInit(){
        openStorageBtn.setOnClickListener((view)->{
            Intent intent = new Intent(this, StorageActivity.class);
            //Ставим флаг что бы при повторном открытии не было лишних копий одной и той же StorageActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
    }
    /*
    * Кнопка для сохранения в БД
    * */
    public void saveBtnInit(){
        //Проверяем на ошибки

        //Сохраняем в БД через поток
        saveBtn.setOnClickListener((view)->{
            if (textInput.getText().toString().matches(" *") || currentLangPair == null) return;
            //Смотрим что бы БД была проинициализиованна
            if(Database.db == null) return;
            Runnable insertRnb = ()->{
                Database.insertLangPair(currentLangPair);
                runOnUiThread(()->{
                    Toast.makeText(this, "Перевод успешно сохранен", Toast.LENGTH_SHORT).show();
                });
            };
            new Thread(insertRnb).start();
        });
    }
    //Иниц. БД
    private void DatabaseInit(){
        if(Database.db != null) return;
        Runnable loadDatabase = () -> {
            Database.databaseInit(getBaseContext());
        };
        new Thread(loadDatabase).start();
    }
}