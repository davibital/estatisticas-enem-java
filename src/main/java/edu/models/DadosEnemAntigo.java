package edu.models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DadosEnemAntigo extends DadosEnem {

  public DadosEnemAntigo(int ano) {
    try {
      if (ano >= 2009)
        throw new IllegalArgumentException("Ano inválido!");

      arquivoCSV = new ArquivoCSV("microdados-enem/antigo/");
      String nomeArquivo = "enem_" + ano;
      arquivoCSV.carregarArquivo(nomeArquivo);

      this.ano = "" + ano;
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.err.println("O ENEM antigo vai do ano 1998 até o ano 2008");
    }
  }

  @Override
  public int obterTotalInscritos() {
    List<String> linhasTabela = arquivoCSV.obterColuna("NU_INSCRICAO");
    // Retorna penúltima linha da tabela, que contém o maior número de inscrição
    return Integer.parseInt(linhasTabela.get(linhasTabela.size() - 1));
  }

  @Override
  public Map<String, Integer> obterRelacaoEstados() {
    Map<String, Integer> relacao = new TreeMap<>();
    List<String> colunaUF = arquivoCSV.obterColuna("SG_UF_RESIDENCIA");

    for (String uf : colunaUF) {
      if (!uf.equals("")) {
        if (!relacao.containsKey(uf)) {
          relacao.put(uf, 1);
        } else {
          int valorAntigo = relacao.get(uf);
          relacao.put(uf, valorAntigo + 1);
        }
      }
    }

    return ordenarAlfabeticamenteInt(relacao);
  }

  public double obterMediaNotasProvaObjetiva() {
    List<String> notasString = arquivoCSV.obterColuna("NU_NOTA_OBJETIVA");
    return obterMediaValores(notasString);
  }

}