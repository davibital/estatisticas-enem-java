package models;

import java.util.Map;

public class DadosEnemNovo extends DadosEnem {

  public DadosEnemNovo(int ano) {
    try {
      if (ano <= 2008)
        throw new IllegalArgumentException("Ano inválido!");

      arquivoCSV = new ArquivoCSV("microdados-enem/");
      String nomeArquivo = "MICRODADOS_ENEM_" + ano;
      arquivoCSV.carregarArquivo(nomeArquivo);
      
      this.ano = "" + ano;
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.err.println("O ENEM antigo vai do ano 2009 até o ano 2022");
    }
  }

  @Override
  public Map<String, Integer> obterRelacaoEstados() {
    
  }
  
}
