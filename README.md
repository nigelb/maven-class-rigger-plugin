
maven-class-rigger-plugin
=========================

This maven plugin provides the capability of conducting simple class file byte code re-engineering.
The byte code manipulation is controlled by a [config.xml](http://nigelb.github.com/maven-class-rigger-plugin/config.html) file.
The default location for this file is ${project.basedir}/src/main/rigger/config.xml.

Usage [See Usage](http://nigelb.github.com/maven-class-rigger-plugin/usage.html):

        <project>
            [...]
            <build>
                <plugins>
                    <plugin>
                        <groupId>maven-class-rigger-plugin</groupId>
                        <artifactId>maven-class-rigger-plugin</artifactId>
                        <version>0.0.1-SNAPSHOT</version>
                        <executions>
                            <execution>
                                <phase>compile</phase>
                                <goals>
                                    <goal>jury-rig</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
            [...]
        </project>
