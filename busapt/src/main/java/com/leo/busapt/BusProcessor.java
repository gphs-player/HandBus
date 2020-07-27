package com.leo.busapt;


import com.leo.annotation.Receive;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes(value = "com.leo.annotation.Receive")
public class BusProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;

    private String mBusIndexClassName;
    private Map<String, List<EventRegisterInfo>> mReceiverMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        mBusIndexClassName = processingEnvironment.getOptions().get("handBusIndex");

        log("目标编译BusIndex : " + mBusIndexClassName);
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (mBusIndexClassName == null || "".equals(mBusIndexClassName.trim())) {
            return false;
        }
        //找到目标注解
        TypeElement receiveElement = null;
        for (TypeElement typeElement : set) {
            if ("com.leo.annotation.Receive".equals(typeElement.getQualifiedName().toString())) {
                receiveElement = typeElement;
                break;
            }
        }
        if (receiveElement == null) return false;
        collectAnnotationInfo(roundEnvironment, receiveElement);

        String mFieldName = "mIndexMap";

        //1.1覆写方法 返回值
//        List<EventMethodInfo> find(Class<?> event);
        ParameterizedTypeName returnType = ParameterizedTypeName.get(
                ClassName.get(List.class),
                Consts.com_leo_bus_EventMethodInfo
        );
        String owArgName = "event";
        String owVarName = "methodInfoWrap";
        //1.2覆写方法和返回值
        MethodSpec overrideMethod = MethodSpec.methodBuilder("find")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(Consts.clazzType, owArgName).build())
                .returns(returnType)
                //如：     EventIndexInfoWrap methodInfoWrap = mIndexMap.get(receiver);
                //        if (methodInfoWrap == null) return null;
                //        return methodInfoWrap.getMethodList();
                .addStatement("$T $L = $L.get($L)", Consts.com_leo_bus_EventIndexInfoWrap, owVarName, mFieldName, owArgName)
                .addStatement("if($L == null)return null", owVarName)
                .addStatement("return $L.getMethodList()", owVarName)
                .build();


        //2.字段
        //private static Map<Class<?>, EventIndexInfoWrap> mIndexMap = new HashMap<>();
        ParameterizedTypeName fieldType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                Consts.clazzType, Consts.com_leo_bus_EventIndexInfoWrap
        );
        FieldSpec fieldSpec = FieldSpec.builder(fieldType, mFieldName, Modifier.STATIC, Modifier.PRIVATE).build();

        //3.添加方法 返回值
//  如     private static void putIndex(EventIndexInfoWrap infoWrap) {
//            mIndexMap.put(infoWrap.getTarget(), infoWrap);
//        }

        String putMethodArgName = "infoWrap";
        MethodSpec putMethod = MethodSpec.methodBuilder("putIndex")
                .addModifiers(Modifier.PRIVATE)
                .addModifiers(Modifier.STATIC)
                .addParameter(ParameterSpec.builder(Consts.com_leo_bus_EventIndexInfoWrap, putMethodArgName).build())
                .addStatement("$L.put($L.getTarget(), $L)", mFieldName, putMethodArgName, putMethodArgName)
                .returns(void.class)
                .build();


        //4.静态初始化代码块
        //mIndexMap = new HashMap();
//        List<Activity<List>>
        CodeBlock.Builder staticBlock = CodeBlock.builder();
        staticBlock.add("$L = new $T();\n", mFieldName, HashMap.class);
        writePutMethodBlock(putMethod, staticBlock);


        TypeSpec clazzIndex = TypeSpec.classBuilder(mBusIndexClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Consts.com_leo_bus_IndexFinder)
                .addField(fieldSpec)
                .addStaticBlock(staticBlock.build())
                .addMethod(overrideMethod)
                .addMethod(putMethod)
                .build();
        JavaFile indexFile = JavaFile.builder(Consts.APP_PACKAGE, clazzIndex).build();
        try {
            indexFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //收集注解相关信息
    private void collectAnnotationInfo(RoundEnvironment roundEnvironment, TypeElement receiveElement) {
        //获取注解信息
        String methodName;
        String methodParamType;
        String methodParamName;

        Set<? extends Element> elementsAnnotated = roundEnvironment.getElementsAnnotatedWith(receiveElement);
        for (Element element : elementsAnnotated) {
            TypeElement classElement = (TypeElement) element
                    .getEnclosingElement();
            ExecutableElement executableElement = (ExecutableElement) element;

            methodName = executableElement.getSimpleName().toString();
            Receive receiver = executableElement.getAnnotation(Receive.class);
            if (executableElement.getParameters().size() == 1) {
                methodParamName = executableElement.getParameters().get(0).toString();
                TypeMirror typeMirror = executableElement.getParameters().get(0).asType();

                methodParamType = typeMirror.toString();
            } else {
                throw new IllegalStateException("the @Receive method [" + executableElement.getSimpleName() + "] must have exactly 1 parameter ");
//                continue;
            }
            String receiverName = classElement.getQualifiedName().toString();
            List<EventRegisterInfo> mFindIndexList = mReceiverMap.get(receiverName);
            if (mFindIndexList == null) {
                mFindIndexList = new ArrayList<>();
                mReceiverMap.put(receiverName, mFindIndexList);
            }
            EventRegisterInfo mRegisterInfo = new EventRegisterInfo();
            mRegisterInfo.receiver = receiverName;
            mRegisterInfo.eventType = methodParamType;
            mRegisterInfo.methodName = methodName;
            mRegisterInfo.threadType = receiver.threadMode();
            mFindIndexList.add(mRegisterInfo);
            log("DefinedClass : " + classElement.toString()
                    + "\n\tmethodName : " + methodName
                    + "\n\t" + "methodParam : " + methodParamType
                    + "\n\t" + "methodParamName : " + methodParamName);

        }
    }

    private void writePutMethodBlock(MethodSpec putMethod, CodeBlock.Builder staticBlock) {
        for (Map.Entry<String, List<EventRegisterInfo>> eventIndex : mReceiverMap.entrySet()) {
            String key = eventIndex.getKey();
            staticBlock.add(putMethod.name + "(new $T($L,new $T {\n", Consts.com_leo_bus_EventIndexInfoWrap, key + ".class", Consts.com_leo_bus_EventIndexInfo_array);
            for (EventRegisterInfo eventRegisterInfo : eventIndex.getValue()) {
//            new EventIndexInfo(StringEvent.class, "doStringEvent", ThreadMode.THREAD_BACKGROUND)
                String receiver = eventRegisterInfo.eventType + ".class";
                staticBlock.add("\tnew $T($L,$S,$L),\n", Consts.com_leo_bus_EventIndexInfo,
                        receiver, eventRegisterInfo.methodName, eventRegisterInfo.threadType);
            }
            staticBlock.addStatement("}))");
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void log(String msg) {
        messager.printMessage(Diagnostic.Kind.OTHER, msg);
    }
}