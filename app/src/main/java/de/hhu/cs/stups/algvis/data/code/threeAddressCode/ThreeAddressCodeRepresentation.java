package de.hhu.cs.stups.algvis.data.code.threeAddressCode;

//enum and record
public record ThreeAddressCodeRepresentation(String a, String b, String c, String d, String e, String f, String g) {

    public String get(int i) {
        return switch (i) {
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

    public static int size() {
        return 7;
    }

    public String toString() {
        return a + ": " + b + " " + c + " " + d + " " + e + " " + f + " " + g;
    }
}
