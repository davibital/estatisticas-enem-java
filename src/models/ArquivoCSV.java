package models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class ArquivoCSV {
  private String caminhoArquivo;
  private String arquivo;
  private long quantidadeLinhasValidas;
  private List<String> nomeColunasArquivo;

  public ArquivoCSV(String caminhoArquivo) {
    this.caminhoArquivo = caminhoArquivo;
    nomeColunasArquivo = new ArrayList<>();
  }

  public void carregarArquivo(String nomeArquivo) {
    Scanner varredorArquivo = null;
    Stream<String> streamLinhasArquivo = null;
    arquivo = caminhoArquivo + nomeArquivo + ".csv";

    try {
      varredorArquivo = new Scanner(new File(arquivo));
      streamLinhasArquivo = Files.lines(new File(arquivo).toPath());
      quantidadeLinhasValidas = streamLinhasArquivo.count() - 1;

      String[] primeiraLinha = varredorArquivo.nextLine().split(";");

      nomeColunasArquivo = Arrays.asList(primeiraLinha);
    } catch (IOException e) {
      System.err.println("O arquivo não foi encontrado: " + arquivo);
      System.err.println("Informe o nome do arquivo corretamente!");
    } finally {
      varredorArquivo.close();
      streamLinhasArquivo.close();
    }
  }

  public long obterQuantidadeLinhasValidas() {
    return quantidadeLinhasValidas;
  }
  
  public List<String> obterColuna(String nomeColuna) {
    Scanner varredorArquivo = null;
    List<String> coluna = new ArrayList<>();
    int indiceColuna = -1;

    try {
      varredorArquivo = new Scanner(new File(arquivo));

      for (int i = 0; i < nomeColunasArquivo.size(); i++) {
        if (nomeColunasArquivo.get(i).equals(nomeColuna))
          indiceColuna = i;
      }

      if (indiceColuna == -1)
        throw new IllegalArgumentException("Coluna não encontrada: " + nomeColuna);

      // Eliminar primeira linha do arquivo que consiste na linha de nomes das colunas
      varredorArquivo.nextLine();

      while (varredorArquivo.hasNextLine()) {
        String[] linha = varredorArquivo.nextLine().split(";");
        coluna.add(linha[indiceColuna]);
      }

    } catch (IOException e) {
      System.err.println("O arquivo não foi encontrado: " + arquivo);
      System.err.println("Carregue o arquivo correto novamente!");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      System.err.println("Informe uma coluna que está presente no arquivo.");
      System.err.println("Colunas do arquivo: " + this.nomeColunasArquivo);
    } finally {
      varredorArquivo.close();
    }

    return coluna;
  }
}
