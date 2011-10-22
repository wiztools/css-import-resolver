package org.wiztools.cssimportresolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.wiztools.commons.Charsets;
import org.wiztools.commons.FileUtil;

/**
 *
 * @author subwiz
 */
public class CSSImportResolverMain {
    
    private static void printHelp(PrintStream out) {
        out.println("Usage: java -jar css-import-resolver-NN-jar-with-dependencies.jar [OPTS] file1 file2 ...");
        out.println("Where OPTS can be:");
        out.println("\t-c\tCharset to use for reading and writing CSS. Defaults to UTF-8.");
        out.println("\t-o\tWrite the out CSS to specified file.");
        out.println("\t-f\tForgiving. When corresponding CSS file not found, ignore.");
        out.println("\t-b\tBase-dir. Directories to search for linked CSS.");
    }
    
    public static void main(String[] arg) throws IOException {
        // Parse the commandline:
        OptionParser parser = new OptionParser("hc:o:fb:");
        OptionSet options = parser.parse(arg);
        
        if(options.has("h")) {
            printHelp(System.out);
            return;
        }
        
        List<String> cssFiles = options.nonOptionArguments();
        
        if(cssFiles.isEmpty()) {
            System.err.println("No input given!");
            printHelp(System.err);
            System.exit(1);
        }
        
        Charset charset = Charsets.UTF_8; // set the default charset
        if(options.has("c")) {
            charset = Charset.forName(options.valueOf("c").toString());
        }
        
        boolean isForgiving = false;
        if(options.has("f")) {
            isForgiving = true;
        }
        
        List<File> baseDirs = new ArrayList<File>();
        if(options.has("b")) {
            List baseDirNames = options.valuesOf("b");
            for(Object name: baseDirNames) {
                File baseDir = new File(name.toString());
                if(!baseDir.isDirectory()) {
                    throw new IOException(
                            "Base directory is not a directory: "
                                + baseDir.getAbsolutePath());
                }
                baseDirs.add(baseDir);
            }
        }
        
        // Resolve!
        CSSImportResolver resolver = new CSSImportResolver(charset, isForgiving, baseDirs);
        for(String fileName: options.nonOptionArguments()) {
            File cssFile = new File(fileName);
            resolver.resolve(cssFile);
        }
        
        if(options.has("o")) {
            File outFile = new File(options.valueOf("o").toString());
            FileUtil.writeString(outFile, resolver.toString(), charset);
        }
        else {
            System.out.write(resolver.toString().getBytes(charset));
        }
    }
}
