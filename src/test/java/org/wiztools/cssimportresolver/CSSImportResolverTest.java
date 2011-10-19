package org.wiztools.cssimportresolver;

import java.io.File;
import java.nio.charset.Charset;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wiztools.commons.Charsets;
import org.wiztools.commons.FileUtil;
import static org.junit.Assert.*;

/**
 *
 * @author subwiz
 */
public class CSSImportResolverTest {
    
    public CSSImportResolverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of resolve method, of class CSSImportResolver.
     */
    @Test
    public void testResolve() throws Exception {
        System.out.println("resolve");
        File file = new File("src/test/resources/main.css");
        Charset charset = Charsets.UTF_8;
        StringBuffer sb = new StringBuffer();
        CSSImportResolver.resolve(file, charset, sb);

        String expectedResult = FileUtil.getContentAsString(
                new File("src/test/resources/expOutput.css"), Charsets.UTF_8);
        System.out.println(sb);
        assertEquals(expectedResult, sb.toString());
    }
}
