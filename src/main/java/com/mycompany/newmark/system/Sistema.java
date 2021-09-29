package com.mycompany.newmark.system;

public class Sistema {

	public static final String VERSAO = "3.3.3.2";

	/*
	 * 3.3.2
	 * 
	 * Correção da pesquisa do pedido que exibia a informação do campo "Etiqueta" na
	 * coluna "Subnúcleo". |Administracao - BuscaID |Chaves_Banco String SUBNUCLEO
	 * |identificadorMateriaDAO - buscaIdentificador - buscaIdentificadorPorId
	 * 
	 * 
	 * Adicionado campo etiqueta ao editar matéria através da janela
	 * "Edição de Matéria Cadastrada". |Controller_TagEdicaoMateria JFXTextField
	 * Etiqueta - Alterar. |IdentificadorMateriaDAO -atualizarIdentificadorMateria
	 * 
	 * Adicionado conversão da String para Maiúsculo dos campos pedido, complemento,
	 * etiqueta ao inserir registro na janela "Identificador de Matérias" na
	 * "Administração". |Administracao -inserirIdentificadorMateria
	 * 
	 * Correção da concatenação do Subnúcleo com a etiqueta. |Triagem_Etiquetas -
	 * triarBanco |Processo_PetiçãoInicial - verificarNucleo
	 * 
	 */

	/*
	 * 3.3.3
	 * 
	 * Correção da exibição da concatenação da etiqueta com o subnúcleo.
	 * |Processo_PetiçãoInicial - verificarNucleo
	 * 
	 * .2
	 * Remoção da exibição da concatenação do subnúcleo quando não se é possível
	 * localizar a palavra chave
	 * 
	 */

}