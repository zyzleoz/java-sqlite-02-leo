import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String insertSQL;
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // Conectar ao banco de dados SQLite:
            connection = DriverManager.getConnection("jdbc:sqlite:teste.db");
            System.out.println("Conexão com SQLite estabelecida!");

            // Criar uma tabela (usuario):
            String createTableSQL = 
            """
                CREATE TABLE IF NOT EXISTS usuario (
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    nome VARCHAR(256) NOT NULL, 
                    nascimento TEXT
                );
            """;

            // Criar e executar uma declaração SQL:
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Tabela 'usuario' criada ou já existe!");

            // Inserir dados na tabela 'usuario':
            insertSQL = 
            """
                INSERT INTO usuario (nome, nascimento) VALUES 
                ('Ana', '2000-06-03'), 
                ('Bruna', '2001-02-17'),
                ('Carlos', '2002-04-21'),
                ('Daniel', '2003-10-30');
            """;
            statement.execute(insertSQL);
            System.out.println("Dados inseridos na tabela 'usuario'!");

            // Consultar dados da tabela 'usuario':
            String selectSQL = "SELECT * FROM usuario;";
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + 
                                   ", Nome: " + resultSet.getString("nome") +
                                   ", Nascimento: " + resultSet.getString("nascimento"));
            }

            // Inserir novos usuários:
            insertSQL = "INSERT INTO usuario (nome, nascimento) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            // Estrutura de repetição para adicionar novos usuários:
            String continuar;
            do {
                // Pedir o nome e data de nascimento do usuário:
                System.out.print("Informe o nome do novo usuário: ");
                String nome = scanner.nextLine();
                System.out.print("Informe a data de nascimento (AAAA-MM-DD): ");
                String nascimento = scanner.nextLine();

                // Definir os valores no PreparedStatement:
                preparedStatement.setString(1, nome);
                preparedStatement.setString(2, nascimento);
                preparedStatement.executeUpdate();
                System.out.println("Novo usuário inserido!");

                // Perguntar se deseja inserir outro usuário:
                System.out.print("Deseja informar outro usuário? (sim/não): ");
                continuar = scanner.nextLine();
            } while (continuar.equalsIgnoreCase("sim"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (scanner != null) {
                    scanner.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}