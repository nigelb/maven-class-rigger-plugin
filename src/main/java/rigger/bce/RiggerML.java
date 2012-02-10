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
package rigger.bce;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import rigger.Log;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType(propOrder = {"matches"})
@XmlRootElement(name = "JuryRigger", namespace = "http://nigelb.github.com/maven-class-rigger-plugin/rigger-ml-1.0.xsd")
public class RiggerML {
    private List<Match> matches = new ArrayList<Match>();

    @XmlElement(namespace = "http://nigelb.github.com/maven-class-rigger-plugin/rigger-ml-1.0.xsd")
    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public boolean process(ClassGen cg, JavaClass jc, Log logger) {
        for (Match match : matches) {
            if(match.match(jc.getClassName()))
            {
                logger.info(String.format("Matched Class: %s", jc.getClassName()));
                return match.juryRig(cg, jc, logger);
            }
        }
        return false;
    }

    public static void main(String[] args) throws JAXBException {
        JAXBContext con = JAXBContext.newInstance(RiggerML.class, FieldFinder.class, MethodFinder.class);
        Marshaller mar = con.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, java.lang.Boolean.TRUE);
        RiggerML rml = new RiggerML();
        NameMatcher nm;
        rml.getMatches().add(nm = new NameMatcher());
        nm.setClassMatch("org\\.example\\..*");
        AddAnnotation aa;
        nm.getRiggers().add(aa=new AddAnnotation());

        AnnotationList fa = new AnnotationList();
        FieldFinder ff = new FieldFinder();
        fa.setFinder(ff);
        aa.getFinder().add(fa);
        ff.getFields().add("field");
        fa.getAnnotations().add(javax.xml.bind.annotation.XmlTransient.class.getCanonicalName());

        AnnotationList ma = new AnnotationList();
        MethodFinder mf = new MethodFinder();
        ma.setFinder(mf);
        aa.getFinder().add(ma);
        mf.getMethods().add("get.*");
        ma.getAnnotations().add(javax.xml.bind.annotation.XmlTransient.class.getCanonicalName());

        mar.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper(){
            @Override
            public String getPreferredPrefix(String s, String s1, boolean b) {
                if(s.equals("http://www.w3.org/2001/XMLSchema-instance"))
                {
                    return "xs";
                }else if(s.equals("http://nigelb.github.com/maven-class-rigger-plugin/rigger-ml-1.0.xsd"))
                {
                    return "rig";
                }
                return null;
            }
        });
        mar.marshal(rml, System.out);
    }

}
