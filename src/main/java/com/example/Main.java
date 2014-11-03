package main.java.com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;






import scala.collection.Iterator;
import static java.lang.System.*;

class Main {
	
    private static Connection[] CONNECTIONS = new Connection[3];
    public static String[] partes;
    
    
    
    public static void main(String[] args) {

        try { 
        	FileReader arq = new FileReader("db/subset_tracks_per_year.txt");
        	BufferedReader lerArq = new BufferedReader(arq);
        	String linha = lerArq.readLine();  
        	while (linha != null) { 
        		
        		linha = lerArq.readLine();// lê da segunda até a última linha
        		if (linha != null){
	        		partes = linha.split("<SEP>");
	        		String ano = partes[0];
	        		String nomeMusica = partes[3];
	        		ArrayList<String> musica = realizaConsulta(0, "SELECT * FROM songs WHERE track_id='" + partes[1] + "'");
	                String[] divisaoMusica = musica.get(0).split(",");
	                String artista = divisaoMusica[4];
	                System.out.println("artista" + artista);
	                ArrayList<String> tags = realizaConsulta(1, "SELECT * FROM artist_mbtag WHERE artist_id='" +
	                artista.trim() + "'");
	                
	                for(String t: tags){
	                	String tag = t.split(",")[1];
	                	if(tag.trim().equals("rock") || tag.trim().equals("reggae") || tag.trim().equals("pop") ||
	                			tag.trim().equals("country") || tag.trim().equals("jazz")){
		                	CSVTests.escreve(ano, tag, nomeMusica);
		                	System.out.println("ano: " + ano + "tag: "+ tag + "musica: " + nomeMusica);
	                	}
	                }
	                
	                System.out.println("________________________________\n\n\n\n");
        		}
        	}
        }catch(IOException e){
        	System.err.printf("Erro na abertura do arquivo: %s.\n",
        	          e.getMessage());
        }
       
    }
    
    private static ArrayList<String> realizaConsulta(int bd, String query2){
    	final String[] DBS = {"subset_track_metadata.db", "subset_artist_term.db", "subset_artist_similarity.db"};
    	ArrayList<String> resultado = new ArrayList<>();
    	try {
            Class.forName("org.sqlite.JDBC");
            
            CONNECTIONS[0] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[0]);
            CONNECTIONS[1] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[1]);
            CONNECTIONS[2] = DriverManager.getConnection("jdbc:sqlite:db/" + DBS[2]);

            while (true) {
                Connection selConn = readDB(bd);
                String query = "SELECT * FROM songs WHERE track_id='" + partes[1] + "'";
                //String query = "SELECT * FROM songs WHERE artist_name='Aerosmith' ";
                		/**readQuery(); **/

                
                
                for (String result : runQuery(selConn, query2)) {
                	resultado.add(result);
                	//out.println(result);
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
    	return resultado;
    }


    private static Connection readDB(int sel) {

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