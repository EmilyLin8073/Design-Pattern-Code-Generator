import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FacadePatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userFClass, userFMethod, userFChildClass, userClass, userCMethod, userCObject;

    //Variable for storing file path
    public String path;

    //Variable to store file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    protected FacadePatternGenerator(String path, String userFClass, String userFMethod, String userFChildClass,
                                  String userClass, String userCMethod, String userCObject){
        this.userFClass = userFClass;
        this.userFMethod = userFMethod;
        this.userFChildClass = userFChildClass;
        this.userClass = userClass;
        this.userCMethod = userCMethod;
        this.userCObject = userCObject;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern(){
        logger().debug("In the facade generator");

        //Get the facade abstract class type
        ClassName FACName = ClassName.get("", userFClass);
        //Get the class1 type
        ClassName CName = ClassName.get("", userClass);
        //Get the method type inside the class1
        ClassName CMName = ClassName.get("", userCMethod);


        //Build the facade abstract class and the method inside it
        TypeSpec FClass = TypeSpec.classBuilder(userFClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.methodBuilder(userFMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(void.class)
                    .build())
                .build();
        javaFile = JavaFile.builder("", FClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will extend the facade abstract class and the method inside it
        TypeSpec FChildClass = TypeSpec.classBuilder(userFChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(CName, userCObject)
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(CName, userCObject)
                    .addStatement("this.$N = $N", userCObject, userCObject)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userFMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addStatement("$N.$T()", userCObject, CMName)
                    .addAnnotation(Override.class)
                    .build())
                .superclass(FACName)
                .build();
        javaFile = JavaFile.builder("", FChildClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build class1 and the method inside it
        TypeSpec Class = TypeSpec.classBuilder(userClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userCMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .build())
                .build();
        javaFile = JavaFile.builder("", Class).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
