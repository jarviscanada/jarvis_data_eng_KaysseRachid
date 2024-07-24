package ca.jrvs.apps.grep;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep
{

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public void process() throws IOException
    {
        List<String> matchedLines = new ArrayList<>();
        for (File file : listFiles(rootPath))
        {
            for (String line : readLines(file))
            {
                if (containsPattern(line)) matchedLines.add(line);
            }
        }
        writeToFile(matchedLines);
        logger.info("process() success");
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        try
        {
            Files.walk(Paths.get(rootDir))
                    .filter(Files::isRegularFile)
                    .forEach(path -> files.add(path.toFile()));
        } catch (IOException e)
        {
            logger.error("Error while trying to list files ", e);
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();

            while (line != null)
            {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e)
        {
            logger.error("Error while trying to read lines ", e);
        }
        logger.info("readLines success");
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException
    {
        BufferedWriter writer;
        try
        {
            writer = new BufferedWriter(new FileWriter(outFile));
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e)
        {
            logger.error("Error while trying to write lines ", e);
        }
        logger.info("writeToFile success");
    }

    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath)
    {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath.txt outFile.txt");
        }

        // Default logger config
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try
        {
            javaGrepImp.process();
        } catch (Exception e)
        {
            javaGrepImp.logger.error("Error: Unable to process", e);
        }
    }
}
