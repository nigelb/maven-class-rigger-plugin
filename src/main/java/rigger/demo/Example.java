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
package rigger.demo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <code>Example</code> is a class whose purpose is to demonstrate the capabilities of the Class Jury Rigger.
 */
public class Example {

    /**
     * <code>filed</code> an arbitrary field.
     */
     private Object field = new Object();
    
    public void run() throws NoSuchFieldException, NoSuchMethodException {
        Class clazz = Example.class;
        System.out.println();
        System.out.format("Annotations of the %s class:", clazz.getName());
        System.out.println();
        for (Annotation a : clazz.getAnnotations()) {
            System.out.format("    %s", a.toString());
            System.out.println();
        }

        System.out.println();
        System.out.format("Annotations on the %s's Fields:", clazz.getName());
        System.out.println();
        for (Field f : clazz.getDeclaredFields()) {
            System.out.format("    %s", f.toString());
            System.out.println();
            for (Annotation a :f.getAnnotations()) {
                System.out.format("        %s", a.toString());
                System.out.println();
            }
            System.out.println();
        }

        System.out.println();
        System.out.format("Annotations on the %s's Methods:", clazz.getName());
        System.out.println();
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.format("    %s", m.toString());
            System.out.println();
            for (Annotation a : m.getAnnotations()) {
                System.out.format("        %s", a.toString());
                System.out.println();
            }
            System.out.println();
        }


    }

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        new Example().run();
    }
}
