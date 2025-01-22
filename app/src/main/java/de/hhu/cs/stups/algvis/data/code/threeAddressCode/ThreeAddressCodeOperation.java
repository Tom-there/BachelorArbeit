package de.hhu.cs.stups.algvis.data.code.threeAddressCode;

public enum ThreeAddressCodeOperation {
        noop, //noop operation
        add, sub, mul, div, // X = Y op Z (5)
        neg, // X = op Y (4)
        eq, // X = Y (3)
        jmp, // goto X (2)
        booleanJump, negatedBooleanJump, //if(false) Y goto X
        neJump, eqJump, leJump, geJump, ltJump, gtJump; //if y relOp z goto X (Relop: != == <= >= < >)
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
                case neJump -> "!=";
                case eqJump -> "==";
                case leJump -> "<=";
                case geJump -> ">=";
                case ltJump -> "<";
                case gtJump -> ">";
            };
        }
    }