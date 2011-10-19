package org.wiztools.cssimportresolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
    
    private static String getFileContent(String cssFileName, List<File> baseDirs, Charset c)
            throws FileNotFoundException, IOException{
        for(File dir: baseDirs) {
            File f = new File(dir, cssFileName);
            if(f.exists()) {
                return FileUtil.getContentAsString(f, c);
            }
        }
        throw new FileNotFoundException("File not found: " + cssFileName);
    }
    
    static void resolve(File file,
            Charset charset,
            StringBuffer sb,
            boolean forgiving,
            List<File> baseDirs) throws IOException {
        final String fileContent = FileUtil.getContentAsString(file, charset);
        
        Matcher m = RE.matcher(fileContent);
        while(m.find()) {
            final String importedFileName = m.group(2);
            try{
                ArrayList<File> l = new ArrayList<File>();
                l.add(file.getParentFile());
                l.addAll(baseDirs);
                
                final String importedContent = getFileContent(importedFileName, l, charset);;
                m.appendReplacement(sb, importedContent);
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
}
