package es.upm.grise.profundizacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.LifecyclePhase;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * 
 * @phase process-sources
 */
@Mojo(name = "detect-smells", defaultPhase = LifecyclePhase.TEST)

public class MyMojo
    extends AbstractMojo
{
	@Parameter(property = "inputFile", required = true)
    private File inputFile;

    @Parameter(property = "jarPath", required = true)
    private File jarPath;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("===============================================");
        getLog().info("     Test Smell Detector - Maven Plugin");
        getLog().info("===============================================");

        if (!inputFile.exists()) {
            throw new MojoExecutionException("El CSV de entrada no existe: " + inputFile.getAbsolutePath());
        }

        if (!jarPath.exists()) {
            throw new MojoExecutionException("El JAR TestSmellDetector no existe: " + jarPath.getAbsolutePath());
        }

        try {
            getLog().info("Ejecutando TestSmellDetector.jar...");
            getLog().info("CSV de entrada: " + inputFile.getAbsolutePath());
            getLog().info("=== Rutas leídas del CSV ===");
            Files.lines(inputFile.toPath())
                .forEach(l -> getLog().info(l));
            getLog().info("============================");

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-jar",
                    jarPath.getAbsolutePath(),
                    inputFile.getAbsolutePath()
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                getLog().info("[Detector] " + line);
            }

            process.waitFor();

            File resultFile = new File("output.csv");

            if (!resultFile.exists()) {
                throw new MojoExecutionException(
                        "No se encontró el archivo de salida output.csv en: " + resultFile.getAbsolutePath()
                );
            }

            getLog().info("===============================================");
            getLog().info("          RESULTADOS DEL ANALISIS");
            getLog().info("===============================================");
            Files.lines(resultFile.toPath())
                    .forEach(l -> getLog().info("RESULT: " + l));

            getLog().info("===============================================");
            getLog().info("Detección de test smells completada.");
            getLog().info("===============================================");

        } catch (Exception e) {
            throw new MojoExecutionException("Error ejecutando TestSmellDetector", e);
        }
    }
}
