package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep
{

    /**
     *
     * @throws IOException
     */
    void process() throws IOException;

    /**
     *
     * @param rootDir
     * @return
     */
    List<File> listFiles(String rootDir);

    /**
     *
     * @param inputFile
     * @return
     */
    List<String> readLines(File inputFile);

    /**
     *
     * @param line
     * @return
     */
    boolean containsPattern(String line);

    /**
     *
     * @param lines
     * @throws IOException
     */
    void writeToFile(List<String> lines) throws IOException;

    /**
     *
     * @return
     */
    String getRootPath();

    /**
     *
     * @param rootPath
     */
    void setRootPath(String rootPath);

    /**
     *
     * @return
     */
    String getRegex();

    /**
     *
     * @param regex
     */
    void setRegex(String regex);

    /**
     *
     * @return
     */
    String getOutFile();

    /**
     *
     * @param outFile
     */
    void setOutFile(String outFile);

}
