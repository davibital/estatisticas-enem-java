package models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DadosEnemNovo extends DadosEnem {

  public DadosEnemNovo(int ano) {
    try {
      if (ano <= 2008)
        throw new IllegalArgumentException("Ano inválido!");

      arquivoCSV = new ArquivoCSV("microdados-enem/novo/");
      String nomeArquivo = "MICRODADOS_ENEM_" + ano;
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
      if (!relacao.containsKey(uf)) {
        relacao.put(uf, 1);
      } else {
        int valorAntigo = relacao.get(uf);
        relacao.put(uf, valorAntigo + 1);
      }
    }

    return ordenarAlfabeticamenteInt(relacao);
  }

  private Map<String, Integer> obterRelacaoPresenca(List<String> relacaoParticipantes) {
    Map<String, Integer> relacaoPresenca = new TreeMap<>();
    relacaoPresenca.put("Ausentes", 0);
    relacaoPresenca.put("Eliminados", 0);
    relacaoPresenca.put("Presentes", 0);

    for (String tipoPresenca : relacaoParticipantes) {

      if (tipoPresenca.equals("0"))
        tipoPresenca = "Ausentes";
      else if (tipoPresenca.equals("1"))
        tipoPresenca = "Presentes";
      else
        tipoPresenca = "Eliminados";

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
