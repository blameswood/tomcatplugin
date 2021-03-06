/* The MIT License
 * (c) Copyright Sysdeo SA 2001-2002
 * (c) Copyright Eclipse Tomcat Plugin 2014-2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.sf.eclipse.tomcat;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;


/**
 * Bootstrap specifics for Tomcat 4
 * See %TOMCAT4_HOME%/bin/catalina.bat
 */
public class Tomcat4Bootstrap extends TomcatBootstrap {

	Tomcat4Bootstrap(String label) {
		super(label);
	}

	@Override
    public String[] getClasspath() {
        ArrayList<String> classpath = new ArrayList<String>();
        classpath.add(getTomcatDir() + File.separator + "bin" + File.separator + "bootstrap.jar");

        // Add tools.jar JDK file to classpath
        String toolsJarLocation = VMLauncherUtility.getVMInstall().getInstallLocation() + File.separator + "lib" + File.separator + "tools.jar";
        if(new File(toolsJarLocation).exists()) {
            classpath.add(toolsJarLocation);
        }
        return (classpath.toArray(new String[0]));
    }

    @Override
    public String getMainClass() {
        return "org.apache.catalina.startup.Bootstrap";
    }

    @Override
    public String getStartCommand() {
        return "start";
    }

    @Override
    public String getStopCommand() {
        return "stop";
    }

    @Override
    public String[] getPrgArgs(String command) {
        String[] prgArgs;
        if (TomcatLauncherPlugin.getDefault().getConfigMode().equals(TomcatLauncherPlugin.SERVERXML_MODE)) {
            prgArgs = new String[3];
            prgArgs[0] = command;
            prgArgs[1] = "-config";
            prgArgs[2] = "\"" + TomcatLauncherPlugin.getDefault().getConfigFile() + "\"";
        } else {
            prgArgs = new String[1];
            prgArgs[0] = command;

        }
        return prgArgs;
    }

    @Override
    public String[] getVmArgs() {
        String[] vmArgs = new String[3];
        vmArgs[0] = "-Dcatalina.home=\"" + getTomcatDir() + "\"";

        String binDir = getTomcatDir() + File.separator + "bin";
        String commonLibDir = getTomcatDir() + File.separator + "common" + File.separator + "lib";
        vmArgs[1] = "-Djava.endorsed.dirs=\"" + binDir + File.pathSeparator + commonLibDir + "\"";

        if (getTomcatBase().length() != 0) {
            vmArgs[2] = "-Dcatalina.base=\"" + getTomcatBase() + "\"";
        } else {
            vmArgs[2] = "-Dcatalina.base=\"" + getTomcatDir() + "\"";
        }

        return vmArgs;
    }

    @Override
    public String getXMLTagAfterContextDefinition() {
        return "</Host>";
    }

    @Override
    public IPath getServletJarPath() {
        return new Path("common").append("lib").append("servlet.jar");
    }

    @Override
    public IPath getJasperJarPath() {
        return new Path("lib").append("jasper-runtime.jar");
    }

    /*
     * No JSP jar for Tomcat 4, JSP classes are in servlet jar
     */
    @Override
    public IPath getJSPJarPath() {
        return null;
    }

    @Override
    public String getContextWorkDir(String workFolder) {
        StringBuffer workDir = new StringBuffer("workDir=");
        workDir.append('"');
        workDir.append(workFolder);

        workDir.append(File.separator);
        workDir.append("org");
        workDir.append(File.separator);
        workDir.append("apache");
        workDir.append(File.separator);
        workDir.append("jsp");

        workDir.append('"');
        return workDir.toString();
    }
}

