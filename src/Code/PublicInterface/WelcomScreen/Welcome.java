package Code.PublicInterface.WelcomScreen;

import Code.PublicInterface.LoginForm.Login;
import Code.PublicInterface.RegistrationForm.RegistrationForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends  JFrame {
    private JPanel welcomePanel;
    private JButton registerButton;
    private JButton signInAsAnButton;
    private JButton signInAsAButton;
    private JButton exitButton;
    private JButton signInAsUserButton;
    public Welcome(){
        setTitle("Welcome");
        setContentPane(welcomePanel);
        setMinimumSize(new Dimension(750,474));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        setVisible(true);
        signInAsAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Welcome.this.dispose();
                Login l = new Login(null,"USER");
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Welcome.this.dispose();
                RegistrationForm registrationForm = new RegistrationForm(null);
            }
        });
        signInAsAnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Welcome.this.dispose();
                Login l = new Login(null,"ADMIN");
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Welcome.this.dispose();
            }
        });
    }

    public static void main(String[] args) {
        Welcome w = new Welcome();

    }
}

