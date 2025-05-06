# Execultar versao 1 thread
1. javac .\Thread1Main.java .\CPFValidator.java .\IOHandler.java
2. java Thread1Main

# Execultar versao 2 ou mais thread
1. Modifique a linha 91 do arquivo CPFProcessor.java
2. javac .\CPFProcessor.java .\CPFValidator.java
3. java CPFProcessor
4. Os ultimos valores printados no terminal, sao os valores de CPFs invalidos e validos corretos.