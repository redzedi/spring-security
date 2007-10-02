/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;

import org.springframework.security.providers.TestingAuthenticationToken;

import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 
DOCUMENT ME!
 *
 * @author Mark St.Godard
 * @version $Id: Main.java 1496 2006-05-23 13:38:33Z benalex $
 */
public class Main {
    //~ Methods ========================================================================================================

    /**
     * This can be done in a web app by using a filter or <code>SpringMvcIntegrationInterceptor</code>.
     */
    private static void createSecureContext() {
        TestingAuthenticationToken auth = new TestingAuthenticationToken("test", "test",
                new GrantedAuthority[] {
                    new GrantedAuthorityImpl("ROLE_TELLER"), new GrantedAuthorityImpl("ROLE_PERMISSION_LIST")
                });

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private static void destroySecureContext() {
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    public static void main(String[] args) throws Exception {
        createSecureContext();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "org/security/config/auto-config.xml");
        BankService service = (BankService) context.getBean("bankService");

        // will succeed
        service.listAccounts();

        // will fail
        try {
            System.out.println(
                "We expect an AccessDeniedException now, as we do not hold the ROLE_PERMISSION_BALANCE granted authority, and we're using a unanimous access decision manager... ");
            service.balance("1");
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }

        destroySecureContext();
    }
}