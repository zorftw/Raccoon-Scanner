package hakery.club.raccscanner.scanner.impl.obfuscators.stringer;

import hakery.club.raccscanner.scanner.Scanner;
import hakery.club.raccscanner.util.OpcodeUtils;
import hakery.club.raccscanner.util.opcodes.InstructionList;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Info about the class in question:
 * - Extends thread
 * - Has three fields [object array, int, BigInteger array]
 * - Decryption method always has 1147 instructions
 * - first 30 (even more) instructions are always the same
 * - If I were to implement frame checks, there's a bunch of local variables
 */

public class StringerStringEncryptionScanner extends Scanner<ArrayList<ClassNode>> {

    final InstructionList stringerDecrypterStringEncryptionV3_0 = new InstructionList(Arrays.asList(
            Opcodes.SIPUSH,
            Opcodes.NEWARRAY,
            Opcodes.ASTORE,
            Opcodes.SIPUSH,
            Opcodes.NEWARRAY,
            Opcodes.ASTORE,
            Opcodes.SIPUSH,
            Opcodes.NEWARRAY,
            Opcodes.ASTORE,
            Opcodes.SIPUSH,
            Opcodes.NEWARRAY,
            Opcodes.ASTORE,
            Opcodes.BIPUSH,
            Opcodes.ANEWARRAY,
            Opcodes.DUP,
            0xFF, /* i forgot what this was */
            Opcodes.ALOAD,
            Opcodes.AASTORE,
            Opcodes.DUP
    ));

    @Override
    public boolean scan() {

        ArrayList<ClassNode> res = new ArrayList<>();

        raccoon.getClasses().forEach((classPath, classNode) -> {

            /* Class must be a Thread */
            if (classNode.superName.contains("java/lang/Thread")) {
                AtomicInteger flags = new AtomicInteger();

                if (classNode.fields.size() == 3)
                    if (classNode.fields.stream().allMatch(fd -> fd.desc.equals("[Ljava/lang/Object;")
                            || fd.desc.equals("I")
                            || fd.desc.equals("[Ljava/math/BigInteger;")))
                        flags.incrementAndGet();

                classNode.methods.forEach(methodNode -> {
                    if (methodNode.desc.equals("(ILjava/lang/Object;)V")) {
                        flags.incrementAndGet();

                        InstructionList instructionList = new InstructionList(methodNode.instructions);

                        if (OpcodeUtils.getInstance().findOpcodes(stringerDecrypterStringEncryptionV3_0, instructionList))
                            flags.incrementAndGet();
                    }
                });

                if (raccoon.isDebugging() && flags.get() != 0)
                    log("%s.class with certainty level: %d (%s)", classPath, flags.get(), flags.get() == 1 ? "Unsure" : flags.get() == 2 ? "Undecisive" : "Confident");

                if (flags.get() >= 2)
                    res.add(classNode);
            }

        });

        setResult(res);
        return true;
    }
}
