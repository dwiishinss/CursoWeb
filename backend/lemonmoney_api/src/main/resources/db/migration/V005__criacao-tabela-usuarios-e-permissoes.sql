CREATE TABLE usuario (
	id BIGINT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150)
);

CREATE TABLE permissao (
	id BIGINT PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
);

CREATE TABLE usuario_permissao (
	id_usuario BIGINT NOT NULL,
	id_permissao BIGINT NOT NULL,
	PRIMARY KEY (id_usuario, id_permissao),
	FOREIGN KEY (id_usuario) REFERENCES usuario(id),
	FOREIGN KEY (id_permissao) REFERENCES permissao(id)
);
