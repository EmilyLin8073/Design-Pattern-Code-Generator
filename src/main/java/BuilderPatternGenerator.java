import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class BuilderPatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userBInterface, userBMethod, userBCOMethod, userBChildClass, userCOClass,
                  userPInterface, userPPInterface, userPPChildClass, userCOCObject, userGNMethod;

    //Variable for storing file path
    public String path;

    //Variable for file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    protected BuilderPatternGenerator(String path, String userBInterface, String userBMethod, String userBChildClass,
                                   String userCOClass, String userCOCObject, String userBCOMethod, String userPInterface,
                                   String userPPInterface, String userPPChildClass, String userGNMethod){
        this.userBInterface = userBInterface;
        this.userBMethod = userBMethod;
        this.userBChildClass = userBChildClass;
        this.userCOClass = userCOClass;
        this.userCOCObject = userCOCObject;
        this.userBCOMethod = userBCOMethod;
        this.userPInterface = userPInterface;
        this.userPPInterface = userPPInterface;
        this.userPPChildClass = userPPChildClass;
        this.userGNMethod = userGNMethod;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern(){
        logger().debug("In the builder generator");

        //Get the userPPInterface interface type
        ClassName userPPIName = ClassName.get("", userPPInterface);
        //Get the userPInterface interface type
        ClassName userPIName = ClassName.get("", userPInterface);
        //Get the userCOClass class type
        ClassName userCOCName = ClassName.get("", userCOClass);
        //Get the userBInterface interface type
        ClassName userBIName = ClassName.get("", userBInterface);
        //Get the userPPChildClass class type
        ClassName PPCCName = ClassName.get("", userPPChildClass);


        //Build the product interface and the method inside it
        TypeSpec productInterface = TypeSpec.interfaceBuilder(userPInterface)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userGNMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(String.class)
                    .build())
                .build();
        javaFile = JavaFile.builder("", productInterface).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the userPPInterface interface
        TypeSpec PPInterface = TypeSpec.interfaceBuilder(userPPInterface)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(userPIName)
                .build();
        javaFile = JavaFile.builder("", PPInterface).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will implement the userPPInterface interface and the
        //method inside it
        TypeSpec PPChildClass = TypeSpec.classBuilder(userPPChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userGNMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S", userPPChildClass)
                    .addAnnotation(Override.class)
                    .build())
                .addSuperinterface(userPPIName)
                .build();
        javaFile = JavaFile.builder("", PPChildClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the complex object class
        TypeSpec COClass = TypeSpec.classBuilder(userCOClass)
                .addModifiers(Modifier.PUBLIC)
                .build();
        javaFile = JavaFile.builder("", COClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the builder interface and the method inside it
        TypeSpec BInterface = TypeSpec.interfaceBuilder(userBInterface)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userBMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(void.class)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userBCOMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(userCOCName)
                    .build())
                .build();
        javaFile = JavaFile.builder("", BInterface).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which implement the builder interface and the method inside it
        TypeSpec BChildClass = TypeSpec.classBuilder(userBChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(userCOCName, userCOCObject)
                        .addModifiers(Modifier.PRIVATE)
                        .initializer("new $T()", userCOCName)
                        .build())
                .addMethod(MethodSpec.methodBuilder(userBMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addStatement("new $T()", PPCCName)
                    .addAnnotation(Override.class)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userBCOMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(userCOCName)
                    .addStatement("return " + userCOCObject)
                    .addAnnotation(Override.class)
                    .build())
                .addSuperinterface(userBIName)
                .build();
        javaFile = JavaFile.builder("", BChildClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
