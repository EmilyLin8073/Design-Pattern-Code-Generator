import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MediatorPatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userMClass, userMMethod, userMChildClass, userMCMethod, userCClass, userCChildClass1,
                  userCChildClass2, userCCCSMethod, userCCCGMethod, userMObject, userCObject,userCCCString,
                  userCCC1Object, userCCC2Object;

    //Variable for storing file path
    public String path;

    //Variable for file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    public MediatorPatternGenerator(String path, String userMClass, String userMMethod, String userMChildClass,
                                    String userMCMethod, String userCClass, String userCChildClass1,
                                    String userCChildClass2, String userCCCSMethod, String userCCCGMethod,
                                    String userMObject, String userCObject, String userCCCString,
                                    String userCCC1Object, String userCCC2Object){
        this.userMClass = userMClass;
        this.userMMethod = userMMethod;
        this.userMChildClass = userMChildClass;
        this.userMCMethod = userMCMethod;
        this.userCClass = userCClass;
        this.userCChildClass1 = userCChildClass1;
        this.userCChildClass2 = userCChildClass2;
        this.userCCCSMethod = userCCCSMethod;
        this.userCCCGMethod = userCCCGMethod;
        this.userMObject = userMObject;
        this.userCObject = userCObject;
        this.userCCCString = userCCCString;
        this.userCCC1Object = userCCC1Object;
        this.userCCC2Object = userCCC2Object;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern(){
        logger().debug("In the mediator generator");

        //Get the mediator abstract class type
        ClassName MCName = ClassName.get("", userMClass);
        //Get the colleague abstract class type
        ClassName CCName = ClassName.get("", userCClass);
        //Get the colleague1 concrete class type
        ClassName CCCName1 = ClassName.get("", userCChildClass1);
        //Get the colleague2 concrete class type
        ClassName CCCName2 = ClassName.get("", userCChildClass2);
        //Get the mediate method type in the mediate abstract class
        ClassName MMName = ClassName.get("", userMMethod);
        //Get the setState method type
        ClassName CCCGMName = ClassName.get("", userCCCGMethod);


        //Build the mediator abstract class and the method in it
        TypeSpec MClass = TypeSpec.classBuilder(userMClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addMethod(MethodSpec.methodBuilder(userMMethod)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(void.class)
                    .addParameter(CCName, userCObject)
                    .build())
                .build();
        javaFile = JavaFile.builder("", MClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the concrete class which will extends the mediator abstract class and the method in it
        TypeSpec MCClass = TypeSpec.classBuilder(userMChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(CCCName1, userCCC1Object)
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec.builder(CCCName2, userCCC2Object)
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethod(MethodSpec.methodBuilder(userMCMethod)
                    .returns(void.class)
                    .addParameter(CCCName1, userCCC1Object)
                    .addParameter(CCCName2, userCCC2Object)
                    .addStatement("this.$N = $N", userCCC1Object, userCCC1Object)
                    .addStatement("this.$N = $N", userCCC2Object, userCCC2Object)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userMMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(CCName, userCObject)
                    .beginControlFlow("if($N == $N)", userCObject, userCCC1Object)
                        .addStatement("$T state = $N.$T()", String.class, userCCC1Object, CCCGMName)
                    .endControlFlow()
                    .beginControlFlow("if($N == $N)", userCObject, userCCC2Object)
                        .addStatement("$T state = $N.$T()", String.class, userCCC2Object, CCCGMName)
                    .endControlFlow()
                    .addAnnotation(Override.class)
                    .build())
                .superclass(MCName)
                .build();
        javaFile = JavaFile.builder("",MCClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build colleague abstract class and the method in is
        TypeSpec CClass = TypeSpec.classBuilder(userCClass)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(FieldSpec.builder(MCName, userMObject)
                    .build())
                .addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(MCName, userMObject)
                    .addStatement("this.$N = $N", userMObject, userMObject)
                    .build())
                .build();
        javaFile = JavaFile.builder("", CClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the first concrete class which extends the colleague abstract class and the method in it
        TypeSpec CCClass1 = TypeSpec.classBuilder(userCChildClass1)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(String.class, userCCCString)
                    .addModifiers(Modifier.PRIVATE)
                    .build())
                .addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(MCName, userMObject)
                    .addStatement("super($N)", userMObject)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userCCCGMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $N", userCCCString)
                    .build())
                .addMethod(MethodSpec.methodBuilder(userCCCSMethod)
                    .returns(void.class)
                    .addParameter(String.class, userCCCString)
                    .beginControlFlow("if($N != this.$N)", userCCCString, userCCCString)
                    .addStatement("this.$N = $N", userCCCString, userCCCString)
                    .addStatement("$N.$T(this)", userMObject, MMName)
                    .endControlFlow()
                    .build())
                .superclass(CCName)
                .build();
        javaFile = JavaFile.builder("", CCClass1).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build the second concrete class which extends the colleague abstract class and the method in it
        TypeSpec CCClass2 = TypeSpec.classBuilder(userCChildClass2)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(String.class, userCCCString)
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(MCName, userMObject)
                        .addStatement("super($N)", userMObject)
                        .build())
                .addMethod(MethodSpec.methodBuilder(userCCCGMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return $N", userCCCString)
                        .build())
                .addMethod(MethodSpec.methodBuilder(userCCCSMethod)
                        .returns(void.class)
                        .addParameter(String.class, userCCCString)
                        .beginControlFlow("if($N != this.$N)", userCCCString, userCCCString)
                        .addStatement("this.$N = $N", userCCCString, userCCCString)
                        .addStatement("$N.$T(this)", userMObject, MMName)
                        .endControlFlow()
                        .build())
                .superclass(CCName)
                .build();
        javaFile = JavaFile.builder("", CCClass2).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
