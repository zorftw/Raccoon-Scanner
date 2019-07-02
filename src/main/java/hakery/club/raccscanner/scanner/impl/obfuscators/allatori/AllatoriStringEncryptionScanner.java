package hakery.club.raccscanner.scanner.impl.obfuscators.allatori;

import hakery.club.raccscanner.scanner.Scanner;
import hakery.club.raccscanner.util.OpcodeUtils;
import hakery.club.raccscanner.util.opcodes.InstructionList;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * V4
 * <p>
 * Put's a bunch of random integers and longs (key values) on the stack,
 * uses some xor trickery, so this is easily found by looking at Xor operations and
 * amount of values pushed onto the stack.
 * <p>
 * V3
 * Always starts with a new exception, getting stacktrace, initializing a new buffer
 * and getting the StackTraceElement name.
 */
public class AllatoriStringEncryptionScanner extends Scanner<ArrayList<ClassNode>> {

    private final InstructionList allatoriStringEncryptionV_3 = new InstructionList(Arrays.asList(
            Opcodes.ALOAD,
            Opcodes.ICONST_1,
            Opcodes.DUP,
            Opcodes.DUP_X2,
            Opcodes.NEW,
            Opcodes.DUP,
            Opcodes.INVOKESPECIAL,
            Opcodes.INVOKEVIRTUAL,
            Opcodes.SWAP,
            Opcodes.AALOAD,
            Opcodes.NEW,
            Opcodes.DUP,
            Opcodes.INVOKESPECIAL,
            Opcodes.SWAP,
            Opcodes.DUP,
            Opcodes.INVOKEVIRTUAL /* StackTraceElement#getClassName() */
    ));

    @Override
    public boolean scan() {
        ArrayList<ClassNode> tmp = new ArrayList<>();

        raccoon.getClasses().forEach((classPath, classNode) -> classNode.methods.forEach(methodNode -> {
            InstructionList instructionList = new InstructionList(methodNode.instructions);

            /**
             * V4 Check
             */

            /* again, thanks to itzsomebody for the idea */
            int siPushCount = OpcodeUtils.getInstance().getOpcodeCount(Opcodes.SIPUSH, instructionList);
            int constCount = OpcodeUtils.getInstance().getConstantCount(instructionList);
            int ixorCount = OpcodeUtils.getInstance().getOpcodeCount(Opcodes.IXOR, instructionList);

            if (siPushCount >= 8 && constCount >= 8 && ixorCount >= 3 && instructionList.size() <= 250) {
                if (raccoon.isDebugging())
                    log("[AllatoriStringEncryptionScanner] %s.class might contain encrypted strings using v4", classPath);

                tmp.add(classNode);
            }

            /**
             * End of V4 Check
             */

            /**
             * V3 Check *
             * */

            if (OpcodeUtils.getInstance().findOpcodes(allatoriStringEncryptionV_3, instructionList)) {
                if (raccoon.isDebugging())
                    log("%s.class might contain encrypted strings using v3", classPath);

                tmp.add(classNode);
            }

            /**
             * End of V3 Check
             */
        }));

        this.setResult(tmp);
        return true;
    }


}
