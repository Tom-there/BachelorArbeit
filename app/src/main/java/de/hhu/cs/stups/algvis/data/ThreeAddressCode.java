package de.hhu.cs.stups.algvis.data;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCode{

    private final int address;
    private final Operation op;
    private final String destination, source, modifier;
    private String comment;

    public ThreeAddressCode(int address, Operation op, String destination, String source, String modifier, String comment){
        this.address = address;
        this.op = op;
        this.destination = destination;
        this.source = source;
        this.modifier = modifier;
        this.comment = comment;
    }
    public void setComment(String c){
        comment = c;
    }
    public Operation getOperation(){return op;}
    public String getDestination() {
        return destination;
    }
    public String getComment() {
        return comment;
    }

    public int getAddress() {
        return address;
    }

    public String toString(){
        return getRepresentation().toString();
    }
    //returns true if the TAC is a jump or a conditional jump
    public boolean canJump() {
        switch (op) { case jmp, booleanJump, negatedBooleanJump -> { return true; } }
        return false;
    }
    //gets representation for Table
    public TACRepresentation getRepresentation(){
        switch (op){
            case add, sub, mul, div -> {
                return new TACRepresentation(Integer.toString(address), destination, "=", source, op.getRepresentation(), modifier, comment);
            }
            case neg -> {
                return new TACRepresentation(Integer.toString(address), destination, "=", op.getRepresentation(), source, "", comment);
            }
            case eq -> {
                return new TACRepresentation(Integer.toString(address), destination, op.getRepresentation(), source, "", "", comment);
            }
            case jmp -> {
                return new TACRepresentation(Integer.toString(address), op.getRepresentation(), destination, "", "", "", comment);
            }
            case booleanJump, negatedBooleanJump -> {
                return new TACRepresentation(Integer.toString(address), op.getRepresentation(), source, "goto", destination, "", comment);
            }
            case noop -> {
                return new TACRepresentation(Integer.toString(address), op.getRepresentation(), "", "", "", "", comment);
            }
            default -> {
                System.err.println("ERR - was unable to get the Representation of a line of TAC because the value of the Operation enum was unexpected");
                System.err.println("DST - " + destination);
                System.err.println("SRC - " + source);
                System.err.println("MOD - " + (modifier.isEmpty() ? modifier : "NO MODIFIER"));
                System.err.println("CMT - " + (modifier.isEmpty() ? modifier : "NO COMMENT"));
                return null;
            }
        }
    }
    //enum and statics
    public record TACRepresentation(String a, String b, String c, String d, String e, String f, String g){

        public String get(int i){
            return switch (i){
                case 0 -> a;
                case 1 -> b;
                case 2 -> c;
                case 3 -> d;
                case 4 -> e;
                case 5 -> f;
                case 6 -> g;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
        }
        public static int size(){return 7;}
        public String toString(){
            return a + ": " + b + " " + c + " " + d + " " + e + " " + f + " " + g;
        }
    }
    public enum Operation{
        noop, //noop operation
        add, sub,  mul, div, // X = Y op Z (5)
        neg, // X = op Y (4)
        eq, // X = Y (3)
        jmp, // goto X (2)
        booleanJump, negatedBooleanJump; //iff Y goto X
        //TODO: x relOp y jumps (if x < y goto #)
        //TODO: call X
        //TODO: param X
        public String getRepresentation() {
            return switch (this) {
                case add -> "+";
                case sub, neg -> "-";
                case mul -> "*";
                case div -> "/";
                case eq -> "=";
                case jmp -> "goto";
                case booleanJump -> "if";
                case negatedBooleanJump -> "ifFalse";
                case noop -> "noop";
            };
        }
    }
    public static ThreeAddressCode fromString(String rawInput, int address){
        String[] pieces = rawInput.split(" ");
        ThreeAddressCode tac;
        switch (pieces.length) {
            case 2 -> //goto X
                    tac = new ThreeAddressCode(address, Operation.jmp, pieces[1], "", "", "");
            case 3 -> // X = Y
                    tac = new ThreeAddressCode(address, Operation.eq, pieces[0], pieces[2], "", "");
            case 4 -> {
                // X = op Y
                // iff Y goto X
                switch (pieces[2]){
                    case "-" -> //X = op Y
                            tac = new ThreeAddressCode(address, Operation.neg, pieces[0], pieces[3], "", "");
                    case "goto" -> {
                        //iff Y goto X
                        switch(pieces[0]){
                            case "if" -> //if Y goto X
                                    tac = new ThreeAddressCode(address, Operation.booleanJump, pieces[3], pieces[1], "", "");
                            case "ifFalse" -> //ifFalse Y goto X
                                    tac = new ThreeAddressCode(address, Operation.negatedBooleanJump, pieces[3], pieces[1], "", "");
                            case null, default -> {
                                tac = new ThreeAddressCode(address, Operation.noop, "", "", "", rawInput);
                                System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(noticed in if / ifFalse switch).");
                            }
                        }
                    }
                    case null, default -> {
                        tac = new ThreeAddressCode(address, Operation.noop, "", "", "", rawInput);
                        System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(noticed in unaryOp / conditionalJump switch).");
                    }
                }
            }
            case 5 -> {
                //X = Y op Z
                switch (pieces[3]){
                    case "+" -> tac = new ThreeAddressCode(address, Operation.add, pieces[0], pieces[2], pieces[4], "");
                    case "-" -> tac = new ThreeAddressCode(address, Operation.sub, pieces[0], pieces[2], pieces[4], "");
                    case "*" -> tac = new ThreeAddressCode(address, Operation.mul, pieces[0], pieces[2], pieces[4], "");
                    case "/" -> tac = new ThreeAddressCode(address, Operation.div, pieces[0], pieces[2], pieces[4], "");
                    default -> {
                        tac = new ThreeAddressCode(address, Operation.noop, "", "", "", rawInput);
                        System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(Noticed while parsing binaryOp).");
                    }
                }
            }
            default -> {
                tac = new ThreeAddressCode(address, Operation.noop, "", "", "", rawInput);
                System.err.println("WRN - unable to parse " + rawInput + " to TAC. Inserted dummy line");
            }
        }
        return tac;
    }
    public static List<ThreeAddressCode> listFromString(String rawInput){
        List<String> inputLines = rawInput.lines().toList();
        List<ThreeAddressCode> code = new ArrayList<>(inputLines.size());
        for (int i = 0; i < inputLines.size(); i++) {
            code.add(ThreeAddressCode.fromString(inputLines.get(i), i));
        }
        return code;
    }
}
