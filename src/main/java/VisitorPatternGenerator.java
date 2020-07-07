import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class VisitorPatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userEClass, userEChildClass, userECMethod, userEMethod, userVClass, userVMethod,
                 userVChildClass, userVCObject, userECObject;

    //Variable for storing file path
    public String path;

    //Variable to store file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    protected VisitorPatternGenerator(String path, String userEClass, String userEChildClass,
                                      String userECMethod, String userEMethod, String userVClass,
                                      String userVMethod, String userVChildClass, String userVCObject,
                                      String userECObject){
        this.userEClass = userEClass;
        this.userEChildClass = userEChildClass;
        this.userECMethod = userECMethod;
        this.userEMethod = userEMethod;
        this.userVClass = userVClass;
        this.userVMethod = userVMethod;
        this.userVChildClass = userVChildClass;
        this.userVCObject = userVCObject;
        this.userECObject = userECObject;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern(){
        logger().debug("In the visitor generator");

        //Get the element abstract class type
        ClassName ECName = ClassName.get("", userEClass);
        //Get the visitor abstract class type
        ClassName VCName = ClassName.get("", userVClass);
        //Get the userEChildClass class type
        ClassName ECCName = ClassName.get("", userEChildClass);
        //Get the method type inside the visitor abstract class
        ClassName VMNName = ClassName.get("", userVMethod);
        //Get the method type inside the concrete element class
        ClassName ECMName = ClassName.get("", userECMethod);


        //Build the element abstract class and the method inside it
        TypeSpec EClass = TypeSpec.classBuilder(userEClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.methodBuilder(userEMethod)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(void.class)
                        .addParameter(VCName, userVCObject)
                        .build())
                .build();
        javaFile = JavaFile.builder("",EClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will extend the element abstract class and the method inside it
        TypeSpec ECClass = TypeSpec.classBuilder(userEChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userEMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(VCName, userVCObject)
                    .addStatement("$N.$T(this)", userVCObject, VMNName)
                    .addAnnotation(Override.class)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userECMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .build())
                .superclass(ECName)
                .build();
        javaFile = JavaFile.builder("", ECClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the visitor abstract class and the method inside it
        TypeSpec VClass = TypeSpec.classBuilder(userVClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.methodBuilder(userVMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(void.class)
                    .addParameter(ECCName, userECObject)
                    .build())
                .build();
        javaFile = JavaFile.builder("", VClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will extends the visitor abstract class and the method inside it
        TypeSpec VCClass = TypeSpec.classBuilder(userVChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userVMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(ECCName, userECObject)
                    .addStatement("$N.$T()", userECObject, ECMName)
                    .addAnnotation(Override.class)
                    .build())
                .superclass(VCName)
                .build();
        javaFile = JavaFile.builder("", VCClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
