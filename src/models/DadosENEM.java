package models;

import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Iterator;

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
    Map<String, Integer> inscritosPorGenero = new TreeMap<>();

    String contagemMasculino = colunaInscritosPorGenero.stream()
        .reduce("0", (acc, elementoColuna) -> {
          int accNum = Integer.parseInt(acc);
          if (elementoColuna.equals("M"))
            accNum += 1;
          acc = "" + accNum;
          return acc;
        });

    String contagemFeminino = colunaInscritosPorGenero.stream()
        .reduce("0", (acc, elementoColuna) -> {
          int accNum = Integer.parseInt(acc);
          if (elementoColuna.equals("M"))
            accNum += 1;
          acc = "" + accNum;
          return acc;
        });

    inscritosPorGenero.put("Masculino", Integer.parseInt(contagemMasculino));
    inscritosPorGenero.put("Feminino", Integer.parseInt(contagemFeminino));

    return inscritosPorGenero;
  }

  public Map<String, Double> obterPercentualInscritosPorGenero() {
    Map<String, Double> percentualInscritosPorGenero = new TreeMap<>();

    int inscritosMasculino = obterNumeroInscritosPorGenero().get("Masculino");
    int inscritosFeminino = obterNumeroInscritosPorGenero().get("Feminino");
    double percentualMasculino = 0;
    double percentualFeminino = 0;
    int total = inscritosMasculino + inscritosFeminino;

    percentualMasculino = ((double) inscritosMasculino / total) * 100;
    percentualFeminino = ((double) inscritosFeminino / total) * 100;

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

  public Map<String, Double> obterRelacaoEstadosPercentual() {
    Map<String, Double> relacaoPercentual = new TreeMap<>();
    Map<String, Integer> relacaoNumerica = obterRelacaoEstados();
    int totalInscritos = obterTotalInscritos();

    Iterator<Map.Entry<String, Integer>> mapIterator = relacaoNumerica.entrySet().iterator();

    while (mapIterator.hasNext()) {
      Map.Entry<String, Integer> proximoValor = mapIterator.next();
      String uf = proximoValor.getKey();
      double percentual = proximoValor.getValue();
      percentual = (percentual / totalInscritos) * 100;
      relacaoPercentual.put(uf, percentual);
    }

    return ordenarAlfabeticamenteDouble(relacaoPercentual);
  }

  private Map<String, Integer> ordenarAlfabeticamenteInt(Map<String, Integer> mapa) {
    Map<String, Integer> mapaOrdenado = new TreeMap<>(
      new Comparator<String>() {

        @Override
        public int compare(String str1, String str2) {
          return str1.compareTo(str2);
        }
      }
    );
      
    mapaOrdenado.putAll(mapa);

    return mapaOrdenado;
  }

  private Map<String, Double> ordenarAlfabeticamenteDouble(Map<String, Double> mapa) {
    Map<String, Double> mapaOrdenado = new TreeMap<>(
      new Comparator<String>() {

        @Override
        public int compare(String str1, String str2) {
          return str1.compareTo(str2);
        }
      }
    );
      
    mapaOrdenado.putAll(mapa);

    return mapaOrdenado;
  }

  public double obterMediaNotasProvaObjetiva() {
    List<String> notasString = arquivoCSV.obterColuna("NU_NOTA_OBJETIVA");
    return obterMedia(notasString);
  }
  
  public double obterMediaNotasRedacao() {
    List<String> notasString = arquivoCSV.obterColuna("NU_NOTA_REDACAO");
    return obterMedia(notasString);
  }

  private double obterMedia(List<String> notasString) {
    double notaTotal = 0;
    double totalInscritos = 0;
    double media;

    for (String notaString : notasString) {
      if (notaString.isEmpty())
        notaString = "0";

      double notaNum = Double.parseDouble(notaString);
      notaTotal += notaNum;
      totalInscritos++;
    }

    media = notaTotal / totalInscritos;

    return media;
  }

  public String obterAno() {
    return ano;
  }
}
