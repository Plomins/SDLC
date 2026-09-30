package main.java.view;

import main.java.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class InputView extends JDialog {
    private JTextField dayField, monthField, yearField;
    private JButton okButton, cancelButton;
    private boolean confirmed = false;

    public InputView(JFrame parent, Controller.LifeCalculatorModel model) {
        super(parent, "Ввод даты рождения", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // День
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("День:"), gbc);
        gbc.gridx = 1;
        dayField = new JTextField(3);
        add(dayField, gbc);

        // Месяц
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Месяц:"), gbc);
        gbc.gridx = 1;
        monthField = new JTextField(3);
        add(monthField, gbc);

        // Год
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Год:"), gbc);
        gbc.gridx = 1;
        yearField = new JTextField(5);
        add(yearField, gbc);

        // Восстановление последней даты
        LocalDate last = model.getLastBirthDate();
        dayField.setText(String.valueOf(last.getDayOfMonth()));
        monthField.setText(String.valueOf(last.getMonthValue()));
        yearField.setText(String.valueOf(last.getYear()));

        // Кнопки
        JPanel buttonPanel = new JPanel();
        okButton = new JButton("OK");
        cancelButton = new JButton("Отмена");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    public void addOkListener(ActionListener listener) {
        okButton.addActionListener(listener);
    }

    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    // ---------- Проверенный ввод ----------

    public int getDay() throws NumberFormatException {
        int day;
        try {
            day = Integer.parseInt(dayField.getText().trim());
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("День должен быть числом");
        }
        if (day <= 0) {
            throw new IllegalArgumentException("День не может быть отрицательным или нулевым");
        }
        if (day > 31) {
            throw new IllegalArgumentException("День не может быть больше 31");
        }
        return day;
    }

    public int getMonth() throws NumberFormatException {
        int month;
        try {
            month = Integer.parseInt(monthField.getText().trim());
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Месяц должен быть числом");
        }
        if (month <= 0) {
            throw new IllegalArgumentException("Месяц не может быть отрицательным или нулевым");
        }
        if (month > 12) {
            throw new IllegalArgumentException("Месяц не может быть больше 12");
        }
        return month;
    }

    public int getYear() throws NumberFormatException {
        int year;
        try {
            year = Integer.parseInt(yearField.getText().trim());
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("Год должен быть числом");
        }
        if (year <= 0) {
            throw new IllegalArgumentException("Год не может быть отрицательным или нулевым");
        }
        if (year < 1900) {
            throw new IllegalArgumentException("Год не может быть меньше 1900");
        }
        if (year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Год не может быть в будущем");
        }
        return year;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}