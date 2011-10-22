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
    private final boolean forgiving;
    private final List<File> baseDirs;
    private final StringBuffer sb = new StringBuffer();

    public CSSImportResolver(Charset charset, boolean forgiving, List<File> baseDirs) {
        this.charset = charset;
        this.forgiving = forgiving;
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
            try{
                ArrayList<File> l = new ArrayList<File>();
                l.add(file.getParentFile());
                l.addAll(baseDirs);

                resolveSubCSS(importedFileName, l, m);
                // m.appendReplacement(sb, importedContent);
            }
            catch(FileNotFoundException ex) {
                if(forgiving) {
                    m.appendReplacement(sb, m.group());
                    
                    // Log the error in STDERR
                    System.err.println(ex.getMessage());
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
