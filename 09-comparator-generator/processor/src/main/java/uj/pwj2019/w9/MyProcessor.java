package uj.pwj2019.w9;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({"uj.pwj2019.w9.MyComparable", "uj.pwj2019.w9.ComparePriority"})
public class MyProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var myComparable=ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(MyComparable.class));
        for(TypeElement annotationedElement: myComparable){
            String packageName = ((PackageElement) annotationedElement.getEnclosingElement()).getQualifiedName().toString();
            String className = annotationedElement.getSimpleName().toString();
            List<? extends Element> fields = processingEnv.getElementUtils().getAllMembers(annotationedElement);

            String fieldsCompare="";
            ArrayList<Pair<Integer, String>> listCompare=new ArrayList();

            for(var field: fields){
                if(field.getKind()== ElementKind.FIELD && field.getModifiers().contains(Modifier.PUBLIC)){
                    if(
                            !(field.asType().toString().equals("boolean") ||
                                field.asType().toString().equals("int") ||
                                field.asType().toString().equals("char") ||
                                field.asType().toString().equals("float") ||
                                field.asType().toString().equals("long") ||
                                field.asType().toString().equals("double") ||
                                field.asType().toString().equals("byte") ||
                                field.asType().toString().equals("short")
                            )
                    ){
                        continue;
                    }

                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Name: " + field.getSimpleName());

                    String fieldCompare="           if(data1."+field.getSimpleName()+"!=data2."+field.getSimpleName()+"){\n" +
                            "               return -1;\n" +
                            "           }\n";


                    if(field.getAnnotation(ComparePriority.class)==null){
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Val: null");
                        Pair<Integer, String> pairComparator=new Pair<>(Integer.MAX_VALUE, fieldCompare);
                        listCompare.add(pairComparator);
                    }else{
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Val: "+field.getAnnotation(ComparePriority.class).value());
                        Pair<Integer, String> pairComparator=new Pair<>(field.getAnnotation(ComparePriority.class).value(), fieldCompare);
                        listCompare.add(pairComparator);
                    }
                    //fieldsCompare+=fieldCompare;
                }
            }
            listCompare.sort(Comparator.comparingInt(Pair::getKey));
            for(var listCompareElement: listCompare){
                fieldsCompare+=listCompareElement.getValue();
            }

            try {

                JavaFileObject file = processingEnv.getFiler().createSourceFile(className + "Comparator");
                try (PrintWriter out = new PrintWriter(file.openWriter())) {

                    String comparator="package "+packageName+";\n" +
                            "\n" +
                            "public class "+className+"Comparator {\n" +
                            "\n" +
                            "    public int compare("+className+" data1, "+className+" data2) {\n"
                                    +fieldsCompare+
                            "        return 0;\n" +
                            "    }\n" +
                            "}\n";

                    out.write(comparator);

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        }


        /*for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + annotation);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + annotatedElements);
            if(annotation.toString().equals("uj.pwj2019.w9.MyComparable"))
                annotatedElements.forEach(this::processElement);
        }*/
        return true;
    }


    private void processElement(Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Producing " + e);
        TypeElement clazz = (TypeElement) e;
        String className = clazz.getQualifiedName().toString();


        try {




            /*Class<?> unitClass = Class.forName(className);
            //Class<?> unitClass = clazz.getClass();


            for(Field field : unitClass.getDeclaredFields()){
                Class type = field.getType();
                String name = field.getName();
                Annotation[] annotation = field.getDeclaredAnnotations();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Type: " + type.getName() + " Name: " + name + "Annotation: "+ annotation.toString());
            }*/


        } catch (Exception ex) {
            ex.printStackTrace();
        }



        try {

            JavaFileObject file = processingEnv.getFiler().createSourceFile(className + "Comparator");
            String packageName = packageName(className);
            try (PrintWriter out = new PrintWriter(file.openWriter())) {

                String comparator="package uj.pwj2019.w9;\n" +
                        "\n" +
                        "public class SecretDataComparator {\n" +
                        "\n" +
                        "    public int compare("+className+" data1, "+className+" data2) {\n" +
                        "        return 0;\n" +
                        "    }\n" +
                        "}\n";

                out.write(comparator);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String packageName(String className) {
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }
        return packageName;
    }



}