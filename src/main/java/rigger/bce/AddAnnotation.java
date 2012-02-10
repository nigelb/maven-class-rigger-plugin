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

import org.apache.bcel.classfile.*;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.generic.*;
import rigger.Log;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class AddAnnotation extends Rigger {
    private List<AnnotationList> finder = new ArrayList<AnnotationList>();

    public boolean juryRig(ClassGen cg, JavaClass jc, Log logger) {
        boolean toRet = false;

        ConstantPoolGen cpg = cg.getConstantPool();
        for (AnnotationList f : finder) {

            for (Pair<FieldGenOrMethodGen, FieldOrMethod> codeGen : f.getFinder().find(jc, cpg, logger)) {
                for (String annotation : f.getAnnotations()) {
                    ObjectType t = new ObjectType(annotation);
                    codeGen.getA().addAnnotationEntry(new AnnotationEntryGen(t, new ArrayList<ElementValuePairGen>(), true, cpg));
                    logger.info(String.format("       Added Annotation: %s", t.getClassName()));
                }
                f.getFinder().replace(cg, codeGen.getA(), codeGen.getB());
                toRet = true;
            }
        }
        return toRet;
    }

    @XmlElement
    public List<AnnotationList> getFinder() {
        return finder;
    }

    public void setFinder(List<AnnotationList> finder) {
        this.finder = finder;
    }
}
