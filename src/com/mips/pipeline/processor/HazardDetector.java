package com.mips.pipeline.processor;

import com.mips.pipeline.processor.data.InstructionList;

import java.util.ArrayList;
import java.util.List;

public class HazardDetector {

    public void printHazards(InstructionList instructionList) {
        for(int i=1; i<instructionList.getInstructionListLength(); i++) {

            List<HazardResponse> dataHazardResponses = this.getDataHazard(instructionList, i);
            for(int j=0; j<dataHazardResponses.size(); j++) {
                System.out.println(dataHazardResponses.get(j).register + " -> Data Hazard");
                System.out.println("\t Previous Instruction: " + instructionList.getList().get(dataHazardResponses.get(j).previousInstructionNumber).getInstructionAsString());
                System.out.println("\t Current Instruction: " + instructionList.getList().get(dataHazardResponses.get(j).currentInstructionNumber).getInstructionAsString());
            }

            String prevDestinationRegister = instructionList.getList().get(i-1).getOutputRegister();
            String[] prevInputRegisters = instructionList.getList().get(i-1).getInputRegisters();
            String currentDestinationRegister = instructionList.getList().get(i).getOutputRegister();
            String[] currentInputRegisters = instructionList.getList().get(i).getInputRegisters();

            /*// Identify Named Write After Read Hazards
            for (String inputRegister : prevInputRegisters) {
                if (inputRegister.equals(currentDestinationRegister)) {
                    // If one of the previous input registers is used as the destination register in
                    // the current instruction then it is a Named Write after Read hazard.
                    System.out.println(inputRegister + " -> Named(Write after Read) Hazard");
                    System.out.println("\t Previous Instruction: " + instructionList.getList().get(i - 1).getInstructionAsString());
                    System.out.println("\t Current Instruction: " + instructionList.getList().get(i).getInstructionAsString());
                }
            }

            // Identify Named Write After Write Hazards
            if (prevDestinationRegister.equals(currentDestinationRegister)) {
                // If one of the previous destination registers is used as the destination register in
                // the current instruction then it is a Named Write after Write hazard.
                System.out.println(prevDestinationRegister + " -> Named(Write after Write) Hazard");
                System.out.println("\t Previous Instruction: " + instructionList.getList().get(i - 1).getInstructionAsString());
                System.out.println("\t Current Instruction: " + instructionList.getList().get(i).getInstructionAsString());
            }*/
        }
    }

    public List<HazardResponse> getDataHazard(InstructionList instructionList, int currentInstructionIx) {
        String[] currentInputRegisters = instructionList.getList().get(currentInstructionIx).getInputRegisters();

        List<HazardResponse> hazardResponse = new ArrayList<>();
        for(int i=currentInstructionIx-1; i>=0 && i>=currentInstructionIx-2; i--) {
            String prevDestinationRegister = instructionList.getList().get(i).getOutputRegister();

            for (String inputRegister : currentInputRegisters) {
                if (inputRegister.equals(prevDestinationRegister)) {
                    // If previous destination register is used as one of the input register in
                    // the current instruction then it is a data hazard.

                    HazardResponse hzResponse = new HazardResponse();
                    hzResponse.register = inputRegister;
                    hzResponse.currentInstructionNumber = currentInstructionIx;
                    hzResponse.previousInstructionNumber = i;

                    if (instructionList.getList().get(i).getCmd().equals("lw")) {
                        hzResponse.dataHazardType = DataHazardType.LOAD_USE;
                    } else {
                        hzResponse.dataHazardType = DataHazardType.READ_AFTER_WRITE;
                    }

                    hazardResponse.add(hzResponse);
                }
            }
        }

        return hazardResponse;
    }
}
