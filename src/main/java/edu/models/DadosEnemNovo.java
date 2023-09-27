package edu.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DadosEnemNovo extends DadosEnem {

  public DadosEnemNovo(int ano) {
    try {
      if (ano <= 2008)
        throw new IllegalArgumentException("Ano inválido!");

      arquivoCSV = new ArquivoCSV("microdados-enem/novo/");
      String nomeArquivo = "enem_" + ano;
      arquivoCSV.carregarArquivo(nomeArquivo);
      
      this.ano = "" + ano;
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.err.println("O ENEM antigo vai do ano 2009 até o ano 2022");
    }
  }

  @Override
  public int obterTotalInscritos() {
    return (int) arquivoCSV.obterQuantidadeLinhasValidas();
  }

  @Override
  public Map<String, Integer> obterRelacaoEstados() {
    Map<String, Integer> relacao = new TreeMap<>();
    List<String> colunaUF = arquivoCSV.obterColuna("SG_UF_ESC");

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

  public List<String> obterAreasConhecimento() {
    return new ArrayList<>(Arrays.asList("Ciencias da Natureza e suas Tecnologias", "Ciencias Humanas e suas Tecnologias", "Linguagens, Códigos e suas Tecnologias", "Matemática e suas Tecnologias")) ;
  }

  public double obterMediaProvaCN() {
    return obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_CN"));
  }

  public double obterMediaProvaCH() {
    return obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_CH"));
  }

  public double obterMediaProvaLC() {
    return obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_LC"));
  }

  public double obterMediaProvaMT() {
    return obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_MT"));
  }
  
}
