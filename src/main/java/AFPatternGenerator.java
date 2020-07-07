import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class AFPatternGenerator implements Logs {
    //Variable for storing user input interface name and class name
    public String userAFInterface, userAFIChildClass, userPIChildClass, userM, userMS, userGNMethod;

    //Variable for storing file path
    public String path;

    //Variable for file path
    File filePath;

    //Variable for building the .java file
    JavaFile javaFile;

    //Constructor
    protected AFPatternGenerator(String path, String userAFInterface, String userM, String userMS,
                                 String userAFIChildClass, String userPIChildClass, String userGNMethod){
        this.userAFInterface = userAFInterface;
        this.userM = userM;
        this.userMS = userMS;
        this.userAFIChildClass = userAFIChildClass;
        this.userPIChildClass = userPIChildClass;
        this.userGNMethod = userGNMethod;

        this.path = path;
        filePath = new File(path);
    }

    public void generatePattern() {
        logger().debug("In the abstract factory generator");

        //Get the class type
        ClassName userMethodSignatureName = ClassName.get("", userMS);
        //Get the product Interface type
        ClassName productIName = ClassName.get("", userMS);
        //Get the concrete child class of product interface type
        ClassName userPIChildClassName = ClassName.get("",  userPIChildClass);
        //Get the abstract factory interface type
        ClassName userInterfaceName = ClassName.get("", userAFInterface);


        //Build the abstract factory interface and the method in it
        TypeSpec AFInterface = TypeSpec.interfaceBuilder(userAFInterface)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder(userM)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(userMethodSignatureName)
                    .build())
                .build();
        javaFile = JavaFile.builder("", AFInterface).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build method inside the concrete class which implement the abstract factory interface
        MethodSpec methodInAFIChildClass = MethodSpec.methodBuilder(userM)
                .addModifiers(Modifier.PUBLIC)
                .returns(userMethodSignatureName)
                .addStatement("return new $T()", userPIChildClassName)
                .addAnnotation(Override.class)
                .build();

        //Build the concrete class which implement the abstract factory interface
        TypeSpec AFIChildClass = TypeSpec.classBuilder(userAFIChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(userInterfaceName)
                .addMethod(methodInAFIChildClass)
                .build();
        javaFile = JavaFile.builder("", AFIChildClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Build product interface and the method inside the product interface
        TypeSpec productInterface = TypeSpec.interfaceBuilder(userMS)
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

        //Build the concrete class which will implement the product interface and the method inside this concrete class
        TypeSpec productChildClass = TypeSpec.classBuilder(userPIChildClass)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(productIName)
                .addMethod(MethodSpec.methodBuilder(userGNMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addAnnotation(Override.class)
                        .addStatement("return $S", userPIChildClass)
                        .build())
                .build();
        javaFile = JavaFile.builder("", productChildClass).build();
        try {
            javaFile.writeTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
