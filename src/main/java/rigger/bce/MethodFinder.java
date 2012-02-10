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
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@XmlRootElement
public class MethodFinder extends Finder {
    private List<String> methods = new ArrayList<String>();

    @XmlTransient
    private List<Pattern> methodPatterns;

    public Pair<FieldGenOrMethodGen, FieldOrMethod>[] find(JavaClass jc, ConstantPoolGen cpg, Log logger) {
        ArrayList<Pair<MethodGen, FieldOrMethod>> toRet = new ArrayList<Pair<MethodGen, FieldOrMethod>>();
        if (methodPatterns == null) {
            methodPatterns = new ArrayList<Pattern>();
            for (String field : methods) {
                methodPatterns.add(Pattern.compile(field));
            }
        }

        for (Method method : jc.getMethods()) {
            for (Pattern fp : methodPatterns) {
                if (fp.matcher(method.getName()).matches()) {
                    logger.info(String.format("   Matched Method: %s", method.getName()));
                    toRet.add(new Pair<MethodGen, FieldOrMethod>(new MethodGen(method, jc.getClassName(), cpg), method));
                }
            }
        }
        Pair[] r = new Pair[toRet.size()];
        toRet.toArray(r);
        return r;
    }

    @Override
    public void replace(ClassGen cg, FieldGenOrMethodGen new_, FieldOrMethod orig) {
        cg.replaceMethod((Method) orig, ((MethodGen)new_).getMethod() );
    }

    @XmlElement
    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}