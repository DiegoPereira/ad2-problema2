package main.java.com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import org.sqlite.JDBC;

import static java.lang.System.*;

class Main {
    private static Scanner in = new Scanner(System.in);
    private static Connection[] CONNECTIONS = new Connection[3];
    public static String[] partes;
    
    
    
    public static void main(String[] args) {

        try { 
        	FileReader arq = new FileReader("db/subset_tracks_per_year.txt");
        	BufferedReader lerArq = new BufferedReader(arq);
        	String linha = lerArq.readLine(); // l� a primeira linha 
        	// a vari�vel "linha" recebe o valor "null" quando o processo 
        	// de repeti��o atingir o final do arquivo texto 
        	while (linha != null) { 
        		//System.out.printf("%s\n", linha);
        		linha = lerArq.readLine();// l� da segunda at� a �ltima linha
        		if (linha != null){
	        		partes = linha.split("<SEP>");
	        		//System.out.println(partes[0]);
	        		//System.out.println(partes[1]);
        		}
        	}
        }catch(IOException e){
        	System.err.printf("Erro na abertura do arquivo: %s.\n",
        	          e.getMessage());
        }
        realizaConsulta(0, "SELECT * FROM songs WHERE artist_name='Aerosmith'");
        realizaConsulta(1, "SELECT * FROM artist_mbtag WHERE artist_id='AR12F2S1187FB56EEF'");
        
    }
    
    private static void realizaConsulta(int bd, String query2){
    	final String[] DBS = {"subset_track_metadata.db", "subset_artist_term.db", "subset_artist_similarity.db"};
    	
    	try {
            Class.forName("org.sqlite.JDBC");

            CONNECTIONS[0] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[0]);
            CONNECTIONS[1] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[1]);
            CONNECTIONS[2] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[2]);

            while (true) {
                Connection selConn = readDB(bd);
                String query = "SELECT * FROM songs WHERE track_id='" + partes[1] + "'";
                System.out.println(query);
                System.out.println(partes[0]);
                System.out.println(partes[1]);
                System.out.println(partes[2]);
                //String query = "SELECT * FROM songs WHERE artist_name='Aerosmith' ";
                		/**readQuery(); **/

                
                for (String result : runQuery(selConn, query2)) {
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