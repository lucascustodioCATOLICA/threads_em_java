import java.util.ArrayList;
import java.util.List;

public class Thread1Main {
    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis();

        CPFValidator validator = new CPFValidator();
        IOHandler handler = new IOHandler();
        List<String> lista = new ArrayList<>();

        for(int i = 1; i<=30; i++) {
            lista.add("input/f" + i + "-4-25.txt");
        }

        for(int i = 0; i<30; i++) {
            handler.processLines(lista.get(i), validator);
        }

        long tempoFinal = System.currentTimeMillis();
        double  tempoExec = (tempoFinal - tempoInicial) / 1000.0;

        handler.writeResultados("output/versao_1_thread.txt", tempoExec);
    }
}