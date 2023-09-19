package models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DadosEnemAntigo extends DadosEnem {

  public DadosEnemAntigo(int ano) {
    try {
      if (ano >= 2009)
        throw new IllegalArgumentException("Ano inválido!");

      arquivoCSV = new ArquivoCSV("microdados-enem/antigo/");
      String nomeArquivo = "MICRODADOS_ENEM_" + ano;
      arquivoCSV.carregarArquivo(nomeArquivo);

      this.ano = "" + ano;
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.err.println("O ENEM antigo vai do ano 1998 até o ano 2008");
    }
  }

  public Map<String, Integer> obterRelacaoPresenca() {
    List<String> relacaoParticipantes = arquivoCSV.obterColuna("TP_PRESENCA");
    Map<String, Integer> relacaoPresenca = new TreeMap<>();
    relacaoPresenca.put("Presentes", 0);
    relacaoPresenca.put("Ausentes", 0);

    for (String tipoPresenca : relacaoParticipantes) {

      if (tipoPresenca.equals("1"))
        tipoPresenca = "Presentes";
      else
        tipoPresenca = "Ausentes";

      int novoValor = relacaoPresenca.get(tipoPresenca) + 1;

      relacaoPresenca.put(tipoPresenca, novoValor);
    }
    
    return relacaoPresenca;
  }

  @Override
  public Map<String, Integer> obterRelacaoEstados() {
    Map<String, Integer> relacao = new TreeMap<>();
    List<String> colunaUF = arquivoCSV.obterColuna("SG_UF_RESIDENCIA");

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

  public double obterMediaNotasProvaObjetiva() {
    List<String> notasString = arquivoCSV.obterColuna("NU_NOTA_OBJETIVA");
    return obterMediaValores(notasString);
  }

}