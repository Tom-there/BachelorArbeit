package de.hhu.cs.stups.algvis.pluginSpecs;

import javax.swing.*;
import java.io.*;
import java.util.stream.Collectors;

public interface LoadCodeFromFile {
    void loadCode(String s);
    static ToolBarButton getButton(LoadCodeFromFile impl){
        return new ToolBarButton() {
            @Override
            public String getText() {
                return "load";
            }

            @Override
            public void action() {
                JFileChooser fileChooser = new JFileChooser();
                switch(fileChooser.showOpenDialog(null)){
                    case JFileChooser.APPROVE_OPTION: break;
                    case JFileChooser.ERROR_OPTION:
                        System.err.println("ERROR - while choosing file(JFileChooser.showOpenDialog(...) returned error_option)");
                    case JFileChooser.CANCEL_OPTION:
                        return;
                }
                try(BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))){
                    impl.loadCode(br.lines().collect(Collectors.joining("\n")));
                } catch (FileNotFoundException e) {
                    System.err.println("ERROR - was not able to find the selected file, was it deleted in the meantime?");
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    System.err.println("ERROR - IOException while reading the file");
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
