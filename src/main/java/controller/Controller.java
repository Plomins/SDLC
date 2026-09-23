package main.java.controller;

import main.java.IObservable;
import main.java.IObserver;
import main.java.view.MainView;
import main.java.view.InputView;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Controller {
    private final LifeCalculatorModel model;
    private final MainView mainView;

    public Controller(LifeCalculatorModel model, MainView mainView) {
        this.model = model;
        this.mainView = mainView;
        initListeners();
    }

    private void initListeners() {
        mainView.getInputButton().addActionListener(e -> showInputDialog());
    }

    private void showInputDialog() {
        InputView dialog = new InputView(mainView, model);
        dialog.addOkListener(e -> {
            try {
                int day = dialog.getDay();
                int month = dialog.getMonth();
                int year = dialog.getYear();
                LocalDate birthDate = LocalDate.of(year, month, day);
                model.setBirthDate(birthDate);
                dialog.setConfirmed(true);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Введите корректные числа для дня, месяца и года",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog,
                        ex.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.addCancelListener(e -> {
            dialog.setConfirmed(false);
            dialog.dispose();
        });
        dialog.setVisible(true);
    }

    public static class LifeCalculatorModel implements IObservable {
        private LocalDate birthDate;
        private LocalDate lastBirthDate;
        private final List<IObserver> observers = new ArrayList<>();

        // === Файл для сохранения даты между запусками ===
        private static final Path SAVE_FILE =
                Paths.get(System.getProperty("user.home"), ".life_calculator.properties");

        private static final double SLEEP_HOURS_PER_DAY = 8.0;
        private static final double BLINKS_PER_MINUTE = 15.0;
        private static final double HEART_BEATS_PER_MINUTE = 70.0;
        private static final double BLOOD_LITERS_PER_MINUTE = 5.0;
        private static final double WATER_LITERS_PER_DAY = 2.0;
        private static final double LAUGHS_PER_DAY = 15.0;

        public LifeCalculatorModel() {
            // Пытаемся загрузить сохранённую дату
            LocalDate loaded = loadBirthDate();
            if (loaded != null) {
                this.birthDate = loaded;
                this.lastBirthDate = loaded;
            } else {
                // Значение по умолчанию – 20 лет назад
                this.birthDate = LocalDate.now().minusYears(20);
                this.lastBirthDate = this.birthDate;
            }
        }

        public void setBirthDate(LocalDate birthDate) {
            if (birthDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Дата рождения не может быть в будущем");
            }
            this.birthDate = birthDate;
            this.lastBirthDate = birthDate;
            saveBirthDate(birthDate);   // сохраняем на диск
            notifyObservers();
        }

        public LocalDate getLastBirthDate() {
            return lastBirthDate;
        }

        // === Сохранение / загрузка ===
        private void saveBirthDate(LocalDate date) {
            Properties props = new Properties();
            props.setProperty("birthdate", date.toString());
            try (OutputStream out = Files.newOutputStream(SAVE_FILE)) {
                props.store(out, "Life Calculator - last birth date");
            } catch (Exception e) {
                System.err.println("Не удалось сохранить дату: " + e.getMessage());
            }
        }

        private LocalDate loadBirthDate() {
            if (!Files.exists(SAVE_FILE)) return null;
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(SAVE_FILE)) {
                props.load(in);
                String s = props.getProperty("birthdate");
                if (s != null && !s.isBlank()) return LocalDate.parse(s);
            } catch (Exception e) {
                System.err.println("Не удалось загрузить дату: " + e.getMessage());
            }
            return null;
        }

        private long getAgeInDays() {
            return ChronoUnit.DAYS.between(birthDate, LocalDate.now());
        }

        private long getAgeInMinutes() {
            return getAgeInDays() * 24L * 60L;
        }

        public double getSleepHours() {
            return getAgeInDays() * SLEEP_HOURS_PER_DAY;
        }

        public double getSleepDays() {
            return getSleepHours() / 24.0;
        }

        public long getBlinks() {
            return Math.round(getAgeInMinutes() * BLINKS_PER_MINUTE);
        }

        public long getHeartBeats() {
            return Math.round(getAgeInMinutes() * HEART_BEATS_PER_MINUTE);
        }

        public double getBloodLiters() {
            return getAgeInMinutes() * BLOOD_LITERS_PER_MINUTE;
        }

        public double getWaterLiters() {
            return getAgeInDays() * WATER_LITERS_PER_DAY;
        }

        public long getLaughs() {
            return Math.round(getAgeInDays() * LAUGHS_PER_DAY);
        }

        @Override
        public void addObserver(IObserver observer) {
            observers.add(observer);
        }

        @Override
        public void removeObserver(IObserver observer) {
            observers.remove(observer);
        }

        @Override
        public void notifyObservers() {
            for (IObserver obs : observers) {
                obs.update(this);
            }
        }
    }
}