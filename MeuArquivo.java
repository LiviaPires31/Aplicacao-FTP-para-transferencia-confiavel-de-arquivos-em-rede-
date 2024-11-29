public class MeuArquivo {
	
	private int id;
	private String nome;
	private byte[] dado;
	private String extensaoArquivo;
	
	public MeuArquivo(int id, String nome, byte[] dado, String extensaoArquivo) {
		this.id = id;
		this.nome = nome;
		this.dado = dado;
		this.extensaoArquivo = extensaoArquivo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public byte[] getDado() {
		return dado;
	}

	public void setDado(byte[] dado) {
		this.dado = dado;
	}

	public String getExtensaoArquivo() {
		return extensaoArquivo;
	}

	public void setExtensaoArquivo(String extensaoArquivo) {
		this.extensaoArquivo = extensaoArquivo;
	}	
}
