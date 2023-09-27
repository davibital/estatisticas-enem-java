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

  private Map<String, Integer> obterRelacaoPresenca(List<String> relacaoParticipantes) {
    Map<String, Integer> relacaoPresenca = new TreeMap<>();
    relacaoPresenca.put("Ausentes", 0);
    relacaoPresenca.put("Eliminados", 0);
    relacaoPresenca.put("Presentes", 0);
    relacaoPresenca.put("Dados não informados", 0);

    for (String tipoPresenca : relacaoParticipantes) {

      if (tipoPresenca.equals("0"))
        tipoPresenca = "Ausentes";
      else if (tipoPresenca.equals("1"))
        tipoPresenca = "Presentes";
      else if (tipoPresenca.equals("2"))
        tipoPresenca = "Eliminados";
      else
        tipoPresenca = "Dados não informados";

      int novoValor = relacaoPresenca.get(tipoPresenca) + 1;

      relacaoPresenca.put(tipoPresenca, novoValor);
    }
    
    return relacaoPresenca;
  }

  public Map<String, Integer> obterRelacaoPresencaCN() {
    return obterRelacaoPresenca(arquivoCSV.obterColuna("TP_PRESENCA_CN"));
  }

  public Map<String, Integer> obterRelacaoPresencaCH() {
    return obterRelacaoPresenca(arquivoCSV.obterColuna("TP_PRESENCA_CH"));
  }

  public Map<String, Integer> obterRelacaoPresencaLC() {
    return obterRelacaoPresenca(arquivoCSV.obterColuna("TP_PRESENCA_LC"));
  }

  public Map<String, Integer> obterRelacaoPresencaMT() {
    return obterRelacaoPresenca(arquivoCSV.obterColuna("TP_PRESENCA_MT"));
  }

  public Map<String, Double> obterMediaNotasProvaObjetiva() {
    Map<String, Double> mediaPorAreaConhecimento = new TreeMap<>();
    double mediaCN = obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_CN"));
    double mediaCH = obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_CH"));
    double mediaLC = obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_LC"));
    double mediaMT = obterMediaValores(arquivoCSV.obterColuna("NU_NOTA_MT"));

    mediaPorAreaConhecimento.put("Ciencias da Natureza e suas Tecnologias", mediaCN);
    mediaPorAreaConhecimento.put("Ciencias Humanas e suas Tecnologias", mediaCH);
    mediaPorAreaConhecimento.put("Linguagens, Códigos e suas Tecnologias", mediaLC);
    mediaPorAreaConhecimento.put("Matemática e suas Tecnologias", mediaMT);

    return mediaPorAreaConhecimento;
  }

  public List<String> obterAreasConhecimento() {
    return new ArrayList<>(Arrays.asList("Ciencias da Natureza e suas Tecnologias", "Ciencias Humanas e suas Tecnologias", "Linguagens, Códigos e suas Tecnologias", "Matemática e suas Tecnologias")) ;
  }
/* 
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
  } */
  
}
