import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog {
    private JTextField tFNombre;
    private JTextField tFUsername;
    private JTextField tFPassword;
    private JButton btRegistrar;
    private JButton btCancelar;
    private JPanel registerPanel;

    public User user;

    public RegistrationForm(JFrame parent){
        super(parent);
        setTitle("Crear nuevo usuario");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String username = tFUsername.getText();
        String password = tFPassword.getText();
        String nombre = tFNombre.getText();
        if(username.isEmpty() || password.isEmpty() || nombre.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Llenar todos los campos",
                    "Intente de nuevo",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

       user = addUserToDataBase(username, password, nombre);
        if (user!=null) {
            dispose();
        }else {
            JOptionPane.showMessageDialog(this,
                    "Error al Registrar al Usuario",
                    "Intente de Nuevo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private User addUserToDataBase(String username, String password, String nombre) {
        User user =null;
        final String DB_URL = "jdbc:mysql://localhost/mvcdemo";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement stmt= conn.createStatement();
            String sql= "INSERT INTO usuarios (username,password,nombre)" +
                    "VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,nombre);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.username = nombre;
                user.password = password;
                user.nombre = nombre;
            }
            stmt.close();
            conn.close();


        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        RegistrationForm myform = new RegistrationForm(null);
        User user = myform.user;
        if (user != null){
            System.out.println("Registro exitoso de: "+ user.nombre);
        }else{
            System.out.println("Registro Cancelado");
        }

    }
}
