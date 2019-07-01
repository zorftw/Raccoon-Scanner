package hakery.club.raccscanner.scanner.impl.obfuscators.paramorphism;

import hakery.club.raccscanner.scanner.Scanner;
import hakery.club.raccscanner.util.OpcodeUtils;
import hakery.club.raccscanner.util.opcodes.InstructionList;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ParamorphismDecrypterScanner extends Scanner<ArrayList<ClassNode>> {

    private final InstructionList initializerInstructions = new InstructionList(Arrays.asList(
            Opcodes.NEW,
            Opcodes.DUP,
            Opcodes.LDC,
            Opcodes.INVOKEVIRTUAL,
            Opcodes.LDC,
            Opcodes.LDC,
            Opcodes.INVOKESPECIAL,
            Opcodes.LDC,
            Opcodes.INVOKEVIRTUAL,
            Opcodes.INVOKEVIRTUAL,
            Opcodes.PUTSTATIC,
            Opcodes.RETURN
    ));

    @Override
    public boolean scan() {
        ArrayList<ClassNode> tmp = new ArrayList<>();

        /* do this for every class */
        rScanner.getClasses().forEach((name, node) -> {

            AtomicInteger flags = new AtomicInteger();

            node.methods.forEach(methodNode -> {
                /* static initializer */
                if(methodNode.name.equals("<clinit>"))
                {
                    InstructionList instructionList = new InstructionList(methodNode.instructions);

                    if(OpcodeUtils.getInstance().compareOpcodes(initializerInstructions, instructionList, 0))
                        flags.addAndGet(2);
                }
            });

            if(rScanner.isDebugging() && flags.get() != 0)
                System.out.printf("[ParamorphismDecrypterScanner] %s.class with certainty level: %d (%s)\n", name, flags.get(), flags.get() == 1 ? "Undecisive" : "Confident");

            if(flags.get() >= 2)
                tmp.add(node);
        });

        this.setResult(tmp);

        return true;
    }
}
