/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.oauth2.web;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.test.LuteceTestCase;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders the error page of the plugin with the HTML auto-escaping of the core: a level 1 heading, the error written as
 * text and every text taken from the bundles of the plugin.
 */
public class Oauth2HandleErrorRenderTest extends LuteceTestCase
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_HANDLE_ERROR = "skin/plugins/oauth2/handle_error.html";
    /** Stand-ins of the core macros handle_error.html calls. */
    private static final String MACROS = "oauth2_test_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro cTpl rest...><#nested></#macro><#macro cContainer rest...><div><#nested></div></#macro>"
            + "<#macro cTitle level=1 rest...><h${level}><#nested></h${level}></#macro>";
    private static final String ERROR = "a&b <i>c</i>";
    private static final String ERROR_ESCAPED = "<strong>a&amp;b &lt;i&gt;c&lt;/i&gt;</strong>";

    /**
     * Renders the error page.
     *
     * @param strError
     *            the error given to the page
     * @param locale
     *            the locale of the page
     * @return the rendering, localized
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strError, Locale locale ) throws IOException, TemplateException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), macros
        } ) );
        cfg.addAutoInclude( MACROS );

        Map<String, Object> model = new HashMap<>( );
        model.put( "error", strError );
        StringWriter writer = new StringWriter( );
        cfg.getTemplate( TEMPLATE_HANDLE_ERROR ).process( model, writer );

        return I18nService.localize( writer.toString( ), locale );
    }

    /**
     * The page has a level 1 heading and writes the error as text.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testErrorWrittenAsText( ) throws Exception
    {
        String strHtml = render( ERROR, Locale.ENGLISH );

        assertTrue( strHtml.contains( "<h1>Sign-in error</h1>" ), strHtml );
        assertTrue( strHtml.contains( "<p>An error occurred: " + ERROR_ESCAPED + "</p>" ), strHtml );
        assertTrue( strHtml.contains( "<p>We apologise for the inconvenience.</p>" ), strHtml );
    }

    /**
     * Without an error, the page writes a sentence without an empty value.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testNoError( ) throws Exception
    {
        String strHtml = render( null, Locale.ENGLISH );

        assertTrue( strHtml.contains( "<p>An error occurred.</p>" ), strHtml );
        assertFalse( strHtml.contains( "<strong>" ), strHtml );
    }

    /**
     * The French texts come from the French bundle, every key is resolved.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testFrenchTexts( ) throws Exception
    {
        String strHtml = render( ERROR, Locale.FRENCH );

        assertTrue( strHtml.contains( "<h1>Erreur de connexion</h1>" ), strHtml );
        assertTrue( strHtml.contains( "<p>Une erreur est survenue : " + ERROR_ESCAPED + "</p>" ), strHtml );
        assertTrue( strHtml.contains( "Veuillez nous excuser pour la gêne occasionnée." ), strHtml );
        assertFalse( strHtml.contains( "#i18n" ), strHtml );
    }
}
