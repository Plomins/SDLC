package main.java.view;

import main.java.controller.Controller;
import main.java.IObserver;
import main.java.IObservable;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame implements IObserver {
    private final Controller.LifeCalculatorModel model;
    private JTextArea resultArea;
    private JButton inputButton;

    public MainView(Controller.LifeCalculatorModel model) {
        this.model = model;
        model.addObserver(this);
        initUI();
        updateDisplay();
    }

    private void initUI() {
        setTitle("🧬 Калькулятор Жизни");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Верхняя панель с заголовком
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94)); // тёмно-синий
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel titleLabel = new JLabel("Ваша жизнь в цифрах", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Кнопка с улучшенным стилем
        inputButton = new JButton("Ввести дату рождения");
        inputButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inputButton.setBackground(new Color(46, 204, 113)); // зелёный
        inputButton.setForeground(Color.WHITE);
        inputButton.setFocusPainted(false);
        inputButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        inputButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Чтобы кнопка была в центре, добавим в отдельную панель
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(inputButton);
        headerPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Центральная панель с результатами
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(236, 240, 241)); // светлый серый
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        resultArea = new JTextArea(14, 45);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        resultArea.setBackground(new Color(255, 255, 255));
        resultArea.setForeground(new Color(44, 62, 80));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        resultArea.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(236, 240, 241));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Нижняя панель (необязательно)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 30));
        JLabel footerLabel = new JLabel("Статистика основана на средних данных", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Размер и положение
        setSize(600, 480);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JButton getInputButton() {
        return inputButton;
    }

    @Override
    public void update(IObservable observable) {
        updateDisplay();
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("Статистика вашей жизни (приблизительно)\n\n");
        sb.append(String.format("Проведено во сне:    %,.2f часов  (%,.2f суток)\n",
                model.getSleepHours(), model.getSleepDays()));
        sb.append(String.format("Моргнули:                  %,d\n", model.getBlinks()));
        sb.append(String.format("Ударов сердца:          %,d\n", model.getHeartBeats()));
        sb.append(String.format("Перекачано крови:   %,.2f литров\n", model.getBloodLiters()));
        sb.append(String.format("Выпито воды:             %,.2f литров\n", model.getWaterLiters()));
        sb.append(String.format("Посмеялся:                  %,d раз\n", model.getLaughs()));
        resultArea.setText(sb.toString());
    }
}