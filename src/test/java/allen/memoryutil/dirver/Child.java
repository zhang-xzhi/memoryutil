package allen.memoryutil.dirver;

class Father {
    byte father_byte_0;
    long father_long;
    byte father_byte_1;
}

class Child extends Father {
    byte child_byte_0;
    long child_long;
    byte child_byte_1;
}

class C_A {
    byte a_byte;
}

class C_B extends C_A {
    long b_long;
}

class C_C extends C_B {
    byte c_byte;
}

class C_D extends C_C {
    long d_long;
}
