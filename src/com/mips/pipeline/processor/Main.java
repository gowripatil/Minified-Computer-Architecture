package com.mips.pipeline.processor;

import com.mips.pipeline.processor.data.Instruction;
import com.mips.pipeline.processor.data.InstructionList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class Main {

    private static String asmFileName = "Input.asm";

    public static void main(String[] args) {

        BufferedReader bufferedReader;
        InstructionList instructionList = new InstructionList();
        try {
            URL path = Main.class.getResource(asmFileName);
            System.out.println("Reading ASM file: " + path.getPath());

            bufferedReader = new BufferedReader(new FileReader(new File(path.getFile())));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineSplit = line.split( "[\\s,]+" );
                if (lineSplit[0].equals("add") || lineSplit[0].equals("sub")) {
                    instructionList.addInstruction(new Instruction(lineSplit[0], lineSplit[1], lineSplit[2], lineSplit[3]));
                } else {
                    instructionList.addInstruction(new Instruction(lineSplit[0], lineSplit[1], lineSplit[2], ""));
                }
            }

            instructionList.printInstructionList();

            // Detect Hazards
            System.out.println();
            System.out.println();
            System.out.println("=== Identifying Hazards ===");
            HazardDetector hazardDetector = new HazardDetector();
            hazardDetector.printHazards(instructionList);

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("=== Printing Timing Sequence Without Forwarding ===");
            WithoutForwardingUnit withoutForwardingUnit = new WithoutForwardingUnit();
            withoutForwardingUnit.generateTimingSequence(instructionList);

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("=== Printing Timing Sequence With Forwarding ===");
            WithForwardingUnit withForwardingUnit = new WithForwardingUnit();
            withForwardingUnit.generateTimingSequence(instructionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
