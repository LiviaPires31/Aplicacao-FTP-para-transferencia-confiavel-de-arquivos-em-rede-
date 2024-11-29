import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Client {

	public static void main(String[] args) {
		
		final File[] fileParaEnviar = new File[1];
		
		JFrame jFrame = new JFrame("Cliente");	
		jFrame.setSize(450, 450);		
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel jlTitulo = new JLabel("Enviar arquivos");
		jlTitulo.setFont(new Font("Arial", Font.BOLD, 25));
		jlTitulo.setBorder(new EmptyBorder(20, 0, 10, 0));
		jlTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		final JLabel jlNomeArquivo = new JLabel("Escolha um arquivo para enviar");
		jlNomeArquivo.setFont(new Font("Arial", Font.BOLD, 20));
		jlNomeArquivo.setBorder(new EmptyBorder(50, 0, 0, 0));
		jlNomeArquivo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jpBotao = new JPanel();
		jpBotao.setBorder(new EmptyBorder(75, 0, 10, 0));
		
		JButton jbEnviarArquivo = new JButton("Enviar arquivo");
		jbEnviarArquivo.setPreferredSize(new Dimension(200, 75));
		jbEnviarArquivo.setFont(new Font("Arial", Font.BOLD, 20));
		
		JButton jbEscolherArquivo = new JButton("Escolher arquivo");
		jbEscolherArquivo.setPreferredSize(new Dimension(150, 75));
		jbEscolherArquivo.setFont(new Font("Arial", Font.BOLD, 20));
		
		jpBotao.add(jbEnviarArquivo);
		jpBotao.add(jbEscolherArquivo);
		
		jbEscolherArquivo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileEscolher = new JFileChooser();
				jFileEscolher.setDialogTitle("Escolha um arquivo para enviar");
				
				if(jFileEscolher.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileParaEnviar[0] = jFileEscolher.getSelectedFile();
					jlNomeArquivo.setText("O arquivo que você quer enviar é: " + fileParaEnviar[0].getName());
					
				}			
			}
		});
		
		jbEnviarArquivo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(fileParaEnviar[0] == null) {
					jlNomeArquivo.setText("Primeiro escolha um arquivo.");
					
				} else {
					
					try {
						
					
						FileInputStream fileInputStream = new FileInputStream(fileParaEnviar[0].getAbsolutePath());
						Socket socket = new Socket("localhost", 1234);
						
						DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
						
						String nomeArquivo = fileParaEnviar[0].getName();
						
						byte[] nomeArquivoBytes = nomeArquivo.getBytes();		
						byte[] conteudoArquivoBytes = new byte[(int)fileParaEnviar[0].length()];
						
						fileInputStream.read(conteudoArquivoBytes);
						
						dataOutputStream.writeInt(nomeArquivoBytes.length);
						dataOutputStream.write(nomeArquivoBytes);
						
						dataOutputStream.writeInt(conteudoArquivoBytes.length);
						dataOutputStream.write(conteudoArquivoBytes);
					
					} catch (IOException error) {
						error.printStackTrace();
					}
				}			
			}
		});
		
		jFrame.add(jlTitulo);
		jFrame.add(jlNomeArquivo);
		jFrame.add(jpBotao);
		jFrame.setVisible(true);
		
	}
}

