package main.java.com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//import org.sqlite.JDBC;




import static java.lang.System.*;

class Main2 {
    private static Scanner in = new Scanner(System.in);
    private static Connection[] CONNECTIONS = new Connection[3];

    public static void main(String[] args) {
        final String[] DBS = {"subset_track_metadata.db", "subset_artist_term.db", "subset_artist_similarity.db"};

        try {
            Class.forName("org.sqlite.JDBC");

            CONNECTIONS[0] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[0]);
            CONNECTIONS[1] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[1]);
            CONNECTIONS[2] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[2]);

            while (true) {
            	System.out.println("1");
                Connection selConn = readDB(0);
                String query = readQuery("SELECT * FROM songs WHERE artist_name='Aerosmith'");
                //String query = "SELECT * FROM songs WHERE artist_name='Aerosmith'";
                System.out.println(query);
                System.out.println("2");
                for (String result : runQuery(selConn, query)) {
                	System.out.println("3");
                    out.println(result);
                }break;
            }

        } catch (Exception e) {
            err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            for (Connection conn : CONNECTIONS) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printDBS() {
        out.println("\n\n");
        out.println("Selecione uma base de dados para fazer a consulta ou qualquer outro dígito para sair.");
        out.println("0 - subset_track_metadata");
        out.println("1 - subset_artist_term");
        out.println("2 - subset_artist_similarity");
    }

    private static Connection readDB(int sel) {
        //printDBS();
        //int sel = in.nextInt();
        if (sel < 0 || sel > 2) {
            System.exit(0);
            return null;
        } else {
            return CONNECTIONS[sel];
        }
    }

    private static String readQuery(String consulta) {
        //out.println("Digite a consulta que você deseja executar");
       // in.nextLine();

        return consulta;
    }

    private static List<String> runQuery(Connection conn, String query) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(query);

        List<String> results = new ArrayList<String>();
        while (resultSet.next()) {
            String result = "";
            int index = 1;
            try {
                while (true) {
                    result += resultSet.getString(index) + ", ";
                    index++;
                }
            } catch (SQLException e) {
                if (!result.equals("")) {
                    result = result.substring(0, result.length() - 2);
                }
                results.add(result);
            }
        }
        stmt.close();
        return results;
    }
}