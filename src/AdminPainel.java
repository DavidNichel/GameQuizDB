import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AdminPainel extends JFrame {
	//criando painel para cadastro de Perguntas e Respostas do ADM.
	private int width = 700;
	private int heigth = 500;
	Container cont;
	private JTextField perguntaInput;
	private JTextField respostaInput;
	
	AdminPainel() {
		this.setTitle("Cadastro de Perguntas e Respostas Corretas");
		this.setBounds(200, 100, width, heigth);
		this.cont = this.getContentPane();
		this.cont.setBackground(Color.decode("#006000"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		this.setVisible(true);
	}
	
	private void init() {
		this.setLayout(null);
		Font fonte = new Font(Font.SERIF, Font.PLAIN, 24);
		//Criando labels e inputs
		criarLabel("Inserir Pergunta:",fonte, Color.WHITE, 60, 60, 500, 30);
		this.perguntaInput = criarTextField(1, 60, 100, 400, 60);
		
		criarLabel("Inserir Resposta Correta:",fonte, Color.WHITE, 60, 160, 500, 30);
		this.respostaInput = criarTextField(1, 60, 200, 400, 60);
		
		JButton enviar = new JButton("Enviar");
		enviar.setBounds(230, 280, 100, 30);
		this.cont.add(enviar);
		//setando um listener para o click do mouse.
		enviar.addActionListener(new ActionListener() {
			//aqui sera recebido o evento esperado
			public void actionPerformed(ActionEvent e) {
				//submeter pergunta
				salvarPerguntas();
			}
			
		});
		
		JButton sair = new JButton("Sair");
		sair.setBounds(360, 280, 100, 30);
		this.cont.add(sair);
		sair.addActionListener(new ActionListener() {
			//aqui sera recebido o evento esperado
			public void actionPerformed(ActionEvent e) {
				sair();
				
			}
			
		});
	}	
	
		public void salvarPerguntas() {
			//Pegas os inputs, verificar e salvar no Banco como nova pergunta.
			String pergunta = this.perguntaInput.getText();
			String resposta = this.respostaInput.getText();
			if(pergunta.length()==0 || resposta.length()==00)return;
			this.perguntaInput.setText("");
			this.respostaInput.setText("");
			Inicio.bd.addPergunta(pergunta, resposta);	
		}
	
		public void sair() {
			this.setVisible(false);
			System.exit(0);
		}
		
		private JTextField criarTextField(int rows, int x, int y, int w, int h) {
			JTextField textfield = new JTextField(rows);
			textfield.setBounds(x, y, w, h);
			this.cont.add(textfield);
			return textfield;
		}

		private JLabel criarLabel(String texto, Font fonte, 
				Color cor, int x, int y, int w, int h) {
			
			JLabel label = new JLabel(texto);
			label.setForeground(cor);
			label.setFont(fonte);
			label.setBounds(x, y, w, h);
			this.cont.add(label);
			return label;
			
		}
		
		
}
