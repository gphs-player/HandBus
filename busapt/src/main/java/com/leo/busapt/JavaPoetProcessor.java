package com.leo.busapt;



import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@SupportedAnnotationTypes(value = "com.leo.bus.Receive")
public class JavaPoetProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {

        super.init(processingEnvironment);

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        String handBusIndex = processingEnvironment.getOptions().get("handBusIndex");
        log(handBusIndex);
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //$L for Literals       addStatement("result = result $L i", op)
        //$S for Strings           .addStatement("return $S", name)
        //$T for Types             .addStatement("return new $T()", Date.class)

        //  $N for Names
        //  MethodSpec hexDigit = MethodSpec.methodBuilder("hexDigit")
        //  .addStatement("result[0] = $N((b >>> 4) & 0xf)", hexDigit)
        MethodSpec methodSpec = MethodSpec.methodBuilder("main")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                //添加代码块
                .addCode("for(int start=0; start <= 10; start++){\n" +
                        "\t System.out.println(start);\n" +
                        "}\n")
                .addParameter(String[].class, "args")
                //添加类型及语句
                .addStatement("$T.out.println($S)", System.class, "Hello JavaPoet")
                //添加流程控制
                .addStatement("int isFinish")
                .beginControlFlow("while(isFinish < 5)")
                .addStatement("\t System.out.println(isFinish);\nisFinish++")
                .endControlFlow()
                .build();
        TypeSpec helloAPT = TypeSpec.classBuilder("HelloAPT")
                .addModifiers(Modifier.PUBLIC,Modifier.FINAL)
                .addMethod(methodSpec).build();
        JavaFile builder = JavaFile.builder("com.leo.bus", helloAPT)

                .build();
        try {
            builder.writeTo(System.err);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    private void log(String msg){
        System.err.println("BUS-APT : "+ msg);
    }
}