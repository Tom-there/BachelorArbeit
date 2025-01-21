package de.hhu.cs.stups.algvis.data;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCodeInstruction {
    private final int address;
    private final Operation op;
    private final String destination, source, modifier;
    private String comment;
    public ThreeAddressCodeInstruction(String rawInput, int address){
        Operation tempOperation;
        String tempModifier,tempSource,tempDestination, tempComment;
        this.address = address;
        String[] pieces = rawInput.split(" ");
        switch (pieces.length) {
            case 2 -> { //goto X
                tempOperation = Operation.jmp;
                tempDestination = pieces[1];
                tempSource = "";
                tempModifier = "";
                tempComment = "";
            }
            case 3 -> {// X = Y
                tempOperation = Operation.eq;
                tempDestination = pieces[0];
                tempSource = pieces[2];
                tempModifier = "";
                tempComment = "";
            }
            case 4 -> {
                // X = op Y
                // iff Y goto X
                switch (pieces[2]){
                    case "-" -> {//X = op Y
                        tempOperation = Operation.neg;
                        tempDestination = pieces[0];
                        tempSource = pieces[3];
                        tempModifier = "";
                        tempComment = "";
                    }
                    case "goto" -> {
                        //iff Y goto X
                        switch(pieces[0]){
                            case "if" -> {//if Y goto X
                                tempOperation = Operation.booleanJump;
                                tempDestination = pieces[3];
                                tempSource = pieces[1];
                                tempModifier = "";
                                tempComment = "";
                            }
                            case "ifFalse" -> {//ifFalse Y goto X
                                tempOperation = Operation.negatedBooleanJump;
                                tempDestination = pieces[3];
                                tempSource = pieces[1];
                                tempModifier = "";
                                tempComment = "";
                            }
                            case null, default -> {
                                tempOperation = Operation.noop;
                                tempDestination = "";
                                tempSource = "";
                                tempModifier = "";
                                tempComment = "s";
                                System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(noticed in if / ifFalse switch).");
                            }
                        }
                    }
                    case null, default -> {
                        tempOperation = Operation.noop;
                        tempDestination = "";
                        tempSource = "";
                        tempModifier = "";
                        tempComment = "s";
                        System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(noticed in unaryOp / conditionalJump switch).");
                    }
                }
            }
            case 5 -> {
                //X = Y op Z
                tempDestination = pieces[0];
                tempSource = pieces[2];
                tempModifier = pieces[4];
                tempComment = "";
                switch (pieces[3]){
                    case "+" ->
                            tempOperation = Operation.add;
                    case "-" ->
                            tempOperation = Operation.sub;
                    case "*" ->
                            tempOperation = Operation.mul;
                    case "/" ->
                            tempOperation = Operation.div;
                    default -> {
                        tempOperation = Operation.noop;
                        tempDestination = "";
                        tempSource = "";
                        tempModifier = "";
                        tempComment = rawInput;
                        System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(Noticed while parsing binaryOp).");
                    }
                }
            }
            default -> {
                tempOperation = Operation.noop;
                tempDestination = "";
                tempSource = "";
                tempModifier = "";
                tempComment = rawInput;
                System.err.println("WRN - unable to parse " + rawInput + " to TAC. Inserted dummy line");
            }
        }
        this.op = tempOperation;
        this.destination = tempDestination;
        this.source = tempSource;
        this.modifier = tempModifier;
        this.comment = tempComment;
    }
    public void setComment(String c){
        comment = c;
    }
    public Operation getOperation(){return op;}
    public String getDestination() {return destination;}
    public String getComment() {return comment;}
    public int getAddress() {return address;}
    public String toString(){return getRepresentation().toString();}
    //returns true if the instruction is a jump or a conditional jump
    public boolean canJump() {
        switch (op) { case jmp, booleanJump, negatedBooleanJump -> { return true; } }
        return false;
    }

    //returns true if the instruction writes to a variable
    public boolean writesValue(){
        switch (op){
            case jmp, noop, booleanJump, negatedBooleanJump ->{
                return false;
            }
            case add, sub,  mul, div, neg, eq -> {
                return true;
            }
            default -> {
                System.err.println("ERR - TACInstruction.writesValue(). Error in switch statement, could not determine if " + op.toString() + " writes or not");
                return false;
            }
        }
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
}
