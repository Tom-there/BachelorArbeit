package de.hhu.cs.stups.algvis.data.code.threeAddressCode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ThreeAddressCodeInstruction implements Comparable<ThreeAddressCodeInstruction>{
    private final int address;
    private final ThreeAddressCodeOperation op;
    private final String destination, source, modifier;
    private String comment;
    public ThreeAddressCodeInstruction(String rawInput, int address){
        ThreeAddressCodeOperation tempOperation;
        String tempModifier,tempSource,tempDestination, tempComment;
        this.address = address;
        String[] pieces = rawInput.split(" ");
        switch (pieces.length) {
            case 2 -> { //goto X
                tempOperation = ThreeAddressCodeOperation.jmp;
                tempDestination = pieces[1];
                tempSource = null;
                tempModifier = null;
                tempComment = null;
            }
            case 3 -> {// X = Y
                tempOperation = ThreeAddressCodeOperation.eq;
                tempDestination = pieces[0];
                tempSource = pieces[2];
                tempModifier = null;
                tempComment = null;
            }
            case 4 -> {
                // X = op Y
                // iff Y goto X
                switch (pieces[2]){
                    case "-" -> {//X = op Y
                        tempOperation = ThreeAddressCodeOperation.neg;
                        tempDestination = pieces[0];
                        tempSource = pieces[3];
                        tempModifier = null;
                        tempComment = null;
                    }
                    case "goto" -> {
                        //iff Y goto X
                        switch(pieces[0]){
                            case "if" -> {//if Y goto X
                                tempOperation = ThreeAddressCodeOperation.booleanJump;
                                tempDestination = pieces[3];
                                tempSource = pieces[1];
                                tempModifier = null;
                                tempComment = null;
                            }
                            case "ifFalse" -> {//ifFalse Y goto X
                                tempOperation = ThreeAddressCodeOperation.negatedBooleanJump;
                                tempDestination = pieces[3];
                                tempSource = pieces[1];
                                tempModifier = null;
                                tempComment = null;
                            }
                            case null, default -> {
                                tempOperation = ThreeAddressCodeOperation.noop;
                                tempDestination = null;
                                tempSource = null;
                                tempModifier = null;
                                tempComment = "s";
                                System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(noticed in if / ifFalse switch).");
                            }
                        }
                    }
                    case null, default -> {
                        tempOperation = ThreeAddressCodeOperation.noop;
                        tempDestination = null;
                        tempSource = null;
                        tempModifier = null;
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
                tempComment = null;
                switch (pieces[3]){
                    case "+" ->
                            tempOperation = ThreeAddressCodeOperation.add;
                    case "-" ->
                            tempOperation = ThreeAddressCodeOperation.sub;
                    case "*" ->
                            tempOperation = ThreeAddressCodeOperation.mul;
                    case "/" ->
                            tempOperation = ThreeAddressCodeOperation.div;
                    default -> {
                        tempOperation = ThreeAddressCodeOperation.noop;
                        tempDestination = null;
                        tempSource = null;
                        tempModifier = null;
                        tempComment = rawInput;
                        System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(Noticed while parsing binaryOp).");
                    }
                }
            }
            case 6 -> {
                // if y relop z goto x
                tempDestination = pieces[5];
                tempSource = pieces[1];
                tempModifier = pieces[3];
                tempComment = null;
                switch (pieces[2]){
                    case "<"  -> tempOperation = ThreeAddressCodeOperation.ltJump;
                    case "<=" -> tempOperation = ThreeAddressCodeOperation.leJump;
                    case ">"  -> tempOperation = ThreeAddressCodeOperation.gtJump;
                    case ">=" -> tempOperation = ThreeAddressCodeOperation.geJump;
                    case "==" -> tempOperation = ThreeAddressCodeOperation.eqJump;
                    case "!=" -> tempOperation = ThreeAddressCodeOperation.neJump;
                    default -> {
                        tempOperation = ThreeAddressCodeOperation.noop;
                        tempDestination = null;
                        tempSource = null;
                        tempModifier = null;
                        tempComment = rawInput;
                        System.err.println("ERR - unable to parse " + rawInput + " to TAC.\n inserted dummy line(Noticed while conditionalJump).");
                    }
                }
            }
            default -> {
                tempOperation = ThreeAddressCodeOperation.noop;
                tempDestination = null;
                tempSource = null;
                tempModifier = null;
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
    //returns a Set of the next possible addresses
    public Set<Integer> nextPossibleInstructionAdresses() {
        switch (op) {
            case noop -> {
                return Set.of();
            }
            case jmp -> {
                return Set.of(Integer.parseInt(destination));
            }
            case booleanJump, negatedBooleanJump, neJump, eqJump, leJump, geJump, ltJump, gtJump -> {
                return Set.of(Integer.parseInt(destination), address+1);
            }
            case add, sub, mul, div, neg, eq -> {
                return Set.of(address+1);
            }
            case null -> {
                System.err.println("ERR - TACInstruction.nextPossibleInstructionAdresses()\nOperation is null, should be impossible");
                return null;
            }
        }
    }
    //returns true if the instruction is a jump or a conditional jump
    public boolean canJump() {
        switch (op) {
            case noop, add, sub, mul, div, neg, eq -> {
                return false;
            }
            case jmp, booleanJump, negatedBooleanJump, neJump, eqJump, leJump, geJump, ltJump, gtJump -> {
                return true;
            }
            case null -> {
                System.err.println("ThreeAddressCodeInstruction.canJump() operation was null, should be impossible");
                return false;
            }
        }
    }
    //returns true if the instruction writes to a variable
    public boolean writesValue(){
        switch (op){
             case   noop,
                    jmp,
                    booleanJump, negatedBooleanJump,
                    eqJump, neJump, leJump, geJump, ltJump, gtJump -> {
                return false;
            }
            case    eq,
                    add, sub,  mul, div, neg -> {
                return true;
            }
            case null -> {
                System.err.println("ERR - Instruction has Operation value null. should be impossible");
                return false;
            }
        }
    }
    //returns the used Identifiers in this instruction
    public Collection<String> getUsedIdentifiers() {
        Set<String> ret = new HashSet<>();
        if(source!=null)
            try{
                Integer.parseInt(source);
            }catch (NumberFormatException e){
                ret.add(source);
            }
        if(modifier!=null)
            try{
                Integer.parseInt(modifier);
            }catch (NumberFormatException e){
                ret.add(modifier);
            }
        return ret;
    }
    //setters
    public void setComment(String c){
        comment = c;
    }
    //getters
    public ThreeAddressCodeOperation getOperation(){return op;}
    public String getDestination() {return destination;}
    public String getComment() {return comment;}
    public int getAddress() {return address;}
    @Override
    public int compareTo(ThreeAddressCodeInstruction comparator) {
        return address-comparator.getAddress();
    }
    //gets representation for Table
    public String[] getRepresentationAsStringArray(){
        switch (op){
            case add, sub, mul, div -> {
                return new String[]{String.valueOf(address), destination, "=", source, op.getRepresentation(), modifier, comment};
            }
            case neg -> {
                return new String[]{String.valueOf(address), destination, "=", op.getRepresentation(), source, "", comment};
            }
            case eq -> {
                return new String[]{String.valueOf(address), destination, op.getRepresentation(), source, "", "", comment};
            }
            case jmp -> {
                return new String[]{String.valueOf(address), op.getRepresentation(), destination, "", "", "", comment};
            }
            case booleanJump, negatedBooleanJump -> {
                return new String[]{String.valueOf(address), op.getRepresentation(), source, "goto", destination, "", comment};
            }
            case eqJump, neJump, leJump, geJump, ltJump, gtJump -> {
                return new String[]{String.valueOf(address), "if", (source + op.getRepresentation() + modifier) , "goto", destination, "", comment};
            }
            case noop -> {
                return new String[]{String.valueOf(address), op.getRepresentation(), "", "", "", "", comment};
            }
            case null -> {
                System.err.println("ERR - was unable to get the Representation of a line of TAC because the value of the Operation enum was null");
                System.err.println("DST - " + destination);
                System.err.println("SRC - " + source);
                System.err.println("MOD - " + (modifier.isEmpty() ? modifier : "NO MODIFIER"));
                System.err.println("CMT - " + (modifier.isEmpty() ? modifier : "NO COMMENT"));
                return null;
            }
        }
    }
}
