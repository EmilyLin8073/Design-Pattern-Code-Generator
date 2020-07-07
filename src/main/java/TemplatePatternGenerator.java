import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TemplatePatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userTClass, userTChildClass, userTCMethod, userTMethod;

    //Variable for storing file path
    public String path;

    //Variable to store file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    protected TemplatePatternGenerator(String path, String userTClass, String userTChildClass,
                                       String userTCMethod, String userTMethod){
        this.userTClass = userTClass;
        this.userTChildClass = userTChildClass;
        this.userTCMethod = userTCMethod;
        this.userTMethod = userTMethod;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern(){
        logger().debug("In the template generator");

        //Get the template abstract class type
        ClassName TCName = ClassName.get("", userTClass);
        //Get the method type
        ClassName TCMName = ClassName.get("", userTCMethod);


        //Build the template abstract class and the method inside it
        TypeSpec TClass = TypeSpec.classBuilder(userTClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.methodBuilder(userTCMethod)
                    .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                    .returns(void.class)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userTMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .returns(void.class)
                    .addStatement("$T()", TCMName)
                    .build())
                .build();
        javaFile = JavaFile.builder("", TClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will extend the template abstract class and the method inside it
        TypeSpec TCClass = TypeSpec.classBuilder(userTChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userTCMethod)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(void.class)
                    .addAnnotation(Override.class)
                    .build())
                .superclass(TCName)
                .build();
        javaFile = JavaFile.builder("", TCClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
