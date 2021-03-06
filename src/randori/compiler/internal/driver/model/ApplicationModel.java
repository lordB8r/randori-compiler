/***
 * Copyright 2013 Teoti Graphix, LLC.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.internal.driver.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.problems.ICompilerProblem;
import org.apache.flex.compiler.tree.as.ITypeNode;
import org.apache.flex.compiler.units.ICompilationUnit;

import randori.compiler.codegen.as.IASWriter;
import randori.compiler.config.IRandoriTargetSettings;
import randori.compiler.driver.IRandoriBackend;
import randori.compiler.internal.utils.FileUtils;

/**
 * The compilation set for the base randori application project.
 * <p>
 * TODO There is going to be more logic needed in the class to determine
 * multiple projects? Still in the dark about how exactly the projects other
 * than the libraries can be setup.
 * 
 * @author Michael Schmalle
 */
public class ApplicationModel extends BaseCompilationSet
{
    private static final String RANDORI = "randori";

    private static final String GUICE = "guice";

    public ApplicationModel(FlexProject project, IRandoriTargetSettings settings)
    {
        super(project, settings);
    }

    @Override
    protected boolean accept(ITypeNode node)
    {
        return node != null && !node.getQualifiedName().startsWith(GUICE)
                && !node.getQualifiedName().startsWith(RANDORI);
    }

    @Override
    public void generate(IRandoriBackend backend,
            List<ICompilerProblem> problems, File output)
    {
        super.generate(backend, problems, output);

        // as you can see, we override generate to allow monolithic or individual
        // file export here using the superclasses methods to do the dirty work
        boolean classesAsFiles = settings.getJsClassesAsFiles();
        if (classesAsFiles)
        {
            for (ICompilationUnit unit : getCompilationUnits())
            {
                try
                {
                    write(unit);
                }
                catch (RuntimeException e)
                {
                    // cancel compile
                    break;
                }
            }
        }
        else
        {
            String basePath = settings.getJsBasePath();
            String appName = settings.getAppName();
            if (appName == null || appName.equals(""))
            {
                // TODO Create a Problem that app-name is not configured, this should actually
                // be done in the configure() method of the compiler
            }
            writeFull(basePath, appName + ".js");
        }
    }

    /**
     * Writes an individual {@link ICompilationUnit} to file.
     * <p>
     * This method uses the unit's package name to calculate the directory
     * structure of the output class file.
     * 
     * @param unit The {@link ICompilationUnit} to output.
     */
    void write(ICompilationUnit unit)
    {
        String basePath = settings.getJsBasePath();

        File outputFolder = new File(outputDirectory, basePath);
        if (!outputFolder.exists())
            outputFolder.mkdirs();

        File outputClassFile = null;
        IASWriter writer = null;
        BufferedOutputStream out = null;

        // TODO this is a mess, all this output needs to be refactored into the build target

        try
        {
            outputClassFile = FileUtils.toOutputFile(unit.getQualifiedNames()
                    .get(0), outputFolder, "js");

            System.out.println("Compiling file: " + outputClassFile);

            writer = backend.createWriter(project,
                    (List<ICompilerProblem>) problems, unit, false);

            out = new BufferedOutputStream(
                    new FileOutputStream(outputClassFile));

            writer.writeTo(out);
            out.flush();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (RuntimeException e)
        {
            // XXX HACK not sure how to "not" create this file with the stream
            System.err.println("Compiling file failed "
                    + outputClassFile.getName());
            throw e;
        }
        finally
        {
            try
            {
                out.close();
                writer.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
