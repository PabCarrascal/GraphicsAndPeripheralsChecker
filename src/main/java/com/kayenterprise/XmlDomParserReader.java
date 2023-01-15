package com.kayenterprise;

import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class XmlDomParserReader {
    private static final String FILE_PATCH = "{index}";
    private static final String FILENAME = "gpuTemperature";

    private static final String FILE_EXTENSION = ".xml";
    private static final String NVIDIA_GET_GPUS_COMMAND = "nvidia-smi -L";
    private static final String NVIDIA_GET_TEMP_COMMAND = "nvidia-smi -q -i 0 -x -f " + FILE_PATCH + " --dtd";

    public static String getGPUsWithNvidiaCommand() {
        BufferedReader br = null;
        StringBuilder gpuTemp = new StringBuilder();
        try {
            // get a list of gpus
            Process process = Runtime.getRuntime().exec(NVIDIA_GET_GPUS_COMMAND);
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line, filename, gpuName;
            int count = 0;
            while ((line = br.readLine()) != null) {
                gpuName = line.substring(7, line.indexOf("(") - 1);
                filename = FILENAME + count++ + FILE_EXTENSION;
                generateXMLWithNvidiaCommand(filename);
                gpuTemp.append(gpuName).append(" - ").append(getGpuTemperatureFromXML(filename)).append("º");
                deleteTempFile(filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gpuTemp.toString();
    }

    private static void generateXMLWithNvidiaCommand(String fileName) {
        try {
            // generate xml file
            Runtime.getRuntime().exec(NVIDIA_GET_TEMP_COMMAND.replace(FILE_PATCH, fileName));
            // wait for the file to be created
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getGpuTemperatureFromXML(String fileName) {
        // Set the name of the node which has the gpu temperature
        String gpuTempNode = "gpu_temp";
        // instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // optional, but recommended; just in case ;)
            // process XML securely avoiding attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(fileName));

            // optional, but recommended; normalize dom
            doc.getDocumentElement().normalize();

            // return the temperature without ºC and spaces
            return doc.getElementsByTagName(gpuTempNode).item(0).getTextContent().replace('C', ' ').trim();
        } catch (Exception e) {
            // return 0 instead of errors
            return "0";
        }
    }

    private static void deleteTempFile(String fileName) {
        File file = new File(fileName);
        file.delete();
    }
}
