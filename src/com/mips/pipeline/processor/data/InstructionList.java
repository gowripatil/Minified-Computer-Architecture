package com.mips.pipeline.processor.data;

import java.util.ArrayList;
import java.util.List;

public class InstructionList {


    private InstructionList instructionList;

    /**
     * List of instructions
     */
    private List<Instruction> list;

    /**
     * Program counter used to fetch next instruction.
     */
    private int programCounter;

    public InstructionList() {
        list = new ArrayList<Instruction>();
        programCounter = 0;
    }

    /**
     * Add a new instruction to the instruction list
     * @param instruction
     */
    public void addInstruction(Instruction instruction) {
        list.add(instruction);
    }

    public List<Instruction> getList() {
        return list;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    /**
     * Get total number of instructions in the instruction list.
     * @return
     */
    public int getInstructionListLength() {
        return list.size();
    }

    /**
     * Reset the insturction list and program counter.
     */
    public void resetInstructionList() {
        list.clear();
        programCounter = 0;
    }

    /**
     * move program counter by "n" instructions.
     */
    public void moveProgramCounter(int n) {
        if (n > 0 && programCounter + n < list.size()) {
            programCounter += n;
        }
    }

    /**
     *
     */
    public void printInstructionList() {
        System.out.println("=== List of Instructions to Execute:");
        for(Instruction instruction: list) {
            System.out.println(instruction.getInstructionAsString());
        }
    }
}
