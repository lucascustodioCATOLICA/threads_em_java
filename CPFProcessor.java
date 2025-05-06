import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CPFProcessor {
    private static final int NUM_ARQUIVOS = 30; // 30 arquivos de entrada
    private static AtomicInteger totalValidos = new AtomicInteger(0);  // Contagem total de CPFs válidos
    private static AtomicInteger totalInvalidos = new AtomicInteger(0); // Contagem total de CPFs inválidos

    // Método para processar os arquivos
    public static void processFiles(List<File> files, int numThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Callable<Void>> tasks = new ArrayList<>();

        // Adicionar uma tarefa para cada arquivo
        for (File file : files) {
            tasks.add(() -> {
                processFile(file);  // Processar o arquivo
                return null;
            });
        }

        long startTime = System.currentTimeMillis();
        executor.invokeAll(tasks);  // Executa as tarefas em paralelo
        long endTime = System.currentTimeMillis();

        executor.shutdown();

        long duration = endTime - startTime;
        System.out.println("Tempo total de execução: " + duration + "ms");

        // Salvar tempo de execução
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/versao_" + numThreads + "_threads.txt"))) {
            writer.write("Tempo total de execução: " + duration + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Exibir a soma total dos CPFs válidos e inválidos
        System.out.println("Total de CPFs válidos: " + totalValidos.get());
        System.out.println("Total de CPFs inválidos: " + totalInvalidos.get());
    }

    // Processa cada arquivo e atualiza as contagens globais de CPFs válidos e inválidos
    private static void processFile(File file) {
        int validCount = 0;
        int invalidCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String cpf = line.trim();
                if (CPFValidator.validaCPF(cpf)) {
                    validCount++;
                } else {
                    invalidCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Atualizar as contagens totais de CPFs válidos e inválidos de forma segura
        totalValidos.addAndGet(validCount);   // Soma de CPFs válidos
        totalInvalidos.addAndGet(invalidCount);  // Soma de CPFs inválidos
    }

    public static List<List<File>> divideFiles(List<File> files, int numThreads) {
        List<List<File>> dividedFiles = new ArrayList<>();
        int filesPerThread = files.size() / numThreads;
        int remainder = files.size() % numThreads;

        int start = 0;
        for (int i = 0; i < numThreads; i++) {
            int end = start + filesPerThread + (i < remainder ? 1 : 0);
            dividedFiles.add(files.subList(start, end));
            start = end;
        }
        return dividedFiles;
    }

    public static void main(String[] args) throws InterruptedException {
        File folder = new File("input");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (listOfFiles != null) {
            List<File> files = Arrays.asList(listOfFiles);

            int numThreads = 1; // Modificar para 2, 3, etc.

            List<List<File>> dividedFiles = divideFiles(files, numThreads);

            // Criar tarefas para processar os arquivos em paralelo
            for (List<File> fileSubset : dividedFiles) {
                processFiles(fileSubset, numThreads);
            }
        }
    }
}
