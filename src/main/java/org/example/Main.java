package org.example;


import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static final String API_KEY = "a7b76e3c606d48effb4e257c3ff96b01";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {

        JFrame frame = new JFrame("Weatherify");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JTextField cityInput = new JTextField();
        frame.add(cityInput, BorderLayout.NORTH);
        cityInput.setForeground(Color.GREEN);
        cityInput.setBackground(Color.BLACK);

        JTextArea weatherOutput = new JTextArea();
        weatherOutput.setEditable(false);
        frame.add(new JScrollPane(weatherOutput), BorderLayout.CENTER);

        JButton fetchWeatherButton = new JButton("Узнать погоду");
        frame.add(fetchWeatherButton, BorderLayout.SOUTH);

        fetchWeatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityInput.getText().trim();
                if (city.isEmpty()) {
                    weatherOutput.setText("Ошибка, введите название города");
                    return;
                }

                try {
                    String weatherData = getWeatherData(city);
                    if (weatherData != null) {
                        weatherOutput.setText(parseAndDisplayWeather(weatherData));
                    } else {
                        weatherOutput.setText("Не удалось получить данные. Проверьте название города");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        frame.setVisible(true);
    }

    private static String parseAndDisplayWeather(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String cityName = jsonObject.getString("name");
            JSONObject main = jsonObject.getJSONObject("main");
            double temperature = main.getDouble("temp");
            double feelsLike = main.getDouble("feels_like");
            int humidity = main.getInt("humidity");

            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            String description = weather.getString("description");

            return "Город " + cityName + "\nТемпература " + temperature + "°C" + "\nВлажность " + humidity + "%" +
                    "\nОщущается, как " + feelsLike + "°C" + "\nОписание " + description ;
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка обработки данных";
        }
    }

    private static String getWeatherData(String city) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=en";

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                System.out.println("Error" + response.code() + "" + response.message());
                return null;
            }
        }
    }
}