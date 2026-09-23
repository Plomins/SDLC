package main;

import main.java.view.MainView;
import main.java.controller.Controller;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Если Nimbus недоступен, оставляем системный
        }
        javax.swing.SwingUtilities.invokeLater(() -> {
            Controller.LifeCalculatorModel model = new Controller.LifeCalculatorModel();
            MainView view = new MainView(model);
            Controller controller = new Controller(model, view);
        });
    }
}