/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.xml;


import org.testng.annotations.Test;


import static com.codenvy.commons.xml.NewElement.createElement;
import static com.codenvy.commons.xml.XMLTreeUtil.closeTagLength;
import static com.codenvy.commons.xml.XMLTreeUtil.indexOf;
import static com.codenvy.commons.xml.XMLTreeUtil.indexOfAttributeName;
import static com.codenvy.commons.xml.XMLTreeUtil.single;
import static com.codenvy.commons.xml.XMLTreeUtil.insertBetween;
import static com.codenvy.commons.xml.XMLTreeUtil.insertInto;
import static com.codenvy.commons.xml.XMLTreeUtil.lastIndexOf;
import static com.codenvy.commons.xml.XMLTreeUtil.openTagLength;
import static com.codenvy.commons.xml.XMLTreeUtil.tabulate;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * Tests for {@link XMLTreeUtil}
 *
 * @author Eugene Voevodin
 */
public class XMLTreeUtilTest {

    @Test
    public void shouldTabulateOneLineString() {
        final String src = "text here";

        assertEquals(tabulate(src, 2), "        " + src);
    }

    @Test
    public void shouldTabulateMultilineString() {
        final String src = "first line\nsecond line";

        assertEquals(tabulate(src, 1), "    first line\n    second line");
    }

    @Test
    public void shouldReturnFirstElement() {
        assertEquals(single(asList("first")), "first");
    }

    @Test(expectedExceptions = XMLTreeException.class)
    public void shouldThrowExceptionWhenListContainsNotOnlyElement() {
        single(asList("first", "second"));
    }

    @Test
    public void shouldInsertContentBetweenTwoAnchors() {
        //                        6     12
        final byte[] src = "<name>content</name>".getBytes();

        final byte[] newSrc = insertBetween(src, 6, 12, "new content");

        assertEquals(newSrc, "<name>new content</name>".getBytes());
    }

    @Test
    public void shouldInsertContentToCharArrayInSelectedPlace() {
        //                        6
        final byte[] src = "<name></name>".getBytes();

        final byte[] newSrc = insertInto(src, 6, "new content");

        assertEquals(new String(newSrc).intern(), "<name>new content</name>");
    }

    @Test
    public void shouldBeAbleToFindLastIndexOf() {
        //                             11        20      28
        final byte[] src = "...</before>\n       <current>...".getBytes();

        assertEquals(lastIndexOf(src, '>', 20), 11);
        assertEquals(lastIndexOf(src, '>', src.length - 1), 28);
    }

    @Test
    public void shouldBeAbleToGetElementOpenTagLength() {
        //<test>test</test>
        final NewElement newElement = createElement("test", "test");

        assertEquals(openTagLength(newElement), 6);
    }

    @Test
    public void shouldBeAbleToGetElementCloseTagLength() {
        //<test>test</test>
        final NewElement newElement = createElement("test", "test");

        assertEquals(closeTagLength(newElement), 7);
    }

    @Test
    public void shouldBeAbleToGetIndexOf() {
        final String src = "<element attribute1=\"value1\" attribute2=\"value2\" attribute3=\"value3\">text</element>";
        final byte[] byteSrc = src.getBytes();

        assertEquals(indexOf(byteSrc, "attribute1".getBytes(), 0), src.indexOf("attribute1"));
        assertEquals(indexOf(byteSrc, "attribute2".getBytes(), 0), src.indexOf("attribute2"));
        assertEquals(indexOf(byteSrc, "attribute3".getBytes(), 0), src.indexOf("attribute3"));
    }

    @Test
    public void shouldReturnMinusOneIfTargetBytesWereNotFound() {
        final String src = "source string";
        final byte[] byteSrc = src.getBytes();

        assertNotEquals(indexOf(byteSrc, "string".getBytes(), 0), -1);
        assertEquals(indexOf(byteSrc, "strings".getBytes(), 0), -1);
    }

    @Test
    public void shouldBeAbleToFindIndexOfAttributeNameBytes() {
        final String src = "<element attribute1=\"value1\" attribute2=\"value2\" attribute3=\"value3\">text</element>";
        final byte[] byteSrc = src.getBytes();

        assertEquals(indexOfAttributeName(byteSrc, "attribute1".getBytes(), 0), src.indexOf("attribute1"));
        assertEquals(indexOfAttributeName(byteSrc, "attribute2".getBytes(), 0), src.indexOf("attribute2"));
        assertEquals(indexOfAttributeName(byteSrc, "attribute3".getBytes(), 0), src.indexOf("attribute3"));
    }

    @Test
    public void shouldReturnMinusOneIfAttributeNameBytesWereNotFound() {
        final String src = "<element attribute12=\"value1\"/>";
        final byte[] byteSrc = src.getBytes();

        assertEquals(indexOfAttributeName(byteSrc, "attribute1".getBytes(), 0), -1);
    }
}
