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

import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.FieldGenOrMethodGen;
import rigger.Log;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@XmlRootElement
public class FieldFinder extends Finder{
    private List<String> fields = new ArrayList<String>();

    @XmlTransient
    private List<Pattern> fieldPatterns;

    public Pair<FieldGenOrMethodGen, FieldOrMethod>[] find(JavaClass jc, ConstantPoolGen cpg, Log logger)
    {
        ArrayList<Pair<FieldGen, FieldOrMethod>> toRet = new ArrayList<Pair<FieldGen, FieldOrMethod>>();
        if(fieldPatterns == null)
        {
            fieldPatterns = new ArrayList<Pattern>();
            for (String field : fields) {
                fieldPatterns.add(Pattern.compile(field));
            }
        }

        for (Field field : jc.getFields()) {

            for (Pattern fp : fieldPatterns) {
                if(fp.matcher(field.getName()).matches())
                {
                    logger.info(String.format("   Matched Field: %s", field.getName()));
                    toRet.add(new Pair<FieldGen, FieldOrMethod>(new FieldGen(field, cpg), field));
                }
            }
        }
        Pair[] r = new Pair[toRet.size()];
        toRet.toArray(r);
        return r;
    }

    @Override
    public void replace(ClassGen cg, FieldGenOrMethodGen new_, FieldOrMethod orig) {
        cg.replaceField((Field) orig, ((FieldGen)new_).getField());
    }

    @XmlElement
    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }



}
