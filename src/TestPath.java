import com.wildcard.gysf0325.util.PathFileWildcard;

public class TestPath {
    public static void main(String[] args) {
        PathFileWildcard pfw = new PathFileWildcard();
        String path = "/home/gysf0325/javaproject/source_01/src/main/java/com/wildcard/gysf0325/util/PathFileWildcard.java";

        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/src/main/java/com/wildcard/gysf0325/util/PathFileWildcard.java", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/src/main/java/com/wildcard/gysf0325/util/Pa*leW*card.java", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/src/main/java/com/wildcard/gysf0325/util/P*.java", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/src/main/java/com/wildcard/gysf0325/util/*d.java", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/src/**/util/PathFileWildcard.java", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/src/main/java/com/wildcard/gysf0325/util/PathFileWildcardBug.java", path));//false
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_02", path));//false
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_02/", path));//false
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_01/**", path));//true
        System.out.println(pfw.match("/home/gysf0325/javaproject/source_02/**", path));//false
        System.out.println(pfw.match("src/main/java/com/wildcard/gysf0325/util/PathFileWildcard.java", path));//false 相对路径暂不支持
        System.out.println(pfw.match("source/main/java/com/wildcard/gysf0325/util/PathFileWildcard.java", path));//false
        System.out.println(pfw.match("**/src/main/java/com/wildcard/gysf0325/util/PathFileWildcard.java", path));//true
        System.out.println(pfw.match("**/source/main/java/com/wildcard/gysf0325/util/PathFileWildcard.java", path));//false
        System.out.println(pfw.match("**/util/**", path));//true
        System.out.println(pfw.match("**/service/**", path));//false
        System.out.println(pfw.match("**/source_01/**/util", path));//false 暂时没思路，后面有空调调看
        System.out.println(pfw.match("**/source_01/**/service", path));//false
        System.out.println(pfw.match("**/source_01/**/util/**", path));//true
        System.out.println(pfw.match("**/source_01/**/service/**", path));//false
        System.out.println(pfw.match("**/source_01/**/u*l/**", path));//true
        System.out.println(pfw.match("**/source_01/**/u*/**", path));//true
        System.out.println(pfw.match("**/source_01/**/*l/**", path));//true
        System.out.println(pfw.match("**/source_01/**/us*l/**", path));//false
        System.out.println(pfw.match("**/src/main/java/com/wildcard/gysf0325/util/Pa*eW*card.java", path));//true
        System.out.println(pfw.match("**/src/main/java/com/wildcard/gysf0325/util/P*.java", path));//true
        System.out.println(pfw.match("**/src/main/java/com/wildcard/gysf0325/util/*ard.java", path));//true
        System.out.println(pfw.match("**/src/main/java/com/wildcard/gysf0325/util/path*wildcard.java", path));//false
    }
}
