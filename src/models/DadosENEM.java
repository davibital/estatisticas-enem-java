package models;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.HashMap;

public class DadosENEM {
  private String ano;
  private ArquivoCSV arquivoCSV;

  public DadosENEM(String ano) {
    arquivoCSV = new ArquivoCSV("microdados-enem/");
    String nomeArquivo = "MICRODADOS_ENEM_" + ano;
    arquivoCSV.carregarArquivo(nomeArquivo);

    this.ano = ano;
  }

  public int obterTotalInscritos() {
    List<String> linhasTabela = arquivoCSV.obterColuna("NU_INSCRICAO");
    return linhasTabela.size();
  }

  public Map<String, Integer> obterNumeroInscritosPorGenero() {
    List<String> colunaInscritosPorGenero = arquivoCSV.obterColuna("TP_SEXO");
    Map<String, Integer> inscritosPorGenero = new HashMap<>();
    Stream<String> generoMasculino = colunaInscritosPorGenero.stream().filter(genero -> genero.equals("M"));
    Stream<String> generoFeminino = colunaInscritosPorGenero.stream().filter(genero -> genero.equals("F"));
    
    int contagemMasculino = (int) generoMasculino.count();
    int contagemFeminino = (int) generoFeminino.count(); 

    inscritosPorGenero.put("Masculino", contagemMasculino);
    inscritosPorGenero.put("Feminino", contagemFeminino);

    return inscritosPorGenero;
  }

  public Map<String, Double> obterPercentualInscritosPorGenero() {
    Map<String, Double> percentualInscritosPorGenero = new HashMap<>();

    int inscritosMasculino = obterNumeroInscritosPorGenero().get("Masculino");
    int inscritosFeminino = obterNumeroInscritosPorGenero().get("Feminino");
    double percentualMasculino = 0;
    double percentualFeminino = 0;
    int total = inscritosMasculino + inscritosFeminino;

    percentualMasculino = ((double)inscritosMasculino / total) * 100;
    percentualFeminino = ((double)inscritosFeminino / total) * 100;

    percentualInscritosPorGenero.put("Masculino", percentualMasculino);
    percentualInscritosPorGenero.put("Feminino", percentualFeminino);

    return percentualInscritosPorGenero;
  }

  public int obterTotalPresentes() {
    int totalPresentes = 0;
    List<String> relacaoParticipantes = arquivoCSV.obterColuna("TP_PRESENCA");

    for (String presenca : relacaoParticipantes) {
      if (presenca.equals("1"))
        totalPresentes++;
    }

    return totalPresentes;
  }

  public int obterTotalAusentes() {
    return obterTotalInscritos() - obterTotalPresentes();
  }
    
  public String obterAno() {
    return ano;
  }
}
