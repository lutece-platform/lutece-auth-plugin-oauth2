/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * AuthLoginServlet
 */
public class OAuthCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CallbackHandler _callbackHandler;


	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
    	String strHandlerNameParam=request.getParameter(Constants.PARAMETER_HANDLER_NAME);
	
    	if(_callbackHandler==null ||(!StringUtils.isEmpty(strHandlerNameParam) && !strHandlerNameParam.equals(_callbackHandler.getHandlerName())|| !_callbackHandler.isDefault( )) )
		{
			
			_callbackHandler=getConfiguration(request,strHandlerNameParam);
		}
		_callbackHandler.handle(request, response);
	}

	/**
     * Get CallbackHandler configuration
     */
    private CallbackHandler getConfiguration(HttpServletRequest request ,String strHandlerName )
    {
        
        	
        	CallbackHandler callbackHandler=null;

        	List<CallbackHandler> callBackList=SpringContextService.getBeansOfType( CallbackHandler.class );
        	
        	
        	
        	if(!StringUtils.isEmpty(strHandlerName) && callBackList.size()>0)
        	{  
        	    
        	    callbackHandler= callBackList.stream( ).filter( x ->   strHandlerName.equals( x.getHandlerName( ))).findFirst( ).orElse(null);
        	 
        	}
        	
        	//getDefaultHandler
        	if(callbackHandler==null)
        	{
        	    
        	    callbackHandler= callBackList.stream( ).filter( x ->   x.isDefault( )).findFirst( ).orElse(null);
                
        	}
        	
      	
        return callbackHandler;
        	
            
            
      }
    
    
}
