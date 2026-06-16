package br.com.giben.usuarioM;

public class Usuario {

	private int usuarioid;
	private String nick;
	private String senha;
	private String ri;
	private int pessoa_id;
	private String avatar;

	public void criar() {

	}

	public String getAvatar() {
		return avatar;
	}

	public String getNick() {
		return nick;
	}

	public int getPessoa_id() {
		return pessoa_id;
	}

	public String getRi() {
		return ri;
	}

	public String getSenha() {
		return senha;
	}

	public int getUsuarioid() {
		return usuarioid;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setPessoa_id(int pessoa_id) {
		this.pessoa_id = pessoa_id;
	}

	public void setRi(String ri) {
		this.ri = ri;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setUsuarioid(int usuarioid) {
		this.usuarioid = usuarioid;
	}

}
