package com.mips.pipeline.processor;

import com.mips.pipeline.processor.data.InstructionList;

import java.util.*;

public class WithForwardingUnit {
    private static List<String> DEFAULT_STAGE_SEQUENCE = Arrays.asList("IF", "ID", "EX", "MEM", "WB");
    private static String STALL = "S";
    private static String BLANK = "";

    private List<List<String>> programTimingSequence;

    public void generateTimingSequence(InstructionList instructionList) {
        if (instructionList.getInstructionListLength() <= 0) {
            return;
        }

        programTimingSequence = new ArrayList<>();
        programTimingSequence.add(0, DEFAULT_STAGE_SEQUENCE);
        HazardDetector hazardDetector = new HazardDetector();
        for(int i=1; i<instructionList.getInstructionListLength(); i++) {
            List<String> instructionTimingSequence = new ArrayList<>();
            int ifStartIx = getIDForInstruction(instructionList, i-1);
            setBlanksUntilIfStartIx(instructionTimingSequence, ifStartIx);
            instructionTimingSequence.addAll(DEFAULT_STAGE_SEQUENCE);

            List<HazardResponse> dataHazardResponses = hazardDetector.getDataHazard(instructionList, i);
            int numberOfStallsToInsert = 0;
            if(dataHazardResponses.size() > 0) {
                if (dataHazardResponses.get(0).dataHazardType.equals(DataHazardType.LOAD_USE)) {
                    numberOfStallsToInsert = 1;
                }
            }

            insertStalls(instructionTimingSequence, numberOfStallsToInsert);

            programTimingSequence.add(i, instructionTimingSequence);
        }

        printProgramTimingSequence(instructionList, programTimingSequence);
        int clockCycles = getWBForInstruction(instructionList, instructionList.getInstructionListLength()-1) + 1;
        System.out.println();
        System.out.println("Total Number of clock cycles(Without Forwarding Unit): " + clockCycles);
    }

    private void printProgramTimingSequence(InstructionList instructionList, List<List<String>> programTimingSequence) {
        System.out.println();
        for(int i=0; i<programTimingSequence.size(); i++) {
            System.out.print(instructionList.getList().get(i).getInstructionAsString() + "\t");
            programTimingSequence.get(i).forEach(stage -> {
                System.out.print(stage + "\t");
            });
            System.out.println();
        }
    }

    private int getIDForInstruction(InstructionList instructionList, int ix) {
        List<String> instructionTimingSequence = programTimingSequence.get(ix);
        int index = 0;
        for(int i=0; i<instructionTimingSequence.size(); i++) {
            if (instructionTimingSequence.get(i).equals("ID")) {
                break;
            }
            index++;
        }

        return index;
    }

    private int getWBForInstruction(InstructionList instructionList, int ix) {
        List<String> instructionTimingSequence = programTimingSequence.get(ix);
        int index = 0;
        for(int i=0; i<instructionTimingSequence.size(); i++) {
            if (instructionTimingSequence.get(i).equals("WB")) {
                break;
            }
            index++;
        }

        return index;
    }

    private void insertStalls(List<String> instructionTimingSequence, int numberOfStalls) {
        int index = 0;
        for (String stage : instructionTimingSequence) {
            if (stage.equals("IF")) {
                break;
            }
            index++;
        }

        for(int i=0; i<numberOfStalls; i++) {
            instructionTimingSequence.add(index + 1, STALL);
        }

    }

    private void setBlanksUntilIfStartIx(List<String> instructionTimingSequence, int ifStartIx) {
        for(int i=0; i<ifStartIx; i++) {
            instructionTimingSequence.add(i, BLANK);
        }
    }
}
