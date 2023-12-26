 package com.example.webtest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOError;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

 public class TranslateAPI {
     //API ссылка с которой работаем
    public static final String API_URL = "https://api.mymemory.translated.net/get?q=";
    //Константа для вывода ошибки
    public static final String NETWORK_ERROR = "Проверьте соединение с интернетром ошибка при подключении";
    public static String translateText(String text, String from, String to){
        //Создаем клиента для запроса к API
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 10, TimeUnit.SECONDS))
                .build();
        /*
        * Создаем сам запрос, заметь как собирается строка запроса, сама URL плюс текст для
        * перевода + параметры с какого на какой язык переводим, в нашем случае они всегда будут
        * ru в en
        * */
        Request request = new Request.Builder()
                .url(API_URL + text + "&langpair=" + from + "|" + to)
                .build();
        //Начинаем запрос попутно обрабатывая ошибки которые могут возникнуть при подключении
        try(Response res = client.newCall(request).execute()){
            //После загрузки данных извлекаем их в json
            JSONObject json = new JSONObject(res.body().string());
            //Получаем переведенный текст из json обьекта и возвращаем его
            String translatedText = json.getJSONObject("responseData").getString("translatedText");
            return encodingToUTF8(translatedText);
        } catch (IOException | JSONException e){
            e.printStackTrace();
            return NETWORK_ERROR;
        }
    }
    //Функция необходимая для декодирования текста, он приходит в формате UNICODE типо
     //\u0427\u0435\u0440\u0442\u043e\u0432\u0449\u0438\u043d\u0430, а тут делают текст читаемым
    private static String encodingToUTF8(String text){
        return new String(text.getBytes(StandardCharsets.UTF_8));
    }
}
