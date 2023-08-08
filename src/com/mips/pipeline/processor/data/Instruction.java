package com.mips.pipeline.processor.data;

import java.util.regex.Pattern;

public class Instruction {

    private String cmd;
    private String arg1;
    private String arg2;
    private String arg3;

    /**
     * Creates new instruction. Sets every attribute to empty string.
     */
    public Instruction() {
        this.cmd = "";
        this.arg1 = "";
        this.arg2 = "";
        this.arg3 = "";
    }

    /**
     * Creates new Instruction based on parameters.
     * @param cmd
     * @param arg1
     * @param arg2
     * @param arg3
     */
    public Instruction(String cmd, String arg1, String arg2, String arg3) {
        this.cmd = cmd;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public String getCmd() {
        return cmd;
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getArg3() {
        return arg3;
    }

    /**
     * Returns the full instruction as a String.
     * @return
     */
    public String getInstructionAsString() {
        String instruction = "";
        if (cmd != null) instruction = instruction + cmd + " ";
        if (arg1 != null) instruction = instruction + arg1 + " ";
        if (arg2 != null) instruction = instruction + arg2 + " ";
        if (arg3 != null) instruction = instruction + arg3 + " ";
        return instruction;
    }

    /**
     * Get input registers
     */
    public String[] getInputRegisters() {
        String[] input;

        switch (cmd) {
            case "lw":
                input = new String[1];
                // second operand for lw is in the form 0($t0). Using java regex to
                // get the register name.
                input[0] = arg2.substring(arg2.indexOf("(")+1, arg2.indexOf(")"));
                break;
            case "sw":
                input = new String[2];
                // second operand for sw is in the form 0($t0). Using java regex to
                // get the register name.
                input[0] = arg1.substring(arg1.indexOf("(")+1, arg1.indexOf(")"));
                input[1] = arg2;
                break;
            default:
                input = new String[2];
                input[0] = arg2;
                input[1] = arg3;
        }

        return input;
    }

    /**
     * Get output register
     */
    public String getOutputRegister() {
        String output;

        switch (cmd) {
            case "sw":
                output = null;
                break;
            default:
                output = arg1;
        }

        return output;
    }
}
