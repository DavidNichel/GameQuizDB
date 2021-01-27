import javax.swing.JOptionPane;

public class Inicio {
	
	static BancoDB bd;
	static TelaDoJogo tj;
	
	public static void main(String[] args) {
		bd = new BancoDB();
		
		String username = JOptionPane.showInputDialog("Seja bem-vindo, insira seu nome: ");
		
		if(username == null)System.exit(0);
		if(username.equals("admin")) {
			//Abrir painel do admin -JFrame
			new AdminPainel();
		}else {
			
			tj = new TelaDoJogo(username); 
			
		}
		
	}
	
	
	//Verificar um vencedor em paralelo com a thread
	//Verificar se a questao foi respondida corretamente por alguem.
	
	
	//Rodar em paralelo com a thread
	static void updateTentativas() {
		Thread thread = new Thread() {
			public void run() {
				
				do {
					pause(300);
					//pegando as tentativas do BD.
					int tries = bd.getTotalTentativas(tj.activePergunta.ID);
					tj.setTotalTentativas(tries);
				}while(true);
				
			}
		};
		thread.start();
	}
	static void pause(int millis) {
		try {
			Thread.sleep(millis);
		}catch(Exception e) {
			
		}
	}
}
