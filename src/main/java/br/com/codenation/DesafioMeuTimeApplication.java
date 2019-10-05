package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

	private HashMap<Long, Time> timesCadastrados        = new HashMap<>(); //Hash para armazenar todos os times cadastrados e seus IDs na memória
	private HashMap<Long, Jogador> jogadoresCadastrados = new HashMap<>(); //Hash para armazenar todos os jogadores cadastrados e seus IDs na memória

	@Desafio("incluirTime")
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
		if (timesCadastrados.containsKey(id)) //se já existir um time cadastrado
			throw new IdentificadorUtilizadoException(); //retorna exceprion

		Time novoTime = new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario); //criar novo time
		timesCadastrados.put(id, novoTime); //adiciona na lista da memória
	}

	@Desafio("incluirJogador")
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {

		Time time = timesCadastrados.get(idTime); //busca o objeto time a qual o idTime pertece

		if (time == null)
			throw new TimeNaoEncontradoException(); //se não encontrar time cadastrado com esse id, retorna exception

		if (jogadoresCadastrados.containsKey(id)) //se este jogador já estiver cadastrado neste time
			throw new IdentificadorUtilizadoException(); //retorna exception

		Jogador novoJogador = new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario); //criar novo jogador
		jogadoresCadastrados.put(id, novoJogador);//adiciona na lista da memória
	}

	@Desafio("definirCapitao")
	public void definirCapitao(Long idJogador) {

		Jogador capitao = jogadoresCadastrados.get(idJogador); //encontra o jogador cujo id aponta que é o captão

		if (capitao == null)
			throw new JogadorNaoEncontradoException(); //se não encontrar, retorna exception

		Time timeCapitao = timesCadastrados.get(capitao.getIdTime()); //encontra o time ao qual o captão pertence
		timeCapitao.setIdCapitao(capitao.getId()); //seta o jogador como captão do time
	}

	@Desafio("buscarCapitaoDoTime")
	public Long buscarCapitaoDoTime(Long idTime) {

		Time time = timesCadastrados.get(idTime); //encontra o time conforme idTime

		if (time == null)
			throw new TimeNaoEncontradoException(); //se não encontrar, retorna exception

		if (time.getIdCapitao() == null)
			throw new CapitaoNaoInformadoException(); //se o time não tiver captão setado, retorna exception

		return time.getIdCapitao(); //retorna o captão do time
	}

	@Desafio("buscarNomeJogador")
	public String buscarNomeJogador(Long idJogador) {
		Jogador jogador = jogadoresCadastrados.get(idJogador); //encontra o jogador

		if (jogador == null)
			throw new JogadorNaoEncontradoException(); //se não encontrar, retorna exception

		return jogador.getNome(); //retorna o nome do jogador
	}

	@Desafio("buscarNomeTime")
	public String buscarNomeTime(Long idTime) {
		Time time = timesCadastrados.get(idTime); //encontra o time

		if (time == null)
			throw new TimeNaoEncontradoException(); //retorna exception se não encontrar

		return time.getNome(); //retorna o nome do time
	}

	@Desafio("buscarJogadoresDoTime")
	public List<Long> buscarJogadoresDoTime(Long idTime) {

		Time time = timesCadastrados.get(idTime); //encontra o time

		if (time == null)
			throw new TimeNaoEncontradoException(); //retorna exception se não encontrar

		Stream<Jogador> listaJogadores = jogadoresCadastrados.values().stream(); //lista auxiliar que recebe uma cópia da lista de jogadores da memória

		return listaJogadores.filter(jogadores -> jogadores.getIdTime().equals(idTime)).map(Jogador::getId).collect(Collectors.toList()); //filtra a lista auxiliar pelo idTime e retorna uma lista com os IDs dos jogadores deste time
	}

	@Desafio("buscarMelhorJogadorDoTime")
	public Long buscarMelhorJogadorDoTime(Long idTime) {
		Time time = timesCadastrados.get(idTime); //encontra o time

		if (time == null)
			throw new TimeNaoEncontradoException(); //retorna exception se não encontrar

		Stream<Jogador> melhoresJogadores = jogadoresCadastrados.values().stream(); //lista auxiliar que recebe uma cópia da lista de jogadores

		return melhoresJogadores.filter(jogadores -> jogadores.getIdTime().equals(idTime)).max(Comparator.comparing(Jogador::getNivelHabilidade)).get().getId(); //filtra a lista auxiliar pelo idTime e retorna uma lista com os IDs dos melhores jogadores deste time
	}

	@Desafio("buscarJogadorMaisVelho")
	public Long buscarJogadorMaisVelho(Long idTime) {
		Time time = timesCadastrados.get(idTime); //encontra o time

		if (time == null)
			throw new TimeNaoEncontradoException(); //retorna exception se não encontrar

		Stream<Jogador> jogadoresMaisVelhos = jogadoresCadastrados.values().stream(); //lista auxiliar que recebe uma cópia da lista de jogadores

		return jogadoresMaisVelhos.filter(jogadores -> jogadores.getIdTime().equals(idTime)).min(Comparator.comparing(Jogador::getDataNascimento)).get().getId(); //filtra a lista auxiliar pelo idTime e retorna uma lista com os IDs dos jogadores mais velhos deste time
	}

	@Desafio("buscarTimes")
	public List<Long> buscarTimes() {
		return new LinkedList<>(timesCadastrados.keySet()); //retorna a lista com os IDs dos times
	}

	@Desafio("buscarJogadorMaiorSalario")
	public Long buscarJogadorMaiorSalario(Long idTime) {
		Time time = timesCadastrados.get(idTime); //encontra o time

		if (time == null)
			throw new TimeNaoEncontradoException(); //retorna exception se não encontrar

		Stream<Jogador> jogadoresMaiorSalario = jogadoresCadastrados.values().stream(); //lista auxiliar que recebe uma cópia da lista de jogadores

		return jogadoresMaiorSalario.filter(jogadores -> jogadores.getIdTime().equals(idTime)).max(Comparator.comparing(Jogador::getSalario)).get().getId(); //filtra a lista auxiliar pelo idTime e retorna uma lista com os IDs dos jogadores com maior salário deste time
	}

	@Desafio("buscarSalarioDoJogador")
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		Jogador jogador = jogadoresCadastrados.get(idJogador); //encontra jogador

		if (jogador == null)
			throw new JogadorNaoEncontradoException(); //retorna exception se não encontrar

		return jogador.getSalario(); //retorna salário do jogador
	}

	@Desafio("buscarTopJogadores")
	public List<Long> buscarTopJogadores(Integer top) {
		List<Long> topJogadores = new LinkedList<>(); //lista auxiliar para os IDs
		List<Jogador> aux = new LinkedList<>(jogadoresCadastrados.values()); //lista auxiliar que recebe uma cópia da lista de jogadores da memória
		aux.sort(Comparator.comparing(Jogador::getNivelHabilidade).reversed()); //ordena a lista de jogadores por nível de habilidade decrescente

		for(int i = 0; i < top; i++) { //em vez de while, fiz um for que deixou o código mais simplificado
			if (i >= aux.size()) //se não tiver top jogadores cadastrados
				continue; //continua até terminar o laço e retorna somente os jogadores cadastrados ordenados por nível de habilidade

			topJogadores.add(aux.get(i).getId()); //insere o ID do top jogador na lista auxiliar
		}
		return topJogadores; //retorna a lista com os IDs dos top jogadores
	}
	@Desafio("buscarCorCamisaTimeDeFora")
	public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {
		Time timeCasa = timesCadastrados.get(timeDaCasa); //encontra time da casa
		Time timeFora = timesCadastrados.get(timeDeFora); //encontra time de fora

		if (timeCasa.getCorUniformePrincipal().equals(timeFora.getCorUniformePrincipal())) //se a cor do uniforme principal do time da casa for igual a cor do uniforme principal do time de fora
			return timeFora.getCorUniformeSecundario(); //retorna a cor do uniforme secundário do time de fora

		return timeFora.getCorUniformePrincipal(); //retorna a cor do uniforme principal do tima de fora
	}
}
