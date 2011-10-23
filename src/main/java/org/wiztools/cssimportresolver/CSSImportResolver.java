package org.wiztools.cssimportresolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wiztools.commons.FileUtil;

/**
 *
 * @author subwiz
 */
class CSSImportResolver {
    
    private static final Pattern RE = Pattern.compile("@import\\s+url\\(\\s*('|\")?([^)^'^\"]*)('|\")?\\s*\\)\\s*;");
    
    private final Charset charset;
    private final boolean isLenient;
    private final boolean isVerbose;
    private final List<File> baseDirs;
    private final StringBuffer sb = new StringBuffer();

    public CSSImportResolver(Charset charset,
            boolean isLenient,
            List<File> baseDirs,
            boolean isQuiet) {
        this.charset = charset;
        this.isLenient = isLenient;
        this.isVerbose = !isQuiet;
        this.baseDirs = Collections.unmodifiableList(baseDirs);
    }
    
    private void resolveSubCSS(String cssFileName,
            List<File> baseDirs,
            Matcher m) throws FileNotFoundException, IOException{
        for(File dir: baseDirs) {
            File f = new File(dir, cssFileName);
            if(f.exists()) {
                m.appendReplacement(sb, "");
                resolve(f);
                return;
            }
        }
        throw new FileNotFoundException("File not found: " + cssFileName);
    }
    
    void resolve(File file) throws IOException {
        resolve(file, baseDirs);
    }
    
    private void resolve(File file, List<File> baseDirs) throws IOException {
        final String fileContent = FileUtil.getContentAsString(file, charset);
        
        Matcher m = RE.matcher(fileContent);
        while(m.find()) {
            final String importedFileName = m.group(2);
            { // check if http url:
                final String lowered = importedFileName.toLowerCase();
                if(lowered.startsWith("http://")
                        || lowered.startsWith("https://")) {
                    if(isVerbose) {
                        System.err.println("HTTP url not processed: " + importedFileName);
                    }
                    continue;
                }
            }
            try{
                ArrayList<File> l = new ArrayList<File>();
                l.add(file.getParentFile());
                l.addAll(baseDirs);

                resolveSubCSS(importedFileName, l, m);
                // m.appendReplacement(sb, importedContent);
            }
            catch(FileNotFoundException ex) {
                if(isLenient) {
                    m.appendReplacement(sb, m.group());
                    
                    // Log the error in STDERR
                    if(isVerbose) {
                        System.err.println(ex.getMessage());
                    }
                }
                else {
                    throw ex;
                }
            }
        }
        m.appendTail(sb);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
