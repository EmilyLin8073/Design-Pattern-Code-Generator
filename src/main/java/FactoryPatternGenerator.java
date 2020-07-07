import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class FactoryPatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userCClass, userCChildClass, userPInterface, userCMethod, userPChildClass,
                  userPObject, userGNMethod;

    //Variable for storing file path
    public String path;

    //Variable to store file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    protected FactoryPatternGenerator(String path, String userCClass, String userCChildClass, String userPInterface,
                                      String userCMethod, String userPChildClass, String userPObject,
                                      String userGNMethod){
        this.userCClass = userCClass;
        this.userCChildClass = userCChildClass;
        this.userPInterface = userPInterface;
        this.userCMethod = userCMethod;
        this.userPChildClass = userPChildClass;
        this.userPObject = userPObject;
        this.userGNMethod = userGNMethod;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern(){
        logger().debug("In the factory generator");

        //Get the creator abstract class type
        ClassName CClassName = ClassName.get("", userCClass);
        //Get the product interface type
        ClassName PIName = ClassName.get("", userPInterface);
        //Get the userPChildClass class type
        ClassName PCCName = ClassName.get("", userPChildClass);


        //Build the creator abstract class and the method and field in it
        TypeSpec CClass = TypeSpec.classBuilder(userCClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(FieldSpec.builder(PIName, userPObject)
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethod(MethodSpec.methodBuilder(userCMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(PIName)
                    .build())
                .build();
        javaFile = JavaFile.builder("", CClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which extend the creator abstract class
        TypeSpec CCClass = TypeSpec.classBuilder(userCChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userCMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(PIName)
                    .addStatement("return new $T()", PCCName)
                    .addAnnotation(Override.class)
                    .build())
                .superclass(CClassName)
                .build();
        javaFile = JavaFile.builder("", CCClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the product interface and the method in it
        TypeSpec PInterface = TypeSpec.interfaceBuilder(userPInterface)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userGNMethod)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(String.class)
                        .build())
                .build();
        javaFile = JavaFile.builder("", PInterface).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will implement the product interface and the method in it
        TypeSpec PChildClass = TypeSpec.classBuilder(userPChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userGNMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $S", userPChildClass)
                        .addAnnotation(Override.class)
                        .build())
                .addSuperinterface(PIName)
                .build();
        javaFile = JavaFile.builder("", PChildClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
