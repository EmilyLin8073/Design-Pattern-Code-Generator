import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CORPatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userHClass, userHObject, userHMethod, userHChildClass1, userHChildClass2,
                  userHBMethod;

    //Variable for storing file path
    public String path;

    //Variable to store file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    protected CORPatternGenerator(String path, String userHClass, String userHObject, String userHMethod,
                               String userHChildClass1, String userHChildClass2, String userHBMethod){
        this.userHClass = userHClass;
        this.userHObject = userHObject;
        this.userHMethod = userHMethod;
        this.userHChildClass1 = userHChildClass1;
        this.userHChildClass2 = userHChildClass2;
        this.userHBMethod = userHBMethod;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern(){
        logger().debug("In the COR generator");

        //Get the handler abstract class type
        ClassName HCName = ClassName.get("", userHClass);
        //Get the method type inside the handler abstract class
        ClassName HMName = ClassName.get("", userHMethod);
        //Get the boolean method type inside the handler abstract class
        ClassName HBMName = ClassName.get("", userHBMethod);


        //Build the handler abstract class and the method inside it
        TypeSpec HClass = TypeSpec.classBuilder(userHClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(FieldSpec.builder(HCName, userHObject)
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(HCName, userHObject)
                        .addStatement("this.$N = $N", userHObject, userHObject)
                        .build())
                .addMethod(MethodSpec.methodBuilder(userHMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .beginControlFlow("if($N != null)", userHObject)
                        .addStatement("$N.$T()", userHObject, HMName)
                        .endControlFlow()
                        .build())
                .addMethod(MethodSpec.methodBuilder(userHBMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(boolean.class)
                        .addStatement("return false")
                        .build())
                .build();
        javaFile = JavaFile.builder("",HClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will extends the handler abstract class and the method inside it
        TypeSpec HCClass1 = TypeSpec.classBuilder(userHChildClass1)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.constructorBuilder()
                    .addParameter(HCName, userHObject)
                    .addStatement("super($N)", userHObject)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userHMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .beginControlFlow("if($T())", HBMName)
                    .addStatement("$T.out.println($S)", System.class, "Handling the request")
                    .nextControlFlow("else")
                    .addStatement("super.$T()", HMName)
                    .endControlFlow()
                    .addAnnotation(Override.class)
                    .build())
                .superclass(HCName)
                .build();
        javaFile = JavaFile.builder("", HCClass1).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the second concrete class which will extend the handler abstract class and the method inside it
        TypeSpec HCClass2 = TypeSpec.classBuilder(userHChildClass2)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userHMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .addStatement("$T.out.println($S)", System.class, "Handling the request")
                        .addAnnotation(Override.class)
                        .build())
                .superclass(HCName)
                .build();
        javaFile = JavaFile.builder("", HCClass2).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
