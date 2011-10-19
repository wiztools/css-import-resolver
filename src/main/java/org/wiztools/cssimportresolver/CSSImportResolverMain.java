package org.wiztools.cssimportresolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
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
    }
    
    public static void main(String[] arg) throws IOException {
        // Parse the commandline:
        OptionParser parser = new OptionParser("hc:o:");
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
        
        // Output StringBuffer
        final StringBuffer sb = new StringBuffer();
        
        for(String fileName: options.nonOptionArguments()) {
            File cssFile = new File(fileName);
            CSSImportResolver.resolve(cssFile, charset, sb);
        }
        
        if(options.has("o")) {
            File outFile = new File(options.valueOf("o").toString());
            FileUtil.writeString(outFile, sb.toString(), charset);
        }
        else {
            System.out.write(sb.toString().getBytes(charset));
        }
    }
}
