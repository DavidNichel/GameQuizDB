import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BancoDB {
	
	//usando imports de java.sql
	private Connection con;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public BancoDB() {
		
		try {
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_bd", "root", "root");
			this.stmt = con.createStatement();
			this.criarTabelas();
			this.inserirPerguntasPadrao();
		}catch(Exception e){
			System.out.println("Problemas com o setup do Banco de Dados");
			
		}
		
	}
	//metodo criado para add perguntas com as respostas
	void addPergunta(String pergunta, String resposta) {
		String query = "INSERT INTO tbl_perguntas (pergunta, resposta)"
				+ "VALUES(?, ?)";
		try {
			this.pstmt = this.con.prepareStatement(query);
			this.pstmt.setString(1, pergunta);
			this.pstmt.setString(2, resposta);
			this.pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("Problemas com addPergunta();" +e);
			System.out.println(query);
		}
	}
	
	
	
	int getTotalTentativas(int perguntaID) {
		String query = "Select count(1) as count from tbl_respostas where pergunta_id="+perguntaID;
		try{
			this.rs = stmt.executeQuery(query);
			while(this.rs.next()) {
				int count = this.rs.getInt("count");
				return count;
			}
			
		}catch(Exception e) {
			System.out.println("Problema com getTotalTentativas();" + e);
			System.out.println(query);
		}
		return 0;
	}
	
	//	INJECOES SQL.
	 void addPalpiteResposta(String palpite, int perguntaID, boolean isCorreta, String jogadorNome) {
		String query ="INSERT INTO tbl_respostas(pergunta_id, jogador, `texto`, is_correta, vencedor, tempo_resposta)" 
				+ "VALUES (?, ?, ?, ?, ((SELECT COUNT(1) FROM tbl_respostas tb WHERE pergunta_id=? and is_correta=1) = 0 AND ? = 1), CURRENT_TIMESTAMP)";
		try {
			this.pstmt = this.con.prepareStatement(query);
			this.pstmt.setInt(1,perguntaID);
			this.pstmt.setString(2,jogadorNome);
			this.pstmt.setString(3,palpite);
			this.pstmt.setInt(4,isCorreta ? 1 : 0);
			this.pstmt.setInt(5,perguntaID);
			this.pstmt.setInt(6,isCorreta ? 1 : 0);
			this.pstmt.executeUpdate();
			this.verificarVencedor(perguntaID);
		}catch(Exception e){
			System.out.println("Problemas ao tentar responder a pergunta." + e);
			System.out.println(query);
		}
	}
	 
	 //verificar se tem vencedor
	 void verificarVencedor(int perguntaID) {
		 try {
			String jogador = this.getAcertouPergunta(perguntaID);
			if(jogador==null)return;
			setAcertouPergunta(perguntaID, jogador);
		} catch (Exception e) {
			System.out.println("Problemas com verificarVencedor();"+e);
		}
	 }
	 //setando vencedor.
	 void setAcertouPergunta(int perguntaID, String jogador) {
		 try {
			String query = "UPDATE tbl_perguntas set vencedor = ?, respondido = 1 WHERE id= ?";
			this.pstmt = this.con.prepareStatement(query);
			this.pstmt.setString(1, jogador);
			this.pstmt.setInt(2, perguntaID);
			this.pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			System.out.println("Problemas com setAcertouPergunta();"+e);
		}
	 }
	 //pegando vencedor.
	 String getAcertouPergunta(int perguntaID) {
		 try {
			String query = "SELECT jogador from tbl_respostas WHERE pergunta_id="+perguntaID+" AND is_correta=1 ORDER BY tempo_resposta ASC LIMIT 1";
			this.rs = stmt.executeQuery(query);
			while(this.rs.next()) {
				return this.rs.getString("jogador");
			}
		 } catch (Exception e) {
			System.out.println("Problema com getAcertouPergunta();"+e);
		}
		 return null;
	 }
	
	//buscando perguntas do Banco de Dados
	 Pergunta getActivePergunta() {
		//ID, Pergunta, RespostaCorreta
		String query = "SELECT * FROM tbl_perguntas WHERE respondido=0 ORDER BY id ASC LIMIT 1";
		try {
			this.rs = stmt.executeQuery(query);
			while(this.rs.next()) {
				int ID = this.rs.getInt("id");
				String texto = this.rs.getString("pergunta");
				String resposta = this.rs.getString("resposta");
				Pergunta pergunta = new Pergunta(ID, texto, resposta);
				return pergunta;
			}
		}catch(Exception e){
			System.out.println("Problema com getActivePergunta(); " + e);
		}
		return null;
	}
	private void inserirPerguntasPadrao() {
		try {
			String p1 = "INSERT INTO tbl_perguntas(pergunta,resposta)"
					+ "VALUES('Qual é capital do Brasil','brasilia')";
			String p2 ="INSERT INTO tbl_perguntas(pergunta,resposta)"
					+ "VALUES('Qual é a capital da Argentina ','buenos aires')";
			String p3 ="INSERT INTO tbl_perguntas(pergunta,resposta)"
					+ "VALUES('Qual a é capital do Chile','santiago')";
			this.insert(p1);
			this.insert(p2);
			this.insert(p3);
		}catch(Exception e){
			
		}
		
	}
	//metodo para criar tabelas
	private void criarTabelas() {
		String tblCriarPerguntas ="CREATE TABLE IF NOT EXISTS `tbl_perguntas` (\r\n" + 
				"	`id` INT(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				"	`pergunta` VARCHAR(500) NOT NULL DEFAULT '' COLLATE 'utf8_general_ci',\r\n" + 
				"	`resposta` VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8_general_ci',\r\n" + 
				"	`respondido` TINYINT(1) NOT NULL DEFAULT '0',\r\n" + 
				"	`vencedor` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',\r\n" + 
				"	PRIMARY KEY (`id`) USING BTREE\r\n" + 
				")\r\n" + 
				"COLLATE='utf8_general_ci'\r\n" + 
				"ENGINE=InnoDB\r\n" + 
				";\r\n" + 
				"";
		String tblCriarRespostas="CREATE TABLE IF NOT EXISTS `tbl_respostas` (\r\n" + 
				"	`id` INT(11) NOT NULL AUTO_INCREMENT,\r\n" + 
				"	`pergunta_id` INT(11) NOT NULL,\r\n" + 
				"	`jogador` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',\r\n" + 
				"	`texto` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',\r\n" + 
				"	`is_correta` TINYINT(1) NOT NULL DEFAULT '0',\r\n" + 
				"	`vencedor` TINYINT(1) NOT NULL DEFAULT '0',\r\n" + 
				"	`tempo_resposta` DATETIME NOT NULL,\r\n" + 
				"	PRIMARY KEY (`id`) USING BTREE\r\n" + 
				")\r\n" + 
				"COLLATE='utf8_general_ci'\r\n" + 
				"ENGINE=InnoDB\r\n" + 
				";\r\n" + 
				"";
		this.insert(tblCriarPerguntas);
		this.insert(tblCriarRespostas);
		
	}
	
	
	//metodo inserir
	private void insert(String query) {
		try {
			this.stmt.executeUpdate(query);
		}catch(Exception e){
			System.out.println("Problemas com inserir query");
			System.out.println(query);
		}
		
	}
}
