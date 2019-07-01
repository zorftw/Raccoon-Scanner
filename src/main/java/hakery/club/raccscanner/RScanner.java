package hakery.club.raccscanner;

import hakery.club.raccscanner.scanner.Scanners;
import hakery.club.raccscanner.util.OpcodeUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class RScanner {

    private File targetFile;
    private Map<String, ClassNode> classes;
    private boolean debugging = true;

    private Scanners scanners;

    private boolean debugInfoFound = false;

    public RScanner(File target) throws IOException {
        this.targetFile = target;
        this.classes = readClasses();

        assert this.classes.size() > 0;

        this.scanners = new Scanners(this);
        this.scanners.scan();
    }

    /**
     * Use this if you've already filtered the jar file /  already have a classpath you want to scan
     * this will prevent limitations that might exist
     * @param classes
     */
    public RScanner(Map<String, ClassNode> classes)
    {
        this.classes = classes;
        this.targetFile = null;

        assert this.classes.size() > 0;

        this.scanners = new Scanners(this);
        this.scanners.scan();
    }

    public Map<String, ClassNode> readClasses() throws IOException {
        Map<String, ClassNode> tmp = new HashMap<>();
        assert targetFile != null;

        ZipFile zip = new ZipFile(this.targetFile);
        Enumeration<? extends ZipEntry> zipEntries = zip.entries();

        while (zipEntries.hasMoreElements()) {
            ZipEntry entry = zipEntries.nextElement();

            try {
                /** Credits to ItzSomebody **/
                if (entry.getName().endsWith(".class")) {
                    byte[] classBytes = OpcodeUtils.getInstance().toByteArray(zip.getInputStream(entry));

                    ClassReader classReader = new ClassReader(classBytes);
                    ClassNode classNode = new ClassNode();

                    classReader.accept(classNode, Opcodes.ASM6 | ClassReader.SKIP_FRAMES);

                    for (int i = 0; i < classNode.methods.size(); i++) {
                        MethodNode methodNode = classNode.methods.get(i);
                        JSRInlinerAdapter adapter = new JSRInlinerAdapter(methodNode, methodNode.access, methodNode.name, methodNode.desc, methodNode.signature, methodNode.exceptions.toArray(new String[0]));
                        methodNode.accept(adapter);
                        classNode.methods.set(i, adapter);
                    }

                    tmp.put(classNode.name, classNode);

                }
            } catch (IllegalArgumentException e) {
                System.out.println(String.format("[Raccoon] Couldn't parse %s", entry.getName()));
            }
        }
        zip.close();
        return tmp;
    }

    public boolean isDebugging() {
        return debugging;
    }

    public boolean isDebugInfoFound() {
        return debugInfoFound;
    }

    public void setDebugInfoFound(boolean value) {
        this.debugInfoFound = value;
    }

    public Map<String, ClassNode> getClasses() {
        return this.classes;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public Scanners getScanner() {
        return scanners;
    }

    public static void main(String[] args) throws IOException {
        RScanner scanner = new RScanner(new File("D:\\Allatori-Cracked\\Hello-World-obf.jar"));
    }
}
