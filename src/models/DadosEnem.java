package models;

import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Iterator;

public abstract class DadosEnem {
  protected String ano;
  protected ArquivoCSV arquivoCSV;

  public abstract Map<String, Integer> obterRelacaoEstados();

  public int obterTotalInscritos() {
    List<String> linhasTabela = arquivoCSV.obterColuna("NU_INSCRICAO");
    // Retorna penúltima linha da tabela, que contém o maior número de inscrição
    return Integer.parseInt(linhasTabela.get(linhasTabela.size() - 1));
  }

  public Map<String, Integer> obterRelacaoIdade() {
    List<String> colunaIdade = arquivoCSV.obterColuna("TP_FAIXA_ETARIA");
    Map<String, Integer> relacaoIdade = new TreeMap<>();
    relacaoIdade.put("Menor de 17 anos", 0);
    relacaoIdade.put("Entre 17 e 20 anos", 0);
    relacaoIdade.put("Entre 21 e 25 anos", 0);
    relacaoIdade.put("Entre 26 e 30 anos", 0);
    relacaoIdade.put("Entre 31 e 35 anos", 0);
    relacaoIdade.put("Entre 36 e 40 anos", 0);
    relacaoIdade.put("Entre 41 e 45 anos", 0);
    relacaoIdade.put("Entre 46 e 50 anos", 0);
    relacaoIdade.put("Entre 51 e 55 anos", 0);
    relacaoIdade.put("Entre 56 e 60 anos", 0);
    relacaoIdade.put("Maior de 60 anos", 0);
    
    for (String codigo : colunaIdade) {
      int codigoNum = Integer.parseInt(codigo);

      if (codigoNum < 1)
        incrementarEm(relacaoIdade, "Menor de 17 anos");
      else if (codigoNum <= 5)
        incrementarEm(relacaoIdade, "Entre 17 e 20 anos");
      else if (codigoNum <= 10)
        incrementarEm(relacaoIdade, "Entre 21 e 25 anos");
      else if (codigoNum == 11)
        incrementarEm(relacaoIdade, "Entre 26 e 30 anos");
      else if (codigoNum == 12)
        incrementarEm(relacaoIdade, "Entre 31 e 35 anos");
      else if (codigoNum == 13)
        incrementarEm(relacaoIdade, "Entre 36 e 40 anos");
      else if (codigoNum == 14)
        incrementarEm(relacaoIdade, "Entre 41 e 45 anos");
      else if (codigoNum == 15)
        incrementarEm(relacaoIdade, "Entre 46 e 50 anos");
      else if (codigoNum == 16)
        incrementarEm(relacaoIdade, "Entre 51 e 55 anos");
      else if (codigoNum == 17)
        incrementarEm(relacaoIdade, "Entre 56 e 60 anos");
      else
        incrementarEm(relacaoIdade, "Maior de 60 anos");
    }

    int dadosNaoInformados = (int) arquivoCSV.obterQuantidadeLinhasValidas() - obterTotalInscritos();

    relacaoIdade.put("Dados não informados", dadosNaoInformados);

    return relacaoIdade;
  }

  public Map<String, Double> obterRelacaoIdadePercentual() {
    Map<String, Integer> relacaoIdade = obterRelacaoIdade();
    Map<String, Double> relacaoIdadePercentual = new TreeMap<>();

    int total = obterTotalInscritos();

    relacaoIdade.forEach((chave, valor) -> {
      double valorPercentual = ((double) valor / total) * 100;
      relacaoIdadePercentual.put(chave, valorPercentual);
    });

    return relacaoIdadePercentual;
  }
  
  private void incrementarEm(Map<String, Integer> mapa, String chave) {
    int valorAntigo = mapa.get(chave);
    mapa.put(chave, valorAntigo + 1);
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
          if (elementoColuna.equals("F"))
            accNum += 1;
          acc = "" + accNum;
          return acc;
        });

    int dadosNaoInformados = (int) arquivoCSV.obterQuantidadeLinhasValidas() - obterTotalInscritos();

    inscritosPorGenero.put("Masculino", Integer.parseInt(contagemMasculino));
    inscritosPorGenero.put("Feminino", Integer.parseInt(contagemFeminino));
    inscritosPorGenero.put("Dados não informados", dadosNaoInformados);

    return inscritosPorGenero;
  }

  public Map<String, Double> obterPercentualInscritosPorGenero() {
    Map<String, Integer> numerosInscritosPorGenero = obterNumeroInscritosPorGenero();
    Map<String, Double> percentualInscritosPorGenero = new TreeMap<>();

    int inscritosMasculino = numerosInscritosPorGenero.get("Masculino");
    int inscritosFeminino = numerosInscritosPorGenero.get("Feminino");
    int dadosNaoInformados = numerosInscritosPorGenero.get("Dados não informados");
    
    double percentualMasculino = 0;
    double percentualFeminino = 0;
    double percentualNaoInformado = 0;
    int total = inscritosMasculino + inscritosFeminino + dadosNaoInformados;

    percentualMasculino = ((double) inscritosMasculino / total) * 100;
    percentualFeminino = ((double) inscritosFeminino / total) * 100;
    percentualNaoInformado = ((double) dadosNaoInformados / total) * 100;

    percentualInscritosPorGenero.put("Masculino", percentualMasculino);
    percentualInscritosPorGenero.put("Feminino", percentualFeminino);
    percentualInscritosPorGenero.put("Dados não informados", percentualNaoInformado);

    return percentualInscritosPorGenero;
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

  protected Map<String, Integer> ordenarAlfabeticamenteInt(Map<String, Integer> mapa) {
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

  protected Map<String, Double> ordenarAlfabeticamenteDouble(Map<String, Double> mapa) {
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
  
  public double obterMediaNotasRedacao() {
    List<String> notasString = arquivoCSV.obterColuna("NU_NOTA_REDACAO");
    return obterMediaValores(notasString);
  }

  protected double obterMediaValores(List<String> valores) {
    double valorTotal = 0;
    double quantidade = 0;
    double media;

    for (String valorString : valores) {
      if (valorString.isEmpty())
        valorString = "0";

      double notaNum = Double.parseDouble(valorString);
      valorTotal += notaNum;
      quantidade++;
    }

    media = valorTotal / quantidade;

    return media;
  }

  public String obterAno() {
    return ano;
  }
}
