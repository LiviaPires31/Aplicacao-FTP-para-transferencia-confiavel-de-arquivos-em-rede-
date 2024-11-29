import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Server {
	
	static ArrayList<MeuArquivo> meusAquivos = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		
		int arquivoID = 0;
		
		JFrame jFrame = new JFrame("Servidor");	
		jFrame.setSize(450, 450);		
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
			
		JScrollPane jsPainelRolagem = new JScrollPane(jPanel);
		jsPainelRolagem.setVerticalScrollBarPolicy(jsPainelRolagem.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		JLabel jlTitulo = new JLabel("Receber arquivos");
		jlTitulo.setFont(new Font("Arial", Font.BOLD, 25));
		jlTitulo.setBorder(new EmptyBorder(20, 0, 10, 0));
		jlTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		jFrame.add(jlTitulo);
		jFrame.add(jsPainelRolagem);
		jFrame.setVisible(true);
		
		ServerSocket servidorSocket = new ServerSocket(1234);
		
		while(true) {
			
			try {
				
				Socket socket = servidorSocket.accept();
				
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
				
				int nomeArquivoTamanho = dataInputStream.readInt();
				
				if(nomeArquivoTamanho > 0) {
					byte[] nomeArquivoBytes = new byte[nomeArquivoTamanho];
					dataInputStream.readFully(nomeArquivoBytes, 0, nomeArquivoBytes.length);
					String nomeArquivo = new String(nomeArquivoBytes);
					
					int tamanhoConteudoArquivo = dataInputStream.readInt();
					
					if(tamanhoConteudoArquivo > 0) {
						byte[] conteudoArquivoBytes = new byte[tamanhoConteudoArquivo];
						dataInputStream.readFully(conteudoArquivoBytes, 0, tamanhoConteudoArquivo);
						
						JPanel jpArquivoFilaJPanel = new JPanel();
						jpArquivoFilaJPanel.setLayout(new BoxLayout(jpArquivoFilaJPanel, BoxLayout.Y_AXIS));
						
						JLabel jlNomeArquivo = new JLabel(nomeArquivo);
						jlNomeArquivo.setFont(new Font("Arial", Font.BOLD, 20));
						jlNomeArquivo.setBorder(new EmptyBorder(10, 0, 10, 0));
						jlNomeArquivo.setAlignmentX(Component.CENTER_ALIGNMENT);
						
						if(pegarExtensaoArquivo(nomeArquivo).equalsIgnoreCase("txt")) {
							jpArquivoFilaJPanel.setName(String.valueOf(arquivoID));
							jpArquivoFilaJPanel.addMouseListener(getMyMouseListener());
							
							jpArquivoFilaJPanel.add(jlNomeArquivo);
							jPanel.add(jpArquivoFilaJPanel);
							jFrame.validate();
					} else {
						jpArquivoFilaJPanel.setName(String.valueOf(arquivoID));
						jpArquivoFilaJPanel.addMouseListener(getMyMouseListener());
						jpArquivoFilaJPanel.add(jlNomeArquivo);
						jPanel.add(jpArquivoFilaJPanel);
						
						jFrame.validate();
					}
						meusAquivos.add(new MeuArquivo(arquivoID, nomeArquivo, conteudoArquivoBytes, pegarExtensaoArquivo(nomeArquivo)));
						arquivoID++;
				}	
			} 
		} catch(IOException error) {
			error.printStackTrace();
		}
	}
}
	
	public static String pegarExtensaoArquivo(String nomeArquivo) {
		
		int i = nomeArquivo.lastIndexOf('.');
		
		if(i > 0) {
			return nomeArquivo.substring(i+1);
		} else {
			return "Extensão não encontrada!";
		}
	}
	
	public static MouseListener getMyMouseListener() {
		
		return new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				JPanel jPanel = (JPanel) e.getSource();
				
				int arquivoId = Integer.parseInt(jPanel.getName());
				
				for(MeuArquivo meuArquivo: meusAquivos) {
					if(meuArquivo.getId() == arquivoId) {
						JFrame jfPreview = criarFrame(meuArquivo.getNome(), meuArquivo.getDado(), meuArquivo.getExtensaoArquivo());
						jfPreview.setVisible(true);
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	public static JFrame criarFrame(final String nomeArquivo, final byte[] dadoArquivo, String extensaoArquivo) {
		
		final JFrame jFrame = new JFrame("Download de arquivos");
		jFrame.setSize(450, 450);		
		
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		
		JLabel jlTitulo = new JLabel("Download de arquivos");
		jlTitulo.setFont(new Font("Arial", Font.BOLD, 25));
		jlTitulo.setBorder(new EmptyBorder(20, 0, 10, 0));
		jlTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel jlConfirmarJLabel = new JLabel("Tem certeza que deseja realizar o download do arquivo " + nomeArquivo + " ?");
		jlConfirmarJLabel.setFont(new Font("Arial", Font.BOLD, 20));
		jlConfirmarJLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
		jlConfirmarJLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton jbSim = new JButton("Sim");
		jbSim.setPreferredSize(new Dimension(150, 75));
		jbSim.setFont(new Font("Arial", Font.BOLD, 20));
		
		JButton jbNao = new JButton("Não");
		jbNao.setPreferredSize(new Dimension(150, 75));
		jbNao.setFont(new Font("Arial", Font.BOLD, 20));
		
		JLabel jlConteudoArquivo = new JLabel();	
		jlConteudoArquivo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jpBotoes = new JPanel();
		jpBotoes.setBorder(new EmptyBorder(20, 0, 10, 0));
		jpBotoes.add(jbSim);
		jpBotoes.add(jbNao);
		
		if(extensaoArquivo.equalsIgnoreCase("txt")) {
			jlConteudoArquivo.setText("<html>" + new String(dadoArquivo) + "</html>");
		} else {
			jlConteudoArquivo.setIcon(new ImageIcon(dadoArquivo));
		}
		
		jbSim.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				File fileParaBaixar = new File(nomeArquivo);
				
				try {
					FileOutputStream fileOutputStream = new FileOutputStream(fileParaBaixar);
					
					fileOutputStream.write(dadoArquivo);
					fileOutputStream.close();
					jFrame.dispose();
					
				} catch(IOException error) {
					error.printStackTrace();
				}
			}
		});
		
		jbNao.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				jFrame.dispose();
			}
		});
		
		jPanel.add(jlTitulo);
		jPanel.add(jlConfirmarJLabel);
		jPanel.add(jlConteudoArquivo);
		jPanel.add(jpBotoes);
		
		jFrame.add(jPanel);
		
		return jFrame;
	}
}

