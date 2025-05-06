import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IOHandler {
    private int validos;
    private int invalidos;

    public IOHandler() {
        this.validos = 0;
        this.invalidos = 0;
    }

    public void processLines(String file, CPFValidator validator) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(file));
            for (String line : lines) {
                boolean resultado = validator.validaCPF(line);
                if(resultado == true) {
                    this.validos++;
                } else {
                    this.invalidos++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getValidos() {
        return this.validos;
    }

    public int getInvalidos() {
        return this.invalidos;
    }

    public void writeResultados(String path, double tempoExecEmSegundos) {
        System.out.println("Cpfs Validos: " + this.validos);
        System.out.println("Cpfs Invalidos: " + this.invalidos);

        try {
            File file = new File(path);
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(path);) {
            writer.write("Tempo Total de Execucao: " + tempoExecEmSegundos + " segundos");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
