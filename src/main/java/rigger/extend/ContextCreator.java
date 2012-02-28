/**
 *  A Maven plugin to jury rig java class files with the Apache BCEL library.
 *  Copyright (C) 2012 NigelB
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package rigger.extend;

import rigger.Log;
import rigger.bce.RiggerML;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class ContextCreator {

    private Log logger;

    public ContextCreator(Log logger) {
        this.logger = logger;
    }

    public JAXBContext createContext() throws JAXBException {
        return createContext(ContextCreator.class.getClassLoader());
    }

    public JAXBContext createContext(ClassLoader cl) throws JAXBException {
        StringBuilder b = new StringBuilder(RiggerML.class.getPackage().getName());
        String _package = Log.class.getPackage().getName();
        String property = String.format("%s.context", _package);
        String prop = System.getProperty(property);
        if (prop != null) {
            b.append(":").append(prop);
        }
        try {
            Enumeration<URL> urls = cl.getResources(String.format("META-INF/%s/rigger.properties", _package));
            InputStream is;
            Properties p = new Properties();
            URL url = new URL("not a url");
            while (urls.hasMoreElements()) {
                try {
                    is = (url = urls.nextElement()).openStream();
                    p.load(is);
                    is.close();
                    prop = p.getProperty(property);
                    if (prop != null) {
                        b.append(":").append(prop);
                    }

                } catch (IOException e) {
                    logger.info(String.format("Error reading rigger.properties: %s", url.toString()));
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return JAXBContext.newInstance(b.toString(), cl);
    }
}
