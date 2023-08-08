package com.mips.pipeline.processor;

public class HazardResponse {
    String register;
    int currentInstructionNumber;
    int previousInstructionNumber;
    DataHazardType dataHazardType;
}
