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

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ClassGen;
import rigger.Log;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@XmlRootElement
public class NameMatcher extends Match {
    private Pattern classMatch = Pattern.compile(".*");
    private List<Rigger> riggers = new ArrayList<Rigger>();

    @Override
    public boolean match(String className) {
        return classMatch.matcher(className).matches();
    }

    public boolean juryRig(ClassGen cg, JavaClass jc, Log logger) {
        boolean toRet = false;
        for (Rigger rigger : riggers) {
            toRet = rigger.juryRig(cg, jc, logger);
        }

        return toRet;
    }

    @XmlElement
    public String getClassMatch() {
        return classMatch.pattern();
    }

    public void setClassMatch(String classMatch) {
        this.classMatch = Pattern.compile(classMatch);
    }

    @XmlElement(name = "rigger")
    public List<Rigger> getRiggers() {
        return riggers;
    }

    public void setRiggers(List<Rigger> riggers) {
        this.riggers = riggers;
    }
}
