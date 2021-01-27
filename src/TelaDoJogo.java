import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TelaDoJogo extends JFrame implements KeyListener {
	
	private int width = 700;
	private int height = 500;
	//Container para armazenar os componentes do JFrame.
	Container cont;
	
	//Labels e TextField
	private JLabel perguntaLabel;
	private JLabel palpiteLabel;
	private JLabel statusLabel;
	private JTextField respostaInput;
	//Tentativas
	private int totalTentativas;
	private int jogadorTentativas;
	
	private String username;
	Pergunta activePergunta;
	private static String defaultStatusLabelText ="Tente responder rapidamente";
	//criando a tela do Jogo.
	TelaDoJogo(String username){
		this.username = username;
		this.setTitle("Quiz-Davi-2021");
		this.setBounds(200, 100, width, height);
		this.cont = this.getContentPane();
		this.cont.setBackground(Color.decode("#006000"));
		//encerrando o programa ao clicar no x da tela.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		setupPergunta();
		updatePergunta();
		
		this.setVisible(true);
		
		
	}
	
	//metodo setup()
	private void setupPergunta() {
		this.activePergunta = Inicio.bd.getActivePergunta();
		if(this.activePergunta==null) {
			System.exit(0);
		}
		this.jogadorTentativas = 0;
		this.totalTentativas = 0;
		this.perguntaLabel.setText("Olá, "+this.username+"! " + this.activePergunta.texto + "?");
		this.jogadorTentativas = 0;
		this.statusLabel.setText(defaultStatusLabelText);
		this.repaint();
		
	}
	
	
	private void init() {
		// LabelPergunta, campo de resposta, e outras labels
		this.cont.setLayout(null);
		Font fonteTitulo = new Font(Font.SERIF, Font.BOLD, 19);
		Font fonteTexto = new Font(Font.SERIF, Font.PLAIN, 18);
		Font statusFonte = new Font(Font.SERIF, Font.ITALIC, 20);
		//PERGUNTASLABEL
		this.perguntaLabel = criarLabel("", 
				fonteTitulo, Color.WHITE, 60, 90, 500, 30);
		//STATUSLABEL
		this.statusLabel = criarLabel(defaultStatusLabelText,
				statusFonte, Color.WHITE, 60, 210, 500, 30);
		//TentativasLabel
		this.palpiteLabel = criarLabel("",fonteTexto,Color.WHITE, 420, 350, 250 ,90);
		this.palpiteLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		Inicio.updateTentativas();
		this.updateTentativas();
		//RespostaInput
		this.respostaInput = new JTextField(3);
		this.respostaInput.setBounds(60, 140, 400, 60);
		this.respostaInput.setFont(fonteTexto);
		this.respostaInput.addKeyListener(this);
		this.cont.add(this.respostaInput);
		
	}
	
	//Metodo para criar novas Labels evitando repetição de codigo.
	private JLabel criarLabel(String texto, Font fonte, 
			Color cor, int x, int y, int w, int h) {
		
		JLabel label = new JLabel(texto);
		label.setForeground(cor);
		label.setFont(fonte);
		label.setBounds(x, y, w, h);
		this.cont.add(label);
		return label;
		
	}
	
	void setTotalTentativas(int totalTentativas) {
		this.totalTentativas = totalTentativas;
		updateTentativas();
	}
	
	//metodo para atualizar o placar de tentativas
	private void updateTentativas() {
		String tryHtml = "<html><div style='padding: 7px'>Total tentativas: "+this.totalTentativas+""
				+ "<br/>Suas tentativas: "+this.jogadorTentativas+"</div></html>";
		this.palpiteLabel.setText(tryHtml);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Quando a tecla ENTER for pressionada a tentativa sera submetida caso o campo não esteja vazio.
		if(e.getKeyCode()!=KeyEvent.VK_ENTER) return;
		//solucao para evitar espacos desnecessarios. trim()
		String text = this.respostaInput.getText().trim();
		//campo vazio, nada acontece ao pressionar enter.
		if(text.length()==0) return;
		this.jogadorTentativas++;
		this.totalTentativas++;
		this.updateTentativas();
		
		boolean isCorrecta = text.equalsIgnoreCase(this.activePergunta.resposta);		
		Inicio.bd.addPalpiteResposta(text, this.activePergunta.ID, isCorrecta, this.username);
		
		this.respostaInput.setText("");
		if (isCorrecta) {
			this.statusLabel.setText("Acertou!");
			setupPergunta();
		}else {
			if(this.jogadorTentativas==1)this.statusLabel.setText("Errou, tente novamente!");
			else this.statusLabel.setText("você já errou "+this.jogadorTentativas+" vezes, tente novamente!");
		}
		
	}
		//passar proxima pergunta ao ter um vencedor
		void updatePergunta() {
			Thread thread = new Thread() {
				public void run() {
				do {
					Inicio.pause(300);
					//buscando ID na classe statica
					String winner = Inicio.bd.getAcertouPergunta(Inicio.tj.activePergunta.ID);
					//se tem vencedor setupQuestion, para a proxima pergunta.
					if(winner != null) Inicio.tj.setupPergunta();
				}while(true);
			}
			
		};
		thread.start();
		}
		
		
	//não serao usados
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
		}
}
