package hakery.club.raccscanner.util;

import hakery.club.raccscanner.util.opcodes.InstructionList;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Printer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class OpcodeUtils {

    private static final OpcodeUtils instance = new OpcodeUtils();
    private OpcodeUtils() {
        /** NOTE TO SELF:
         *      Read the docs dipshit
         *
         try {
         Field[] fields = Opcodes.class.getDeclaredFields();

         for (Field field : fields) {
         //is an int
         if (field.getType().toString().equals(int.class.getTypeName())) {
         if (!opcodeNames.containsKey(field.getInt(null)))
         opcodeNames.put(field.getInt(null), field.getName());
         }
         }
         } catch (Exception e) {
         e.printStackTrace();
         }
         */
    }

    public static OpcodeUtils getInstance() {
        return instance;
    }

    public void dumpOpcodes(InstructionList src, String path, String method) {
        int idx = 0;

        try {
            File dump = new File(String.format("dump%d", idx));
            idx++;

            if (!dump.exists())
                dump.createNewFile();

            AtomicInteger i = new AtomicInteger();
            src.getOpcodes().forEach(op -> {
                try {
                    String toWrite = String.format("%d %s\n", i.getAndIncrement(), Printer.OPCODES[op]);
                    Files.write(dump.toPath(), toWrite.getBytes(), StandardOpenOption.APPEND);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            System.out.println(String.format("[Raccoon] Dumped %s#%s to %s", path, method, dump.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean compareOpcodes(InstructionList from, InstructionList source, int sourceOffset) {
        if (source.size() == 0)
            return false;

        int scanLenght = Math.min(from.size(), from.size()); /* get lowest amount to compare */

        for (int idx = 0; idx < scanLenght; idx++) {
            if (from.isReplaceable(idx)) /* this means an byte can be replaced by anything else*/
                continue;

            int fromOP = from.get(idx);
            int sourceOP = source.get(idx + sourceOffset);

            if (sourceOP != fromOP)
                return false;
        }

        return true;
    }

    public int getConstantCount(InstructionList source) {
        int res = 0;
        for (int i = 0; i < source.size(); i++)
            if (source.get(i) >= Opcodes.ICONST_0 && source.get(i) <= Opcodes.ICONST_5)
                res++;
        return res;
    }

    public int getOpcodeCount(int opcode, InstructionList source) {
        int res = 0;
        for (int i = 0; i < source.size(); i++)
            if (source.get(i) == opcode)
                res++;
        return res;
    }

    public boolean findOpcodes(InstructionList toFind, InstructionList source) {
        if (toFind.size() > source.size())
            return false;

        /* loop over every instruction in the source */
        for (int i = 0; i < source.size(); i++) {
            if (compareOpcodes(toFind, source, i))
                return true;
        }

        return false;
    }

    public byte[] toByteArray(InputStream is) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;

            byte[] data = new byte[1024 * 4 * 4];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
